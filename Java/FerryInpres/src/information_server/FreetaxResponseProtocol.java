/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.Serializable;

/**
 *
 * @author rapha
 */
public class FreetaxResponseProtocol implements Serializable {
    private FreetaxProduit[] _produits;

    public FreetaxResponseProtocol(FreetaxProduit[] produits) {
        this._produits = produits;
    }

    /**
     * @return the _produits
     */
    public FreetaxProduit[] getProduits() {
        return _produits;
    }

    /**
     * @param produits the _produits to set
     */
    public void setProduits(FreetaxProduit[] produits) {
        this._produits = produits;
    }
}