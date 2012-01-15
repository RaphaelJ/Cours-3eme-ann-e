------------------------------------------------------
-- BE
------------------------------------------------------

INSERT INTO site_fournisseur VALUES (1, 'Amazon');

CREATE DATABASE LINK usa CONNECT TO usa IDENTIFIED BY pass USING 'oracle';

create or replace
PACKAGE GESTION_CATALOGUE AS 
  TYPE ProduitsResultSet IS REF CURSOR RETURN site_produit%ROWTYPE;
  TYPE CommentairesResultSet IS REF CURSOR RETURN site_commentaire%ROWTYPE;
  
  FUNCTION Produits(ArgPage IN INTEGER)
  RETURN ProduitsResultSet;
  
  FUNCTION ProduitsSimilaires(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN ProduitsResultSet;
  
  FUNCTION Fournisseur(ArgId IN SITE_FOURNISSEUR.id%TYPE)
  RETURN SITE_FOURNISSEUR%ROWTYPE;
  
  FUNCTION NbreProduits
  RETURN INTEGER;

  FUNCTION Produit(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN SITE_PRODUIT%ROWTYPE;
  
  FUNCTION Commentaires(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN CommentairesResultSet;
  
  PROCEDURE AjouterCommentaire(
    ArgEan IN SITE_PRODUIT.ean%TYPE,
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgMessage IN SITE_commentaire.message%TYPE
  );
  
  FUNCTION CommentaireAutorise(
    ArgEan IN SITE_PRODUIT.ean%TYPE,
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN NUMBER;
  
  
END GESTION_CATALOGUE;

create or replace
PACKAGE BODY GESTION_CATALOGUE AS
  FUNCTION Produits(ArgPage IN INTEGER)
  RETURN ProduitsResultSet IS
    curs ProduitsResultSet;
    ARTICLES_PAGE NUMBER;
  BEGIN
    ARTICLES_PAGE := 5;
  
    OPEN curs FOR
      SELECT ean, titre, description, langue, prix, stock, devise,
      fournisseur_id, origine, replique
      FROM (SELECT p.*, ROWNUM num
            FROM site_produit p
            WHERE ROWNUM <= ArgPage * ARTICLES_PAGE)
      WHERE num > (ArgPage - 1) * ARTICLES_PAGE;
      
    RETURN curs;
  END Produits;
  
  FUNCTION ProduitsSimilaires(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN ProduitsResultSet IS
    curs ProduitsResultSet;
    ARTICLES NUMBER;
  BEGIN
    ARTICLES := 5;
    
    -- Sélectionne les 5 articles les plus commandés avec cet articles
    OPEN curs FOR
      SELECT p.*
      FROM site_commandeproduit cp -- Obtient les commandes du produit
      INNER JOIN site_commandeproduit cp2 -- Obtient les produits de ces commandes
        ON cp2.commande_id = cp.commande_id
      INNER JOIN site_produit p
        ON p.ean = cp2.produit_id 
      WHERE cp.produit_id = ArgEan 
        AND p.ean != ArgEan
        AND ROWNUM <= ARTICLES
      GROUP BY p.ean, p.titre, p.description, p.langue, p.prix, p.stock,
        p.devise, p.fournisseur_id, p.origine, p.replique
      ORDER BY SUM(cp2.quantite) DESC; -- Trie les produits par nombre de commandes en commun
      
    RETURN curs;
  END ProduitsSimilaires;
  
  FUNCTION Fournisseur(ArgId IN SITE_FOURNISSEUR.id%TYPE)
  RETURN SITE_FOURNISSEUR%ROWTYPE IS
    Fournisseur SITE_FOURNISSEUR%ROWTYPE;
  BEGIN
    SELECT * INTO Fournisseur
    FROM SITE_FOURNISSEUR
    WHERE id = ArgId;
  
    RETURN Fournisseur;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_catalogue.Fournisseur',
        errnums_pkg.no_data_const, 'Fournisseur introuvable', 
        strip_code => true
      );
  END Fournisseur;
  
  FUNCTION NbreProduits
  RETURN INTEGER IS
    Nbre INTEGER;
  BEGIN
    SELECT COUNT(*) INTO Nbre
    FROM SITE_PRODUIT;
    
    RETURN Nbre;
  END NbreProduits;
  
  FUNCTION Produit(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN SITE_PRODUIT%ROWTYPE IS
    VarProduit SITE_PRODUIT%ROWTYPE;
  BEGIN
      SELECT * INTO VarProduit
      FROM SITE_PRODUIT
      WHERE ean = ArgEan;
      
      RETURN VarProduit;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_catalogue.Produit',
        errnums_pkg.no_data_const, 'Produit introuvable', 
        strip_code => true
      );
  END Produit;
  
  FUNCTION Commentaires(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN CommentairesResultSet IS
    curs CommentairesResultSet;
  BEGIN
    OPEN curs FOR
      SELECT *
      FROM site_commentaire
      WHERE produit_id = ArgEan;
      
    RETURN curs;
  END Commentaires;
  
  PROCEDURE AjouterCommentaire(
    ArgEan IN SITE_PRODUIT.ean%TYPE,
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgMessage IN SITE_commentaire.message%TYPE
  ) IS
  BEGIN
    INSERT INTO site_commentaire
    VALUES (null, ArgUtilisateur, ArgEan, current_date, ArgMessage);
  END AjouterCommentaire;
  
  FUNCTION CommentaireAutorise(
    ArgEan IN SITE_PRODUIT.ean%TYPE,
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN NUMBER IS
    Autorise NUMBER;
  BEGIN
    -- Vérifie si le client a déjà recu ce produit
    SELECT COUNT(*) INTO Autorise
    FROM site_commande c
    INNER JOIN site_commandepaquet cp
      ON cp.commande_id = c.id
    INNER JOIN site_commandepaquetproduit cpp
      ON cpp.paquet_id = cp.id
    WHERE cpp.produit_id = ArgEan
      AND c.utilisateur_id = ArgUtilisateur
      AND cp.STATUS = 'LIVRE';
  
    RETURN Autorise;   
  END CommentaireAutorise;
END GESTION_CATALOGUE;

create or replace
PACKAGE GESTION_COMMANDES AS 
  TYPE CaddieResultSet IS REF CURSOR;
  TYPE CommandesResultSet IS REF CURSOR RETURN site_commande%ROWTYPE;
  TYPE ProduitsCommandeResultSet IS REF CURSOR;
  TYPE PaquetsResultSet IS REF CURSOR;

  FUNCTION ProduitsCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE,
    ArgPage IN INTEGER
  ) RETURN CaddieResultSet;
  
  FUNCTION TotalCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN NUMBER;
  
  FUNCTION NbreProduitsCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN INTEGER;
  
  FUNCTION CaddieEnStock(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN INTEGER;
  
  PROCEDURE AjouterCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  );
  
  PROCEDURE SupprimerCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  );
  
  PROCEDURE ViderCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE
  );
  
  FUNCTION QuantiteProduit(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  ) RETURN INTEGER;
  
  PROCEDURE ModifierQuantite(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE,
    ArgQuantite SITE_CADDIEPRODUIT.quantite%TYPE
  );
  
  PROCEDURE PasserCommande(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgAdresseId SITE_ADRESSE.ID%TYPE
  );
  
  FUNCTION HistoriqueCommandes(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN CommandesResultSet;
  
  FUNCTION ProduitsCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  ) RETURN ProduitsCommandeResultSet;
  
  FUNCTION PaquetsCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  ) RETURN PaquetsResultSet;
  
  PROCEDURE AnnulerCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  );
  
  FUNCTION ProduitCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeProduitId IN SITE_commandeproduit.id%TYPE    
  ) RETURN SITE_COMMANDEPRODUIT%ROWTYPE;
  
  PROCEDURE ModifierCommandeQuantite(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeProduitId IN SITE_commandeproduit.id%TYPE,
    ArgQuantite IN SITE_commandeproduit.quantite%TYPE
  );
END GESTION_COMMANDES;

create or replace
PACKAGE BODY GESTION_COMMANDES AS
  FUNCTION ProduitsCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE,
    ArgPage IN INTEGER
  ) RETURN CaddieResultSet IS
    curs CaddieResultSet;
    ARTICLES_PAGE NUMBER;
  BEGIN
    ARTICLES_PAGE := 5;
  
    OPEN curs FOR
      SELECT ean, titre, description, langue, prix, stock, devise,
      fournisseur_id, origine, replique, quantite
      FROM (SELECT p.*, c.quantite, ROWNUM num
            FROM site_caddieproduit c
            INNER JOIN site_produit p
              ON p.ean = c.produit_id
            WHERE c.utilisateur_id = ArgUtilisateur)
      WHERE num BETWEEN (ArgPage - 1) * ARTICLES_PAGE + 1
        AND ArgPage * ARTICLES_PAGE;
      
    RETURN curs;
  END ProduitsCaddie;
  
  FUNCTION TotalCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN NUMBER IS
    Total NUMBER;
  BEGIN  
    SELECT SUM(p.prix * c.quantite) INTO Total
    FROM site_caddieproduit c
    INNER JOIN site_produit p
      ON p.ean = c.produit_id
    WHERE c.utilisateur_id = ArgUtilisateur;
      
    RETURN Total;
  END TotalCaddie;
  
  FUNCTION NbreProduitsCaddie(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN INTEGER IS
    Nbre INTEGER;
  BEGIN
    SELECT COUNT(*) INTO Nbre
    FROM site_caddieproduit c
    WHERE c.utilisateur_id = ArgUtilisateur;
    
    RETURN Nbre;
  END NbreProduitsCaddie;
  
  FUNCTION CaddieEnStock(
    ArgUtilisateur IN SITE_CADDIEPRODUIT.utilisateur_id%TYPE
  ) RETURN INTEGER IS
    EnStock INTEGER;
  BEGIN
    SELECT COUNT(*) INTO EnStock
    FROM site_caddieproduit c
    INNER JOIN site_produit p
      ON p.ean = c.produit_id
    WHERE c.quantite > p.stock;
    
    RETURN EnStock;
  END CaddieEnStock;
  
  PROCEDURE AjouterCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  ) IS
  BEGIN
    BEGIN
      INSERT INTO SITE_CADDIEPRODUIT VALUES(null, ArgUtilisateur, ArgEan, 1);
    EXCEPTION
      WHEN OTHERS THEN -- Element déjà dans le caddie
        UPDATE SITE_CADDIEPRODUIT
        SET Quantite = Quantite + 1
        WHERE Utilisateur_id = ArgUtilisateur AND Produit_id = ArgEan;
    END;
  END AjouterCaddie;
  
  PROCEDURE SupprimerCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  ) IS
  BEGIN
    DELETE FROM SITE_CADDIEPRODUIT 
    WHERE Utilisateur_id = ArgUtilisateur AND Produit_id = ArgEan;
  END SupprimerCaddie;
  
  PROCEDURE ViderCaddie(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE
  ) IS
  BEGIN
    DELETE FROM SITE_CADDIEPRODUIT 
    WHERE Utilisateur_id = ArgUtilisateur;
  END ViderCaddie;
  
  FUNCTION QuantiteProduit(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE
  ) RETURN INTEGER IS
    Quant INTEGER;
  BEGIN
    SELECT Quantite INTO Quant
    FROM SITE_CADDIEPRODUIT
    WHERE Utilisateur_id = ArgUtilisateur AND Produit_id = ArgEan;
    
    RETURN Quant;
  END QuantiteProduit;
  
  PROCEDURE ModifierQuantite(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgEan SITE_PRODUIT.ean%TYPE,
    ArgQuantite SITE_CADDIEPRODUIT.quantite%TYPE
  ) IS
  BEGIN
    UPDATE SITE_CADDIEPRODUIT 
    SET Quantite = ArgQuantite
    WHERE Utilisateur_id = ArgUtilisateur AND Produit_id = ArgEan;
  END ModifierQuantite;
  
  PROCEDURE PasserCommande(
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE,
    ArgAdresseId SITE_ADRESSE.ID%TYPE
  ) IS
    CURSOR update_cursor IS
      SELECT c.*
      FROM SITE_CADDIEPRODUIT c
      WHERE Utilisateur_id = ArgUtilisateur;
  BEGIN
    IF CaddieEnStock(ArgUtilisateur) = 0 THEN
      IF NbreProduitsCaddie(ArgUtilisateur) > 0 THEN
        -- Force une adresse appartenant à l'utilisateur
        INSERT INTO SITE_COMMANDE VALUES (
          null, ArgUtilisateur,
          (SELECT ID FROM SITE_ADRESSE
           WHERE ID = ArgAdresseId AND Utilisateur_ID = ArgUtilisateur),
          'BE', CURRENT_DATE
        );
        
        -- DB LINK USA
        INSERT INTO usa.SITE_COMMANDE VALUES (
          null, ArgUtilisateur,
          (SELECT ID FROM SITE_ADRESSE
           WHERE ID = ArgAdresseId AND Utilisateur_ID = ArgUtilisateur),
          'BE', CURRENT_DATE
        );
        
        INSERT INTO site_commandeproduit
          (SELECT null, SITE_COMMANDE_SQ.CURRVAL, cp.produit_id, 
           cp.quantite, p.prix, p.devise
           FROM SITE_CADDIEPRODUIT cp
           INNER JOIN SITE_PRODUIT p
              ON p.ean = cp.produit_id
           WHERE cp.Utilisateur_id = ArgUtilisateur
        );
        
        INSERT INTO usa.site_commandeproduit
          (SELECT null, SITE_COMMANDE_SQ.CURRVAL, cp.produit_id, 
           cp.quantite, p.prix, p.devise
           FROM SITE_CADDIEPRODUIT cp
           INNER JOIN SITE_PRODUIT p
              ON p.ean = cp.produit_id
           WHERE cp.Utilisateur_id = ArgUtilisateur
        );
        
        -- Réduit les stocks des objets achetés
        FOR cp in update_cursor
        LOOP
          UPDATE site_produit
          SET stock = stock - cp.quantite
          WHERE ean = cp.produit_id;
        END LOOP;
        
        DELETE FROM SITE_CADDIEPRODUIT
        WHERE Utilisateur_id = ArgUtilisateur;
      ELSE
        RAISE errnums_pkg.caddie_vide_ex;
      END IF;
    ELSE
      RAISE errnums_pkg.pas_en_stock_ex;
    END IF;
  EXCEPTION
    WHEN errnums_pkg.caddie_vide_ex THEN
      err_pkg.report_and_stop(
        'gestion_commandes.PasserCommande',
        errnums_pkg.caddie_vide_const, 'Tentative de commande d''un caddie vide', 
        strip_code => true
      );
    WHEN errnums_pkg.pas_en_stock_ex THEN
      err_pkg.report_and_stop(
        'gestion_commandes.PasserCommande',
        errnums_pkg.pas_en_stock_const,
        'Tentative de commande de produits non en stock', 
        strip_code => true
      );
  END PasserCommande;
  
  FUNCTION HistoriqueCommandes(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN CommandesResultSet IS
    curs CaddieResultSet;
  BEGIN
    OPEN curs FOR
      SELECT *
      FROM site_commande
      WHERE utilisateur_id = ArgUtilisateur;
      
    RETURN curs;
  END HistoriqueCommandes;
  
  FUNCTION ProduitsCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  ) RETURN ProduitsCommandeResultSet IS 
    curs ProduitsCommandeResultSet;
  BEGIN
    OPEN curs FOR
      SELECT p.*, cp.quantite, cp.id
      FROM site_commande c
      INNER JOIN site_commandeproduit cp
        ON cp.commande_id = c.id
      INNER JOIN site_produit p
        ON p.ean = cp.produit_id
      WHERE c.utilisateur_id = ArgUtilisateur
        AND c.id = ArgCommandeId;
      
    RETURN curs;
  END ProduitsCommande;
  
  FUNCTION PaquetsCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  ) RETURN PaquetsResultSet IS
    curs PaquetsResultSet;
  BEGIN
    OPEN curs FOR
      SELECT cp.*
      FROM site_commande c
      INNER JOIN site_commandepaquet cp
        ON cp.commande_id = c.id
      WHERE c.utilisateur_id = ArgUtilisateur
        AND c.id = ArgCommandeId;
    
    RETURN curs;
  END PaquetsCommande;
  
  PROCEDURE AnnulerCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeId IN SITE_commande.id%TYPE    
  ) IS
  BEGIN
    DELETE FROM site_commande
    WHERE utilisateur_id = ArgUtilisateur
      AND id = ArgCommandeId
      AND NOT EXISTS ( -- Qui n'a pas encore été empaqueté 
        SELECT *
        FROM site_commandepaquet
        WHERE commande_id = ArgCommandeId
    );
    
    DELETE FROM usa.site_commande
    WHERE utilisateur_id = ArgUtilisateur
      AND id = ArgCommandeId;
  END AnnulerCommande;
  
  FUNCTION ProduitCommande(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeProduitId IN SITE_commandeproduit.id%TYPE    
  ) RETURN SITE_COMMANDEPRODUIT%ROWTYPE IS
    VarCommandeProduit SITE_COMMANDEPRODUIT%ROWTYPE;
  BEGIN
      SELECT cp.* INTO VarCommandeProduit
      FROM SITE_COMMANDEPRODUIT cp
      INNER JOIN SITE_COMMANDE c
        ON c.id = cp.commande_id
      WHERE c.utilisateur_id = ArgUtilisateur
        AND cp.id = ArgCommandeProduitId;
      
      RETURN VarCommandeProduit;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_commandes.ProduitCommande',
        errnums_pkg.no_data_const, 'Produit de la commande introuvable', 
        strip_code => true
      );
  END ProduitCommande;
  
  PROCEDURE ModifierCommandeQuantite(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE,
    ArgCommandeProduitId IN SITE_commandeproduit.id%TYPE,
    ArgQuantite IN SITE_commandeproduit.quantite%TYPE
  ) IS
    QuantiteDispo number;
    IdProduit SITE_PRODUIT.ean%TYPE;
  BEGIN
    -- Récupère la quantité maximale commandable
    SELECT cp.quantite + p.stock, p.ean
    INTO QuantiteDispo, IdProduit
    FROM site_commandeproduit cp
    INNER JOIN site_commande c
      ON c.id = cp.commande_id
    INNER JOiN site_produit p
      ON p.ean = cp.produit_id
    WHERE c.utilisateur_id = ArgUtilisateur
      AND cp.id = ArgCommandeProduitId;
      
    IF ArgQuantite > QuantiteDispo THEN
      RAISE errnums_pkg.pas_en_stock_ex;
    ELSE
      UPDATE site_commandeproduit 
      SET quantite = ArgQuantite
      WHERE id = ArgCommandeProduitId;
      
      UPDATE usa.site_commandeproduit 
      SET quantite = ArgQuantite
      WHERE id = ArgCommandeProduitId;
      
      UPDATE site_produit
      SET stock = QuantiteDispo - ArgQuantite
      WHERE ean = (SELECT produit_id
        FROM site_commandeproduit 
        WHERE id = ArgCommandeProduitId
      );
    END IF;
  EXCEPTION
    WHEN errnums_pkg.pas_en_stock_ex THEN
      err_pkg.report_and_stop(
        'gestion_commandes.PasserCommande',
        errnums_pkg.pas_en_stock_const,
        'Tentative de commande de produits non en stock', 
        strip_code => true
      ); 
    
  END ModifierCommandeQuantite;
END GESTION_COMMANDES;

create or replace
PACKAGE GESTION_SESSIONS AS
  PROCEDURE Ajouter (ArgCle IN SITE_SESSION.cle%TYPE,
    ArgDonnees IN SITE_SESSION.donnees%TYPE,
    ArgExpiration IN SITE_SESSION.expiration%TYPE);

  PROCEDURE Modifier (ArgCle IN SITE_SESSION.cle%TYPE,
    ArgDonnees IN SITE_SESSION.donnees%TYPE,
    ArgExpiration IN SITE_SESSION.expiration%TYPE);

  FUNCTION Chercher (ArgCle IN SITE_SESSION.cle%TYPE)
    RETURN SITE_SESSION%ROWTYPE;

  PROCEDURE Supprimer (ArgCle IN SITE_SESSION.cle%TYPE);
END GESTION_SESSIONS;

create or replace
PACKAGE BODY GESTION_SESSIONS AS

  PROCEDURE Ajouter (ArgCle IN SITE_SESSION.cle%TYPE,
    ArgDonnees IN SITE_SESSION.donnees%TYPE,
    ArgExpiration IN SITE_SESSION.expiration%TYPE) AS
  BEGIN
    INSERT INTO SITE_SESSION VALUES (ArgCle, ArgDonnees, ArgExpiration);
  END Ajouter;

  PROCEDURE Modifier (ArgCle IN SITE_SESSION.cle%TYPE,
    ArgDonnees IN SITE_SESSION.donnees%TYPE,
    ArgExpiration IN SITE_SESSION.expiration%TYPE) AS
  BEGIN
    UPDATE SITE_SESSION SET donnees = ArgDonnees, expiration = ArgExpiration
    WHERE cle = ArgCle;
  END Modifier;

  FUNCTION Chercher (ArgCle IN SITE_SESSION.cle%TYPE)
    RETURN SITE_SESSION%ROWTYPE
  AS
    VarSession SITE_SESSION%ROWTYPE;
  BEGIN
    SELECT * INTO VarSession FROM SITE_SESSION WHERE cle = ArgCle;

    RETURN VarSession;
  END Chercher;

  PROCEDURE Supprimer (ArgCle IN SITE_SESSION.cle%TYPE)
  AS
  BEGIN
    DELETE FROM SITE_SESSION WHERE cle = ArgCle;
  END Supprimer;

END GESTION_SESSIONS;

create or replace
PACKAGE GESTION_UTILISATEURS AS
  TYPE AdressesResultSet IS REF CURSOR RETURN site_adresse%ROWTYPE;

  PROCEDURE Ajouter (login IN SITE_UTILISATEUR.LOGIN%TYPE,
    mot_de_passe IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE, -- MDP en SHA1
    email IN SITE_UTILISATEUR.EMAIL%TYPE,
    nom IN SITE_UTILISATEUR.NOM%TYPE,
    prenom IN SITE_UTILISATEUR.PRENOM%TYPE);

  PROCEDURE Modifier (ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgMdp IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE, -- MDP en SHA1
    ArgNom IN SITE_UTILISATEUR.NOM%TYPE,
    ArgPrenom IN SITE_UTILISATEUR.PRENOM%TYPE);

  FUNCTION LoginExiste(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN NUMBER;

  FUNCTION EmailExiste(ArgEmail IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN NUMBER;

  -- Connecte l'utilisateur en vérifiant son mot de passe
  FUNCTION Connexion(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgMotDePasse IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE) -- MDP en SHA1
  RETURN SITE_UTILISATEUR%ROWTYPE;
  -- Recherche l'utilisateur avec son login
  FUNCTION Utilisateur(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN SITE_UTILISATEUR%ROWTYPE;
  
  -- Retourne les adresses d'un utilisateur
  FUNCTION Adresses(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN AdressesResultSet;
  
  -- Retourne une adresse d'un utilisateur
  FUNCTION Adresse(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgAdresse IN SITE_ADRESSE.ID%TYPE
  ) RETURN SITE_ADRESSE%ROWTYPE;
  
  PROCEDURE AjouterAdresse(
    ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE, 
    ArgAdresse IN SITE_ADRESSE.ADRESSE%TYPE,
    ArgVille IN SITE_ADRESSE.Ville%TYPE,
    ArgCodePostal IN SITE_ADRESSE.CODE_POSTAL%TYPE,
    ArgPays IN SITE_ADRESSE.PAYS%TYPE
  );
  
  PROCEDURE SupprimerAdresse(
    ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgAdressId IN SITE_ADRESSE.ID%TYPE
  );
END GESTION_UTILISATEURS;

create or replace
PACKAGE BODY GESTION_UTILISATEURS AS
  PROCEDURE Ajouter (login IN SITE_UTILISATEUR.LOGIN%TYPE,
    mot_de_passe IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE, -- MDP en SHA1
    email IN SITE_UTILISATEUR.EMAIL%TYPE,
    nom IN SITE_UTILISATEUR.NOM%TYPE,
    prenom IN SITE_UTILISATEUR.PRENOM%TYPE) AS
  BEGIN
    INSERT INTO SITE_UTILISATEUR VALUES (login, mot_de_passe, email, 0,
      nom, prenom, current_timestamp, 'BE', null, null);
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
      err_pkg.report_and_stop(
        'gestion_utilisateurs.Ajouter',
        errnums_pkg.utilisateur_existant_const,
        'L''email ou le login est déjà enregistré', 
        strip_code => true
      );
  END Ajouter;

  PROCEDURE Modifier (ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgMdp IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE, -- MDP en SHA1
    ArgNom IN SITE_UTILISATEUR.NOM%TYPE,
    ArgPrenom IN SITE_UTILISATEUR.PRENOM%TYPE) AS
  BEGIN
    UPDATE SITE_UTILISATEUR SET mot_de_passe = ArgMdp, nom = ArgNom,
                                prenom = ArgPrenom, site_modification = 'BE'
    WHERE login = ArgLogin;
  END Modifier;

  FUNCTION LoginExiste(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN NUMBER
  AS
    nbre NUMBER;
  BEGIN
    SELECT COUNT(*) INTO nbre
    FROM SITE_UTILISATEUR u
    WHERE login = ArgLogin;

    RETURN nbre;
  END LoginExiste;

  FUNCTION EmailExiste(ArgEmail IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN NUMBER
  AS
    nbre NUMBER;
  BEGIN
    SELECT COUNT(*) INTO nbre
    FROM SITE_UTILISATEUR u
    WHERE email = ArgEmail;

    RETURN nbre;
  END EmailExiste;

  -- Connecte l'utilisateur en vérifiant son mot de passe
  FUNCTION Connexion(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgMotDePasse IN SITE_UTILISATEUR.MOT_DE_PASSE%TYPE) -- MDP en SHA1
  RETURN SITE_UTILISATEUR%ROWTYPE
  AS
    VarUtilisateur SITE_UTILISATEUR%ROWTYPE;
  BEGIN
    SELECT * INTO VarUtilisateur
    FROM SITE_UTILISATEUR
    WHERE login = ArgLogin AND mot_de_passe = ArgMotDePasse;

    RETURN VarUtilisateur;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_utilisateurs.Connexion',
        errnums_pkg.utilisateur_inexistant_const,
        'Mauvais identifiants de connexion', 
        strip_code => true
      );
  END Connexion;

  -- Recherche l'utilisateur avec son login
  FUNCTION Utilisateur(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN SITE_UTILISATEUR%ROWTYPE
  AS
    VarUtilisateur SITE_UTILISATEUR%ROWTYPE;
  BEGIN
    SELECT * INTO VarUtilisateur
    FROM SITE_UTILISATEUR
    WHERE login = ArgLogin;

    RETURN VarUtilisateur;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_utilisateurs.Utilisateur',
        errnums_pkg.utilisateur_inexistant_const, 'Mauvais login', 
        strip_code => true
      );
  END Utilisateur;
  
  FUNCTION Adresses(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE)
  RETURN AdressesResultSet IS
    curs AdressesResultSet;
  BEGIN
    OPEN curs FOR
      SELECT *
      FROM site_adresse
      WHERE utilisateur_id = ArgLogin;
    RETURN curs;
  END Adresses;
  
  -- Retourne une adresse d'un utilisateur
  FUNCTION Adresse(ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgAdresse IN SITE_ADRESSE.ID%TYPE
  ) RETURN SITE_ADRESSE%ROWTYPE IS
    VarAdresse SITE_ADRESSE%ROWTYPE;
  BEGIN
      SELECT * INTO VarAdresse
      FROM SITE_ADRESSE
      WHERE ID = ArgAdresse AND UTILISATEUR_ID = ArgLogin;
      
      RETURN VarAdresse;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      err_pkg.report_and_stop(
        'gestion_utilisateurs.Adresse',
        errnums_pkg.no_data_const, 'Adresse introuvable', 
        strip_code => true
      );
  END Adresse;
  
  PROCEDURE AjouterAdresse(
    ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE, 
    ArgAdresse IN SITE_ADRESSE.ADRESSE%TYPE,
    ArgVille IN SITE_ADRESSE.Ville%TYPE,
    ArgCodePostal IN SITE_ADRESSE.CODE_POSTAL%TYPE,
    ArgPays IN SITE_ADRESSE.PAYS%TYPE
  ) AS
  BEGIN
    INSERT INTO SITE_ADRESSE (adresse, ville, code_postal, pays, utilisateur_id)
    VALUES (ArgAdresse, ArgVille, ArgCodePostal, ArgPays, ArgLogin);
  END AjouterAdresse;
  
  PROCEDURE SupprimerAdresse(
    ArgLogin IN SITE_UTILISATEUR.LOGIN%TYPE,
    ArgAdressId IN SITE_ADRESSE.ID%TYPE
  ) AS
  BEGIN
    DELETE FROM SITE_ADRESSE
    WHERE ID = ArgAdressId AND UTILISATEUR_ID = ArgLogin;
  END SupprimerAdresse;
END GESTION_UTILISATEURS;

CREATE OR REPLACE PROCEDURE EMPAQUETERPAQUETS AS 
  -- Cursor sélectionnant les produits non livrés
  CURSOR non_livres IS
    SELECT c.adresse_livraison_id, cp.id, cp.commande_id, cp.produit_id, cp.prix, cp.devise,
      (cp.quantite - COALESCE((
      SELECT SUM(cpp.quantite) quantite_livree-- Recherche la quantité livrée de chaque produit
      FROM site_commandepaquetproduit cpp
      INNER JOIN site_commandepaquet cp2
        ON cp2.id = cpp.paquet_id
      WHERE cp2.commande_id = cp.commande_id
        AND cpp.produit_id = cp.produit_id), 0)) quantite_restante, cp.quantite
    FROM site_commandeproduit cp
    INNER JOIN site_commande c
      ON c.id = cp.commande_id
    WHERE c.date_commande + 3/1440 < current_date -- La commande doit avoir été faite il y a 3 minutes
    GROUP BY cp.id, cp.quantite, cp.commande_id, cp.produit_id, cp.prix, cp.devise, c.adresse_livraison_id
    HAVING (cp.quantite - COALESCE((
      SELECT SUM(cpp.quantite) quantite_livree-- Recherche la quantité livrée de chaque produit
      FROM site_commandepaquetproduit cpp
      INNER JOIN site_commandepaquet cp2
        ON cp2.id = cpp.paquet_id
      WHERE cp2.commande_id = cp.commande_id
        AND cpp.produit_id = cp.produit_id), 0)) > 0 -- Sélectionne les produits non livrés
    ORDER BY c.adresse_livraison_id; -- Permet de limiter le nombre de livraison par adresse
    
  MaxPaquetsAdresse NUMBER;
  MaxProduitsPaquet NUMBER;
    
  NbrePaquetsAdresse NUMBER;
  
  CurAdresse NUMBER;
  ResteALivrer NUMBER;
  Quantite NUMBER;
BEGIN
  MaxPaquetsAdresse := 3;
  MaxProduitsPaquet := 5;
  CurAdresse := 0;
  
  FOR c in non_livres LOOP
    IF c.adresse_livraison_id != CurAdresse THEN -- Changement d'adresse 
      NbrePaquetsAdresse := 0;
    END IF;
    CurAdresse := c.adresse_livraison_id;
    
    ResteALivrer := c.quantite_restante;
    
    WHILE NbrePaquetsAdresse < MaxPaquetsAdresse AND ResteALivrer > 0 LOOP
      -- Peut encore envoyer un paquet à l'adresse
      INSERT INTO SITE_COMMANDEPAQUET VALUES (
        null, c.commande_id, 'ATTEN', current_date
      );
      INSERT INTO usa.SITE_COMMANDEPAQUET VALUES (
        null, c.commande_id, 'ATTEN', current_date
      );
      
      IF ResteALivrer > MaxProduitsPaquet THEN
        Quantite := MaxProduitsPaquet;
      ELSE
        Quantite := ResteALivrer;
      END IF;
      
      INSERT INTO site_commandepaquetproduit VALUES (
        null, SITE_COMMANDEPAQUET_SQ.CURRVAL, c.produit_id,
        quantite, c.prix, c.devise
      );
      INSERT INTO usa.site_commandepaquetproduit VALUES (
        null, SITE_COMMANDEPAQUET_SQ.CURRVAL, c.produit_id,
        quantite, c.prix, c.devise
      );
      
      ResteALivrer := ResteALivrer - quantite;
      NbrePaquetsAdresse := NbrePaquetsAdresse + 1;
    END LOOP;
    
  END LOOP;

  COMMIT;
END EMPAQUETERPAQUETS;

create or replace
TRIGGER REPLIC_UTILISATEUR_DELETE
AFTER DELETE ON SITE_UTILISATEUR
FOR EACH ROW
DECLARE
  VarExiste NUMBER;
BEGIN
  -- Supprime l'utilisateur de l'autre base s'il existe toujours
  SELECT COUNT(*) INTO VarExiste
  FROM site_utilisateur@usa
  WHERE login = :old.login;

  IF VarExiste > 0 THEN
    DELETE FROM site_utilisateur@usa
    WHERE login = :old.login;
  END IF;
END;

create or replace
TRIGGER "REPLIC_UTILISATEUR_INSERT" AFTER INSERT ON SITE_UTILISATEUR FOR EACH ROW
BEGIN
  IF :new.site_modification = 'BE' THEN
    INSERT INTO site_utilisateur@usa VALUES(
      :new.login, :new.mot_de_passe, :new.email, :new.admin, :new.nom,
      :new.prenom, :new.date_inscription, :new.site_modification,
      :new.mode_paiement, :new.mode_livraison
    );
  END IF;
END;

create or replace
TRIGGER REPLIC_UTILISATEUR_UPDATE
AFTER UPDATE ON SITE_UTILISATEUR FOR EACH ROW
BEGIN
  IF :new.site_modification = 'BE' THEN
    UPDATE site_utilisateur@usa SET login = :new.login,
      mot_de_passe = :new.mot_de_passe, email = :new.email,
      admin = :new.admin, nom = :new.nom, prenom = :new.prenom,
      date_inscription = :new.date_inscription,
      site_modification = :new.site_modification,
      mode_paiement = :new.mode_paiement, mode_livraison = :new.mode_livraison
    ;
  END IF;
END;

BEGIN
sys.dbms_scheduler.set_attribute_null( name => '"BE"."EMPAQUETAGE"', attribute => 'destination_name');
        SYS.DBMS_SCHEDULER.REMOVE_JOB_EMAIL_NOTIFICATION (    
             job_name => '"BE"."EMPAQUETAGE"'
             );
        SYS.DBMS_SCHEDULER.ADD_JOB_EMAIL_NOTIFICATION (    
             job_name => '"BE"."EMPAQUETAGE"', 
             recipients => '',
             sender => '',
             subject => 'Oracle Scheduler Job Notification - %job_owner%.%job_name%.%job_subname% %event_type%',
             body => 'Job: %job_owner%.%job_name%.%job_subname%
Event: %event_type%
Date: %event_timestamp%
Log id: %log_id%
Job class: %job_class_name%
Run count: %run_count%
Failure count: %failure_count%
Retry count: %retry_count%
Error code: %error_code
%Error message: %error_message%
',
             events => 'JOB_STARTED, JOB_BROKEN, JOB_CHAIN_STALLED, JOB_FAILED, JOB_OVER_MAX_DUR, JOB_SCH_LIM_REACHED',
                filter_condition =>  ''
             );

END; 