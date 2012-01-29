package entites;

import entites.Vol;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.2.0.v20110202-r8913", date="2011-11-15T17:17:19")
@StaticMetamodel(Pilote.class)
public class Pilote_ { 

    public static volatile SingularAttribute<Pilote, String> prenom;
    public static volatile SingularAttribute<Pilote, Date> dateNaissance;
    public static volatile CollectionAttribute<Pilote, Vol> volCollection;
    public static volatile SingularAttribute<Pilote, String> nom;
    public static volatile SingularAttribute<Pilote, Integer> idPilote;

}