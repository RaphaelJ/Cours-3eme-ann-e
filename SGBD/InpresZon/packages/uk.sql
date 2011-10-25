------------------------------------------------------
-- UK
------------------------------------------------------



INSERT INTO site_fournisseur VALUES (1, 'Amazon');

CREATE DATABASE LINK be CONNECT TO be IDENTIFIED BY pass USING 'oracle';

----------------------------------------------
-- DECLENCHEURS
----------------------------------------------

create or replace
TRIGGER REPLIC_FILMS_INSERT
AFTER INSERT ON SITE_FILM
DECLARE
  VarNbre NUMERIC;

  VarProduit SITE_PRODUIT%ROWTYPE;
  CURSOR CurProduit IS SELECT * INTO VarProduit
    FROM SITE_PRODUIT
    WHERE REPLIQUE = 0
    FOR UPDATE;

  VarFilm SITE_FILM%ROWTYPE;

  VarExiste NUMERIC;

  VarArtiste SITE_ARTISTE%ROWTYPE;

  CURSOR CurActeur(FilmEan SITE_FILM.produit_ptr_id%TYPE) IS
    SELECT artiste_id
    FROM SITE_FILM_ACTEURS fa
    WHERE fa.film_id = FilmEan;

  CURSOR CurRealisateur(FilmEan SITE_FILM.produit_ptr_id%TYPE) IS
    SELECT artiste_id
    FROM SITE_FILM_REALISATEURS fr
    WHERE fr.film_id = FilmEan;
BEGIN
  SELECT COUNT(*) INTO VarNbre
  FROM SITE_PRODUIT
  WHERE REPLIQUE = 0;

  IF VarNbre > 20 THEN -- Réplique toutes les 20 insertions
    FOR VarProduit IN CurProduit LOOP

      UPDATE SITE_PRODUIT SET REPLIQUE = 1 WHERE CURRENT OF CurProduit;

      INSERT INTO SITE_PRODUIT@be VALUES (
        VarProduit.ean, VarProduit.titre, VarProduit.description,
        VarProduit.langue, VarProduit.prix, VarProduit.stock,
        VarProduit.devise, VarProduit.fournisseur_id, VarProduit.origine, 1
      );

      -- Copie le film
      SELECT * INTO VarFilm
      FROM SITE_FILM
      WHERE produit_ptr_id = VarProduit.ean;

      -- Ajoute le studio s'il n'existe pas
      SELECT COUNT(*) INTO VarExiste
      FROM SITE_EDITEUR@be
      WHERE nom = VarFilm.studio_id;

      IF VarExiste = 0 THEN -- Le studio n'existe pas
        INSERT INTO SITE_EDITEUR@be VALUES (
          VarFilm.studio_id
        );
      END IF;

      -- Ajoute les acteurs s'ils n'existent pas
      FOR VarArtiste IN CurActeur(VarFilm.produit_ptr_id) LOOP
        SELECT COUNT(*) INTO VarExiste
        FROM SITE_ARTISTE@be
        WHERE nom = VarArtiste.artiste_id;

        IF VarExiste = 0 THEN -- L'acteur n'existe pas
          INSERT INTO SITE_ARTISTE@be
          VALUES (VarArtiste.artiste_id);
        END IF;
      END LOOP;

      -- Ajoute les réalisateurs s'ils n'existent pas
      FOR VarArtiste IN CurRealisateur(VarFilm.produit_ptr_id) LOOP
        SELECT COUNT(*) INTO VarExiste
        FROM SITE_ARTISTE@be
        WHERE nom = VarArtiste.artiste_id;

        IF VarExiste = 0 THEN -- Le réalisateur n'existe pas
          INSERT INTO SITE_ARTISTE@be
          VALUES (VarArtiste.artiste_id);
        END IF;
      END LOOP;

      INSERT INTO SITE_FILM@be VALUES (
        VarFilm.produit_ptr_id, VarFilm.studio_id, VarFilm.support,
        VarFilm.disques, VarFilm.notation, VarFilm.duree
      );

    END LOOP;
  END IF;
END;