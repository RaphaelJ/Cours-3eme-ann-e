# -*- coding: UTF-8 -*-

from models import Utilisateur

class AuthBackend():
    """
    Utilise les procédures PL/SQL pour autentifier l'utilisateur
    """

    supports_inactive_user = False

    def authenticate(self, login=None, mot_de_passe=None):
        try:
            return Utilisateur.objects
                              .get(login=username, mot_de_passe=password)
        except Utilisateur.DoesNotExist:
            return None

    def get_user(self, user_id):
        try:
            return Utilisateur.objects.get(login=user_id)
        except Utilisateur.DoesNotExist:
            return None
