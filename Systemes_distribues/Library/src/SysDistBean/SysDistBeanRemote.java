package SysDistBean;

//import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.annotation.security.RolesAllowed;

import entites.*;

@Remote
public interface SysDistBeanRemote {

    Pilote[] listerPilotes();
    @RolesAllowed("grh")
    Pilote rechercherPilote(String nom);
    @RolesAllowed("grh")
    void ajouterPilote(Pilote p);
    @RolesAllowed("grh")
    void modifierPilote(Pilote p);
    @RolesAllowed("grh")
    void supprimerPilote(Pilote p);
    
    @RolesAllowed("infra")
    Aeroport[] listerAeroports();
    @RolesAllowed("infra")
    void ajouterAeroport(Aeroport a);
    @RolesAllowed("infra")
    void modifierAeroport(Aeroport a);
    @RolesAllowed("infra")
    void supprimerAeroport(Aeroport a);
    
    @RolesAllowed("infra")
    Avion[] listerAvions();
    @RolesAllowed("infra")
    void ajouterAvion(Avion a);
    @RolesAllowed("infra")
    void modifierAvion(Avion a);
    @RolesAllowed("infra")
    void supprimerAvion(Avion a);
    
    Vol[] listerVols();
    void ajouterVol(Vol v);
    void modifierVol(Vol v);
    void supprimerVol(Vol v);
}
