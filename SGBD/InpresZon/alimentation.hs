import Control.Monad
import Control.Monad.IO.Class
import Database.CouchDB
import Data.List
import Text.JSON

couchHote = "127.0.0.1"
couchDb = db "products"
couchDesign = doc "index"

-- | Contenu des filtres pour l'importation depuis CouchDB.
data Filtre = Artiste String | Prix Double Double | Categorie Categorie
            | Langue Langue | ASIN String deriving (Show)
data Categorie = Musique | Livre | Film deriving (Show)
data Langue = Fr | En deriving (Show)

main = do menu True [ ("Insérer dans BE", insertionBe)
                    , ("Insérer dans USA", insertionUsa)
                    , ("Insérer dans UK", insertionUk)]

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
        choix <- read `fmap` getLine
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
        prixMin <- read `fmap` getLine
        putStrLn "Prix maximal: "
        prixMax <- read `fmap` getLine
        return $ Prix prixMin prixMax
    filtreASIN = do
        putStrLn "Code ASIN: "
        ASIN `fmap` getLine

-- | Donne la liste des ids des documents qui respectent tous les filtres.
idsFiltres [] = queryViewKeys couchDb couchDesign (doc "by_asin") []
idsFiltres filtres = do
    -- Ids retournés par chaque filtre
    ids <- forM (map requeteFiltre filtres) $ \(vue, options) ->
        queryViewKeys couchDb couchDesign vue options
    
    return $ foldl1' intersect (nub ids) -- Retourne les ids communs
    
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

-- | Insère tous les documents de la liste dans la base de données.
inserer id_docs = do
    let n = length id_docs
    liftIO $ putStrLn $ show n ++ " média(s) à insérer..."
    forM_ id_docs $ \id_doc -> do
        Just (_, _, JSObject contenu) <- getDoc couchDb id_doc
        let assoc_contenu = fromJSObject contenu
--         liftIO $ print $ lookup "title" assoc_contenu
--         liftIO $ print $ lookup "category" assoc_contenu
        return ()
    liftIO $ putStrLn $ show n ++ " média(s) inséré(s)."

-- | Demande les filtres l'utilisateur, récupère les documents et les insère
-- dans la base de données
insertion filtres = runCouchDB couchHote 5984 $ do
    filtres' <- liftIO $ gestionFiltres filtres
    ids <- idsFiltres filtres'
    inserer ids

insertionBe = insertion [Langue Fr]
insertionUsa = insertion [Langue En, Categorie Livre]
insertionUk = insertion [Langue En, Categorie Film]