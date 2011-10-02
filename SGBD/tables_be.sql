CREATE TABLE "site_pays" (
    "id" integer NOT NULL PRIMARY KEY,
    "nom_fr" varchar(30) NOT NULL,
    "nom_en" varchar(30) NOT NULL
)
;
CREATE TABLE "site_adresse" (
    "id" integer NOT NULL PRIMARY KEY,
    "adresse" varchar(255) NOT NULL,
    "ville" varchar(30) NOT NULL,
    "code_postal" varchar(10) NOT NULL,
    "pays_id" integer NOT NULL REFERENCES "site_pays" ("id")
)
;
CREATE TABLE "site_utilisateur_adresses" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(30) NOT NULL,
    "adresse_id" integer NOT NULL REFERENCES "site_adresse" ("id"),
    UNIQUE ("utilisateur_id", "adresse_id")
)
;
CREATE TABLE "site_utilisateur" (
    "login" varchar(30) NOT NULL PRIMARY KEY,
    "mot_de_passe" varchar(64) NOT NULL,
    "email" varchar(75) NOT NULL UNIQUE,
    "admin" bool NOT NULL,
    "nom" varchar(30) NOT NULL,
    "prenom" varchar(30) NOT NULL,
    "date_inscription" datetime NOT NULL,
    "mode_paiement" varchar(4) NOT NULL,
    "mode_livraison" varchar(4) NOT NULL
)
;
CREATE TABLE "site_session" (
    "cle" varchar(40) NOT NULL PRIMARY KEY,
    "donnees" text NOT NULL,
    "expiration" datetime NOT NULL
)
;
CREATE TABLE "site_fournisseur" (
    "id" integer NOT NULL PRIMARY KEY,
    "nom" varchar(30) NOT NULL,
    "adresse_id" integer NOT NULL REFERENCES "site_adresse" ("id")
)
;
CREATE TABLE "site_produit" (
    "ean" bigint NOT NULL PRIMARY KEY,
    "titre" varchar(30) NOT NULL,
    "description" text NOT NULL,
    "langue" varchar(2) NOT NULL,
    "prix" decimal NOT NULL,
    "stock" integer NOT NULL,
    "devise" varchar(3) NOT NULL,
    "fournisseur_id" integer NOT NULL REFERENCES "site_fournisseur" ("id"),
    "origine" varchar(3) NOT NULL
)
;
CREATE TABLE "site_artiste" (
    "id" integer NOT NULL PRIMARY KEY,
    "nom" varchar(30) NOT NULL,
    "nationalite_id" integer NOT NULL REFERENCES "site_pays" ("id")
)
;
CREATE TABLE "site_editeur" (
    "nom" varchar(30) NOT NULL PRIMARY KEY
)
;
CREATE TABLE "site_livre_auteurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "livre_id" bigint NOT NULL,
    "artiste_id" integer NOT NULL REFERENCES "site_artiste" ("id"),
    UNIQUE ("livre_id", "artiste_id")
)
;
CREATE TABLE "site_livre" (
    "produit_ptr_id" bigint NOT NULL PRIMARY KEY REFERENCES "site_produit" ("ean"),
    "isbn" varchar(13) NOT NULL UNIQUE,
    "editeur_id" varchar(30) NOT NULL REFERENCES "site_editeur" ("nom"),
    "reliure" varchar(30) NOT NULL,
    "pages" integer,
    "publication" date,
    "num_edition" integer
)
;
CREATE TABLE "site_film_acteurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "film_id" bigint NOT NULL,
    "artiste_id" integer NOT NULL REFERENCES "site_artiste" ("id"),
    UNIQUE ("film_id", "artiste_id")
)
;
CREATE TABLE "site_film" (
    "produit_ptr_id" bigint NOT NULL PRIMARY KEY REFERENCES "site_produit" ("ean"),
    "realisateurs_id" integer NOT NULL REFERENCES "site_artiste" ("id"),
    "studio_id" varchar(30) NOT NULL REFERENCES "site_editeur" ("nom"),
    "support" varchar(4) NOT NULL,
    "disques" integer,
    "notation" varchar(5) NOT NULL,
    "duree" integer
)
;
CREATE TABLE "site_musique_auteurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "musique_id" bigint NOT NULL,
    "artiste_id" integer NOT NULL REFERENCES "site_artiste" ("id"),
    UNIQUE ("musique_id", "artiste_id")
)
;
CREATE TABLE "site_musique" (
    "produit_ptr_id" bigint NOT NULL PRIMARY KEY REFERENCES "site_produit" ("ean"),
    "label_id" varchar(30) REFERENCES "site_editeur" ("nom"),
    "support" varchar(4) NOT NULL,
    "disques" integer,
    "publication" date
)
;
CREATE TABLE "site_caddieproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(30) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "produit_id" bigint NOT NULL REFERENCES "site_produit" ("ean"),
    "quantite" integer NOT NULL,
    UNIQUE ("utilisateur_id", "produit_id")
)
;
CREATE TABLE "site_listeenvies" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(30) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "nom" varchar(30) NOT NULL,
    UNIQUE ("utilisateur_id", "nom")
)
;
CREATE TABLE "site_listeenviesproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "liste_id" integer NOT NULL REFERENCES "site_listeenvies" ("id"),
    "produit_id" bigint NOT NULL REFERENCES "site_produit" ("ean"),
    "quantite" integer NOT NULL,
    UNIQUE ("liste_id", "produit_id")
)
;
CREATE TABLE "site_commande" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(30) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "adresse_livraison_id" integer NOT NULL REFERENCES "site_adresse" ("id"),
    "date_commande" datetime NOT NULL
)
;
CREATE TABLE "site_commandeproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "commande_id" integer NOT NULL REFERENCES "site_commande" ("id"),
    "produit_id" bigint NOT NULL REFERENCES "site_produit" ("ean"),
    "quantite" integer NOT NULL,
    "prix" decimal NOT NULL,
    "devise" varchar(3) NOT NULL,
    UNIQUE ("commande_id", "produit_id")
)
;
CREATE TABLE "site_commandepaquet" (
    "id" integer NOT NULL PRIMARY KEY,
    "commande_id" integer NOT NULL REFERENCES "site_commande" ("id"),
    "status" varchar(5) NOT NULL,
    "status_changement" datetime NOT NULL
)
;
CREATE TABLE "site_commandepaquetproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "paquet_id" integer NOT NULL REFERENCES "site_commandepaquet" ("id"),
    "produit_id" bigint NOT NULL REFERENCES "site_produit" ("ean"),
    "quantite" integer NOT NULL,
    "prix" decimal NOT NULL,
    "devise" varchar(3) NOT NULL,
    UNIQUE ("paquet_id", "produit_id")
)
;

CREATE INDEX "site_artiste_a05fba75" ON "site_artiste" ("nationalite_id");
CREATE INDEX "site_livre_10264149" ON "site_livre" ("editeur_id");
CREATE INDEX "site_film_88bbf040" ON "site_film" ("realisateurs_id");
CREATE INDEX "site_film_c86a621c" ON "site_film" ("studio_id");
CREATE INDEX "site_musique_63868627" ON "site_musique" ("label_id");
CREATE INDEX "site_caddieproduit_5f9483ee" ON "site_caddieproduit" ("utilisateur_id");
CREATE INDEX "site_caddieproduit_e04c88f4" ON "site_caddieproduit" ("produit_id");
CREATE INDEX "site_listeenvies_5f9483ee" ON "site_listeenvies" ("utilisateur_id");
CREATE INDEX "site_listeenviesproduit_7e1a3fb0" ON "site_listeenviesproduit" ("liste_id");
CREATE INDEX "site_commande_5f9483ee" ON "site_commande" ("utilisateur_id");
CREATE INDEX "site_commandeproduit_5152678" ON "site_commandeproduit" ("commande_id");
CREATE INDEX "site_commandepaquet_5152678" ON "site_commandepaquet" ("commande_id");
CREATE INDEX "site_commandepaquetproduit_27d26bca" ON "site_commandepaquetproduit" ("paquet_id");