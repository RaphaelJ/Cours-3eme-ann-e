/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

import java.io.Serializable;
import java.sql.*;
import java.util.LinkedList;

/**
 *
 * @author rapha
 */
public class DatabaseBean implements Serializable {
    private Connection _conn;
    
    // Se connecte à la base de données MySQL
    public DatabaseBean()
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException
    {   
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost/ferryinpres";
        this._conn = DriverManager.getConnection(url, "ferryinpres", "pass");
    }
    
    public LinkedList<Accompagnant> getAccompagnants() throws SQLException
    {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM accompagnant");
        
        LinkedList<Accompagnant> accompagnants = new LinkedList<Accompagnant>();
        while (rs.next()) {
            accompagnants.add(
                new Accompagnant(
                    rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                    rs.getString("adresse"), rs.getInt("voyageur")
                )
            );
        }
        
        return accompagnants;
    }

    /**
     * @return the _navires
     */
    public LinkedList<Navire> getNavires() throws SQLException {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM navire");
        
        LinkedList<Navire> navires = new LinkedList<Navire>();
        while (rs.next()) {
            navires.add(
                new Navire(
                    rs.getInt("id"), rs.getString("nom"),
                    rs.getInt("capa_voitures"), rs.getInt("capa_camions")
                )
            );
        }
        
        return navires;
    }

    /**
     * @return the _ports
     */
    public LinkedList<Port> getPorts() throws SQLException {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM port");
        
        LinkedList<Port> ports = new LinkedList<Port>();
        while (rs.next()) {
            ports.add(
                new Port(
                    rs.getString("nom"), rs.getInt("nombre_terminaux"),
                    rs.getInt("nombre_terminaux_occupes"),
                    rs.getString("pays")
                )
            );
        }
        
        return ports;
    }

    /**
     * @return the _reservations
     */
    public LinkedList<Reservation> getReservations() throws SQLException {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM reservation");
        
        LinkedList<Reservation> reservations = new LinkedList<Reservation>();
        while (rs.next()) {
            reservations.add(
                new Reservation(
                    rs.getInt("id"), rs.getBoolean("paye"),
                    rs.getBoolean("checkin"), rs.getInt("traversee"),
                    rs.getInt("voyageur")
                )
            );
        }
        
        return reservations;
    }

    /**
     * @return the _traversees
     */
    public LinkedList<Traversee> getTraversees() throws SQLException {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM traversee");
        
        LinkedList<Traversee> traversees = new LinkedList<Traversee>();
        while (rs.next()) {
            traversees.add(
                new Traversee(
                    rs.getInt("id"), rs.getTimestamp("date_depart"),
                    rs.getInt("navire"), rs.getString("port_depart"),
                    rs.getString("port_arrivee")
                )
            );
        }
        
        return traversees;
    }

    /**
     * @return the _voyageurs
     */
    public LinkedList<Voyageur> getVoyageurs() throws SQLException {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery("SELECT * FROM voyageur");
        
        LinkedList<Voyageur> voyageurs = new LinkedList<Voyageur>();
        while (rs.next()) {
            voyageurs.add(
                new Voyageur(
                    rs.getInt("id"), rs.getString("nom"),
                    rs.getString("prenom"), rs.getString("adresse"),
                    rs.getString("email")
                )
            );
        }
        
        return voyageurs;
    }

    /**
     * @return the _conn
     */
    public Connection getConn() {
        return _conn;
    }
}