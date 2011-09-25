# -*- coding: UTF-8 -*-

from django.contrib.auth.models import User, check_password

from models import Utilisateur

class PLSQLBackend():
    """
    Utilise les procédures PL/SQL pour autentifier l'utilisateur
    """

    supports_inactive_user = False

    def authenticate(self, username=None, password=None):
        print "AUTH"
        try:
            u = Utilisateur.objects.get(login=username, mot_de_passe=password)
            print "user "+ str(u)
            return construire_user(u)
        except Utilisateur.DoesNotExist:
            print "error"
            return None
        except Exception as e:
            print "ok"
            print e

    def get_user(self, user_id):
        try:
            u = Utilisateur.objects.get(login='John')
            return construire_user(u)
        except Utilisateur.DoesNotExist:
            return None

def construire_user(u):
    """ Construit un objet User à partir d'un objet Utilisateur """
    user = User(
        id=1, username=u.login, first_name=u.prenom,
        last_name=u.nom, email=u.email, password=u.mot_de_passe,
        is_staff=u.admin, is_superuser=u.admin,
        date_joined=u.date_inscription
    )
    user.utilisateur = u
    user.save = user.utilisateur.save
    
    return user