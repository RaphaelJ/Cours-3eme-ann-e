/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceREST;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
@Table(name = "Pilote")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pilote.findAll", query = "SELECT p FROM Pilote p"),
    @NamedQuery(name = "Pilote.findByIdPilote", query = "SELECT p FROM Pilote p WHERE p.idPilote = :idPilote"),
    @NamedQuery(name = "Pilote.findByNom", query = "SELECT p FROM Pilote p WHERE p.nom = :nom"),
    @NamedQuery(name = "Pilote.findByPrenom", query = "SELECT p FROM Pilote p WHERE p.prenom = :prenom"),
    @NamedQuery(name = "Pilote.findByDateNaissance", query = "SELECT p FROM Pilote p WHERE p.dateNaissance = :dateNaissance")})
public class Pilote implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPilote")
    private Integer idPilote;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "dateNaissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pilote")
    private Collection<Vol> volCollection;

    public Pilote() {
    }

    public Pilote(Integer idPilote) {
        this.idPilote = idPilote;
    }

    public Pilote(Integer idPilote, String nom, String prenom) {
        this.idPilote = idPilote;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Integer getIdPilote() {
        return idPilote;
    }

    public void setIdPilote(Integer idPilote) {
        this.idPilote = idPilote;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
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
        hash += (idPilote != null ? idPilote.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pilote)) {
            return false;
        }
        Pilote other = (Pilote) object;
        if ((this.idPilote == null && other.idPilote != null) || (this.idPilote != null && !this.idPilote.equals(other.idPilote))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WebServiceREST.Pilote[ idPilote=" + idPilote + " ]";
    }
    
}
