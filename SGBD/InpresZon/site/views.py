from django.shortcuts import render_to_response

from models import Produit

def catalogue(request):
    
    return render_to_response("catalogue.html", {
        'produits': Produit.objects.all(),
    })
    
def produit(request, produit_ean):
    return render_to_response("produit.html", {
        'produit': Produit.objects.get(ean=produit_ean),
    })