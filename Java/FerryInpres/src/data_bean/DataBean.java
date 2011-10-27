/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

import java.beans.*;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

/**
 *
 * @author rapha
 */
public class DataBean implements Serializable {
    private Connection _conn;
    
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";
    private String sampleProperty;
    private PropertyChangeSupport propertySupport;
    
    public DataBean() {
        propertySupport = new PropertyChangeSupport(this);
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost/ferryinpres";
        conn = DriverManager.getConnection(url, "ferryinpres", "pass");
    }
    
    public LinkedList<>
    
    public String getSampleProperty() {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
}
