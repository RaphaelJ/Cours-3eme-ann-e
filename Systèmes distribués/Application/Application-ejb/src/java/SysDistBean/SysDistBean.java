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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author rapha
 */
@DeclareRoles({"grh", "infra", "plan"})
@Stateless
public class SysDistBean implements SysDistBeanRemote {
    @PersistenceContext(unitName="LibraryPU")
    EntityManager em;
    
    @RolesAllowed({"grh", "plan"})
    @Override
    public Pilote[] listerPilotes()
    {        
        return (Pilote[]) em.createNamedQuery("Pilotes.findAll")
                            .getResultList().toArray();
    }

    @RolesAllowed("grh")
    @Override
    public Pilote rechercherPilote(String nom) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("grh")
    @Override
    public void ajouterPilote(Pilote p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("grh")
    @Override
    public void modifierPilote(Pilote p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("grh")
    @Override
    public void supprimerPilote(Pilote p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed({"infra", "plan"})
    @Override
    public Aeroport[] listerAeroports() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void ajouterAeroport(Aeroport a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void modifierAeroport(Aeroport a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void supprimerAeroport(Aeroport a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed({"infra", "plan"})
    @Override
    public Avion[] listerAvions() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void ajouterAvion(Avion a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void modifierAvion(Avion a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("infra")
    @Override
    public void supprimerAvion(Avion a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("plan")
    @Override
    public Vol[] listerVols() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("plan")
    @Override
    public void ajouterVol(Vol v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("plan")
    @Override
    public void modifierVol(Vol v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @RolesAllowed("plan")
    @Override
    public void supprimerVol(Vol v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
