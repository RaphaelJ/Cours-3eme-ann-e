CREATE TABLE "site_pays" (
    "nom" varchar(30) NOT NULL PRIMARY KEY
)
;
CREATE TABLE "site_adresse" (
    "id" integer NOT NULL PRIMARY KEY,
    "adresse" varchar(255) NOT NULL,
    "ville" varchar(30) NOT NULL,
    "code_postal" varchar(10) NOT NULL,
    "pays_id" varchar(30) NOT NULL REFERENCES "site_pays" ("nom")
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
    "nationalite_id" varchar(30) REFERENCES "site_pays" ("nom")
)
;
CREATE TABLE "site_editeur" (
    "nom" varchar(30) NOT NULL PRIMARY KEY
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

CREATE INDEX "site_artiste_a05fba75" ON "site_artiste" ("nationalite_id");
CREATE INDEX "site_film_88bbf040" ON "site_film" ("realisateurs_id");
CREATE INDEX "site_film_c86a621c" ON "site_film" ("studio_id");