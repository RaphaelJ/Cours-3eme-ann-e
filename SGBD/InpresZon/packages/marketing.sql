------------------------------------------------------
-- MARKETING
------------------------------------------------------

CREATE DATABASE LINK be CONNECT TO be IDENTIFIED BY pass USING 'orcl';
CREATE DATABASE LINK usa CONNECT TO usa IDENTIFIED BY pass USING 'orcl';
CREATE DATABASE LINK uk CONNECT TO uk IDENTIFIED BY pass USING 'orcl';

CREATE OR REPLACE 
PACKAGE CHARGEMENTDONNEES AS 
  PROCEDURE ChargementBe;
  PROCEDURE ChargementUk;
END CHARGEMENTDONNEES;

create or replace
PACKAGE BODY CHARGEMENTDONNEES AS

  PROCEDURE ChargementBe AS
  BEGIN
    MERGE INTO site_utilisateur u
    USING (SELECT * FROM be.site_utilisateur) u_imp
      ON (u.login = u_imp.login)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        u_imp.login, u_imp.nom,u_imp.prenom, u_imp.date_inscription
      );
      
    MERGE INTO site_produit p
    USING (SELECT p.*, CASE 
          WHEN EXISTS(SELECT * -- Détecte le type du produit
            FROM be.site_livre
            WHERE produit_ptr_id = p.ean
          ) THEN 'LIVRE'
          WHEN EXISTS(SELECT *
            FROM be.site_film
            WHERE produit_ptr_id = p.ean
          ) THEN 'FILM'
          WHEN EXISTS(SELECT *
            FROM be.site_musique
            WHERE produit_ptr_id = p.ean
          ) THEN 'MUSIQUE'
        END type
        FROM be.site_produit p
      ) p_imp
      ON (p.ean = p_imp.ean)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        p_imp.ean, p_imp.titre, p_imp.type, p_imp.langue, p_imp.prix
      );
      
    MERGE INTO site_fournisseur f
    USING (SELECT * FROM be.site_fournisseur) f_imp
      ON (f.id = f_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (f_imp.id, f_imp.nom);
      
    MERGE INTO site_commentaire c
    USING (SELECT * FROM be.site_commentaire) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        null, c_imp.utilisateur_id, c_imp.produit_id, 'BE', c_imp.creation
      );
            
    MERGE INTO site_commande c
    USING (SELECT * FROM be.site_commande) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (c_imp.id, c_imp.utilisateur_id, 'BE', c_imp.date_commande);
      
    MERGE INTO site_commandeproduit c
    USING (SELECT cp.*, ( -- Inspecte si le produit est présent dans la liste d'envies
          SELECT COUNT(*)
          FROM be.site_listeenviesproduit lp
          INNER JOIN be.site_listeenvies l
            ON l.id = lp.liste_id
          WHERE l.utilisateur_id = c.utilisateur_id
            AND lp.produit_id = cp.produit_id
        ) dans_liste_envies
        FROM be.site_commandeproduit cp
        INNER JOIN be.site_commande c
          ON c.id = cp.commande_id
      ) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        c_imp.id, c_imp.commande_id, c_imp.produit_id, c_imp.quantite,
        c_imp.dans_liste_envies, c_imp.prix , c_imp.devise
      );
  END ChargementBe;

  PROCEDURE ChargementUk AS
  BEGIN
    MERGE INTO site_produit p
    USING (SELECT p.*, 'FILM' type
        FROM uk.site_produit p
      ) p_imp
      ON (p.ean = p_imp.ean)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        p_imp.ean, p_imp.titre, p_imp.type, p_imp.langue, p_imp.prix
      );
      
    MERGE INTO site_fournisseur f
    USING (SELECT * FROM uk.site_fournisseur) f_imp
      ON (f.id = f_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (f_imp.id, f_imp.nom);
  END ChargementUk;

END CHARGEMENTDONNEES;

CREATE MATERIALIZED VIEW "MARKETING"."VUE_COMMENTAIRES_APPORTES" ("LOGIN", "TYPE", "DATE_COMMANDE") ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "USERS" BUILD IMMEDIATE USING INDEX REFRESH FORCE ON DEMAND USING DEFAULT LOCAL ROLLBACK SEGMENT USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
AS
  SELECT c.utilisateur_id login,
    p.type,
    c.creation date_commande
  FROM site_commentaire c
  INNER JOIN site_produit p
  ON p.ean = c.produit_id;
  COMMENT ON MATERIALIZED VIEW "MARKETING"."VUE_COMMENTAIRES_APPORTES"
IS
  'snapshot table for snapshot MARKETING.VUE_COMMENTAIRES_APPORTES';

CREATE MATERIALIZED VIEW "MARKETING"."VUE_FREQUENCE_STOCKS" ("EAN", "TYPE", "DATE_COMMANDE") ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING TABLESPACE "USERS" BUILD IMMEDIATE USING INDEX REFRESH FORCE ON DEMAND USING DEFAULT LOCAL ROLLBACK SEGMENT USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
AS
  SELECT p.ean,
    p.type,
    l.date_inscription date_commande
  FROM site_livraison l
  INNER JOIN site_produit p
  ON p.ean = l.produit_id;
  COMMENT ON MATERIALIZED VIEW "MARKETING"."VUE_FREQUENCE_STOCKS"
IS
  'snapshot table for snapshot MARKETING.VUE_FREQUENCE_STOCKS';

CREATE MATERIALIZED VIEW "MARKETING"."VUE_VENTES" ("QUANTITE", "DANS_LISTE_ENVIE", "EAN", "TYPE", "ORIGINE", "DATE_COMMANDE", "LOGIN") ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645 PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) TABLESPACE "USERS" BUILD IMMEDIATE USING INDEX REFRESH FORCE ON DEMAND USING DEFAULT LOCAL ROLLBACK SEGMENT USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
AS
  SELECT cp.quantite,
    cp.dans_liste_envie,
    p.ean,
    p.type,
    c.origine,
    c.date_commande,
    c.utilisateur_id login
  FROM site_commande c
  INNER JOIN site_commandeproduit cp
  ON cp.commande_id = c.id
  INNER JOIN site_produit p
  ON p.ean = cp.produit_id;
  COMMENT ON MATERIALIZED VIEW "MARKETING"."VUE_VENTES"
IS
  'snapshot table for snapshot MARKETING.VUE_VENTES';

CREATE MATERIALIZED VIEW "MARKETING"."VUE_VENTES_LIVRES" ("QUANTITE", "QUANTITE_LIVRES", "LANGUE", "ORIGINE", "DATE_COMMANDE") ORGANIZATION HEAP PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING TABLESPACE "USERS" BUILD IMMEDIATE USING INDEX REFRESH FORCE ON DEMAND USING DEFAULT LOCAL ROLLBACK SEGMENT USING ENFORCED CONSTRAINTS DISABLE QUERY REWRITE
AS
  SELECT cp.quantite,
    CASE
      WHEN p.type = 'LIVRE'
      THEN cp.quantite
      ELSE 0
    END quantite_livres,
    p.langue,
    c.origine,
    c.date_commande
  FROM site_commande c
  INNER JOIN site_commandeproduit cp
  ON cp.commande_id = c.id
  INNER JOIN site_produit p
  ON p.ean     = cp.produit_id
  WHERE p.type = 'LIVRE';
  COMMENT ON MATERIALIZED VIEW "MARKETING"."VUE_VENTES_LIVRES"
IS
  'snapshot table for snapshot MARKETING.VUE_VENTES_LIVRES';