import Control.Monad
import Control.Monad.IO.Class
import Database.CouchDB
import Database.HDBC.Types
import Database.HDBC.ODBC
import Data.Char
import Data.Digits
import Data.List
import Data.HashTable (hashString)
import Data.Maybe
import qualified Data.Set as S
import Text.JSON

import Debug.Trace

couchHote = "127.0.0.1"
couchDb = db "products"
couchDesign = doc "index"

beHote = "127.0.0.1"
usaHote = "127.0.0.1"
ukHote = "127.0.0.1"

codeFournisseurAmazon = 1

-- | Contenu des filtres pour l'importation depuis CouchDB.
data Filtre = Artiste String | Prix Double Double | Categorie Categorie
            | Langue Langue | ASIN String deriving (Show)
data Categorie = Musique | Livre | Film deriving (Show)
data Langue = Fr | En deriving (Show)

-- | Informations sur une connexion à une base de données
data Connexion = Connexion Connection String -- DBConnection Origine

main = do
    connBe <- oracleConn beHote "be"
    connUsa <- oracleConn usaHote "usa2"
    connUk <- oracleConn ukHote "uk"
    menu True [ ("Insérer dans BE"
                , insertionBe $ Connexion connBe "BE")
              , ("Insérer dans USA"
                , insertionUsa $ Connexion connUsa "USA")
              , ("Insérer dans UK"
                , insertionUk $ Connexion connUk "UK") ]
    disconnect connBe
    disconnect connUsa
    disconnect connUk
  where
    oracleConn hote user =
        connectODBC $ "DRIVER=oracle;Dbq=//"++hote++":1521/oracle.oracle;\
                         \UID="++user++";PWD=pass"
                       
    mysqlConn hote user =
        connectODBC $ "DRIVER=mysql;SERVER="++hote++";DATABASE="++user++";\
                      \USER="++user++";PASSWORD=pass;"

-- | Fonction générique pour la gestion des menus. Accepte une liste d'actions
-- associées à une description.
menu :: Bool -> [(String, IO a)] -> IO [a]
menu repeter actions =
    menu'
 where
    menu' = do
        forM_ (zip [1..] actions) $ \(i, action) -> -- Affiche les options
            putStrLn $ show i ++ ". " ++ fst action

        putStrLn $ show (length actions + 1) ++ ". Retour"

        putStrLn "Votre choix: "
        choix <- readLn
        if choix > length actions -- Lance l'action ou quitte
           then return []
           else do cur <- snd (actions !! (choix-1))
                   if repeter
                      then do next <- menu'
                              return $ cur:next
                      else return [cur]

-- | Demande à l'utilisateur une liste de filtres, une liste prédéfinie de
-- de filtres peut être fournie.
gestionFiltres :: [Filtre] -> IO [Filtre]
gestionFiltres filtres = do
    putStrLn "Liste des filtres:"
    forM_ filtres print
    putStrLn ""
    
    filtres' <- menu False $ [("Ajouter un filtre sur l'artiste", filtreArtiste)
        , ("Ajouter un filtre sur le prix", filtrePrix)
        , ("Ajouter un filtre sur l'ASIN", filtreASIN)]

    putStrLn ""
        
    case filtres' of
        []        -> return filtres -- Option retour du menu
        otherwise -> gestionFiltres (filtres' ++ filtres)
    
  where
    filtreArtiste = do
        putStrLn "Nom de l'auteur: "
        Artiste `fmap` getLine
    filtrePrix = do
        putStrLn "Prix minimal: "
        prixMin <- readLn
        putStrLn "Prix maximal: "
        prixMax <- readLn
        return $ Prix prixMin prixMax
    filtreASIN = do
        putStrLn "Code ASIN: "
        ASIN `fmap` getLine

-- | Donne la liste des ids des documents qui respectent tous les filtres.
idsFiltres [] = queryViewKeys couchDb couchDesign (doc "by_asin") []
idsFiltres filtres = do
    liftIO $ putStrLn "Recuperation des IDs"
    -- Ids retournés par chaque filtre
    ids <- forM (map requeteFiltre filtres) $ \(vue, options) ->
        queryViewKeys couchDb couchDesign vue options

    -- Retourne les ids communs uniques en effectuant une intersections
    -- entre tous les filtres (ids stockés dans des arbres binaires). 
    liftIO $ putStrLn "Intersection des IDs"
    return $ S.toList $ foldl1 S.intersection $ map S.fromList ids
    
  where      
    requeteFiltre :: Filtre -> (Doc, [(String, JSValue)])
    requeteFiltre (Artiste nom) =
        (doc "by_artist", [("key", JSString $ toJSString nom)])
    requeteFiltre (Prix minPrix maxPrix) =
        (doc "by_price", [ ("startkey", JSRational True (toRational minPrix))
                         , ("endkey", JSRational True (toRational maxPrix))])
    requeteFiltre (Categorie Musique) =
        (doc "by_category", [("key", JSString $ toJSString "music")])
    requeteFiltre (Categorie Livre) =
        (doc "by_category", [("key", JSString $ toJSString "book")])
    requeteFiltre (Categorie Film) =
        (doc "by_category", [("key", JSString $ toJSString "movie")])
    requeteFiltre (Langue Fr) =
        (doc "by_language", [("keys", JSArray [
                                  JSString $ toJSString "Francais"
                                , JSString $ toJSString "French"])])
    requeteFiltre (Langue En) =
        (doc "by_language", [("keys", JSArray [
                                  JSString $ toJSString "Anglais"
                                , JSString $ toJSString "English"])])
    requeteFiltre (ASIN amazon_asin) =
        (doc "by_asin", [("key", JSString $ toJSString amazon_asin)])

insertionUk = insertion [Langue En, Categorie Film]
insertionBe = insertion [Langue Fr{-, Categorie Musique-}]
insertionUsa = insertion [Langue En, Categorie Livre]

-- | Demande les filtres l'utilisateur, récupère les documents et les insère
-- dans la base de données
insertion filtres conn = do
    filtres' <- gestionFiltres filtres
    runCouchDB couchHote 5984 $ do
        ids <- idsFiltres filtres'
        inserer ids conn

-- | Insère tous les documents de la liste dans la base de données.
inserer id_docs (Connexion conn origine) = do
    let n = length id_docs
    liftIO $ putStrLn $ show n ++ " média(s) à insérer..."
    forM_ id_docs $ \id_doc -> do
        Just (_, _, JSObject contenu) <- getDoc couchDb id_doc
        let assoc_contenu = fromJSObject contenu
        liftIO $ do
            case stringValue "category" assoc_contenu of
                "music" -> insererMusique assoc_contenu
                "book" -> insererLivre assoc_contenu
                "movie" -> insererFilm assoc_contenu
            commit conn
    liftIO $ putStrLn $ show n ++ " média(s) inséré(s)."
  where
    insererMusique doc = do
        let amazon_asin = stringValue "asin" doc
        putStrLn $ "Insertion musique: " ++ amazon_asin
        produit_ean <- insererProduit doc
        
        when (isJust produit_ean) $ do
            insererArtistes $ fromJust $ lookup "artist" doc
            let label = stringOrDefault "label" doc "Unknown"
            insererEditeur label
            let support = stringOrDefault "support" doc "Unknown"
            let disques = intOrDefault "discs" doc 1
            let publication = stringOrDefault "release_date" doc "Unknown"

            req <- prepare conn "INSERT INTO site_musique VALUES (?, ?, ?, ?, ?)"
            execute req [ SqlInteger $ fromJust $ produit_ean, SqlString label
                        , SqlString support, SqlInteger disques
                        , SqlString publication ]
            return ()

    insererLivre doc = do
        let amazon_asin = stringValue "asin" doc
        putStrLn $ "Insertion livre: " ++ amazon_asin
        produit_ean <- insererProduit doc

        when (isJust produit_ean) $ do
            case lookup "authors" doc of
                Just e  -> insererArtistes e
                Nothing -> return ()
            let editeur = stringOrDefault "publisher" doc "Unknown"
            insererEditeur editeur
            let isbn = stringValue "asin" doc
            let reliure = stringValue "format" doc
            let pages = intOrDefault "pages" doc (-1)
            let publication = stringOrDefault "date" doc ""
            let edition = stringOrDefault "edition" doc ""
            req <- prepare conn "INSERT INTO site_livre VALUES (?, ?, ?, ?, ?, ?, ?)"
            execute req [ SqlInteger $ fromJust $ produit_ean, SqlString isbn
                        , SqlString editeur, SqlString reliure
                        , SqlInteger pages, SqlString publication
                        , SqlString edition ]
            return ()
            
    insererFilm doc = do
        let amazon_asin = stringValue "asin" doc
        putStrLn $ "Insertion film: "++ amazon_asin

        produit_ean <- insererProduit doc

        when (isJust produit_ean) $ do
            case lookup "actors" doc of
                 Just acs  -> insererArtistes acs
                 Nothing  -> return ()
            case lookup "directors" doc of
                 Just ds  -> insererArtistes ds
                 Nothing  -> return ()
            let studio = stringValue "studio" doc
            insererEditeur studio
            let support = stringOrDefault "format" doc "Unknown"
            let disques = intOrDefault "discs" doc 1
            let note = stringOrDefault "rating" doc "Unknown"
                
            let duree = stringOrDefault "runtime" doc "Unknown"
            req <- prepare conn "INSERT INTO site_film VALUES (?, ?, ?, ?, ?, ?)"
            execute req [ SqlInteger $ fromJust $ produit_ean, SqlString studio
                        , SqlString support, SqlInteger disques
                        , SqlString note, SqlString duree ]
            return ()
            
    -- Insère les artistes non existants
    insererArtistes (JSArray artistes) = do
        req_insert <- prepare conn "INSERT INTO site_artiste \
                                   \(SELECT ? FROM DUAL WHERE NOT EXISTS \
                                   \    (SELECT * FROM site_artiste WHERE nom = ?));"
        forM_ artistes $ \a -> do
            let a_str = toString a
            putStrLn $ "    Insertion artiste: " ++ a_str
            execute req_insert [SqlString a_str, SqlString a_str]
            return ()

    -- Insère l'editeur s'il n'exite pas
    insererEditeur editeur = do
        putStrLn $ "    Insertion editeur: " ++ editeur
        req_insert <- prepare conn "INSERT INTO site_editeur \
                                   \(SELECT ? FROM DUAL WHERE NOT EXISTS \
                                   \    (SELECT * FROM site_editeur WHERE nom = ?));"
        execute req_insert [SqlString editeur, SqlString editeur]
        return ()

    -- Insère un produit dans la base et retourne l'ean.
    -- Retourne Nothing si le produit existe déjà
    insererProduit doc = do
        let amazon_asin = stringValue "asin" doc
        let produit_ean = ean amazon_asin
        putStrLn $ "    Insertion produit: " ++ amazon_asin
        req <- prepare conn "SELECT ean FROM site_produit WHERE ean = ?;"
        execute req [SqlInteger produit_ean]
        existe <- fetchRow req
        if isJust existe
           then do
               putStrLn "    Produit existant"
               return Nothing -- Produit existant
           else do
               let titre = stringOrDefault "title" doc "Unknown"
               let description = ""
               let langue =
                    case lookup "language" doc of
                       Just l  -> toString l
                       Nothing -> case lookup "languages" doc of
                                      Just (JSArray ls) -> toString $ head ls
                                      Nothing -> ""
               
               let prix = rationalOrDefault "price" doc 0
               let devise = "USD"

               req_insert <- prepare conn "INSERT INTO site_produit VALUES \
                                          \(?, ?, ?, ?, ?, 15, ?, ?, ?, 0);"
               execute req_insert [SqlInteger produit_ean , SqlString titre
                           , SqlString description, SqlString langue
                           , SqlDouble $ fromRational $ prix, SqlString devise
                           , SqlInteger codeFournisseurAmazon
                           , SqlString origine ]
               return $ Just produit_ean

    -- Génère un code EAN depuis le code Amazon
    ean amazon_asin =
        let codeProduit = fromIntegral $ (abs $ hashString amazon_asin) `mod` 10^9
            code = 200 * 10^10 + codeFournisseurAmazon * 10^7 + codeProduit * 10
            (impairs, pairs) = partition (odd . fst) $ zip [1..] (digits 10 code)
            sumImpairs = sum $ map snd $ impairs
            sumPairs = sum $ map snd $ pairs
            checksum = (10 - ((3*sumPairs + sumImpairs) `mod` 10)) `mod` 10
        in code + checksum

    stringValue key doc = toString $ fromJust $ lookup key doc
    valueOrDefault key doc def = fromMaybe def (lookup key doc)
    stringOrDefault key doc def =
        toString $ valueOrDefault key doc (JSString $ toJSString def)
    rationalOrDefault key doc def =
        fromJRational $ valueOrDefault key doc (JSRational True def)
    intOrDefault key doc def =
        floor $ rationalOrDefault key doc (fromIntegral def)
    
    toString (JSString s) = toAscii $ fromJSString s
    fromJRational (JSRational _ r) = r
    toAscii = filter isAscii