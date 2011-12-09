/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

/**
 *
 * @author rapha
 */
public class FreetaxStatsTestComp implements FreetaxStatsProtocol {
    private String _nationalite1;
    private String _nationalite2;
    private String _categorie;
    private String _marque;
    private float _seuil;

    public FreetaxStatsTestComp(String _nationalite1, String _nationalite2, String _categorie, String _marque, float _seuil) {
        this._nationalite1 = _nationalite1;
        this._nationalite2 = _nationalite2;
        this._categorie = _categorie;
        this._marque = _marque;
        this._seuil = _seuil;
    }

    /**
     * @return the _nationalite1
     */
    public String getNationalite1() {
        return _nationalite1;
    }

    /**
     * @param nationalite1 the _nationalite1 to set
     */
    public void setNationalite1(String nationalite1) {
        this._nationalite1 = nationalite1;
    }

    /**
     * @return the _nationalite2
     */
    public String getNationalite2() {
        return _nationalite2;
    }

    /**
     * @param nationalite2 the _nationalite2 to set
     */
    public void setNationalite2(String nationalite2) {
        this._nationalite2 = nationalite2;
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
     * @return the _marque
     */
    public String getMarque() {
        return _marque;
    }

    /**
     * @param marque the _marque to set
     */
    public void setMarque(String marque) {
        this._marque = marque;
    }

    /**
     * @return the _seuil
     */
    public float getSeuil() {
        return _seuil;
    }

    /**
     * @param seuil the _seuil to set
     */
    public void setSeuil(float seuil) {
        this._seuil = seuil;
    }
   
}
