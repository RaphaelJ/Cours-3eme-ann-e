/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceREST;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rapha
 */
@Entity
@Table(name = "Avion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avion.findAll", query = "SELECT a FROM Avion a"),
    @NamedQuery(name = "Avion.findByIdAvion", query = "SELECT a FROM Avion a WHERE a.idAvion = :idAvion"),
    @NamedQuery(name = "Avion.findByType", query = "SELECT a FROM Avion a WHERE a.type = :type"),
    @NamedQuery(name = "Avion.findByNbPlaces", query = "SELECT a FROM Avion a WHERE a.nbPlaces = :nbPlaces")})
public class Avion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idAvion")
    private Integer idAvion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nbPlaces")
    private int nbPlaces;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "avion")
    private Collection<Vol> volCollection;

    public Avion() {
    }

    public Avion(Integer idAvion) {
        this.idAvion = idAvion;
    }

    public Avion(Integer idAvion, String type, int nbPlaces) {
        this.idAvion = idAvion;
        this.type = type;
        this.nbPlaces = nbPlaces;
    }

    public Integer getIdAvion() {
        return idAvion;
    }

    public void setIdAvion(Integer idAvion) {
        this.idAvion = idAvion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    @XmlTransient
    public Collection<Vol> getVolCollection() {
        return volCollection;
    }

    public void setVolCollection(Collection<Vol> volCollection) {
        this.volCollection = volCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAvion != null ? idAvion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Avion)) {
            return false;
        }
        Avion other = (Avion) object;
        if ((this.idAvion == null && other.idAvion != null) || (this.idAvion != null && !this.idAvion.equals(other.idAvion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WebServiceREST.Avion[ idAvion=" + idAvion + " ]";
    }
    
}
