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
        END type_produit
        FROM be.site_produit p
      ) p_imp
      ON (p.ean = p_imp.ean)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        p_imp.ean, p_imp.titre, p_imp.type_produit, p_imp.langue, p_imp.prix,
        CASE 
          WHEN type_produit = 'LIVRE'-- Détecte l'auteur en fonction du type
            THEN (SELECT la.artiste_id
              FROM be.site_livre_auteurs la
              WHERE la.livre_id = p.ean
                AND ROWNUM = 1 
              )
          WHEN type_produit = 'FILM'
            THEN (SELECT fr.artiste_id
              FROM be.site_film_realisateurs fr
              WHERE fr.film_id = p.ean
                AND ROWNUM = 1 
              )
          WHEN type_produit = 'MUSIQUE'
            THEN (SELECT ma.artiste_id
              FROM be.site_musique_auteurs ma
              WHERE ma.musique_id = p.ean
                AND ROWNUM = 1 
              )
        END
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
        c_imp.id, c_imp.utilisateur_id, c_imp.produit_id, 'BE', c_imp.creation
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

  PROCEDURE ChargementUsa AS
  BEGIN
    MERGE INTO site_utilisateur u
    USING (SELECT * FROM usa.site_utilisateur) u_imp
      ON (u.login = u_imp.login)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        u_imp.login, u_imp.nom,u_imp.prenom, u_imp.date_inscription
      );
      
    MERGE INTO site_produit p
    USING (SELECT p.*, 'LIVRE' type, (SELECT la.artiste_id
          FROM usa.site_livre_auteurs la
          WHERE la.livre_id = p.ean
            AND ROWNUM = 1 
          ) auteur
        FROM usa.site_produit p
      ) p_imp
      ON (p.ean = p_imp.ean)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        p_imp.ean, p_imp.titre, p_imp.type, p_imp.langue, p_imp.prix, p_imp.auteur
      );
      
    MERGE INTO site_fournisseur f
    USING (SELECT * FROM usa.site_fournisseur) f_imp
      ON (f.id = f_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (f_imp.id, f_imp.nom);
      
    MERGE INTO site_commentaire c
    USING (SELECT * FROM usa.site_commentaire) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        c_imp.id, c_imp.utilisateur_id, c_imp.produit_id, 'USA', c_imp.creation
      );
            
    MERGE INTO site_commande c
    USING (SELECT * FROM usa.site_commande) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (c_imp.id, c_imp.utilisateur_id, 'USA', c_imp.date_commande);
      
    MERGE INTO site_commandeproduit c
    USING (SELECT cp.*, ( -- Inspecte si le produit est présent dans la liste d'envies
          SELECT COUNT(*)
          FROM usa.site_listeenviesproduit lp
          INNER JOIN usa.site_listeenvies l
            ON l.id = lp.liste_id
          WHERE l.utilisateur_id = c.utilisateur_id
            AND lp.produit_id = cp.produit_id
        ) dans_liste_envies
        FROM usa.site_commandeproduit cp
        INNER JOIN usa.site_commande c
          ON c.id = cp.commande_id
      ) c_imp
      ON (c.id = c_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        c_imp.id, c_imp.commande_id, c_imp.produit_id, c_imp.quantite,
        c_imp.dans_liste_envies, c_imp.prix , c_imp.devise
      );
  END ChargementUsa;

  PROCEDURE ChargementUk AS
  BEGIN
    MERGE INTO site_produit p
    USING (SELECT p.*, 'FILM' type, (SELECT fr.artiste_id
          FROM uk.site_film_realisateurs fr
          WHERE fr.film_id = p.ean
            AND ROWNUM = 1 
          ) auteur
        FROM uk.site_produit p
      ) p_imp
      ON (p.ean = p_imp.ean)
    WHEN NOT MATCHED THEN
      INSERT VALUES (
        p_imp.ean, p_imp.titre, p_imp.type, p_imp.langue, p_imp.prix, p_imp.auteur
      );
      
    MERGE INTO site_fournisseur f
    USING (SELECT * FROM uk.site_fournisseur) f_imp
      ON (f.id = f_imp.id)
    WHEN NOT MATCHED THEN
      INSERT VALUES (f_imp.id, f_imp.nom);
  END ChargementUk;

END CHARGEMENTDONNEES;

CREATE MATERIALIZED VIEW "MARKETING"."VUE_COMMENTAIRES_APPORTES" 
AS
  SELECT c.utilisateur_id login, c.origine, 
    c.creation datetime, p.ean, p.type media_type
  FROM site_commentaire c
  INNER JOIN site_produit p
    ON p.ean = c.produit_id;

CREATE MATERIALIZED VIEW "MARKETING"."VUE_FREQUENCE_STOCKS"
AS
  SELECT p.ean, p.type media_type, l.date_livraison datetime,
      l.origine
  FROM site_livraison l
  INNER JOIN site_produit p
  ON p.ean = l.produit_id;

CREATE MATERIALIZED VIEW "MARKETING"."VUE_VENTES"
AS
  SELECT p.ean, p.type media_type, p.auteur,
    c.date_commande datetime, c.origine, c.utilisateur_id login,
    c.id commande_id, cp.quantite, cp.dans_liste_envie
  FROM site_commande c
  INNER JOIN site_commandeproduit cp
  ON cp.commande_id = c.id
  INNER JOIN site_produit p
  ON p.ean = cp.produit_id;

CREATE MATERIALIZED VIEW "MARKETING"."VUE_VENTES_LIVRES"
AS
  SELECT c.date_commande datetime, c.origine, p.langue,
    cp.quantite,
    CASE
      WHEN p.type = 'LIVRE'
      THEN cp.quantite
      ELSE 0
    END quantite_livres
  FROM site_commande c
  INNER JOIN site_commandeproduit cp
    ON cp.commande_id = c.id
  INNER JOIN site_produit p
    ON p.ean = cp.produit_id;