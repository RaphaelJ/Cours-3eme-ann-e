/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SysDistBean;

import entites.Aeroport;
import entites.Avion;
import entites.Pilote;
import entites.Vol;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author rapha
 */
//@DeclareRoles({"grh", "infra", "plan"})
@Stateless
public class SysDistBean implements SysDistBeanRemote {
    @PersistenceContext(unitName="Application-PU")
    EntityManager em;
    
    @RolesAllowed({"grh", "plan"})
    public Pilote[] listerPilotes()
    {        
        return (Pilote[]) em.createNamedQuery("Pilote.findAll")
                            .getResultList().toArray(new Pilote[0]);
    }

    @RolesAllowed({"grh", "plan"})
    public Pilote rechercherPilote(String nom) {
        Query nq = em.createNamedQuery("Pilote.findByNom");
        nq.setParameter("nom", nom);
        return (Pilote) nq.getSingleResult();
    }

    @RolesAllowed("grh")
    public void ajouterPilote(Pilote p) {
        em.persist(p);
    }

    @RolesAllowed("grh")
    public void modifierPilote(Pilote p) {
        em.merge(p);
    }

    @RolesAllowed("grh")
    public void supprimerPilote(int idPilote) {
        Query nq = em.createNamedQuery("Pilote.findByIdPilote");
        nq.setParameter("idPilote", idPilote);
        em.remove((Pilote) nq.getSingleResult());
    }

    @RolesAllowed({"infra", "plan"})
    public Aeroport[] listerAeroports() {
        return (Aeroport[]) em.createNamedQuery("Aeroport.findAll")
                              .getResultList().toArray(new Aeroport[0]);
    }
    
    @RolesAllowed({"infra", "plan"})
    public Aeroport rechercherAeroport(String nom) {
        Query nq = em.createNamedQuery("Aeroport.findByNom");
        nq.setParameter("nom", nom);
        return (Aeroport) nq.getSingleResult();
    }

    @RolesAllowed("infra")
    public void ajouterAeroport(Aeroport a) {
        em.persist(a);
    }

    @RolesAllowed("infra")
    public void modifierAeroport(Aeroport a) {
        em.merge(a);
    }

    @RolesAllowed("infra")
    public void supprimerAeroport(int idAeroport) {
        Query nq = em.createNamedQuery("Aeroport.findByIdAeroport");
        nq.setParameter("idAeroport", idAeroport);
        em.remove((Aeroport) nq.getSingleResult());
    }

    @RolesAllowed({"infra", "plan"})
    public Avion[] listerAvions() {
        return (Avion[]) em.createNamedQuery("Avion.findAll")
                           .getResultList().toArray(new Avion[0]);      
    }
    
    @RolesAllowed({"infra", "plan"})
    public Avion rechercherAvion(int idAvion) {
        Query nq = em.createNamedQuery("Avion.findByIdAvion");
        nq.setParameter("idAvion", idAvion);
        return (Avion) nq.getSingleResult();
    }

    @RolesAllowed("infra")
    public void ajouterAvion(Avion a) {
        em.persist(a);
    }

    @RolesAllowed("infra")
    public void modifierAvion(Avion a) {
        em.merge(a);
    }

    @RolesAllowed("infra")
    public void supprimerAvion(int idAvion) {
        Query nq = em.createNamedQuery("Avion.findByIdAvion");
        nq.setParameter("idAvion", idAvion);
        em.remove((Avion) nq.getSingleResult());
    }

    @RolesAllowed("plan")
    public Vol[] listerVols() {
        return (Vol[]) em.createNamedQuery("Vol.findAll")
                         .getResultList().toArray(new Vol[0]);  
    }

    @RolesAllowed("plan")
    public void ajouterVol(Vol v) {
        em.persist(v);
    }

    @RolesAllowed("plan")
    public void modifierVol(Vol v) {
        em.merge(v);
    }

    @RolesAllowed("plan")
    public void supprimerVol(int idVol) {
        Query nq = em.createNamedQuery("Vol.findByIdVol");
        nq.setParameter("idVol", idVol);
        em.remove((Vol) nq.getSingleResult());        
    }
}