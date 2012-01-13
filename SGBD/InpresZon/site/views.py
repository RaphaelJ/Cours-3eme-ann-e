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
from forms import UtilisateurForm, ProfilForm, CaddieProduitForm, AdresseForm, \
    CommandeForm, CommandeProduitForm, CommentaireForm
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
    
    if request.user.is_authenticated() and \
       GestionCatalogue.CommentaireAutorise(produit_ean, request.user.utilisateur.login):
        if request.method == 'POST':
            form = CommentaireForm(request.POST)

            if form.is_valid():
                GestionCatalogue.AjouterCommentaire(
                    produit_ean, request.user.utilisateur.login, 
                    form.cleaned_data['message']
                )
        else:
            form = CommentaireForm()
    else:
        form = None
        
    produit = GestionCatalogue.Produit(produit_ean)
    return render_to_response("produit.html", {
        'produit': produit,
        'similaires': GestionCatalogue.ProduitsSimilaires(produit.ean),
        'commentaires':  GestionCatalogue.Commentaires(produit.ean),
        'form': form,
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
            'prenom': request.user.utilisateur.prenom,
        })

    return render_to_response("profil.html", {
        'form': form,
    }, context_instance=RequestContext(request))

@login_required
def adresses(request, supprimer=None):
    """ Liste toutes les adresses enregistrées par l'utilisateur """
    
    message = None
    
    if supprimer != None:
        try:
            GestionUtilisateurs.SupprimerAdresse(
                request.user.utilisateur.login, supprimer
            )
            message = 'supprime' 
        except:
            message = 'suppression impossible'
    
    if request.method == 'POST':
        form = AdresseForm(request.POST)
        if form.is_valid():
            data = form.cleaned_data
            
            GestionUtilisateurs.AjouterAdresse(
                request.user.utilisateur.login, data['adresse'],
                data['ville'], data['code_postal'], data['pays']
            )
            form = AdresseForm()
            message = 'ajoute'
    else:
        form = AdresseForm()

    return render_to_response("adresses.html", {
        'message': message,
        'adresses': GestionUtilisateurs.Adresses(
            request.user.utilisateur.login
        ),
        'form': form,
    }, context_instance=RequestContext(request))

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
    
    adresses = GestionUtilisateurs.Adresses(util.login)
    if request.method == 'POST': # Passer commande
        form = CommandeForm(adresses, request.POST)
        if form.is_valid():
            data = form.cleaned_data
            
            try:
                print(form.cleaned_data['adresse'])
                GestionCommandes.PasserCommande(
                    request.user.utilisateur.login,
                    form.cleaned_data['adresse']
                )
                return redirect('commandes')
            except:
                return redirect('caddie')
    else:
        form = CommandeForm(adresses)   
    
    return render_to_response("caddie.html", {
        'caddie_produits': cps,
        'total': total,
        'en_stock': GestionCommandes.CaddieEnStock(util.login),
        'page': page,
        'page_precedente': page-1 if page > 1 else False,
        'page_suivante': page+1 if page < nbre_pages else False,
        'form': form,
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
def caddie_vider(request):
    """
    Supprime tous les éléments du caddie
    """
    
    GestionCommandes.ViderCaddie(request.user.utilisateur.login)
    
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
def commandes(request, annuler_commande=None):
    """
    Liste les commandes en cours
    """
    
    if annuler_commande != None:
        GestionCommandes.AnnulerCommande(
            request.user.utilisateur.login, annuler_commande
        )
        
        confirmation_annulation = True
    else:
        confirmation_annulation = False
    
    return render_to_response("commandes.html", {
        'confirmation_annulation': confirmation_annulation,
        'commandes': GestionCommandes.HistoriqueCommandes(
            request.user.utilisateur.login
        ),
    }, context_instance=RequestContext(request))

@login_required
def commande(request, commande_id, supprimer_commande_produit_id=None):
    """
        Permet la modification des détails d'une commande
    """
    
    paquets = GestionCommandes.PaquetsCommande(
        request.user.utilisateur.login, commande_id
    )
    
    if len(paquets) > 0: # Commande en livraison
        return redirect('commandes')
    else:
        return render_to_response("commande.html", {
            'commande_id': commande_id,
            'commande_produits': GestionCommandes.ProduitsCommande(
                request.user.utilisateur.login, commande_id
            ),
        }, context_instance=RequestContext(request))

@login_required
def commande_modifier(request, commande_id, commande_produit_id):
    """ Modifie la quantité d'un produit de la commande """
    commande_produit = GestionCommandes.ProduitCommande(
        request.user.utilisateur.login, commande_produit_id
    )
    
    if request.method == 'POST':
        form = CommandeProduitForm(
            commande_produit.produit.stock + commande_produit.quantite,
            request.POST, {
                'quantite': commande_produit.quantite
        })
        
        if form.is_valid():    
            GestionCommandes.ModifierCommandeQuantite(
                request.user.utilisateur.login, commande_produit_id,
                form.cleaned_data['quantite']
            )
            
            return redirect('commande', commande_id)
    else:
        form = CommandeProduitForm(
            commande_produit.produit.stock + commande_produit.quantite, {
                'quantite': commande_produit.quantite
            }
        )
    
    return render_to_response("commande_modifier.html", {
        'form': form,
    }, context_instance=RequestContext(request))