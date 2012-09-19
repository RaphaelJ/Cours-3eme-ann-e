/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FreeTax;

/**
 *
 * @author rapha
 */
public class Produit {

    public Produit(int _id, String _categorie, String _nom, Double _prix, String _unite) {
        this._id = _id;
        this._categorie = _categorie;
        this._nom = _nom;
        this._prix = _prix;
        this._unite = _unite;
    }
    
    private int _id;
    private String _categorie;
    private String _nom;
    private Double _prix;
    private String _unite;

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(int id) {
        this._id = id;
    }

    /**
     * @return the _categorie
     */
    public String getCategorie() {
        return _categorie;
    }

    /**
     * @param categorie the _categorie to set
     */
    public void setCategorie(String categorie) {
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
     * @return the _prix
     */
    public Double getPrix() {
        return _prix;
    }

    /**
     * @param prix the _prix to set
     */
    public void setPrix(Double prix) {
        this._prix = prix;
    }

    /**
     * @return the _unite
     */
    public String getUnite() {
        return _unite;
    }

    /**
     * @param unite the _unite to set
     */
    public void setUnite(String unite) {
        this._unite = unite;
    }
}
