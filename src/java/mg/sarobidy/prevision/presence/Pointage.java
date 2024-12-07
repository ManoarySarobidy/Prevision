/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.presence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.departement.Salle;

/**
 *
 * @author sarobidy
 */
@Table( name = "pointage")
public class Pointage {
    
    @Column(name="idpointage")
    String id;
    @Column
    Date date;
    @Column
    Integer effectif;
    @Column(name = "temp")
    Timestamp time;
    @Column(foreign = true)
    Salle salle;
    
    Pointage[] details;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Pointage[] getDetails() {
        return details;
    }

    public void setDetails(Pointage[] details) {
        this.details = details;
    }

    public Integer getEffectif() {
        return effectif;
    }

    public void setEffectif(Integer effectif) {
        this.effectif = effectif;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }
    
    public Pointage[] getPointages( Connection connection, String requete ) throws Exception{
        Pointage[] pointages = Dao.fetch(connection, this, requete); 
        return pointages;
    }
    
    public void setDetails( String requete ) throws Exception{
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
}
