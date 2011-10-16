# -*- coding: UTF-8 -*-

import cx_Oracle

from django.db import connection

from models import Utilisateur, Session

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
            END;""",variables
        )

        print ("Session:", cle, variables['donnees'].getvalue())

        return Session(
            cle=cle, donnees=variables['donnees'].getvalue(),
            expiration=variables['expiration'].getvalue()
        )
        
    @staticmethod
    def Supprimer(cle):
        connection.cursor()
        cur = connection.connection.cursor()
        return cur.callproc("GESTION_SESSIONS.Supprimer", (cle,))