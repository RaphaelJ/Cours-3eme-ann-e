/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServiceSOAP;

import java.sql.*;
import java.util.LinkedList;

/**
 *
 * @author rapha
 */
public class Database {
    public static final String MYSQL_HOST = "127.0.0.1";
    
    private Connection _conn;
    
    public Database()
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://"+MYSQL_HOST+"/sysdist";
        this._conn = DriverManager.getConnection(url, "sysdist", "pass");
    }
    
    public Vol[] historiqueVols() throws SQLException
    {
        Statement instruc = this._conn.createStatement();
        ResultSet rs = instruc.executeQuery(
                "SELECT * " +
                "FROM view_vols " +
                "WHERE heureArrivee < CURRENT_DATE;"
        );
        
        LinkedList<Vol> vols = new LinkedList<Vol>();
        while (rs.next()) {
            vols.add(new Vol(
                rs.getTimestamp("heureDepart"), rs.getTimestamp("heureArrivee"),
                rs.getString("depart"), rs.getString("arrivee"),
                rs.getString("pilote"), rs.getInt("avion")
            ));
        }
        
        return vols.toArray(new Vol[0]);
    }
    
    public Vol[] prochainsVols() throws SQLException
    {
        Statement instruc = this._conn.createStatement();
        ResultSet rs = instruc.executeQuery(
                "SELECT * " +
                "FROM view_vols " +
                "WHERE heureArrivee >= CURRENT_DATE;"
        );
        
        LinkedList<Vol> vols = new LinkedList<Vol>();
        while (rs.next()) {
            vols.add(new Vol(
                rs.getTimestamp("heureDepart"), rs.getTimestamp("heureArrivee"),
                rs.getString("depart"), rs.getString("arrivee"),
                rs.getString("pilote"), rs.getInt("avion")
            ));
        }
        
        return vols.toArray(new Vol[0]);
    }
    
    public Vol[] volsDate(java.util.Date date) throws SQLException
    {
        PreparedStatement instruc = this._conn.prepareStatement(
                "SELECT * " +
                "FROM view_vols " +
                "WHERE CAST(heureArrivee AS DATE) = ?;"
        );
        instruc.setDate(1, new Date(date.getTime()));
        ResultSet rs = instruc.executeQuery();
        
        LinkedList<Vol> vols = new LinkedList<Vol>();
        while (rs.next()) {
            vols.add(new Vol(
                rs.getTimestamp("heureDepart"), rs.getTimestamp("heureArrivee"),
                rs.getString("depart"), rs.getString("arrivee"),
                rs.getString("pilote"), rs.getInt("avion")
            ));
        }
        
        return vols.toArray(new Vol[0]);
    }
}
