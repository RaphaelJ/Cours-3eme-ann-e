from django.conf.urls.defaults import patterns, include, url
from django.contrib.auth.views import login, logout
from django.db import transaction

import views

urlpatterns = patterns('',
    # Catalogue
    url(r'^$', views.catalogue, name="catalogue"),
    url(r'^produit/(\d+)/$', views.produit, name="produit"),

    # Utilisateur
    url(r'^utilisateur/inscription/$', views.inscription, name="inscription"),
    url(r'^utilisateur/connexion/$', transaction.commit_on_success(login), {
        'template_name': 'connexion.html'
    }, name="connexion"),
    url(r'^utilisateur/profil/$', views.profil, name="profil"),
    url(r'^utilisateur/adresses/$', views.adresses, name="adresses"),
    url(r'^utilisateur/adresses/(\d+)/$', views.adresse, name="adresse-modifier"),
    url(r'^utilisateur/adresses/supprimer/(\d+)/$', views.adresse, name="adresse-supprimer"),
    url(r'^utilisateur/deconnexion/$', transaction.commit_on_success(logout), {
        'template_name': 'deconnexion.html',
        'next_page': '/utilisateur/connexion/',
    }, name="deconnexion"),

    # Caddie
    url(r'^caddie/$', views.caddie, name="caddie"),
    url(r'^caddie/ajout/(\d+)/$', views.caddie_ajout, name="caddie-ajout"),
    url(r'^caddie/supprimer/(\d+)/$',
        views.caddie_supprimer, name="caddie-supprimer"
    ),
    url(r'^caddie/modifier/(\d+)/$',
        views.caddie_modifier, name="caddie-modifier"
    ),

    # Commande
    url(r'^commande/$', views.commande, name="commande"),

    # Fichiers statiques
    (r'^static/(?P<path>.*)$', 'django.views.static.serve',
        {'document_root': '/static'}
    ),
)
