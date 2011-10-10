INSERT INTO site_pays VALUES (1, 'Belgique', 'Belgium');
INSERT INTO site_pays VALUES (2, 'France', 'France');
INSERT INTO site_pays VALUES (3, 'Luxembourg', 'Luxembourg');
INSERT INTO site_pays VALUES (4, 'Etats-Unis', 'USA');
INSERT INTO site_pays VALUES (5, 'Royaume-Uni', 'UK');
INSERT INTO site_pays VALUES (6, 'Allemagne', 'Deutschland');
INSERT INTO site_pays VALUES (7, 'Pays-Bas', 'Nederlands');
INSERT INTO site_pays VALUES (8, 'Espagne', 'Spain');
INSERT INTO site_pays VALUES (9, 'Suède', 'Sweden');
INSERT INTO site_pays VALUES (10, 'Portugal', 'Portugal');

INSERT INTO site_addresse VALUES (1, '48 rue de la Belgique', '5485', 1);
INSERT INTO site_addresse VALUES (2, '17 rue de la France', '78852', 2);
INSERT INTO site_addresse VALUES (3, '87 rue du Luxembourg', 'A365', 3);
INSERT INTO site_addresse VALUES (4, '48 rue des USA', 'AX585', 4);
INSERT INTO site_addresse VALUES (5, '48 rue de la GB', 'AZ5485', 5);
INSERT INTO site_addresse VALUES (6, '48 rue de l''Allemagne', '5485', 6);
INSERT INTO site_addresse VALUES (7, '48 rue des Pays Bas', 'AZA5485', 7);
INSERT INTO site_addresse VALUES (8, '48 rue de l''Espagne', '5485', 8);
INSERT INTO site_addresse VALUES (9, '48 rue de la Suède', '8884', 9);
INSERT INTO site_addresse VALUES (10, '48 rue du Portugal', '5485', 10);
INSERT INTO site_addresse VALUES (11, '48 route de la Belgique', '5485', 1);
INSERT INTO site_addresse VALUES (12, '17 route de la France', '78852', 2);
INSERT INTO site_addresse VALUES (13, '87 route du Luxembourg', 'A365', 3);
INSERT INTO site_addresse VALUES (14, '48 route des USA', 'AX585', 4);
INSERT INTO site_addresse VALUES (15, '48 route de la GB', 'AZ5485', 5);
INSERT INTO site_addresse VALUES (16, '48 route de l''Allemagne', '5485', 6);
INSERT INTO site_addresse VALUES (17, '48 route des Pays Bas', 'AZA5485', 7);
INSERT INTO site_addresse VALUES (18, '48 route de l''Espagne', '5485', 8);
INSERT INTO site_addresse VALUES (19, '48 route de la Suède', '8884', 9);
INSERT INTO site_addresse VALUES (20, '48 route du Portugal', '5485', 10);


INSERT INTO site_utilisateur VALUES ('John', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'john@lennon.com', 1, 'John', 'Lennon', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Paul', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'paul@mccartney.com', 0, 'Paul', 'McCartney', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Ringo', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'rigo@starr.com', 0, 'Ringo', 'Starr', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('George', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'george@harisson', 0, 'George', 'Harison', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Jimmy', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'jimmy@page.com', 0, 'Jimmy', 'Page', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Robert', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'robert@plant.com', 0, 'Robert', 'Plant', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('JohnBonham', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'john@bonham.com', 0, 'John', 'Bonham', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Freddy', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'freddy@mercury.com', 0, 'Freddy', 'Mercury', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Mick', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'mick@jagger.com', 0, 'Mick', 'Jagger', '2012-10-01 12:42', '', '');
INSERT INTO site_utilisateur VALUES ('Keith', '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684', 'keith@richard.com', 0, 'Keith', 'Richard', '2012-10-01 12:42', '', '');

INSERT INTO site_utilisateur_adresses (1, 1, 1);
INSERT INTO site_utilisateur_adresses (2, 2, 2);
INSERT INTO site_utilisateur_adresses (3, 3, 3);
INSERT INTO site_utilisateur_adresses (4, 4, 4);
INSERT INTO site_utilisateur_adresses (5, 5, 5);
INSERT INTO site_utilisateur_adresses (6, 6, 6);
INSERT INTO site_utilisateur_adresses (7, 7, 7);
INSERT INTO site_utilisateur_adresses (8, 8, 8);
INSERT INTO site_utilisateur_adresses (9, 9, 9);
INSERT INTO site_utilisateur_adresses (10, 10, 10);
INSERT INTO site_utilisateur_adresses (1, 1, 11);
INSERT INTO site_utilisateur_adresses (2, 2, 12);
INSERT INTO site_utilisateur_adresses (3, 3, 13);
INSERT INTO site_utilisateur_adresses (4, 4, 14);
INSERT INTO site_utilisateur_adresses (5, 5, 15);
INSERT INTO site_utilisateur_adresses (6, 6, 16);
INSERT INTO site_utilisateur_adresses (7, 7, 17);
INSERT INTO site_utilisateur_adresses (8, 8, 18);
INSERT INTO site_utilisateur_adresses (9, 9, 19);
INSERT INTO site_utilisateur_adresses (10, 10, 20);

INSERT INTO site_fournisseur VALUES (1, 'Amazon', 1);
INSERT INTO site_fournisseur VALUES (2, 'LDLC', 2);
INSERT INTO site_fournisseur VALUES (3, 'Rue du Commerce', 3);
INSERT INTO site_fournisseur VALUES (4, 'Pixmania', 4);
INSERT INTO site_fournisseur VALUES (5, 'Ebay', 5);
INSERT INTO site_fournisseur VALUES (6, 'Fnac', 6);
INSERT INTO site_fournisseur VALUES (7, 'Alternate', 7);
INSERT INTO site_fournisseur VALUES (8, 'Kelkoo', 8);
INSERT INTO site_fournisseur VALUES (9, 'Cdiscount', 9);
INSERT INTO site_fournisseur VALUES (10, 'Oka', 10);

INSERT INTO site_artiste VALUES (1, 'John Lennon', 1);
INSERT INTO site_artiste VALUES (2, 'Paul McCartney', 2);
INSERT INTO site_artiste VALUES (3, 'Ringo Starr', 3);
INSERT INTO site_artiste VALUES (4, 'George Harisson', 4);
INSERT INTO site_artiste VALUES (5, 'Jimmy Page', 5);
INSERT INTO site_artiste VALUES (6, 'Robert Plant', 6);
INSERT INTO site_artiste VALUES (7, 'John Bonham', 7);
INSERT INTO site_artiste VALUES (8, 'Freddy Mercury', 8);
INSERT INTO site_artiste VALUES (9, 'Mick Jagger', 9);
INSERT INTO site_artiste VALUES (10, 'Keith Richard', 10);

INSERT INTO site_editeur VALUES ('Universal');
INSERT INTO site_editeur VALUES ('Paramount');
INSERT INTO site_editeur VALUES ('Vivendi');
INSERT INTO site_editeur VALUES ('Galimard');
INSERT INTO site_editeur VALUES ('Sony');
INSERT INTO site_editeur VALUES ('EMI');
INSERT INTO site_editeur VALUES ('DreamWorks');
INSERT INTO site_editeur VALUES ('Warner Music');
INSERT INTO site_editeur VALUES ('Universal Music');
INSERT INTO site_editeur VALUES ('Warner Bros');