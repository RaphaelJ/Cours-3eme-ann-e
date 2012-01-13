# -*- coding: UTF-8 -*-

from django import forms
from django.utils.translation import ugettext_lazy as _

from auth_backend import crypt_mdp
from models import Utilisateur, CaddieProduit, Commande
from packages import GestionUtilisateurs

class UtilisateurForm(forms.Form):
    """ Formulaire permettant l'inscription d'un utilisateur """

    login = forms.CharField(label=_(u"Nom d'utilisateur"), max_length=30)
    mot_de_passe = forms.CharField(
        label=_(u"Mot de passe"), widget=forms.PasswordInput
    )
    email = forms.CharField(label=_(u"Adresse email"))
    nom = forms.CharField(label=_(u"Nom"), max_length=30)
    prenom = forms.CharField(label=_(u"Prénom"), max_length=30)

    def clean_login(self):
        """ Vérifie si le login existe déjà dans la base """
        login = self.cleaned_data["login"]
        if GestionUtilisateurs.LoginExiste(login):
            raise forms.ValidationError(
                _(u"Le nom d'utilisateur est déjà utilisé")
            )
        else:
            return login

    def clean_email(self):
        """ Vérifie si l'email existe déjà dans la base """
        email = self.cleaned_data["email"]
        if GestionUtilisateurs.EmailExiste(email):
            raise forms.ValidationError(
                _(u"L'adresse email est déjà utilisée")
            )
        else:
            return email

    def clean_mot_de_passe(self):
        """ Crypte la mot de passe avant d'enregistrer """
        mdp_hash = crypt_mdp(self.cleaned_data["mot_de_passe"])
        self.cleaned_data["mot_de_passe_clair"] = self.cleaned_data["mot_de_passe"]
        self.cleaned_data["mot_de_passe"] = mdp_hash
        return mdp_hash

class ProfilForm(forms.Form):
    """
    Formulaire permettant la modification des informations de profil de
    l'utilisateur
    """

    mot_de_passe = forms.CharField(
        label=_(u"Mot de passe"), widget=forms.PasswordInput
    )
    nom = forms.CharField(label=_(u"Nom"), max_length=30)
    prenom = forms.CharField(label=_(u"Prénom"), max_length=30)

    def clean_mot_de_passe(self):
        """ Crypte la mot de passe avant d'enregistrer """
        if self.cleaned_data["mot_de_passe"] == "":
            raise forms.ValidationError(
                _(u"Le mot de passe ne peut être vide")
            )
        
        mdp_hash = crypt_mdp(self.cleaned_data["mot_de_passe"])
        self.cleaned_data["mot_de_passe"] = mdp_hash
        return mdp_hash

class CaddieProduitForm(forms.Form):
    """ Formulaire permettant la modification de la quantité d'un article """
    
    quantite = forms.IntegerField(label=_(u"Quantité"))

    def clean_quantite(self):
        """ Vérifie que la quantité introduite est positive et non nulle """
        quantite = self.cleaned_data['quantite']
        if quantite <= 0:
            raise forms.ValidationError(
                _(u"La quantité du produit doit être supérieure à zéro")
            )
        
        return quantite
        
class CommandeForm(forms.Form):
    """
        Formulaire permettant la validation d'une commande et la sélection de
        l'adresse.
    """
    
    adresse = forms.ChoiceField(label=_(u"Adresse de livraison"))
    
    def __init__(self, adresses, *args, **kwargs):
        super(CommandeForm, self).__init__(*args, **kwargs)
        
        self.fields["adresse"].choices = ((a.id, str(a)) for a in adresses)
    
class CommandeProduitForm(forms.Form):
    """ 
        Formulaire permettant la modification de la quantité d'un article
        sur une command validée.
    """
    
    quantite = forms.IntegerField(label=_(u"Quantité"))
    
    def __init__(self, stock, *args, **kwargs):
        super(CommandeProduitForm, self).__init__(*args, **kwargs)
        
        self.stock = stock

    def clean_quantite(self):
        """
            Vérifie que la quantité introduite est positive, non nulle et
            inférieure au stock.
        """
        quantite = self.cleaned_data['quantite']
        if quantite <= 0:
            raise forms.ValidationError(
                _(u"La quantité du produit doit être supérieure à zéro")
            )
        elif quantite > self.stock:
            raise forms.ValidationError(
                _(u"Il n'y a pas assez de stock pour cet article")
            )
        
        return quantite

class AdresseForm(forms.Form):
    """
    Formulaire permettant d'entrer une nouvelle adresse
    """

    adresse = forms.CharField(label=_(u"Adresse"), max_length=255)
    ville = forms.CharField(label=_(u"Ville"), max_length=30)
    code_postal = forms.CharField(label=_(u"Code postal"), max_length=10)
    pays = forms.CharField(label=_(u"Pays"), max_length=30)
    
class CommentaireForm(forms.Form):
    """ Formulaire permettant de poster un commentaire sur un article """
    
    message = forms.CharField(label=_(u"Message"), widget=forms.Textarea)