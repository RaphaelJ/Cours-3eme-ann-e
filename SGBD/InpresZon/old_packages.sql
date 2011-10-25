-- BE
------------------------------------------------------

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

INSERT INTO site_fournisseur VALUES (1, 'Amazon');

CREATE DATABASE LINK usa CONNECT TO usa IDENTIFIED BY pass USING 'oracle';

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

-- USA
--------------------------------------------------------

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
      nom, prenom, current_timestamp, 'USA', null, null);
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
                                prenom = ArgPrenom, site_modification = 'USA'
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

CREATE DATABASE LINK be CONNECT TO be IDENTIFIED BY pass USING 'oracle';

create or replace
TRIGGER REPLIC_UTILISATEUR_DELETE
AFTER DELETE ON SITE_UTILISATEUR
FOR EACH ROW
DECLARE
  VarExiste NUMBER;
BEGIN
  -- Supprime l'utilisateur de l'autre base s'il existe toujours
  SELECT COUNT(*) INTO VarExiste
  FROM site_utilisateur@be
  WHERE login = :old.login;

  IF VarExiste > 0 THEN
    DELETE FROM site_utilisateur@be
    WHERE login = :old.login;
  END IF;
END;

create or replace
TRIGGER "REPLIC_UTILISATEUR_INSERT" AFTER INSERT ON SITE_UTILISATEUR FOR EACH ROW
BEGIN
  IF :new.site_modification = 'USA' THEN
    INSERT INTO site_utilisateur@be VALUES(
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
  IF :new.site_modification = 'USA' THEN
    UPDATE site_utilisateur@be SET login = :new.login,
      mot_de_passe = :new.mot_de_passe, email = :new.email,
      admin = :new.admin, nom = :new.nom, prenom = :new.prenom,
      date_inscription = :new.date_inscription,
      site_modification = :new.site_modification,
      mode_paiement = :new.mode_paiement, mode_livraison = :new.mode_livraison
    ;
  END IF;
END;

create or replace
TRIGGER REPLIC_LIVRES_INSERT
AFTER INSERT ON SITE_LIVRE FOR EACH ROW
DECLARE
  VarNbre NUMERIC;

  VarProduit SITE_PRODUIT%ROWTYPE;
  VarLivre SITE_LIVRE%ROWTYPE;

  CURSOR CurProduit IS SELECT * INTO VarProduit
    FROM SITE_PRODUIT
    WHERE REPLIQUE = 0
    FOR UPDATE;
BEGIN
  SELECT COUNT(*) INTO VarNbre
  FROM SITE_PRODUIT
  WHERE REPLIQUE = 0;

  IF VarNbre > 20 THEN -- Réplique toutes les 20 insertions
    OPEN CurProduit;
    LOOP
      FETCH CurProduit INTO VarProduit;
      EXIT WHEN CurProduit%NOTFOUND;

      UPDATE SITE_PRODUIT SET REPLIQUE = 1 WHERE CURRENT OF CurProduit;

      INSERT INTO SITE_PRODUIT@be VALUES (
        VarProduit.ean, VarProduit.titre, VarProduit.description,
        VarProduit.langue, VarProduit.prix, VarProduit.stock,
        VarProduit.devise, VarProduit.fournisseur_id, VarProduit.origine, 1
      );

      SELECT * INTO VarLivre
      FROM SITE_LIVRE
      WHERE produit_ptr_id = VarProduit.ean;

      INSERT INTO SITE_LIVRE@be VALUES (
        VarLivre.produit_ptr_id, VarLivre.isbn, VarLivre.editeur_id,
        VarLivre.reliure, VarLivre.pages, VarLivre.publication,
        VarLivre.num_edition
      );

    END LOOP;
    CLOSE CurProduit;
  END IF;
END;