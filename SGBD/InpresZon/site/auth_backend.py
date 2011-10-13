# -*- coding: UTF-8 -*-

import sha

from django.contrib.auth.models import User, check_password

from packages import GestionUtilisateurs

def crypt_mdp(mot_de_passe):
    """ Retourne la valeur hexadécimale du hash SHA1 du mot de passe """
    return sha.new(mot_de_passe).hexdigest()

class PLSQLBackend():
    """ Utilise les procédures PL/SQL pour autentifier l'utilisateur """
    supports_inactive_user = False

    def authenticate(self, username=None, password=None):
        try:
            u = GestionUtilisateurs.Connexion(
                login=username, mot_de_passe=crypt_mdp(password)
            )
            return construire_user(u)
        except Utilisateur.DoesNotExist:
            return None

    def get_user(self, user_id):
        try:
            u = GestionUtilisateurs.Utilisateur(login=user_id)
            return construire_user(u)
        except Utilisateur.DoesNotExist:
            return None

def construire_user(u):
    """ Construit un objet User à partir d'un objet Utilisateur """
    user = User(
        id=u.login, username=u.login, first_name=u.prenom,
        last_name=u.nom, email=u.email, password=u.mot_de_passe,
        is_staff=u.admin, is_superuser=u.admin,
        date_joined=u.date_inscription
    )
    user.utilisateur = u
    user.save = user.utilisateur.save
    
    return user