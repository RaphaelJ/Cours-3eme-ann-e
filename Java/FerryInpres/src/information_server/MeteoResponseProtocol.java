/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.Serializable;
import java.util.TreeMap;

/**
 *
 * @author rapha
 */
public class MeteoResponseProtocol implements Serializable {
    public enum Temps {
        PLUIES, AVERSES, ONDEES, COUVERT, SOLEIL, CHAUD, CANICULE
    };
    
    private TreeMap<Integer, Temps> _resultats = new TreeMap<Integer, Temps>();

    /**
     * @return the _resultats
     */
    public TreeMap<Integer, Temps> getResultats() {
        return _resultats;
    }
}
