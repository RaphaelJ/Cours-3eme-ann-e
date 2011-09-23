BEGIN;
CREATE TABLE "site_utilisateur" (
    "login" varchar(40) NOT NULL PRIMARY KEY,
    "mot_de_passe" varchar(40) NOT NULL,
    "email" varchar(75) NOT NULL UNIQUE,
    "nom" varchar(40) NOT NULL,
    "prenom" varchar(40) NOT NULL,
    "date_inscription" datetime NOT NULL,
    "adresse" varchar(255),
    "ville" varchar(40),
    "code_postal" varchar(10),
    "pays_id" varchar(40),
    "mode_paiement" varchar(4),
    "mode_livraison" varchar(4)
)
;
CREATE TABLE "site_pays" (
    "nom" varchar(40) NOT NULL PRIMARY KEY
)
;
CREATE TABLE "site_produit" (
    "id" integer NOT NULL PRIMARY KEY,
    "titre" varchar(40) NOT NULL,
    "description" text NOT NULL,
    "langue" varchar(2) NOT NULL,
    "prix" decimal NOT NULL,
    "devise" varchar(3) NOT NULL,
    "stock" integer NOT NULL
)
;
CREATE TABLE "site_livre_auteur" (
    "id" integer NOT NULL PRIMARY KEY,
    "livre_id" varchar(13) NOT NULL,
    "artiste_id" integer NOT NULL,
    UNIQUE ("livre_id", "artiste_id")
)
;
CREATE TABLE "site_livre" (
    "produit_ptr_id" integer NOT NULL UNIQUE REFERENCES "site_produit" ("id"),
    "isbn" varchar(13) NOT NULL PRIMARY KEY,
    "editeur_id" integer NOT NULL,
    "reliure" varchar(40) NOT NULL,
    "pages" integer,
    "publication" date,
    "num_edition" integer
)
;
CREATE TABLE "site_film_acteurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "film_id" integer NOT NULL,
    "artiste_id" integer NOT NULL,
    UNIQUE ("film_id", "artiste_id")
)
;
CREATE TABLE "site_film_realisateurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "film_id" integer NOT NULL,
    "artiste_id" integer NOT NULL,
    UNIQUE ("film_id", "artiste_id")
)
;
CREATE TABLE "site_film" (
    "produit_ptr_id" integer NOT NULL PRIMARY KEY REFERENCES "site_produit" ("id"),
    "studio_id" integer NOT NULL,
    "support" varchar(4),
    "disques" integer,
    "notation" varchar(5),
    "duree" integer
)
;
CREATE TABLE "site_musique_auteurs" (
    "id" integer NOT NULL PRIMARY KEY,
    "musique_id" integer NOT NULL,
    "artiste_id" integer NOT NULL,
    UNIQUE ("musique_id", "artiste_id")
)
;
CREATE TABLE "site_musique" (
    "produit_ptr_id" integer NOT NULL PRIMARY KEY REFERENCES "site_produit" ("id"),
    "label_id" integer,
    "support" varchar(4),
    "disques" integer,
    "publication" date
)
;
CREATE TABLE "site_artiste" (
    "id" integer NOT NULL PRIMARY KEY,
    "nom" varchar(40) NOT NULL,
    "prenom" varchar(40) NOT NULL,
    "nationalite_id" varchar(40) NOT NULL REFERENCES "site_pays" ("nom")
)
;
CREATE TABLE "site_editeur" (
    "id" integer NOT NULL PRIMARY KEY,
    "nom" varchar(40) NOT NULL
)
;
CREATE TABLE "site_caddieproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(40) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "produit_id" integer NOT NULL REFERENCES "site_produit" ("id"),
    "quantite" integer NOT NULL,
    UNIQUE ("utilisateur_id", "produit_id")
)
;
CREATE TABLE "site_listeenvies" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(40) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "nom" varchar(40) NOT NULL
)
;
CREATE TABLE "site_listeenviesproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "liste_id" integer NOT NULL REFERENCES "site_listeenvies" ("id"),
    "produit_id" integer NOT NULL REFERENCES "site_produit" ("id"),
    "quantite" integer NOT NULL,
    UNIQUE ("liste_id", "produit_id")
)
;
CREATE TABLE "site_commande" (
    "id" integer NOT NULL PRIMARY KEY,
    "utilisateur_id" varchar(40) NOT NULL REFERENCES "site_utilisateur" ("login"),
    "adresse" varchar(255) NOT NULL,
    "ville" varchar(40) NOT NULL,
    "code_postal" varchar(10) NOT NULL,
    "pays_id" varchar(40) NOT NULL REFERENCES "site_pays" ("nom")
)
;
CREATE TABLE "site_commandepaquet" (
    "id" integer NOT NULL PRIMARY KEY,
    "commande_id" integer NOT NULL REFERENCES "site_commande" ("id"),
    "status" varchar(5) NOT NULL,
    "status_change" datetime NOT NULL
)
;
CREATE TABLE "site_commandeproduit" (
    "id" integer NOT NULL PRIMARY KEY,
    "paquet_id" integer NOT NULL REFERENCES "site_commandepaquet" ("id"),
    "produit_id" integer NOT NULL REFERENCES "site_produit" ("id"),
    "quantite" integer NOT NULL,
    "prix" decimal NOT NULL,
    "devise" varchar(3) NOT NULL,
    UNIQUE ("paquet_id", "produit_id")
)
;
CREATE INDEX "site_utilisateur_f307a6c3" ON "site_utilisateur" ("pays_id");
CREATE INDEX "site_livre_10264149" ON "site_livre" ("editeur_id");
CREATE INDEX "site_film_c86a621c" ON "site_film" ("studio_id");
CREATE INDEX "site_musique_63868627" ON "site_musique" ("label_id");
CREATE INDEX "site_artiste_a05fba75" ON "site_artiste" ("nationalite_id");
CREATE INDEX "site_caddieproduit_5f9483ee" ON "site_caddieproduit" ("utilisateur_id");
CREATE INDEX "site_caddieproduit_e04c88f4" ON "site_caddieproduit" ("produit_id");
CREATE INDEX "site_listeenvies_5f9483ee" ON "site_listeenvies" ("utilisateur_id");
CREATE INDEX "site_listeenviesproduit_7e1a3fb0" ON "site_listeenviesproduit" ("liste_id");
CREATE INDEX "site_listeenviesproduit_e04c88f4" ON "site_listeenviesproduit" ("produit_id");
CREATE INDEX "site_commande_5f9483ee" ON "site_commande" ("utilisateur_id");
CREATE INDEX "site_commande_f307a6c3" ON "site_commande" ("pays_id");
CREATE INDEX "site_commandepaquet_5152678" ON "site_commandepaquet" ("commande_id");
CREATE INDEX "site_commandeproduit_27d26bca" ON "site_commandeproduit" ("paquet_id");
CREATE INDEX "site_commandeproduit_e04c88f4" ON "site_commandeproduit" ("produit_id");
COMMIT;
