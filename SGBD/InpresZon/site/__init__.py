from django.conf import settings

settings.LOGIN_REDIRECT_URL = '/utilisateur/profil/'
settings.LOGIN_URL = '/utilisateur/connexion/'
settings.LOGOUT_URL = '/utilisateur/deconnexion/' 