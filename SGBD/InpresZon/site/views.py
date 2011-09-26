# -*- coding: UTF-8 -*-

from django.contrib.auth.decorators import login_required
from django.shortcuts import render_to_response, redirect
from django.core.context_processors import csrf

from forms import CaddieProduitForm
from models import Produit, CaddieProduit

def catalogue(request):
    """ Affiche tous les éléments du catalogue """
    return render_to_response("catalogue.html", {
        'user': request.user,
        
        'produits': Produit.objects.all(),
    })
    
def produit(request, produit_ean):
    """ Affiche la fiche d'un produit """
    return render_to_response("produit.html", {
        'user': request.user,
        
        'produit': Produit.objects.get(ean=produit_ean),
    })

@login_required(login_url='/utilisateur/connexion/')
def caddie(request, ajout_ean=None):
    """ Affiche le contenu du caddie """        
    cps = CaddieProduit.objects.filter(
        utilisateur=request.user.utilisateur
    )
    
    total = {} # Prix total pour chaque devise
    for cp in cps:
        devise = cp.produit.devise
        if devise in total:
            total[devise] += cp.prix_total
        else:
            total[devise] = cp.prix_total
    
    return render_to_response("caddie.html", {
        'user': request.user,
        
        'caddie_produits': cps,
        'total': total,
    })
    
    
@login_required(login_url='/utilisateur/connexion/')
def caddie_ajout(request, ean):
    """
    Ajoute un élément au caddie et redirige vers la liste des éléments du caddie
    """
    cp, nouveau = CaddieProduit.objects.get_or_create(
        utilisateur=request.user.utilisateur,
        produit=Produit.objects.get(ean=ean)
    )
    
    if not nouveau:
        cp.quantite += 1
    
    cp.save()
    
    return redirect('caddie')
    
    
@login_required(login_url='/utilisateur/connexion/')
def caddie_supprimer(request, ean):
    """
    Supprime un élément du caddie et redirige vers la liste des éléments du caddie
    """
    CaddieProduit.objects.get(
        utilisateur=request.user.utilisateur,
        produit=Produit.objects.get(ean=ean)
    ).delete()
    
    return redirect('caddie')

def caddie_modifier(request, ean):
    """
    Formulaire pour permettre la modification de la quantité d'un produit du caddie
    """
    
    cp = CaddieProduit.objects.get(
        utilisateur=request.user.utilisateur,
        produit=Produit.objects.get(ean=ean)
    )
    
    if request.method == 'POST':
        form = CaddieProduitForm(request.POST, instance=cp)
        if form.is_valid():
            form.save()
            return redirect('caddie')
    else:
        form = CaddieProduitForm(instance=cp)    
    
    c = {
        'user': request.user,
        
        'caddie_produit': cp,
        'form': form,
    }
    c.update(csrf(request))
    return render_to_response("caddie_modifier.html", c)