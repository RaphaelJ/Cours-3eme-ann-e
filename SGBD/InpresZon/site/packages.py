# -*- coding: UTF-8 -*-

import cx_Oracle

from django.db import connection

from models import Utilisateur, Session, Produit, Fournisseur, Commande

class GestionUtilisateurs:
    @staticmethod
    def Ajouter(login, mot_de_passe, email, nom, prenom):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_UTILISATEURS.Ajouter", (
            login, mot_de_passe, email, nom, prenom
        ))
        

    @staticmethod
    def Modifier(login, mot_de_passe, nom, prenom):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_UTILISATEURS.Modifier", (
            login, mot_de_passe, nom, prenom
        ))

    @staticmethod
    def LoginExiste(login):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("GESTION_UTILISATEURS.LoginExiste",
            cx_Oracle.NUMBER, [login]
        ) == 1.0

    @staticmethod
    def EmailExiste(email):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("GESTION_UTILISATEURS.EmailExiste",
            cx_Oracle.NUMBER, [email]
        ) == 1.0

    @staticmethod
    def Connexion(login, mot_de_passe):
        connection.cursor()
        cur = connection.connection.cursor()

        variables = { 'login': login,
            'mot_de_passe': mot_de_passe,
            'email': cur.var(cx_Oracle.STRING),
            'admin': cur.var(cx_Oracle.NUMBER),
            'nom': cur.var(cx_Oracle.STRING),
            'prenom': cur.var(cx_Oracle.STRING),
            'date_inscription': cur.var(cx_Oracle.TIMESTAMP)
        }

        cur.execute("""
            DECLARE
                utilisateur SITE_UTILISATEUR%ROWTYPE;
            BEGIN
                utilisateur := GESTION_UTILISATEURS.Connexion(
                    :login, :mot_de_passe
                );
                :email := utilisateur.email;
                :admin := utilisateur.admin;
                :nom := utilisateur.nom;
                :prenom := utilisateur.prenom;
                :date_inscription := utilisateur.date_inscription;
            END;""",variables
        )
        
        return Utilisateur(
            login=login, mot_de_passe=mot_de_passe,
            email=variables['email'].getvalue(),
            admin=variables['admin'].getvalue(),
            nom=variables['nom'].getvalue(),
            prenom=variables['prenom'].getvalue(),
            date_inscription=variables['date_inscription'].getvalue()
        )

    @staticmethod
    def Utilisateur(login):
        connection.cursor()
        cur = connection.connection.cursor()

        variables = { 'login': login,
            'mot_de_passe': cur.var(cx_Oracle.STRING),
            'email': cur.var(cx_Oracle.STRING),
            'admin': cur.var(cx_Oracle.NUMBER),
            'nom': cur.var(cx_Oracle.STRING),
            'prenom': cur.var(cx_Oracle.STRING),
            'date_inscription': cur.var(cx_Oracle.TIMESTAMP)
        }

        cur.execute("""
            DECLARE
                utilisateur SITE_UTILISATEUR%ROWTYPE;
            BEGIN
                utilisateur := GESTION_UTILISATEURS.Utilisateur(:login);
                :mot_de_passe := utilisateur.mot_de_passe;
                :email := utilisateur.email;
                :admin := utilisateur.admin;
                :nom := utilisateur.nom;
                :prenom := utilisateur.prenom;
                :date_inscription := utilisateur.date_inscription;
            END;""", variables
        )

        return Utilisateur(
            login=login,
            mot_de_passe=variables['mot_de_passe'].getvalue(),
            email=variables['email'].getvalue(),
            admin=variables['admin'].getvalue(),
            nom=variables['nom'].getvalue(),
            prenom=variables['prenom'].getvalue(),
            date_inscription=variables['date_inscription'].getvalue()
        )

class GestionSessions:
    @staticmethod
    def Ajouter(cle, donnees, expiration):
        connection.cursor()
        cur = connection.connection.cursor()
        cur.callproc("GESTION_SESSIONS.Ajouter", (
            cle, donnees, expiration
        ))

    @staticmethod
    def Modifier(cle, donnees, expiration):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_SESSIONS.Modifier", (
            cle, donnees, expiration
        ))

    @staticmethod
    def Chercher(cle):
        connection.cursor()
        cur = connection.connection.cursor()

        variables = { 'cle': cle,
            'donnees': cur.var(cx_Oracle.STRING),
            'expiration': cur.var(cx_Oracle.TIMESTAMP),
        }

        cur.execute("""
            DECLARE
                VarSession SITE_SESSION%ROWTYPE;
            BEGIN
                VarSession := GESTION_SESSIONS.Chercher(:cle);
                :donnees := VarSession.donnees;
                :expiration := VarSession.expiration;
            END;""", variables
        )

        return Session(
            cle=cle, donnees=variables['donnees'].getvalue(),
            expiration=variables['expiration'].getvalue()
        )
        
    @staticmethod
    def Supprimer(cle):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_SESSIONS.Supprimer", (cle,))

class GestionCatalogue:
    ARTICLES_PAGE = 5
    
    @staticmethod
    def Produits(page):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("BEGIN :result := Gestion_Catalogue.Produits(:page); END;",
            result=result, page=page
        )
        
        return [ Produit(
            ean=p[0], titre=p[1], description=p[2], langue=p[3], prix=p[4],
            stock=p[5], devise=p[6],
            fournisseur=GestionCatalogue.Fournisseur(p[7]), origine=p[8],
            replique=p[9]
        ) for p in result ]
        
    @staticmethod
    def NbreProduits():
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("Gestion_Catalogue.NbreProduits",
            cx_Oracle.NUMBER
        )

    @staticmethod
    def Fournisseur(fournisseur_id):
        connection.cursor()
        cur = connection.connection.cursor()
        
        variables = { 'fournisseur_id': fournisseur_id,
            'nom': cur.var(cx_Oracle.STRING),
        }

        cur.execute("""
            DECLARE
                VarFournisseur SITE_FOURNISSEUR%ROWTYPE;
            BEGIN
                VarFournisseur := GESTION_CATALOGUE.Fournisseur(:fournisseur_id);
                :nom := VarFournisseur.nom;
            END;""", variables
        )

        return Fournisseur(
            id=fournisseur_id, nom=variables['nom'].getvalue()
        )
    
    @staticmethod
    def Produit(ean):
        connection.cursor()
        cur = connection.connection.cursor()
        
        variables = { 'ean': ean,
            'titre': cur.var(cx_Oracle.STRING),
            'description': cur.var(cx_Oracle.STRING),
            'langue': cur.var(cx_Oracle.STRING), 
            'prix': cur.var(cx_Oracle.NUMBER),
            'stock': cur.var(cx_Oracle.NUMBER),
            'devise': cur.var(cx_Oracle.STRING),
            'fournisseur_id': cur.var(cx_Oracle.NUMBER), 
            'origine': cur.var(cx_Oracle.STRING),
            'replique': cur.var(cx_Oracle.NUMBER),
        }

        cur.execute("""
            DECLARE
                VarProduit SITE_PRODUIT%ROWTYPE;
            BEGIN
                VarProduit := GESTION_CATALOGUE.Produit(:ean);
                :titre := VarProduit.titre;
                :description := VarProduit.description;
                :langue := VarProduit.langue;
                :prix := VarProduit.prix;
                :stock := VarProduit.stock;
                :devise := VarProduit.devise;
                :fournisseur_id := VarProduit.fournisseur_id;
                :origine := VarProduit.origine;
                :replique := VarProduit.replique;
            END;""", variables
        )

        return Produit(
            ean=ean, titre=variables['titre'].getvalue(),
            description=variables['description'].getvalue(),
            langue=variables['langue'].getvalue(),
            prix=variables['prix'].getvalue(),
            stock=int(variables['stock'].getvalue()),
            devise=variables['devise'].getvalue(),
            fournisseur=GestionCatalogue.Fournisseur(
                variables['fournisseur_id'].getvalue()
            ), origine=variables['origine'].getvalue(),
            replique=variables['replique'].getvalue()
        )
    
class GestionCommandes:
    ARTICLES_PAGE = 5
    
    @staticmethod
    def ProduitsCaddie(utilisateur, page):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("""
            BEGIN
                :result := Gestion_Commandes.ProduitsCaddie(:utilisateur, :page);
            END;""", result=result, utilisateur=utilisateur, page=page
        )
        
        produits = [(Produit(
            ean=p[0], titre=p[1], description=p[2], langue=p[3], prix=p[4],
            stock=p[5], devise=p[6],
            fournisseur=GestionCatalogue.Fournisseur(p[7]), origine=p[8],
            replique=p[9]
        ), p[10]) for p in result]
        
        for p, q in produits:
            p.quantite = q
            p.en_stock = q <= p.stock      
            p.prix_total = q * p.prix
        
        return [ p for p, _ in produits ]
        
    @staticmethod
    def TotalCaddie(utilisateur):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("Gestion_Commandes.TotalCaddie",
            cx_Oracle.NUMBER, [utilisateur]
        )
        
    @staticmethod
    def NbreProduitsCaddie(utilisateur):
        connection.cursor()
        cur = connection.connection.cursor()
        return int(cur.callfunc("Gestion_Commandes.NbreProduitsCaddie",
            cx_Oracle.NUMBER, [utilisateur]
        ))
        
    @staticmethod
    def CaddieEnStock(utilisateur):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("Gestion_Commandes.CaddieEnStock",
            cx_Oracle.NUMBER, [utilisateur]
        ) == 0
        
    @staticmethod
    def AjoutCaddie(utilisateur, ean):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.AjouterCaddie", (
            utilisateur, ean
        ))
        
    @staticmethod
    def SupprimerCaddie(utilisateur, ean):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.SupprimerCaddie", (
            utilisateur, ean
        ))
    
    @staticmethod
    def QuantiteProduit(utilisateur, ean):
        connection.cursor()
        cur = connection.connection.cursor()
        return int(cur.callfunc("GESTION_COMMANDES.QuantiteProduit",
            cx_Oracle.NUMBER, [utilisateur, ean]
        ))
        
    @staticmethod
    def ModifierQuantite(utilisateur, ean, quantite):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.ModifierQuantite",
            [utilisateur, ean, quantite]
        )
    
    @staticmethod
    def PasserCommande(utilisateur):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.PasserCommande",
            [utilisateur]
        )
        
    @staticmethod
    def HistoriqueCommandes(utilisateur):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("""
            BEGIN
                :result := Gestion_Commandes.HistoriqueCommandes(:utilisateur);
            END;""", result=result, utilisateur=utilisateur
        )
        
        return [ Commande(c[1], None, date_commande=c[3]) for c in result ]
    