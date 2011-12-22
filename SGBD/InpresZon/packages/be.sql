------------------------------------------------------
-- BE
------------------------------------------------------

INSERT INTO site_fournisseur VALUES (1, 'Amazon');

CREATE DATABASE LINK usa CONNECT TO usa IDENTIFIED BY pass USING 'oracle';

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
      RAISE_APPLICATION_ERROR(-20001, 'L''email ou le login existe déjà');
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
  END Utilisateur;
END GESTION_UTILISATEURS;

----------------------------------------------
-- DECLENCHEURS
----------------------------------------------

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

create or replace
PACKAGE GESTION_CATALOGUE AS 
  TYPE ProduitsResultSet IS REF CURSOR RETURN site_produit%ROWTYPE;
  
  FUNCTION Produits(ArgPage IN INTEGER)
  RETURN ProduitsResultSet;
  
  FUNCTION Fournisseur(ArgId IN SITE_FOURNISSEUR.id%TYPE)
  RETURN SITE_FOURNISSEUR%ROWTYPE;
  
  FUNCTION NbreProduits
  RETURN INTEGER;

  FUNCTION Produit(ArgEan IN SITE_PRODUIT.ean%TYPE)
  RETURN SITE_PRODUIT%ROWTYPE;
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
  
  FUNCTION Fournisseur(ArgId IN SITE_FOURNISSEUR.id%TYPE)
  RETURN SITE_FOURNISSEUR%ROWTYPE IS
    Fournisseur SITE_FOURNISSEUR%ROWTYPE;
  BEGIN
    SELECT * INTO Fournisseur
    FROM SITE_FOURNISSEUR
    WHERE id = ArgId;
  
    RETURN Fournisseur;
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
  END Produit;
END GESTION_CATALOGUE;

create or replace
PACKAGE GESTION_COMMANDES AS 
  TYPE CaddieResultSet IS REF CURSOR;
  TYPE CommandesResultSet IS REF CURSOR RETURN site_commande%ROWTYPE;

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
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE
  );
  
  FUNCTION HistoriqueCommandes(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN CommandesResultSet;
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
    ArgUtilisateur SITE_UTILISATEUR.login%TYPE
  ) IS
  BEGIN
    IF CaddieEnStock(ArgUtilisateur) = 0 THEN
      IF NbreProduitsCaddie(ArgUtilisateur) > 0 THEN
        INSERT INTO SITE_COMMANDE
        VALUES (null, ArgUtilisateur, null, CURRENT_DATE);
        
        INSERT INTO site_commandeproduit
          (SELECT null, SITE_COMMANDE_SQ.CURRVAL, cp.produit_id, 
           cp.quantite, p.prix, p.devise
           FROM SITE_CADDIEPRODUIT cp
           INNER JOIN SITE_PRODUIT p
              ON p.ean = cp.produit_id
           WHERE cp.Utilisateur_id = ArgUtilisateur
        );
        
        DELETE FROM SITE_CADDIEPRODUIT
        WHERE Utilisateur_id = ArgUtilisateur;
      ELSE
        RAISE_APPLICATION_ERROR (-20002, 'Le caddie est vide');
      END IF;
    ELSE
      RAISE_APPLICATION_ERROR (-20001, 'Certains produits ne sont pas en stock');
    END IF;
  END PasserCommande;
  
  FUNCTION HistoriqueCommandes(
    ArgUtilisateur IN SITE_commande.utilisateur_id%TYPE
  ) RETURN CommandesResultSet IS
    curs CaddieResultSet;
    ARTICLES_PAGE NUMBER;
  BEGIN
    ARTICLES_PAGE := 5;
  
    OPEN curs FOR
      SELECT *
      FROM site_commande
      WHERE utilisateur_id = ArgUtilisateur;
      
    RETURN curs;
  END HistoriqueCommandes;
END GESTION_COMMANDES;