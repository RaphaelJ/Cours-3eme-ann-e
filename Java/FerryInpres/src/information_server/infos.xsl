<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : html_transformer.xsl
    Created on : 7 novembre 2011, 21:13
    Author     : rapha
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/infos">
        <html>
            <head>
                <title>Resultat de votre demande d'informations</title>
            </head>
            <body>
                <h1>Informations aux voyageurs</h1>
                
                <h2>Valeurs des monnaies</h2>
                <ul>
                    <xsl:apply-templates select="monnaies"/>
                </ul> 
                
                <h2>Informations météorologiques</h2>
                <ul>
                    <xsl:apply-templates select="meteo"/>
                </ul> 
                
                <h2>Produits freetax en vente</h2>
                <ul>
                    <xsl:apply-templates select="freetax"/>
                </ul> 
            </body>
        </html>
    </xsl:template>

    <xsl:template match="monnaie">
        <li>
            <xsl:value-of select="@nom"/><xsl:text> = </xsl:text>
            <xsl:value-of select="@valeur"/><xsl:text>€</xsl:text>
        </li>
    </xsl:template>
    
    <xsl:template match="jour">
        <li>
            <xsl:text>Jour </xsl:text><xsl:value-of select="@numero"/><xsl:text>: </xsl:text>
            <xsl:value-of select="@temps"/>
        </li>
    </xsl:template>
    
    <xsl:template match="produit">
        <li>
            <xsl:value-of select="@nom"/><xsl:text> (</xsl:text>
            <xsl:value-of select="@quantite"/><xsl:text>) : </xsl:text>
            <xsl:value-of select="@prix"/>
        </li>
    </xsl:template>
</xsl:stylesheet>