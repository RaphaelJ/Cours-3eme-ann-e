/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

/**
 *
 * @author rapha
 */
public class FreetaxStatsDesc implements FreetaxStatsProtocol {
    private String _categorie;
    private Integer _mois;
    private Integer _semaine;

    public FreetaxStatsDesc(String _categorie, Integer _mois, Integer _semaine) {
        this._categorie = _categorie;
        this._mois = _mois;
        this._semaine = _semaine;
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
}
