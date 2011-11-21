package entites;

import entites.Vol;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-11-15T17:17:19")
@StaticMetamodel(Avion.class)
public class Avion_ { 

    public static volatile CollectionAttribute<Avion, Vol> volCollection;
    public static volatile SingularAttribute<Avion, Integer> idAvion;
    public static volatile SingularAttribute<Avion, Integer> nbPlaces;
    public static volatile SingularAttribute<Avion, String> type;

}