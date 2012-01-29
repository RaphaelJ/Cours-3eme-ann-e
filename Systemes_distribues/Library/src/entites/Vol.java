/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rapha
 */
@Entity
@Table(name = "Vol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vol.findAll", query = "SELECT v FROM Vol v"),
    @NamedQuery(name = "Vol.findByIdVol", query = "SELECT v FROM Vol v WHERE v.idVol = :idVol"),
    @NamedQuery(name = "Vol.findByHeureDepart", query = "SELECT v FROM Vol v WHERE v.heureDepart = :heureDepart"),
    @NamedQuery(name = "Vol.findByHeureArrivee", query = "SELECT v FROM Vol v WHERE v.heureArrivee = :heureArrivee")})
public class Vol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idVol")
    private Integer idVol;
    @Basic(optional = false)
    @Column(name = "heureDepart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureDepart;
    @Basic(optional = false)
    @Column(name = "heureArrivee")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureArrivee;
    @JoinColumn(name = "pilote", referencedColumnName = "idPilote")
    @ManyToOne(optional = false)
    private Pilote pilote;
    @JoinColumn(name = "avion", referencedColumnName = "idAvion")
    @ManyToOne(optional = false)
    private Avion avion;
    @JoinColumn(name = "aeroportArrivee", referencedColumnName = "idAeroport")
    @ManyToOne(optional = false)
    private Aeroport aeroportArrivee;
    @JoinColumn(name = "aeroportDepart", referencedColumnName = "idAeroport")
    @ManyToOne(optional = false)
    private Aeroport aeroportDepart;

    public Vol() {
    }

    public Vol(Integer idVol) {
        this.idVol = idVol;
    }

    public Vol(Integer idVol, Date heureDepart, Date heureArrivee) {
        this.idVol = idVol;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
    }

    public Integer getIdVol() {
        return idVol;
    }

    public void setIdVol(Integer idVol) {
        this.idVol = idVol;
    }

    public Date getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(Date heureDepart) {
        this.heureDepart = heureDepart;
    }

    public Date getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(Date heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public Pilote getPilote() {
        return pilote;
    }

    public void setPilote(Pilote pilote) {
        this.pilote = pilote;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
    }

    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVol != null ? idVol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vol)) {
            return false;
        }
        Vol other = (Vol) object;
        if ((this.idVol == null && other.idVol != null) || (this.idVol != null && !this.idVol.equals(other.idVol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.Vol[ idVol=" + idVol + " ]";
    }
    
}
