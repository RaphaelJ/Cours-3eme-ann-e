package SysDistBean;

import entites.*;
import javax.ejb.Remote;

@Remote
public interface SysDistBeanRemote {

    Pilote[] listerPilotes();
    Pilote rechercherPilote(String nom);
    void ajouterPilote(Pilote p);
    void modifierPilote(Pilote p);
    void supprimerPilote(int idPilote);
    
    Aeroport[] listerAeroports();
    Aeroport rechercherAeroport(String nom);
    void ajouterAeroport(Aeroport a);
    void modifierAeroport(Aeroport a);
    void supprimerAeroport(int idAeroport);
    
    Avion[] listerAvions();
    Avion rechercherAvion(int idAvion);
    void ajouterAvion(Avion a);
    void modifierAvion(Avion a);
    void supprimerAvion(int idAvion);
    
    Vol[] listerVols();
    void ajouterVol(Vol v);
    void modifierVol(Vol v);
    void supprimerVol(int idVol);
}
