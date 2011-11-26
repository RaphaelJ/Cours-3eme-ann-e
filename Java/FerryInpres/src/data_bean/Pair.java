/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Pair<T, U> {
    private T _first;
    private U _second;

    public Pair(T _first, U _second) {
        this._first = _first;
        this._second = _second;
    }

    /**
     * @return the _first
     */
    public T getFirst() {
        return _first;
    }

    /**
     * @param first the _first to set
     */
    public void setFirst(T first) {
        this._first = first;
    }

    /**
     * @return the _second
     */
    public U getSecond() {
        return _second;
    }

    /**
     * @param second the _second to set
     */
    public void setSecond(U second) {
        this._second = second;
    }
}
