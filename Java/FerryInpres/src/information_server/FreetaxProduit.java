/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.Serializable;

/**
 *
 * @author rapha
 */
public class FreetaxProduit implements Serializable {
    public enum Categories {
        ALCOOLS, PARFUMS, TABACS
    };
    
    private Categories _categorie;
    private String _nom;
    private String _quantite;
    private String _prix;

    public FreetaxProduit(Categories categorie, String nom, String quantite,
            String prix) {
        this._categorie = categorie;
        this._nom = nom;
        this._quantite = quantite;
        this._prix = prix;
    }

    /**
     * @return the _categorie
     */
    public Categories getCategorie() {
        return _categorie;
    }

    /**
     * @param categorie the _categorie to set
     */
    public void setCategorie(Categories categorie) {
        this._categorie = categorie;
    }

    /**
     * @return the _nom
     */
    public String getNom() {
        return _nom;
    }

    /**
     * @param nom the _nom to set
     */
    public void setNom(String nom) {
        this._nom = nom;
    }

    /**
     * @return the _quantite
     */
    public String getQuantite() {
        return _quantite;
    }

    /**
     * @param quantite the _quantite to set
     */
    public void setQuantite(String quantite) {
        this._quantite = quantite;
    }

    /**
     * @return the _prix
     */
    public String getPrix() {
        return _prix;
    }

    /**
     * @param prix the _prix to set
     */
    public void setPrix(String prix) {
        this._prix = prix;
    }
}