/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceSOAP;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author rapha
 */
public class Vol {
    private Date _depart; 
    private Date _arrivee;
    private String _aeroportDepart;
    private String _aeroportArrivee;
    private String _pilote;
    private int _avion;
    
    public Vol()
    {
    }

    public Vol(Date _depart, Date _arrivee, String _aeroportDepart,
            String _aeroportArrivee, String _pilote, int _avion) {
        this._depart = _depart;
        this._arrivee = _arrivee;
        this._aeroportDepart = _aeroportDepart;
        this._aeroportArrivee = _aeroportArrivee;
        this._pilote = _pilote;
        this._avion = _avion;
    }

    /**
     * @return the _depart
     */
    @XmlElement(name="depart")
    public Date getDepart() {
        return _depart;
    }

    /**
     * @param depart the _depart to set
     */
    public void setDepart(Date depart) {
        this._depart = depart;
    }

    /**
     * @return the _arrivee
     */
    @XmlElement(name="arrivee")
    public Date getArrivee() {
        return _arrivee;
    }

    /**
     * @param arrivee the _arrivee to set
     */
    public void setArrivee(Date arrivee) {
        this._arrivee = arrivee;
    }

    /**
     * @return the _aeroportDepart
     */
    @XmlElement(name="aeroport_depart")
    public String getAeroportDepart() {
        return _aeroportDepart;
    }

    /**
     * @param aeroportDepart the _aeroportDepart to set
     */
    public void setAeroportDepart(String aeroportDepart) {
        this._aeroportDepart = aeroportDepart;
    }

    /**
     * @return the _aeroportArrivee
     */
    @XmlElement(name="aeroport_arrivee")
    public String getAeroportArrivee() {
        return _aeroportArrivee;
    }

    /**
     * @param aeroportArrivee the _aeroportArrivee to set
     */
    public void setAeroportArrivee(String aeroportArrivee) {
        this._aeroportArrivee = aeroportArrivee;
    }

    /**
     * @return the _pilote
     */
    @XmlElement(name="pilote")
    public String getPilote() {
        return _pilote;
    }

    /**
     * @param pilote the _pilote to set
     */
    public void setPilote(String pilote) {
        this._pilote = pilote;
    }

    /**
     * @return the _avion
     */
    @XmlElement(name="avion")
    public int getAvion() {
        return _avion;
    }

    /**
     * @param avion the _avion to set
     */
    public void setAvion(int avion) {
        this._avion = avion;
    }
}