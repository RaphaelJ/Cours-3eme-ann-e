import Control.Monad
import Control.Monad.IO.Class
import Database.CouchDB
import Data.List
import Text.JSON

couchHote = "192.168.10.100"
couchDb = db "products"
couchDesign = doc "index"

-- | Contenu des filtres pour l'importation depuis CouchDB.
data Filtre = Artiste String | Prix Rational Rational | Categorie Categorie
            | Langue Langue | ASIN String deriving (Show)
data Categorie = Musique | Livre | Film deriving (Show)
data Langue = Fr | En deriving (Show)

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

-- | Demande à l'utilisateur une liste de filtres en chargant une liste
-- prédéfinie de filtres.
gestionFiltres :: [Filtre] -> IO [Filtre]
gestionFiltres filtres = do
    putStrLn "Liste des filtres:"
    forM_ filtres $ \filtre -> print filtre
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
        prixMin <- getLine
        putStrLn "Prix maximal: "
        prixMax <- getLine
        return $ Prix (read prixMin) (read prixMax)
    filtreASIN = do
        putStrLn "Code ASIN: "
        ASIN `fmap` getLine

-- | Donne la liste des ids des documents qui respectent tous les filtres.
idsFiltres [] = queryViewKeys couchDb couchDesign (doc "by_asin") []
idsFiltres filtres = do
    -- Ids retournés par chaque filtre
    ids <- forM (map requeteFiltre filtres) $ \(vue, options) ->
        queryViewKeys couchDb couchDesign vue options
    
    return $ foldl1' intersect ids
    
  where
    requeteFiltre :: Filtre -> (Doc, [(String, JSValue)])
    requeteFiltre (Artiste nom) =
        (doc "by_artist", [("key", JSString $ toJSString nom)])
    requeteFiltre (Prix minPrix maxPrix) =
        (doc "by_price", [ ("startkey", JSRational True minPrix)
                         , ("endkey", JSRational True maxPrix)])
    requeteFiltre (Categorie Musique) =
        (doc "by_artist", [("key", JSString $ toJSString "music")])
    requeteFiltre (Categorie Livre) =
        (doc "by_artist", [("key", JSString $ toJSString "book")])
    requeteFiltre (Categorie Film) =
        (doc "by_artist", [("key", JSString $ toJSString "film")])
    requeteFiltre (Langue Fr) =
        (doc "by_language", [("keys", JSArray [
                                  JSString $ toJSString $ show "Français"
                                , JSString $ toJSString $ show "French"])])
    requeteFiltre (Langue En) =
        (doc "by_language", [("keys", JSArray [
                                  JSString $ toJSString $ show "Anglais"
                                , JSString $ toJSString $ show "English"])])
    requeteFiltre (ASIN amazon_asin) =
        (doc "by_asin", [("key", JSString $ toJSString amazon_asin)])

-- | Insère tous les documents de la liste dans la base de données.
insertion id_docs =
    forM_ id_docs $ \id_doc -> do
        Just (_, _, JSObject contenu) <- getDoc couchDb (doc id_doc)
        let assoc_contenu = fromJSObject contenu
        liftIO $ print $ lookup "title" assoc_contenu
        liftIO $ print $ lookup "category" assoc_contenu