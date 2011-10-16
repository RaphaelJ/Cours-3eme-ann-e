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

  FUNCTION UtilisateurExiste(
    ArgLogin SITE_UTILISATEUR.login%TYPE,
    ArgMotDePasse SITE_UTILISATEUR.mot_de_passe%TYPE,
    ArgEmail SITE_UTILISATEUR.email%TYPE,
    ArgAdmin SITE_UTILISATEUR.admin%TYPE,
    ArgNom SITE_UTILISATEUR.nom%TYPE,
    ArgPrenom SITE_UTILISATEUR.prenom%TYPE,
    ArgDateInscription SITE_UTILISATEUR.date_inscription%TYPE,
    ArgModePaiement SITE_UTILISATEUR.mode_paiement%TYPE,
    ArgModeLivraison SITE_UTILISATEUR.mode_livraison%TYPE
  ) RETURN NUMERIC;

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
      nom, prenom, current_timestamp, null, null);
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
                                prenom = ArgPrenom
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

  FUNCTION UtilisateurExiste(
    ArgLogin SITE_UTILISATEUR.login%TYPE,
    ArgMotDePasse SITE_UTILISATEUR.mot_de_passe%TYPE,
    ArgEmail SITE_UTILISATEUR.email%TYPE,
    ArgAdmin SITE_UTILISATEUR.admin%TYPE,
    ArgNom SITE_UTILISATEUR.nom%TYPE,
    ArgPrenom SITE_UTILISATEUR.prenom%TYPE,
    ArgDateInscription SITE_UTILISATEUR.date_inscription%TYPE,
    ArgModePaiement SITE_UTILISATEUR.mode_paiement%TYPE,
    ArgModeLivraison SITE_UTILISATEUR.mode_livraison%TYPE
  )
  RETURN NUMERIC AS
    VarExiste NUMERIC;
  BEGIN
    SELECT COUNT(*) INTO VarExiste
    FROM SITE_UTILISATEUR
    WHERE login = ArgLogin AND mot_de_passe = ArgMotDePasse AND email = ArgEmail
      AND admin = ArgAdmin AND nom = ArgNom AND prenom = ArgPrenom
      AND date_inscription = ArgDateInscription
      AND mode_paiement = ArgModePaiement AND mode_livraison = ArgModeLivraison;

    RETURN VarExiste;
  END UtilisateurExiste;

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

CREATE DATABASE LINK usa CONNECT TO usa IDENTIFIED BY pass USING 'oracle';

CREATE DATABASE LINK be CONNECT TO be IDENTIFIED BY pass USING 'oracle';