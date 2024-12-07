/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.energie;

import java.sql.Connection;
import mg.sarobidy.generic.classes.Dao;

/**
 *
 * @author sarobidy
 */
public class Panneau extends Source {
    
    public double outputAt( int percent ){
        return (this.getPuissance() * percent) / 10;
    }
    
    public Panneau getPanneau( Connection connection, String requete ) throws Exception{
        Panneau[] panneaux =Dao.fetch(connection, this, requete); 
        return (panneaux.length == 0) ? null : panneaux[0];
    }
    
}
