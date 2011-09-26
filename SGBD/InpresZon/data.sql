INSERT INTO site_artiste VALUES (4, 'Led Zeppelin', '', 'Grande Bretagne');
INSERT INTO site_artiste VALUES (5, 'Leonardo', 'Dicaprio', 'Grande Bretagne');
INSERT INTO site_artiste VALUES (6, 'Toto', 'La', 'Grande Bretagne');

INSERT INTO site_editeur VALUES ('Atlantic');
INSERT INTO site_editeur VALUES ('Paramount Pictures');
INSERT INTO site_editeur VALUES ('Galimard');

INSERT INTO site_produit VALUES (3, 'Imagine', 'Imagine', 'EN', 12.12, 'EUR', 1);
INSERT INTO site_livre VALUES (3, 'aaaaaaaaaaaa', 3, 'collage', 120, '2004-12-12', 1);
INSERT INTO site_livre_auteurs VALUES (5, 3, 6);
INSERT INTO site_livre_auteurs VALUES (50, 3, 5);

INSERT INTO site_produit VALUES (4, 'Led Zeppelin IV', 'CD, Enregistrement original remasterisé', 'EN', 5.99, 'USD', 17);
INSERT INTO site_musique VALUES (4, 4, 'CD', 1, '1994-07-1');
INSERT INTO site_musique_auteurs VALUES (4, 4, 4);

INSERT INTO site_produit VALUES (5, 'Shutter Island', 'En 1954, le marshal Teddy Daniels et son coéquipier Chuck Aule sont envoyés enquêter', 'FR', 13.64, 'EUR', 548);
INSERT INTO site_film VALUES (5, 5, 'BLR', 1, 'PG-13', 158);
INSERT INTO site_film_acteurs VALUES (5, 5, 5);
INSERT INTO site_film_acteurs VALUES (50, 5, 4);
INSERT INTO site_film_realisateurs VALUES (5, 5, 5);

INSERT INTO site_produit VALUES (6, 'Nothin', 'Nothin', 'FR', 17.42, 'EUR', 85);
INSERT INTO site_livre VALUES (6, 'adzdazdzazdazd', 6, 'collage', 120, '2004-12-12', 1);
INSERT INTO site_livre_auteurs VALUES (6, 6, 6);

SELECT * FROM site_pays;
SELECT * FROM site_caddieproduit;
SELECT * FROM site_livre;
SELECT * FROM site_film;
SELECT * FROM site_produit;

SELECT * FROM site_utilisateur;

SELECT * FROM site_livre_auteurs;
SELECT * FROM site_session;
SELECT * FROM site_utilisateur;