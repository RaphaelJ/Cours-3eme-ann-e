/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.LinkedList;

/**
 *
 * @author rapha
 */
public class CSVBean {
    private LinkedList<Accompagnant> _accompagnants;
    private LinkedList<Navire> _navires;
    private LinkedList<Port> _ports;
    private LinkedList<Reservation> _reservations;
    private LinkedList<Traversee> _traversees;
    private LinkedList<Voyageur> _voyageurs;
    
    // Charge les fichiers CSV de données dans le répertoire csv_dir
    public CSVBean(String csv_dir) throws FileNotFoundException, IOException
    {
        File dir = new File(csv_dir);
        
        this._accompagnants = new LinkedList<Accompagnant>();
        for (String[] l : loadCSV(new File(dir, "accompagnants.csv"))) {
            this._accompagnants.add(
                new Accompagnant(
                   Integer.valueOf(l[0]), l[1], l[2], l[3],
                   Integer.valueOf(l[4])
                )
            );
        }
        
        this._navires = new LinkedList<Navire>();
        for (String[] l : loadCSV(new File(dir, "navires.csv"))) {
            this._navires.add(
                new Navire(
                    Integer.valueOf(l[0]), l[1], Integer.valueOf(l[2]),
                    Integer.valueOf(l[3])
                )
            );
        }
        
        this._ports = new LinkedList<Port>();
        for (String[] l : loadCSV(new File(dir, "ports.csv"))) {
            this._ports.add(
                new Port(
                    l[0], Integer.valueOf(l[1]), Integer.valueOf(l[2]), l[3]
                )
            );
        }
        
        this._reservations = new LinkedList<Reservation>();
        for (String[] l : loadCSV(new File(dir, "reservations.csv"))) {
            this._reservations.add(
                new Reservation(
                    Integer.valueOf(l[0]), Boolean.valueOf(l[1]),
                    Boolean.valueOf(l[2]), Integer.valueOf(l[3]),
                    Integer.valueOf(l[4])
                )
            );
        }
        
        this._traversees = new LinkedList<Traversee>();
        for (String[] l : loadCSV(new File(dir, "traversees.csv"))) {
            this._traversees.add(
                new Traversee(
                    Integer.valueOf(l[0]), Timestamp.valueOf(l[1]),
                    Integer.valueOf(l[2]), l[3], l[4]
                )
            );
        }
        
        this._voyageurs = new LinkedList<Voyageur>();
        for (String[] l : loadCSV(new File(dir, "voyageurs.csv"))) {
            this._voyageurs.add(
                new Voyageur(
                    Integer.valueOf(l[0]), l[1], l[2], l[3], l[4]
                )
            );
        }
    }
    
    public static LinkedList<String[]> loadCSV(File path)
            throws FileNotFoundException, IOException
    {
        BufferedReader f = new BufferedReader(
            new InputStreamReader(new DataInputStream(new FileInputStream(path)))
        );
        
        LinkedList<String[]> lines = new LinkedList<String[]>();
        String line;
        while ((line = f.readLine()) != null) {
            String[] values = line.split(",");
            lines.add(values);
        }
        
        return lines;
    }

    /**
     * @return the _accompagnants
     */
    public LinkedList<Accompagnant> getAccompagnants() {
        return _accompagnants;
    }

    /**
     * @return the _navires
     */
    public LinkedList<Navire> getNavires() {
        return _navires;
    }

    /**
     * @return the _ports
     */
    public LinkedList<Port> getPorts() {
        return _ports;
    }

    /**
     * @return the _reservations
     */
    public LinkedList<Reservation> getReservations() {
        return _reservations;
    }

    /**
     * @return the _traversees
     */
    public LinkedList<Traversee> getTraversees() {
        return _traversees;
    }

    /**
     * @return the _voyageurs
     */
    public LinkedList<Voyageur> getVoyageurs() {
        return _voyageurs;
    }
}
