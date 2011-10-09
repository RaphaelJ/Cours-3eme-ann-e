# -*- coding: UTF-8 -*-

from django.db import models
from django.conf import settings
from django.utils.translation import ugettext_lazy as _

DEVISES = (
    ('USD', _(u"dollars américains")),
    ('EUR', _(u"euros")),
    ('GBP', _(u"livres sterling")),
)

MODES_PAIEMENT = (
    ("CRED", _(u"Carte de crédit")),
    ("BANQ", _(u"Virement bancaire")),
)

MODES_LIVRAISON = (
    ("TRAN", _(u"Paquet postal")),
    ("ENTR", _(u"Réception à l'entrepôt'")),
)

# Utilisateurs

class Pays(models.Model):
    nom_fr = models.CharField(max_length=30)
    nom_en = models.CharField(max_length=30)

    @property
    def nom(self):
        """ Donne le nom du pays dans la langue du site """
        if settings.LANGUAGE_CODE == 'fr_FR':
            return self.nom_fr
        else:
            return self.nom_en
    
    def __unicode__(self):
        return self.nom

class Adresse(models.Model):
    adresse = models.CharField(max_length=255)
    ville = models.CharField(max_length=30)
    code_postal = models.CharField(max_length=10)
    pays = models.ForeignKey(Pays, related_name="adresses")

class Utilisateur(models.Model):
    login = models.CharField(max_length=30, primary_key=True)
    mot_de_passe = models.CharField(max_length=64) # SHA-1 + sel
    email = models.EmailField(unique=True)
    admin = models.BooleanField(default=False, editable=False)
    
    nom = models.CharField(max_length=30)
    prenom = models.CharField(max_length=30)
    date_inscription = models.DateTimeField(auto_now_add=True, editable=False)

    adresses = models.ManyToManyField(Adresse, null=True, editable=False)

    mode_paiement = models.CharField(
        max_length=4, choices=MODES_PAIEMENT, blank=True, editable=False
    )
    
    mode_livraison = models.CharField(
        max_length=4, choices=MODES_LIVRAISON, blank=True, editable=False
    )  
    
    caddie = models.ManyToManyField(
        'Produit', through='CaddieProduit', editable=False
    )
    
    class Meta:
        ordering = ['login']

class Session(models.Model):
    """
    Stoque les informations sur une session d'un visiteur.
    Les données sont stockées dans un dictionnaire sérialisé et sont
    accessibles via une clé génerée et stockée dans un cookie sur le navigateur
    du visiteur.
    """
    cle = models.CharField(max_length=40, primary_key=True)
    donnees = models.TextField()
    expiration = models.DateTimeField(db_index=True)

# Catalogue

LIVRE = "LIVRE"
FILM = "FILM"
MUSIQUE = "MUSIQUE"

class Fournisseur(models.Model):
    nom = models.CharField(max_length=30)
    adresse = models.ForeignKey(Adresse)
    
class Produit(models.Model):
    LANGUES = (
        ('FR', _(u"Français")),
        ('EN', _(u"Anglais")),
    )
    ORIGINES = (
        ('BE', _(u"Centrale belge")),
        ('USA', _(u"Centrale américaine")),
        ('UK', _(u"Centrale anglaise")),
    )
    
    ean = models.BigIntegerField(primary_key=True)
    titre = models.CharField(max_length=30)
    description = models.TextField()
    langue = models.CharField(max_length=2, choices=LANGUES)
    
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)
    
    stock = models.IntegerField(default=0)
    devise = models.CharField(max_length=3, choices=DEVISES)

    fournisseur = models.ForeignKey(Fournisseur)

    origine = models.CharField(max_length=3, choices=ORIGINES)
    
    class Meta:
        ordering = ('titre',)

    @property
    def type_produit(self):
        try:
            self.livre
            return LIVRE
        except:
            try:
                self.film
                return FILM
            except:
                try:
                    self.musique
                    return MUSIQUE
                except:
                    return None

class Artiste(models.Model):
    nom = models.CharField(max_length=30)
    nationalite = models.ForeignKey(Pays, related_name="artistes", null=True)

    class Meta:
        ordering = ('nom',)

class Editeur(models.Model):
    nom = models.CharField(max_length=30, primary_key=True)

    class Meta:
        ordering = ('nom',)

class Livre(Produit):
    isbn = models.CharField(max_length=13, unique=True)
    auteurs = models.ManyToManyField(Artiste, related_name="livres")
    editeur = models.ForeignKey(Editeur, related_name="livres")
    
    reliure = models.CharField(max_length=30)
    pages = models.IntegerField(null=True)
    publication = models.DateField(null=True)
    num_edition = models.IntegerField(null=True)
    
class Film(Produit):   
    SUPPORTS = (
        ('DVD', u"DVD"),
        ('BLR', u"Blue-Ray"),
        ('NUM', _(u"En ligne (contenu numérique)")),
        ('AUT', _(u"Autre")),
    )
    
    NOTATIONS = (
        ('G', _(u"Tout public")),
        ('PG', _(u"Surveillance parentale recommandée")),
        ('PG-13', _(u"13 ans ou plus")),
        ('R', _(u"Enfants de moins de 17 ans accompagnés")),
        ('NC-17', _(u"17 ans ou plus")),
    )
    
    acteurs = models.ManyToManyField(Artiste, related_name="films_acteur")
    realisateurs = models.ForeignKey(Artiste, related_name="films_realisateur")
    studio = models.ForeignKey(Editeur, related_name="films")
    
    support = models.CharField(max_length=4, choices=SUPPORTS, blank=True)
    disques = models.IntegerField(null=True)
    notation = models.CharField(max_length=5, choices=NOTATIONS, blank=True)
    duree = models.IntegerField(null=True) # Durée du film en secondes

class Musique(Produit):
    SUPPORTS = (
        ('CD', _(u"CD audio")),
        ('NUM', _(u"En ligne (numérique)")),
        ('AUT', _(u"Autre")),
    )
    
    auteurs = models.ManyToManyField(Artiste, related_name="musiques")
    label = models.ForeignKey(Editeur, related_name="musiques", null=True)
    
    support = models.CharField(max_length=4, choices=SUPPORTS, blank=True)
    disques = models.IntegerField(null=True)
    publication = models.DateField(null=True)
        
# Caddies, listes d'envies, commandes et livraisons
    
class CaddieProduit(models.Model):
    utilisateur = models.ForeignKey(Utilisateur, editable=False)
    produit = models.ForeignKey(Produit, editable=False)
    quantite = models.IntegerField(default=1)

    @property
    def prix_total(self):
        return self.quantite * self.produit.prix

    @property
    def en_stock(self):
        return self.quantite <= self.produit.stock
    
    class Meta:
        unique_together = ('utilisateur', 'produit')

class ListeEnvies(models.Model):
    utilisateur = models.ForeignKey(Utilisateur, editable=False)
    nom = models.CharField(max_length=30)
    produits = models.ManyToManyField(
        Produit, through='ListeEnviesProduit', editable=False
    )

    class Meta:
        unique_together = ('utilisateur', 'nom')
    
class ListeEnviesProduit(models.Model):
    liste = models.ForeignKey(ListeEnvies, editable=False)
    produit = models.ForeignKey(Produit, editable=False)
    
    quantite = models.IntegerField(default=1)
    
    class Meta:
        unique_together = ('liste', 'produit')
    
class Commande(models.Model):    
    utilisateur = models.ForeignKey(Utilisateur, editable=False)

    adresse_livraison = models.ForeignKey(Adresse)

    date_commande = models.DateTimeField(auto_now_add=True, editable=False)

    produits = models.ManyToManyField(
        Produit, through='CommandeProduit', editable=False
    )

class CommandeProduit(models.Model):
    commande = models.ForeignKey(Commande)
    produit = models.ForeignKey(Produit)
    quantite = models.IntegerField(default=1)

    # Copie du prix d'achat
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)

    class Meta:
        unique_together = ('commande', 'produit')
    
class CommandePaquet(models.Model):
    STATUS = (
        ('ATTEN', _(u"En attente du transporteur")),
        ('TRANS', _(u"En cours de livraison")),
        ('LIVRE', _(u"Livraison effectuée")),
    )
    
    commande = models.ForeignKey(Commande, editable=False)
    
    status = models.CharField(max_length=5, choices=STATUS)
    status_changement = models.DateTimeField(auto_now=True, editable=False)
    
    produits = models.ManyToManyField(
        Produit, through='CommandePaquetProduit', editable=False
    )

class CommandePaquetProduit(models.Model):
    paquet = models.ForeignKey(CommandePaquet)
    produit = models.ForeignKey(Produit)
    quantite = models.IntegerField(default=1)

    # Copie du prix d'achat
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)

    class Meta:
        unique_together = ('paquet', 'produit')

# Gestion des erreurs

class Erreur(models.Model):
    date_erreur = models.DateTimeField(auto_now_add=True)
    erreur = models.TextField()
    utilisateur = models.ForeignKey(Utilisateur, null=True)