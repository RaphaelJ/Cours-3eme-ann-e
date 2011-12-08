/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

/**
 *
 * @author rapha
 */
public class FreetaxStats1D implements FreetaxStatsProtocol {
    public static final int SECTORIEL = 1;
    public static final int HISTOGRAMME = 2;

    private String _categorie;
    private Integer _mois;
    private Integer _semaine;
    private int _type;

    public FreetaxStats1D(String _categorie, Integer _mois, Integer _semaine,
            int _type) {
        this._categorie = _categorie;
        this._mois = _mois;
        this._semaine = _semaine;
        this._type = _type;
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
     * @return the _mois
     */
    public Integer getMois() {
        return _mois;
    }

    /**
     * @param mois the _mois to set
     */
    public void setMois(Integer mois) {
        this._mois = mois;
    }

    /**
     * @return the _semaine
     */
    public Integer getSemaine() {
        return _semaine;
    }

    /**
     * @param semaine the _semaine to set
     */
    public void setSemaine(Integer semaine) {
        this._semaine = semaine;
    }

    /**
     * @return the _type
     */
    public int getType() {
        return _type;
    }

    /**
     * @param type the _type to set
     */
    public void setType(int type) {
        this._type = type;
    }
    
}