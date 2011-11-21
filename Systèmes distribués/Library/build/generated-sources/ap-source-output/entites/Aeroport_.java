package entites;

import entites.Vol;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-11-15T17:17:19")
@StaticMetamodel(Aeroport.class)
public class Aeroport_ { 

    public static volatile SingularAttribute<Aeroport, String> adresse;
    public static volatile SingularAttribute<Aeroport, String> codePostal;
    public static volatile CollectionAttribute<Aeroport, Vol> volCollection;
    public static volatile SingularAttribute<Aeroport, String> ville;
    public static volatile CollectionAttribute<Aeroport, Vol> volCollection1;
    public static volatile SingularAttribute<Aeroport, String> nom;
    public static volatile SingularAttribute<Aeroport, Integer> idAeroport;

}