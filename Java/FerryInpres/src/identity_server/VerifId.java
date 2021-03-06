/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

/**
 *
 * @author rapha
 */
public class VerifId implements Protocol {
    private String _clientName;
    private String _clientSurname;
    private String _nationalite;
    private int _clientNationalId;

    public VerifId(String _clientName, String _clientSurname, String _nationalite, int _clientNationalId) {
        this._clientName = _clientName;
        this._clientSurname = _clientSurname;
        this._nationalite = _nationalite;
        this._clientNationalId = _clientNationalId;
    }

    /**
     * @return the _clientName
     */
    public String getClientName() {
        return _clientName;
    }

    /**
     * @param clientName the _clientName to set
     */
    public void setClientName(String clientName) {
        this._clientName = clientName;
    }

    /**
     * @return the _clientSurname
     */
    public String getClientSurname() {
        return _clientSurname;
    }

    /**
     * @param clientSurname the _clientSurname to set
     */
    public void setClientSurname(String clientSurname) {
        this._clientSurname = clientSurname;
    }

    /**
     * @return the _nationalite
     */
    public String getNationalite() {
        return _nationalite;
    }

    /**
     * @param nationalite the _nationalite to set
     */
    public void setNationalite(String nationalite) {
        this._nationalite = nationalite;
    }

    /**
     * @return the _clientNationalId
     */
    public int getClientNationalId() {
        return _clientNationalId;
    }

    /**
     * @param clientNationalId the _clientNationalId to set
     */
    public void setClientNationalId(int clientNationalId) {
        this._clientNationalId = clientNationalId;
    }
}
