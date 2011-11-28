/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

import company_server.Config;
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
        String url = "jdbc:mysql://"+Config.MYSQL_HOST+"/ferryinpres";
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
    
    public Reservation getReservation(int id) throws SQLException
    {
        PreparedStatement instruc = this.getConn().prepareStatement(
            "SELECT * FROM reservation WHERE id = ?"
        );
        
        instruc.setInt(1, id);
        ResultSet rs = instruc.executeQuery();
        
        if (rs.next()) {
            return new Reservation(
                rs.getInt("id"), rs.getBoolean("paye"),
                rs.getBoolean("checkin"), rs.getInt("traversee"),
                rs.getInt("voyageur")
            );
        } else
            return null;
    }
    
    public void validateCheckin(int id) throws SQLException {
        PreparedStatement instruc = this.getConn().prepareStatement(
            "UPDATE reservation SET checkin = 1 WHERE id = ?"
        );
        instruc.setInt(1, id);
        instruc.executeUpdate();
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
    
    public Pair<Traversee, Navire> prochaineTraversee() throws SQLException
    {
        Statement instruc = this.getConn().createStatement();
        ResultSet rs = instruc.executeQuery(
            "SELECT t.*, t.id AS t_id, n.* "+
            "FROM traversee AS t " +
            "INNER JOIN navire AS n ON n.id = t.navire " +
            "WHERE t.date_depart > CURRENT_DATE " +
            "AND EXTRACT(DAY FROM t.date_depart) = EXTRACT(DAY FROM CURRENT_DATE) " +
            "ORDER BY t.date_depart ASC " +
            "LIMIT 1;"
        );
        
        if (rs.next()) {
            return new Pair<Traversee, Navire>(
                new Traversee(
                    rs.getInt("t_id"), rs.getTimestamp("date_depart"),
                    rs.getInt("navire"), rs.getString("port_depart"),
                    rs.getString("port_arrivee")
                ),
                new Navire(
                    rs.getInt("navire"), rs.getString("nom"),
                    rs.getInt("capa_voitures"), rs.getInt("capa_camions")
                )
            );
        } else 
            return null;
    }
    
    public int achatTicket(String nom_voyageur, int traversee_id)
            throws SQLException
    {
        PreparedStatement instruc = this.getConn().prepareStatement(
            "INSERT INTO voyageur(nom) VALUES (?);"
        );
        instruc.setString(1, nom_voyageur);
        instruc.execute();
        
        instruc = this.getConn().prepareStatement(
            "SELECT last_insert_id()"
        );  
        ResultSet rs = instruc.executeQuery();
        rs.next();
        int voyageur_id = rs.getInt("last_insert_id()");    
        
        instruc = this.getConn().prepareStatement(
            "INSERT INTO reservation " +
            "(paye, checkin, traversee, voyageur) VALUES(1, 1, ?, ?);"
        );
        instruc.setInt(1, traversee_id);
        instruc.setInt(2, voyageur_id);
        instruc.execute();
        
        return voyageur_id;
    }
    
    public void ajoutFile(int traversee_id, int voyageur_id,
            String immatriculation) throws SQLException
    {
        PreparedStatement instruc = this.getConn().prepareStatement(
            "INSERT INTO file (immatriculation, traversee_id, voyageur_id) " +
            "VALUES (?, ?, ?);"
        );
        instruc.setString(1, immatriculation);
        instruc.setInt(2, traversee_id);
        instruc.setInt(3, voyageur_id);
        instruc.execute();
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