/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Navire {
    private int _id;
    private String _nom;
    private int _capa_voitures;
    private int _capa_camions;
    
    public Navire(int id, String nom, int capa_voitures, int capa_camions)
    {
        this._id = id;
        this._nom = nom;
        this._capa_voitures = capa_voitures;
        this._capa_camions = capa_camions;
    }

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
     * @return the _capa_voitures
     */
    public int getCapaVoitures() {
        return _capa_voitures;
    }

    /**
     * @param capa_voitures the _capa_voitures to set
     */
    public void setCapaVoitures(int capa_voitures) {
        this._capa_voitures = capa_voitures;
    }

    /**
     * @return the _capa_camions
     */
    public int getCapaCamions() {
        return _capa_camions;
    }

    /**
     * @param capa_camions the _capa_camions to set
     */
    public void setCapaCamions(int capa_camions) {
        this._capa_camions = capa_camions;
    }   
}
