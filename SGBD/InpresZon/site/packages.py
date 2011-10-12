# -*- coding: UTF-8 -*-

import cx_Oracle

from django.db import connection

from models import Utilisateur

# Effectue l'appel d'une procédure ou une fonction PL/SQL
callproc = connection.cursor().callproc
callfunc = connection.cursor().callfunc

class GestionUtilisateurs:
    @staticmethod
    def Ajouter(login, mot_de_passe, email, nom, prenom):
        callproc("GESTION_UTILISATEURS.Ajouter", (
            login, mot_de_passe, email, nom, prenom
        ))

    @staticmethod
    def LoginExiste(login):
        return callfunc("GESTION_UTILISATEURS.LoginExiste",
            cx_Oracle.NUMBER, [login]
        ) == '1'

    @staticmethod
    def EmailExiste(email):
        return callfunc("GESTION_UTILISATEURS.EmailExiste",
            cx_Oracle.NUMBER, [email]
        ) == '1'

    @staticmethod
    def Connexion(login, mot_de_passe):
        cur = connection.cursor()
        
        email = cur.var(cx_Oracle.STRING)
        admin = cur.var(cx_Oracle.NUMBER)
        nom = cur.var(cx_Oracle.STRING)
        prenom = cur.var(cx_Oracle.STRING)
        date_inscription = cur.var(cx_Oracle.TIMESTAMP)

        cur.execute("""
            DECLARE;
                utilisateur SITE_UTILISATEUR%%ROWTYPE
            BEGIN;
                utilisateur := GESTION_UTILISATEURS.Connexion(
                    :login, :mot_de_passe
                );
                :email := utilisateur.email;
                :admin := utilisateur.admin;
                :nom := utilisateur.nom;
                :prenom := utilisateur.prenom;
                :date_inscription := utilisateur.date_inscription;
            END;""", login=login, mot_de_passe=mot_de_passe, email=email,
            admin=admin, nom=nom, prenom=prenom,
            date_inscription=date_inscription
        )
        
        return Utilisateur(
            login=login, mot_de_passe=mot_de_passe, email=email, admin=admin,
            nom=nom, prenom=prenom, date_inscription=date_inscription
        )

    @staticmethod
    def Utilisateur(login):
        cur = connection.cursor()

        variables = { 'login': login,
            'mot_de_passe': cur.var(cx_Oracle.STRING),
            'email': cur.var(cx_Oracle.STRING),
            'admin': cur.var(cx_Oracle.NUMBER),
            'nom': cur.var(cx_Oracle.STRING),
            'prenom': cur.var(cx_Oracle.STRING),
            'date_inscription': cur.var(cx_Oracle.TIMESTAMP)
        }

        cur.execute(u"""
            DECLARE;
                utilisateur SITE_UTILISATEUR%%ROWTYPE
            BEGIN;
                utilisateur := GESTION_UTILISATEURS.Utilisateur(:login);
                :mot_de_passe := utilisateur.mot_de_passe
                :email := utilisateur.email;
                :admin := utilisateur.admin;
                :nom := utilisateur.nom;
                :prenom := utilisateur.prenom;
                :date_inscription := utilisateur.date_inscription;
            END;""", variables
        )

        return Utilisateur(
            login=login, mot_de_passe=mot_de_passe, email=email, admin=admin,
            nom=nom, prenom=prenom, date_inscription=date_inscription
        )