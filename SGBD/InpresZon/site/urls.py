from django.conf.urls.defaults import patterns, include, url

import views

urlpatterns = patterns('',
    url(r'^$', views.catalogue),
    url(r'^produit/(\d+)$/', views.produit),
)