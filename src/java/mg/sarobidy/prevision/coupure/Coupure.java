/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.coupure;

/**
 *
 * @author sarobidy
 */
import java.sql.Connection;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import mg.sarobidy.prevision.departement.Secteur;
import mg.sarobidy.prevision.energie.Luminosite;
import sql.Connect;

public class Coupure {
    
    Date dateCoupure;
    Secteur[] secteurs;

    public Secteur[] getSecteurs() {
        return secteurs;
    }

    public void setSecteurs(Secteur[] secteurs) {
        this.secteurs = secteurs;
    }

    public Date getDateCoupure() {
        return dateCoupure;
    }

    public void setDateCoupure(Date dateCoupure) {
        this.dateCoupure = dateCoupure;
    }
    
    public void setDateCoupure(String date){
        this.setDateCoupure(Date.valueOf(date));
    }
    
    public Coupure getCoupureAt( String date ) throws Exception{
        Coupure coupure = new Coupure();
        coupure.setDateCoupure(date);
        try(  Connection connection = new Connect().getPostgres()) {
            Secteur[] secteurss = new Secteur().getSecteurs( connection );
            Luminosite[] lums = new Luminosite().getLuminositeAt(connection, date);
            for( Secteur secteur : secteurss ){                       
//                secteur.getTotalEleveAt( connection, date); // ovaina getTotalElevePresent
                secteur.test(connection);
                secteur.predict(lums, date);
//                secteur.affineResults(connection, lums, date);
            }
            coupure.setSecteurs(secteurss);
            return coupure; 
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }      
    }
    
    public Coupure getFicheCoupure( String date ) throws Exception{
              Coupure coupure = new Coupure();
              coupure.setDateCoupure( date );
              try(Connection connection = new Connect().getPostgres()){
                    Secteur[] secteurss = new Secteur().getSecteurs(connection);
                    for( Secteur secteur : secteurss ){
                              secteur.getTotalEleveAt(connection, date);
                              secteur.findResults(connection);
                    }
                    coupure.setSecteurs(secteurss);
                    return coupure;
              }
    }
    
    public static int extractDate( String date ) throws Exception{
        Date toSee = Date.valueOf(date);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(toSee);
        
//        System.out.println("Day of week is :: " + timestamp.getHours() + " : " + timestamp.getMinutes());
        System.out.println("Day of week is :: " + calendar.get(Calendar.DAY_OF_WEEK));
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
     
}
