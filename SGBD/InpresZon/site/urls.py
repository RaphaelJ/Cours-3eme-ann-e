from django.conf.urls.defaults import patterns, include, url
from django.contrib.auth.views import login, logout

import views

urlpatterns = patterns('',
    # Catalogue
    url(r'^$', views.catalogue, name="catalogue"),
    url(r'^produit/(\d+)/$', views.produit, name="produit"),

    # Utilisateur
    url(r'^utilisateur/inscription/$', views.inscription, name="inscription"),
    url(r'^utilisateur/connexion/$', login, {
        'template_name': 'connexion.html'
    }, name="connexion"),
    url(r'^utilisateur/profil/$', views.profil, name="profil"),
    url(r'^utilisateur/adresses/$', views.adresses, name="adresses"),
    url(r'^utilisateur/adresse/(\d+)/$', views.adresse, name="adresse"),
    url(r'^utilisateur/deconnexion/$', logout, {
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
)
