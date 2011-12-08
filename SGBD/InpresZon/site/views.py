# -*- coding: UTF-8 -*-

import math

from cx_Oracle import IntegrityError

from django.contrib.auth import authenticate, login
from django.contrib.auth.decorators import login_required
from django.core.context_processors import csrf
from django.db import connection, transaction
from django.db.utils import DatabaseError
from django.forms.util import ErrorList
from django.shortcuts import render_to_response, redirect
from django.template import RequestContext
from django.utils.translation import ugettext_lazy as _

from auth_backend import crypt_mdp
from forms import UtilisateurForm, ProfilForm, CaddieProduitForm
from models import Produit, CaddieProduit, Commande, CommandePaquet
from packages import GestionUtilisateurs, GestionCatalogue, GestionCommandes

#[{'time': '0.154', 'sql': u'SELECT * FROM (SELECT ROWNUM AS "_RN", "_SUB".* FROM (SELECT (1) AS "A" FROM "SITE_UTILISATEUR" WHERE "SITE_UTILISATEUR"."LOGIN" = Rapha ) "_SUB" WHERE ROWNUM <= 1) WHERE "_RN" > 0'}, {'time': '0.032', 'sql': u'SELECT * FROM (SELECT ROWNUM AS "_RN", "_SUB".* FROM (SELECT (1) AS "A" FROM "SITE_UTILISATEUR" WHERE "SITE_UTILISATEUR"."EMAIL" = rap@gmail.com ) "_SUB" WHERE ROWNUM <= 1) WHERE "_RN" > 0'}, {'time': '0.004', 'sql': u'SELECT * FROM (SELECT ROWNUM AS "_RN", "_SUB".* FROM (SELECT (1) AS "A" FROM "SITE_UTILISATEUR" WHERE "SITE_UTILISATEUR"."LOGIN" = Rapha ) "_SUB" WHERE ROWNUM <= 1) WHERE "_RN" > 0'}, {'time': '0.196', 'sql': u'
#INSERT INTO "SITE_UTILISATEUR" ("LOGIN", "MOT_DE_PASSE", "EMAIL", "ADMIN", "NOM", "PRENOM", "DATE_INSCRIPTION", "MODE_PAIEMENT", "MODE_LIVRAISON") VALUES (Rapha, fefd47cb7691a8016da2735af04ce41b46a8365e, rap@gmail.com, False, Rap, Ha, 2011-10-12 09:43:21.784305, , )'}, {'time': '0.028', 'sql': u'SELECT "SITE_UTILISATEUR"."LOGIN", "SITE_UTILISATEUR"."MOT_DE_PASSE", "SITE_UTILISATEUR"."EMAIL", "SITE_UTILISATEUR"."ADMIN", "SITE_UTILISATEUR"."NOM", "SITE_UTILISATEUR"."PRENOM", "SITE_UTILISATEUR"."DATE_INSCRIPTION", "SITE_UTILISATEUR"."MODE_PAIEMENT", "SITE_UTILISATEUR"."MODE_LIVRAISON" FROM "SITE_UTILISATEUR" WHERE ("SITE_UTILISATEUR"."LOGIN" = Rapha  AND "SITE_UTILISATEUR"."MOT_DE_PASSE" = fefd47cb7691a8016da2735af04ce41b46a8365e )'}, {'time': '0.008', 'sql': u'SELECT "SITE_SESSION"."CLE", "SITE_SESSION"."DONNEES", "SITE_SESSION"."EXPIRATION" FROM "SITE_SESSION" WHERE ("SITE_SESSION"."CLE" = a2b72f0291a6b1f1dd135e5ccd335669  AND "SITE_SESSION"."EXPIRATION" > TO_TIMESTAMP(2011-10-12 09:43:22.056324, \'YYYY-MM-DD HH24:MI:SS.FF\') )'}, {'time': '0.006', 'sql': u'SELECT "SITE_SESSION"."CLE", "SITE_SESSION"."DONNEES", "SITE_SESSION"."EXPIRATION" FROM "SITE_SESSION" WHERE "SITE_SESSION"."CLE" = 22a5761400f7d0f67dcbb723110e5783 '}, {'time': '0.015', 'sql': u'INSERT INTO "SITE_SESSION" ("CLE", "DONNEES", "EXPIRATION") VALUES (22a5761400f7d0f67dcbb723110e5783, ZjdmMDY3MDFiNzk2NjAxMzRhZmE2MmExNDY2YzU0N2ZhNzBjYzUzZTqAAn1xAVUKdGVzdGNvb2tp\nZVUGd29ya2Vkcy4=\n, 2011-10-26 09:43:22.108453)'}, {'time': '0.005', 'sql': u'SELECT "SITE_SESSION"."CLE", "SITE_SESSION"."DONNEES", "SITE_SESSION"."EXPIRATION" FROM "SITE_SESSION" WHERE "SITE_SESSION"."CLE" = a2b72f0291a6b1f1dd135e5ccd335669 '}, {'time': '0.013', 'sql': u'DELETE FROM "SITE_SESSION" WHERE "CLE" IN (a2b72f0291a6b1f1dd135e5ccd335669)'}, {'time': '0.007', 'sql': u'SELECT "SITE_UTILISATEUR"."LOGIN", "SITE_UTILISATEUR"."MOT_DE_PASSE", "SITE_UTILISATEUR"."EMAIL", "SITE_UTILISATEUR"."ADMIN", "SITE_UTILISATEUR"."NOM", "SITE_UTILISATEUR"."PRENOM", "SITE_UTILISATEUR"."DATE_INSCRIPTION", "SITE_UTILISATEUR"."MODE_PAIEMENT", "SITE_UTILISATEUR"."MODE_LIVRAISON" FROM "SITE_UTILISATEUR" WHERE "SITE_UTILISATEUR"."LOGIN" = Rapha '}, {'time': '0.005', 'sql': u'SELECT * FROM (SELECT ROWNUM AS "_RN", "_SUB".* FROM (SELECT (1) AS "A" FROM "SITE_UTILISATEUR" WHERE "SITE_UTILISATEUR"."LOGIN" = Rapha ) "_SUB" WHERE ROWNUM <= 1) WHERE "_RN" > 0'}, {'time': '0.028', 'sql': u'UPDATE "SITE_UTILISATEUR" SET "MOT_DE_PASSE" = fefd47cb7691a8016da2735af04ce41b46a8365e, "EMAIL" = rap@gmail.com, "ADMIN" = False, "NOM" = Rap, "PRENOM" = Ha, "DATE_INSCRIPTION" = 2011-10-12 09:43:21.784305, "MODE_PAIEMENT" = , "MODE_LIVRAISON" =  WHERE "SITE_UTILISATEUR"."LOGIN" = Rapha '}]

def catalogue(request, page=1):
    """ Affiche tous les éléments du catalogue """
    
    page = int(page)
    
    nbre_pages = math.ceil(
        GestionCatalogue.NbreProduits() / GestionCatalogue.ARTICLES_PAGE
    )  
    
    if not 0 < page <= nbre_pages:
        page = 1
    
    return render_to_response("catalogue.html", {
        'produits': GestionCatalogue.Produits(page),
        'page': page,
        'page_precedente': page-1 if page > 1 else False,
        'page_suivante': page+1 if page < nbre_pages else False,
    }, context_instance=RequestContext(request))
    
def produit(request, produit_ean):
    """ Affiche la fiche d'un produit """
    
    return render_to_response("produit.html", {
        'produit': GestionCatalogue.Produit(produit_ean),
    }, context_instance=RequestContext(request))
    
def inscription(request):
    """ Affiche et enregistre le formulaire d'inscription """

    if request.method == 'POST':
        form = UtilisateurForm(request.POST)

        if form.is_valid():
            data = form.cleaned_data

            GestionUtilisateurs.Ajouter(
                data["login"], data["mot_de_passe"], data["email"],
                data["nom"], data["prenom"]
            )

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
        form = ProfilForm(request.POST)

        if form.is_valid():
            GestionUtilisateurs.Modifier(
                request.user.utilisateur.login,
                form.cleaned_data["mot_de_passe"], form.cleaned_data["nom"],
                form.cleaned_data["prenom"],
            )
    else:
        form = ProfilForm({
            'nom': request.user.utilisateur.nom,
            'prenom': request.user.utilisateur.prenom
        })

    return render_to_response("profil.html", {
        'form': form,
    }, context_instance=RequestContext(request))

@login_required
def adresses(request):
    """ Liste toutes les adresses enregistrées par l'utilisateur '"""

    return render_to_response("adresses.html", {
        'adresses': request.user.utilisateur.adresses.all(),
    }, context_instance=RequestContext(request))

@login_required
def adresse(request):
    pass

def _cadie_produits_total(utilisateur, page):
    """
    Retourne les éléments présents dans le caddie ainsi que le prix total
    de la commande.
    """
    
    articles = GestionCommandes.ProduitsCaddie(utilisateur.login, page)
    total = GestionCommandes.TotalCaddie(utilisateur.login)
    
    return articles, total

@login_required
def caddie(request, page=1):
    """ Affiche le contenu du caddie """
    
    page = int(page)
    
    util = request.user.utilisateur
    nbre_pages = math.ceil(
        GestionCommandes.NbreProduitsCaddie(util.login) / GestionCommandes.ARTICLES_PAGE
    )    
    
    if not 0 < page <= nbre_pages:
        page = 1
    
    cps, total = _cadie_produits_total(util, page)
    
    return render_to_response("caddie.html", {
        'caddie_produits': cps,
        'total': total,
        'en_stock': GestionCommandes.CaddieEnStock(util.login),
        'page': page,
        'page_precedente': page-1 if page > 1 else False,
        'page_suivante': page+1 if page < nbre_pages else False,
    }, context_instance=RequestContext(request))
    
    
@login_required
def caddie_ajout(request, ean):
    """
    Ajoute un élément au caddie et redirige vers la liste des éléments du caddie
    """
    
    GestionCommandes.AjoutCaddie(request.user.utilisateur.login, ean)
    
    return redirect('caddie')
    
@login_required
def caddie_supprimer(request, ean):
    """
    Supprime un élément du caddie et redirige vers la liste des éléments du caddie
    """
    
    GestionCommandes.SupprimerCaddie(request.user.utilisateur.login, ean)
    
    return redirect('caddie')

@login_required
def caddie_modifier(request, ean):
    """
    Formulaire pour permettre la modification de la quantité d'un produit du caddie
    """
    
    quantite = GestionCommandes.QuantiteProduit(
        request.user.utilisateur.login, ean
    )
    
    if request.method == 'POST':
        form = CaddieProduitForm(request.POST, { 'quantite': quantite })
        if form.is_valid():
            data = form.cleaned_data

            GestionCommandes.ModifierQuantite(
                request.user.utilisateur.login, ean, data['quantite']
            )
            
            return redirect('caddie')
    else:
        form = CaddieProduitForm({ 'quantite': quantite })    
    
    return render_to_response("caddie_modifier.html", {
        'quantite': quantite,
        'form': form,
    }, context_instance=RequestContext(request))

@login_required
def commande(request):
    """
    Demande une confirmation de l'adresse de livraison, propose un moyen de
    paiement et crée une nouvelle commande
    """
    
    try:
        GestionCommandes.PasserCommande(request.user.utilisateur.login)
    except:
        pass
    
    return redirect('commandes')
    #except:
        #return render_to_response("commande.html", {
            #'caddie_produits': cps,
            #'form': form,
        #}, context_instance=RequestContext(request))
        
@login_required
def commandes(request):
    """
    Liste les commandes en cours
    """
    
    return render_to_response("commandes.html", {
        'commandes': GestionCommandes.HistoriqueCommandes(
            request.user.utilisateur.login
        ),
    }, context_instance=RequestContext(request))