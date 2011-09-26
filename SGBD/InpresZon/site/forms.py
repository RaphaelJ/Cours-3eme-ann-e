# -*- coding: UTF-8 -*-

from django.forms import ModelForm

from models import CaddieProduit

class CaddieProduitForm(ModelForm):
    """ Formulaire permettant la modification de la quantité d'un article """
    class Meta:
        model = CaddieProduit
        fields = ('quantite',)