/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceREST;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NamedQuery(name = "Vol.findByHeureArrivee", query = "SELECT v FROM Vol v WHERE v.heureArrivee = :heureArrivee"),
    @NamedQuery(name = "Vol.findByStatus", query = "SELECT v FROM Vol v WHERE v.status = :status")})
public class Vol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idVol")
    private Integer idVol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heureDepart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureDepart;
    @Basic(optional = false)
    @NotNull
    @Column(name = "heureArrivee")
    @Temporal(TemporalType.TIMESTAMP)
    private Date heureArrivee;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "pilote", referencedColumnName = "idPilote")
    @ManyToOne(optional = false)
    private Pilote pilote;
    @JoinColumn(name = "avion", referencedColumnName = "idAvion")
    @ManyToOne(optional = false)
    private Avion avion;
    @JoinColumn(name = "aeroportDepart", referencedColumnName = "idAeroport")
    @ManyToOne(optional = false)
    private Aeroport aeroportDepart;
    @JoinColumn(name = "aeroportArrivee", referencedColumnName = "idAeroport")
    @ManyToOne(optional = false)
    private Aeroport aeroportArrivee;

    public Vol() {
    }

    public Vol(Integer idVol) {
        this.idVol = idVol;
    }

    public Vol(Integer idVol, Date heureDepart, Date heureArrivee, String status) {
        this.idVol = idVol;
        this.heureDepart = heureDepart;
        this.heureArrivee = heureArrivee;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Aeroport getAeroportDepart() {
        return aeroportDepart;
    }

    public void setAeroportDepart(Aeroport aeroportDepart) {
        this.aeroportDepart = aeroportDepart;
    }

    public Aeroport getAeroportArrivee() {
        return aeroportArrivee;
    }

    public void setAeroportArrivee(Aeroport aeroportArrivee) {
        this.aeroportArrivee = aeroportArrivee;
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
        return "WebServiceREST.Vol[ idVol=" + idVol + " ]";
    }
    
}
