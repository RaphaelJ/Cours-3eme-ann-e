# -*- coding: UTF-8 -*-

from django.db import models

DEVISES = (
    ('USD', "dollars américains"),
    ('EUR', "euros"),
    ('GBP', "livres sterling"),
)

MODES_PAIEMENT = (
    ("CRED", "Carte de crédit"),
    ("BANQ", "Virement bancaire"),
)

MODES_LIVRAISON = (
    ("TRAN", "Paquet postal"),
    ("ENTR", "Réception à l'entrepôt'"),
)

# Utilisateurs

class Utilisateur(models.Model):
    login = models.CharField(max_length=40, primary_key=True)
    mot_de_passe = models.CharField(max_length=40)
    email = models.EmailField(unique=True)
    
    nom = models.CharField(max_length=40)
    prenom = models.CharField(max_length=40)
    date_inscription = models.DateTimeField(auto_now_add=True)
    
    adresse = models.CharField(max_length=255, null=True)
    ville = models.CharField(max_length=40, null=True)
    code_postal = models.CharField(max_length=10, null=True)
    pays = models.ForeignKey('Pays', related_name="utilisateurs", null=True)

    mode_paiement = models.CharField(
        max_length=4, choices=MODES_PAIEMENT, null=True
    )
    
    mode_livraison = models.CharField(
        max_length=4, choices=MODES_LIVRAISON, null=True
    )  
    
    caddie = models.ManyToManyField('Produit', through='CaddieProduit')
    
    class Meta:
        ordering = ['login']

class Pays(models.Model):
    nom = models.CharField(max_length=40, primary_key=True)

# Catalogue
    
class Produit(models.Model):
    LANGUES = (
        ('FR', "Français"),
        ('EN', "Anglais"),
    )
    
    #ean = models.BigIntegerField(primary_key=True)
    titre = models.CharField(max_length=40)
    description = models.TextField()
    langue = models.CharField(max_length=2, choices=LANGUES)
    
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)
    
    stock = models.IntegerField()
    
    class Meta:
        ordering = ['titre']

class Livre(Produit):
    isbn = models.CharField(max_length=13, primary_key=True)
    auteur = models.ManyToManyField('Artiste', related_name="livres")
    editeur = models.ForeignKey('Editeur', related_name="livres")
    
    reliure = models.CharField(max_length=40)
    pages = models.IntegerField(null=True)
    publication = models.DateField(null=True)
    num_edition = models.IntegerField(null=True)
    
class Film(Produit):   
    SUPPORTS = (
        ('DVD', "DVD"),
        ('BLR', "Blue-Ray"),
        ('NUM', "En ligne (numérique)"),
        ('AUT', "Autre"),
    )
    
    NOTATIONS = (
        ('G', "Tout public"),
        ('PG', "Surveillance parentale recommandée"),
        ('PG-13', "13 ans ou plus"),
        ('R', "Enfants de moins de 17 ans accompagnés"),
        ('NC-17', "17 ans ou plus"),
    )
    
    acteurs = models.ManyToManyField('Artiste', related_name="films_acteur")
    realisateurs = models.ManyToManyField('Artiste', related_name="films_realisateur")
    studio = models.ForeignKey('Editeur', related_name="films")
    
    support = models.CharField(max_length=4, choices=SUPPORTS, null=True)
    disques = models.IntegerField(null=True)
    notation = models.CharField(max_length=5, choices=NOTATIONS, null=True)
    duree = models.IntegerField(null=True) # Durée du film en secondes

class Musique(Produit):
    SUPPORTS = (
        ('CD', "CD audio"),
        ('NUM', "En ligne (numérique)"),
        ('AUT', "Autre"),
    )
    
    auteurs = models.ManyToManyField('Artiste', related_name="musiques")
    label = models.ForeignKey('Editeur', related_name="musiques", null=True)
    
    support = models.CharField(max_length=4, choices=SUPPORTS, null=True)
    disques = models.IntegerField(null=True)
    publication = models.DateField(null=True)

class Artiste(models.Model):
    nom = models.CharField(max_length=40)
    prenom = models.CharField(max_length=40)
    nationalite = models.ForeignKey(Pays, related_name="artistes")
        
    class Meta:
        ordering = ['prenom', 'nom']

class Editeur(models.Model):
    nom = models.CharField(max_length=40)
    
    class Meta:
        ordering = ['nom']
        
# Caddies, listes d'envies, commandes et livraisons
    
class CaddieProduit(models.Model):
    utilisateur = models.ForeignKey('Utilisateur')
    produit = models.ForeignKey(Produit)
    quantite = models.IntegerField(default=1)
    
    class Meta:
        unique_together = ('utilisateur', 'produit')
        
class ListeEnvies(models.Model):
    utilisateur = models.ForeignKey('Utilisateur')
    nom = models.CharField(max_length=40)  
    produits = models.ManyToManyField(Produit, through='ListeEnviesProduit')
    
class ListeEnviesProduit(models.Model):
    liste = models.ForeignKey('ListeEnvies')
    produit = models.ForeignKey(Produit)
    
    quantite = models.IntegerField(default=1)
    
    class Meta:
        unique_together = ('liste', 'produit')
    
class Commande(models.Model):    
    utilisateur = models.ForeignKey('Utilisateur')
    
    # Adresse de livraison
    adresse = models.CharField(max_length=255)
    ville = models.CharField(max_length=40)
    code_postal = models.CharField(max_length=10)
    pays = models.ForeignKey(Pays, related_name="livraisons")
    
class CommandePaquet(models.Model):
    STATUS = (
        ('ATTEN', "En attente du transporteur"),
        ('TRANS', "En cours de livraison"),
        ('LIVRE', "Livraison effectuée"),
    )
    
    commande = models.ForeignKey('Commande')
    
    status = models.CharField(max_length=5, choices=STATUS)
    status_change = models.DateTimeField(auto_now=True)
       
    produits = models.ManyToManyField(Produit, through='CommandeProduit')
    
class CommandeProduit(models.Model):
    paquet = models.ForeignKey('CommandePaquet')
    produit = models.ForeignKey(Produit)
    quantite = models.IntegerField(default=1)
    
    # Copie du prix d'achat
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)
    
    class Meta:
        unique_together = ('paquet', 'produit')