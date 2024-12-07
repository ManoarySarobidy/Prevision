/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.energie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.departement.Secteur;

/**
 *
 * @author sarobidy
 */
@Table(name = "meteo")
public class Luminosite {
    
    @Column(isPrimaryKey = true)
    Integer id;
    @Column(name="temp")
    Timestamp time;
    @Column(name = "valeur")
    Integer value;
    @Column(name="timerep")
    Integer timeRepresentation;
    @Column
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTimeRepresentation() {
        return timeRepresentation;
    }

    public void setTimeRepresentation(Integer timeRepresentation) {
        this.timeRepresentation = timeRepresentation;
    }
    
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
    
    public void setTime(String date) throws Exception{
        this.setTime( Luminosite.parseTimestamp(date) );
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
    
    public Luminosite[] getLuminositeAt( Connection connection,  String date ) throws Exception{
        String sql = "select * from meteo where date = '%s' order by timerep asc";
        sql = String.format(sql, date);
        Luminosite[] luminosites = Dao.fetch(connection, this, sql);
        return luminosites;
    }
    
    public Luminosite[] getLuminositeAt( Connection connection, Date date ) throws Exception{
        return this.getLuminositeAt(connection, date.toString());
    }
    
    public double outputOf( Panneau panneau ){
        return ( panneau == null ) ? 0 : panneau.outputAt(this.getValue());
    }
    
    public double calculateDiff(Secteur secteur, double matin, double apres){
        double outputOfPanneau = this.outputOf( (Panneau)secteur.getSources()[0] );
         double diff = outputOfPanneau - (( this.getTimeRepresentation() < 12 ) ? matin : apres);
//         System.out.println("Besoin ============ " + matin + " .......... " + apres + " ................ " + diff);
         return diff;
    }
    
//// Static methods
    public static Timestamp parseTimestamp( String d ) throws Exception{
        d = d.replace("T", " ");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = (Date) format.parse(d);
        return new Timestamp(date.getTime());
    }
    
}
