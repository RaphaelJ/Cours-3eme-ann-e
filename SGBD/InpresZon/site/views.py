# -*- coding: UTF-8 -*-

from django.contrib.auth.decorators import login_required
from django.shortcuts import render_to_response

from models import Produit, CaddieProduit

def catalogue(request):
    
    return render_to_response("catalogue.html", {
        'produits': Produit.objects.all(),
    })
    
def produit(request, produit_ean):
    return render_to_response("produit.html", {
        'produit': Produit.objects.get(ean=produit_ean),
    })

@login_required(login_url='/utilisateur/connexion/')
def caddie(request, ajout_ean=None):
    def ajouter_quantite(produit, quantite):
        """ Rajoute un champs quantité au produit """
        produit.quantite = quantite
        return produit

    produits = [ ajouter_quantite(e.produit, e.quantite)
                 for e in CaddieProduit.objects.filter(utilisateur__login)]
    
    return render_to_response("caddie.html", {
        'produits': Produit.objects.get(ean=produit_ean),
    })