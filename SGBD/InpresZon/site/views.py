# -*- coding: UTF-8 -*-

from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.core.context_processors import csrf
from django.shortcuts import render_to_response, redirect
from django.template import RequestContext

from auth_backend import crypt_mdp
from forms import UtilisateurForm, ProfilForm, CaddieProduitForm
from models import Produit, CaddieProduit

def catalogue(request):
    """ Affiche tous les éléments du catalogue """
    
    return render_to_response("catalogue.html", {
        'produits': Produit.objects.all(),
    }, context_instance=RequestContext(request))
    
def produit(request, produit_ean):
    """ Affiche la fiche d'un produit """
    
    return render_to_response("produit.html", {
        'produit': Produit.objects.get(ean=produit_ean),
    }, context_instance=RequestContext(request))

def inscription(request):
    """ Affiche et enregistre le formulaire d'inscription """

    if request.method == 'POST':
        form = UtilisateurForm(request.POST)

        if form.is_valid():
            form.save()

            l = form.cleaned_data['login']
            mdp = form.cleaned_data['mot_de_passe_clair']

            u = authenticate(username=l, password=mdp)
            login(request, u)
            
            return redirect('profil')
    else:
        form = UtilisateurForm()
    
    return render_to_response("inscription.html", {
        'form': form,
    }, context_instance=RequestContext(request))

@login_required
def profil(request):
    """ Affiche les informations de l'utilisateur et permet de les modifier """

    if request.method == 'POST':
        form = ProfilForm(request.POST, instance=request.user.utilisateur)

        if form.is_valid():
            form.save()
    else:
        form = ProfilForm(instance=request.user.utilisateur)

    return render_to_response("profil.html", {
        'form': form,
    }, context_instance=RequestContext(request))

def _cadie_produits_total(utilisateur):
    """
    Retourne les éléments présents dans le caddie ainsi que le prix total
    de la commande.
    """
    
    cps = CaddieProduit.objects.filter(
        utilisateur=utilisateur
    )

    total = {} # Prix total pour chaque devise
    for cp in cps:
        devise = cp.produit.devise
        if devise in total:
            total[devise] += cp.prix_total
        else:
            total[devise] = cp.prix_total

    return cps, total
    
def _en_stock(cps):
    """ Retourne True si tous les articles sont en stock """
    return all(cp.en_stock for cp in cps)

@login_required
def caddie(request):
    """ Affiche le contenu du caddie """
    
    cps, total = _cadie_produits_total(request.user.utilisateur)
    
    return render_to_response("caddie.html", {
        'caddie_produits': cps,
        'total': total,
        'en_stock': _en_stock(cps),
    }, context_instance=RequestContext(request))
    
    
@login_required
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
    
    
@login_required
def caddie_supprimer(request, ean):
    """
    Supprime un élément du caddie et redirige vers la liste des éléments du caddie
    """
    
    CaddieProduit.objects.get(
        utilisateur=request.user.utilisateur,
        produit=Produit.objects.get(ean=ean)
    ).delete()
    
    return redirect('caddie')

@login_required
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
    
    return render_to_response("caddie_modifier.html", {
        'caddie_produit': cp,
        'form': form,
    }, context_instance=RequestContext(request))

@login_required
def commande(request):
    """
    Demande une confirmation de l'adresse de livraison, propose un moyen de
    paiement et crée une nouvelle commande
    """

    cps, total = _cadie_produits_total(request.user.utilisateur)

    if len(cps) <= 0: # Pas de commande sans article
        return redirect('caddie')
    elif not _en_stock(cps): # Certains articles ne sont pas en stock
        return redirect('caddie')
    else:
        if request.method == 'POST': # Validation de la commande
            pass
        else: # Demande de confirmation de la commande
            
        
        return render_to_response("commande.html", {
            'caddie_produits': cps,
            'form': form,
        }, context_instance=RequestContext(request))