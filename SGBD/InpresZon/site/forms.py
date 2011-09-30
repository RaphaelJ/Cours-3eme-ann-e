# -*- coding: UTF-8 -*-

from django.forms import ModelForm, CharField, PasswordInput, ValidationError
from django.utils.translation import ugettext_lazy as _

from auth_backend import crypt_mdp
from models import Utilisateur, CaddieProduit, Commande

class UtilisateurForm(ModelForm):
    """ Formulaire permettant l'inscription d'un utilisateur """

    mot_de_passe = CharField(label=_("Mot de passe"), widget=PasswordInput)

    def clean_mot_de_passe(self):
        """ Crypte la mot de passe avant d'enregistrer """
        mdp_hash = crypt_mdp(self.cleaned_data["mot_de_passe"])
        self.cleaned_data["mot_de_passe_clair"] = self.cleaned_data["mot_de_passe"]
        self.cleaned_data["mot_de_passe"] = mdp_hash
        return mdp_hash

    class Meta:
        model = Utilisateur

class ProfilForm(UtilisateurForm):
    """
    Formulaire permettant la modification des informations de profil de
    l'utilisateur
    """

    class Meta:
        model = Utilisateur
        exclude = ('login')

class CaddieProduitForm(ModelForm):
    """ Formulaire permettant la modification de la quantité d'un article """

    def clean_quantite(self):
        """ Vérifie que la quantité introduite est positive et non nulle """
        quantite = self.cleaned_data['quantite']
        if quantite <= 0:
            raise ValidationError(
                _(u"La quantité du produit doit être supérieure à zéro")
            )
        
        return quantite
    
    class Meta:
        model = CaddieProduit

class Adresse(ModelForm):
    """
    Formulaire permettant d'entrer une nouvelle adresse
    """

    class Meta:
        model = Commande