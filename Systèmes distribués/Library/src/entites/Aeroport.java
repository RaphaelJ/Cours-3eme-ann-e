/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rapha
 */
@Entity
@Table(name = "Aeroport")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Aeroport.findAll", query = "SELECT a FROM Aeroport a"),
    @NamedQuery(name = "Aeroport.findByIdAeroport", query = "SELECT a FROM Aeroport a WHERE a.idAeroport = :idAeroport"),
    @NamedQuery(name = "Aeroport.findByNom", query = "SELECT a FROM Aeroport a WHERE a.nom = :nom"),
    @NamedQuery(name = "Aeroport.findByAdresse", query = "SELECT a FROM Aeroport a WHERE a.adresse = :adresse"),
    @NamedQuery(name = "Aeroport.findByVille", query = "SELECT a FROM Aeroport a WHERE a.ville = :ville"),
    @NamedQuery(name = "Aeroport.findByCodePostal", query = "SELECT a FROM Aeroport a WHERE a.codePostal = :codePostal")})
public class Aeroport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idAeroport")
    private Integer idAeroport;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "adresse")
    private String adresse;
    @Basic(optional = false)
    @Column(name = "ville")
    private String ville;
    @Basic(optional = false)
    @Column(name = "codePostal")
    private String codePostal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aeroportArrivee")
    private Collection<Vol> volCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "aeroportDepart")
    private Collection<Vol> volCollection1;

    public Aeroport() {
    }

    public Aeroport(Integer idAeroport) {
        this.idAeroport = idAeroport;
    }

    public Aeroport(Integer idAeroport, String nom, String adresse, String ville, String codePostal) {
        this.idAeroport = idAeroport;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.codePostal = codePostal;
    }

    public Integer getIdAeroport() {
        return idAeroport;
    }

    public void setIdAeroport(Integer idAeroport) {
        this.idAeroport = idAeroport;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    @XmlTransient
    public Collection<Vol> getVolCollection() {
        return volCollection;
    }

    public void setVolCollection(Collection<Vol> volCollection) {
        this.volCollection = volCollection;
    }

    @XmlTransient
    public Collection<Vol> getVolCollection1() {
        return volCollection1;
    }

    public void setVolCollection1(Collection<Vol> volCollection1) {
        this.volCollection1 = volCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAeroport != null ? idAeroport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Aeroport)) {
            return false;
        }
        Aeroport other = (Aeroport) object;
        if ((this.idAeroport == null && other.idAeroport != null) || (this.idAeroport != null && !this.idAeroport.equals(other.idAeroport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.Aeroport[ idAeroport=" + idAeroport + " ]";
    }
    
}
