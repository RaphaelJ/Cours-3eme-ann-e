# -*- coding: UTF-8 -*-

from django.db import models

# Utilisateurs

class Utilisateur(models.Model):
    login = models.CharField(max_length=40, primary_key=True)
    mot_de_passe = models.CharField(max_length=40)
    email = models.EmailField(unique=True)
    
    nom = models.CharField(max_length=40)
    prenom = models.CharField(max_length=40)
    date_inscription = models.DateTimeField(auto_now=True)
    
    adresse = models.CharField(max_length=255)
    ville = models.CharField(max_length=40)
    code_postal = models.CharField(max_length=10)
    pays = models.ForeignKey('Pays', related_name="utilisateurs")
    
    caddie = models.ManyToManyField('Produit', through='CaddieElement')
    
    class Meta:
        ordering = ['login']

class Pays(models.Model):
    nom = models.CharField(max_length=40, primary_key=True)

# Catalogue
    
class Produit(models.Model):
    DEVISES = (
        ('USD', "dollars américains"),
        ('EUR', "euros"),
        ('GBP', "livres sterling"),
    )
    
    #ean = models.BigIntegerField(primary_key=True)
    titre = models.CharField(max_length=40)
    description = models.TextField()
    langue = models.CharField(max_length=2)
    
    prix = models.DecimalField(max_digits=10, decimal_places=2)
    devise = models.CharField(max_length=3, choices=DEVISES)
    
    stock = models.IntegerField()
 
    DEVISES = (
        ('USD', "dollars américains"),
        ('EUR', "euros"),
        ('GBP', "livres sterling"),
    )
    
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
        ('BR', "Blue-Ray"),
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
    nationalite = models.ForeignKey('Pays', related_name="artistes")
        
    class Meta:
        ordering = ['prenom', 'nom']

class Editeur(models.Model):
    nom = models.CharField(max_length=40)
    
    class Meta:
        ordering = ['nom']
        
# Caddies, commandes et livraisons
    
class CaddieElement(models.Model):
    utilisateur = models.ForeignKey('Utilisateur')
    produit = models.ForeignKey('Produit')
    
    quantite = models.IntegerField(default=1)
    
    class Meta:
        unique_together = ('utilisateur', 'produit')
    
class Commande(models.Model):
    utilisateurs = models.ForeignKey('Utilisateur')
    
    # Livraison
    adresse = models.CharField(max_length=255)
    ville = models.CharField(max_length=40)
    code_postal = models.CharField(max_length=10)
    pays = models.ForeignKey('Pays', related_name="livraisons")
    
    produits = models.ManyToManyField('Produit', through='CommandeElement')
    
class CommandeElement(models.Model):
    commande = models.ForeignKey('Commande')
    produit = models.ForeignKey('Produit')
    
    quantite = models.IntegerField(default=1)
    
    class Meta:
        unique_together = ('commande', 'produit')
        
class Livraison(models.Model):
    STATUS = (
        ('0', 'En attente du transporteur'),
        ('1', 'En cours de livraison'),
        ('2', 'Livré'),
    )
    
    commande = models.ForeignKey('Commande', primary_key=True)
    status = models.CharField(max_length=1, choices=STATUS)