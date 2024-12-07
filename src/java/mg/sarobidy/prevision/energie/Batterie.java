/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.energie;

import java.sql.Connection;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.coupure.Etat;
import mg.sarobidy.prevision.exception.CoupureException;

/**
 *
 * @author sarobidy
 */

public class Batterie extends Source {
    
    Double limit = 50.0;
    Double deduit = 0.0;
    boolean avalaible = true;

    public boolean isAvalaible() {
        return avalaible;
    }

    public void setAvalaible(boolean avalaible) {
        this.avalaible = avalaible;
    }
    
    
    public Double getDeduit() {
        return deduit;
    }

    public void setDeduit(Double deduit) {
        this.deduit = deduit;
    }
    
        
    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }
    
    public double getReste(){
        return this.getPuissance() - this.getDeduit();
    }

    public void deduire( double value ) throws CoupureException{
        double percent = this.getPercent();
        double deduction_temporaire = this.getDeduit() + value;
//        System.out.println(deduction_temporaire);
        if( value >= percent || this.getDeduit() >= percent ||  deduction_temporaire >= percent ){
            this.setAvalaible(false);
            throw new CoupureException("Courant insuffisant"); 
        }
        this.setDeduit(deduction_temporaire);
    }
    
    public void affineResults( double aDeduire ) throws CoupureException{ // Ny ataony de ny mitady hoe amin'ny firy precisement, Ny anaovana azy de alaina le valeur a deduire iny de 
        double minute = this.getOutputPerMinute(aDeduire);
        
        System.out.println("======> deduction  is " + aDeduire + " .............  " + this.getDeduit() + " ..... minute ....." + minute);
        
        // Ovaina kely ny fomba i-calculena azy eto
        // Andao itady fika hafa kely
        
        double consomme = 0;
        int m = 0;
        while( consomme != aDeduire && m <= 59){
                  // 
                  try{
                            this.deduire(minute);
                            consomme = consomme + minute;
                            System.out.println("Consomme is =  " + consomme + " minutes == " + m);
                            m = m + 1;
                            
                  }catch( CoupureException coupure ){
                              coupure.setMinute( m);
                              throw coupure;
                  }
        }
        
        
//        
//        for( int i = 1 ; i <= 59 ; i++ ){
//            try{
//                this.deduire( minute );
//                // Ato izao isika
//            }catch(CoupureException coupure){
//                      System.out.println("------------------ 4 > Batterie s'est arrété à : " + i);
//                coupure.setMinute( i );
//                throw coupure;
//            }
//        }
    }
    
    public void affineResults( Etat etat, double aDeduire ) throws CoupureException{ // Ny ataony de ny mitady hoe amin'ny firy precisement, Ny anaovana azy de alaina le valeur a deduire iny de 
        double minute = this.getOutputPerMinute(aDeduire);
        
//        System.out.println("======> deduction  is " + aDeduire + " .............  " + this.getDeduit() + " ..... minute ....." + minute);
        
        // Ovaina kely ny fomba i-calculena azy eto
        // Andao itady fika hafa kely
        
        double consomme = 0;
        int m = 1;
        
        while( consomme != aDeduire && m <= 59){
                  // 
                  try{
                            this.deduire(minute);
                            consomme = consomme + minute;
                            etat.setRealMinutes(m);
                            //System.out.println(etat.getRealMinute());
                            m = m + 1;    
                  }catch( CoupureException coupure ){
//                              coupure.setMinute( m);
                              throw coupure;
                  }
        }
    }
    
    public double getOutputPerMinute( double perHour ){
        // 1 heure -> perHour -> 60min
        //                  ?perMinute <- 1min
        double perMinute = perHour / 60;
        return perMinute;
    }
    
    public double getOutputPerSecond( double hour ){
        /// 1 min -> perMinute -> 60 sec
        ///                ?perSecond <- 1 sec
        double minute = this.getOutputPerMinute(hour);
        double second = minute / 60;
        return second;
    }
    
    public Batterie getBatterie( Connection connection, String requete ) throws Exception{
        Batterie[] batteries = Dao.fetch(connection, this, requete);
        return (batteries.length == 0) ? null : batteries[0];
    }
    
    public double getPercent(){
        return (this.getPuissance()* this.getLimit()) / 100;
    }
    
    public boolean canBeUsed(){
        double d = this.getDeduit();
        double percent = this.getPercent();
        return (d < percent);
    }
    
    public void recharger( double valeur ) throws Exception{
              // Alaina ilay valeur tiana hi-rechargena ilay batterie
              // Rehefa azo de alaina ny valeur 
              double temp = this.getDeduit();
              double diff = Math.abs( temp - valeur );
              if( this.getDeduit() > 0 && temp - valeur >=0 ){
                        this.setDeduit( temp - valeur );
              }else if( this.getDeduit() > 0 && temp - valeur < 0 ){
                      this.setDeduit( this.getDeduit() - diff );                        
              }
              
    }
    
}
