from django.conf.urls.defaults import patterns, include, url
from django.contrib.auth.views import login, logout

import views

urlpatterns = patterns('',
    url(r'^$', views.catalogue, name="catalogue"),
    url(r'^produit/(\d+)/$', views.produit, name="produit"),
    
    url(r'^caddie/$', views.caddie, name="caddie"),
    url(r'^caddie/ajout/(\d+)/$', views.caddie, name="ajout-caddie"),

    url(r'^utilisateur/connexion/$', login, {
        'template_name': 'connexion.html'
    }),
)