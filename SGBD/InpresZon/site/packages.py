# -*- coding: UTF-8 -*-

import cx_Oracle

from django.db import connection

from models import Utilisateur, Session, Produit, Fournisseur, Commande, \
    Adresse, CommandePaquet, CommandeProduit, Commentaire

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
    
    @staticmethod
    def Adresses(login):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("BEGIN :result := Gestion_Utilisateurs.Adresses(:login); END;",
            result=result, login=login
        )
        
        return [ Adresse(
            id=p[0], adresse=p[1], ville=p[2], code_postal=p[3], pays=p[4]
        ) for p in result ]
        
    @staticmethod
    def Adresse(login, adresse_id):
        connection.cursor()
        cur = connection.connection.cursor()
        
        variables = {
            'login': login,
            'adresse_id': adresse_id,
            'adresse': cur.var(cx_Oracle.STRING),
            'ville': cur.var(cx_Oracle.STRING),
            'code_postal': cur.var(cx_Oracle.STRING), 
            'pays': cur.var(cx_Oracle.STRING),
            'utilisateur_id': cur.var(cx_Oracle.STRING),
        }

        cur.execute("""
            DECLARE
                VarAdresse SITE_ADRESSE%ROWTYPE;
            BEGIN
                VarAdresse := GESTION_UTILISATEURS.Adresse(:login, :adresse_id);
                :adresse := VarAdresse.adresse;
                :ville := VarAdresse.ville;
                :code_postal := VarAdresse.code_postal;
                :pays := VarAdresse.pays;
                :utilisateur_id := VarAdresse.utilisateur_id;
            END;""", variables
        )

        return Adresse(
            id=adresse_id, adresse=variables['adresse'].getvalue(),
            ville=variables['ville'].getvalue(),
            code_postal=variables['code_postal'].getvalue(),
            pays=variables['pays'].getvalue()
        )
    
    @staticmethod
    def AjouterAdresse(login, adresse, ville, code_postal, pays):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_UTILISATEURS.AjouterAdresse", (
            login, adresse, ville, code_postal, pays
        ))
        
    @staticmethod
    def SupprimerAdresse(login, adresse_id):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_UTILISATEURS.SupprimerAdresse", (
            login, adresse_id
        ))

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
    def ProduitsSimilaires(produit_id):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("""
            BEGIN
                :result := Gestion_Catalogue.ProduitsSimilaires(
                    :produit_id
                );
            END;""",
            result=result, produit_id=produit_id
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
        
    @staticmethod
    def Commentaires(produit_id):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute(
            "BEGIN :result := Gestion_Catalogue.Commentaires(:produit_id); END;",
            result=result, produit_id=produit_id
        )
        
        def create_commentaire(c):
            ret = Commentaire(id=c[0], creation=c[3], message=c[4])
            ret.login = c[1]
            return ret
        
        return [ create_commentaire(c) for c in result ]
        
    @staticmethod
    def AjouterCommentaire(produit_id, utilisateur_id, message):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("Gestion_Catalogue.AjouterCommentaire", (
            produit_id, utilisateur_id, message
        ))
        
    @staticmethod
    def CommentaireAutorise(produit_id, utilisateur_id):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callfunc("Gestion_Catalogue.CommentaireAutorise",
            cx_Oracle.NUMBER, [produit_id, utilisateur_id]
        ) > 0
    
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
    def ViderCaddie(utilisateur):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.ViderCaddie", (
            utilisateur,
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
    def PasserCommande(utilisateur, adresse_id):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.PasserCommande",
            [utilisateur, adresse_id]
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
        
        def create_command(c):
            ret = Commande(
                id=c[0], adresse_livraison=GestionUtilisateurs.Adresse(
                    utilisateur, c[2]
                ), origine=c[3], date_commande=c[4]
            )
            ret.paquets = GestionCommandes.PaquetsCommande(
                    utilisateur, c[0]
            )
            return ret
        
        return [ create_command(c) for c in result ]
        
    @staticmethod
    def ProduitsCommande(utilisateur_id, commande_id):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("""
            BEGIN
                :result := Gestion_Commandes.ProduitsCommande(
                    :utilisateur, :commande
                );
            END;""", result=result, utilisateur=utilisateur_id,
            commande=commande_id
        )
        
        def create_produit(p):
            ret = Produit(
                ean=p[0], titre=p[1], description=p[2], langue=p[3], prix=p[4],
                stock=p[5], devise=p[6], origine=p[8], replique=p[9],
            )
            ret.quantite = p[10]
            ret.commande_produit_id = p[11]
            return ret
        
        return [ create_produit(p) for p in result]
    
    @staticmethod
    def PaquetsCommande(utilisateur_id, commande_id):
        connection.cursor()
        result = connection.connection.cursor()
        cur = connection.connection.cursor()
        
        cur.execute("""
            BEGIN
                :result := Gestion_Commandes.PaquetsCommande(
                    :utilisateur, :commande
                );
            END;""", result=result, utilisateur=utilisateur_id,
            commande=commande_id
        )
        
        return [
            CommandePaquet(id=p[0], status=p[2], status_changement=p[3])
            for p in result
        ]
    
    @staticmethod
    def AnnulerCommande(utilisateur_id, commande_id):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.AnnulerCommande", (
            utilisateur_id, commande_id
        ))
        
    @staticmethod
    def ProduitCommande(utilisateur_id, commande_produit_id):
        connection.cursor()
        cur = connection.connection.cursor()
        
        variables = {
            'utilisateur_id': utilisateur_id,
            'commande_produit_id': commande_produit_id,
            'produit_id': cur.var(cx_Oracle.NUMBER),
            'quantite': cur.var(cx_Oracle.NUMBER),
        }

        cur.execute("""
            DECLARE
                VarCommandeProduit SITE_COMMANDEPRODUIT%ROWTYPE;
            BEGIN
                VarCommandeProduit := GESTION_COMMANDES.ProduitCommande(
                    :utilisateur_id, :commande_produit_id
                );
                :produit_id := VarCommandeProduit.produit_id;
                :quantite := VarCommandeProduit.quantite;
            END;""", variables
        )

        return CommandeProduit(
            id=commande_produit_id,
            produit=GestionCatalogue.Produit(
                variables['produit_id'].getvalue()
            ), quantite=variables['quantite'].getvalue()
        )
    
    @staticmethod
    def ModifierCommandeQuantite(utilisateur_id, commande_produit_id, quantite):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_COMMANDES.ModifierCommandeQuantite", (
            utilisateur_id, commande_produit_id, quantite
        ))