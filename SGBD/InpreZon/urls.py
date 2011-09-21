from django.conf.urls.defaults import patterns, include, url

import be, usa

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('',
    #url(r'^usa/$', views.livres, name='livres'),    
    
    #url(r'^$', 'InpreZon.views.home', name='home'),
    
    url(r'^be/', include(be.urls)),
    url(r'^usa/', include(usa.urls)),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)

