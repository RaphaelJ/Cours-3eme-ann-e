package entites;

import entites.Aeroport;
import entites.Avion;
import entites.Pilote;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-11-15T17:17:19")
@StaticMetamodel(Vol.class)
public class Vol_ { 

    public static volatile SingularAttribute<Vol, Pilote> pilote;
    public static volatile SingularAttribute<Vol, Integer> idVol;
    public static volatile SingularAttribute<Vol, Date> heureDepart;
    public static volatile SingularAttribute<Vol, Aeroport> aeroportDepart;
    public static volatile SingularAttribute<Vol, Aeroport> aeroportArrivee;
    public static volatile SingularAttribute<Vol, Date> heureArrivee;
    public static volatile SingularAttribute<Vol, Avion> avion;

}