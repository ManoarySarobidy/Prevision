/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.coupure;

import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.departement.Secteur;
import mg.sarobidy.prevision.energie.Batterie;
import mg.sarobidy.prevision.energie.Luminosite;
import mg.sarobidy.prevision.energie.Panneau;
import mg.sarobidy.prevision.exception.CoupureException;

/**
 *
 * @author sarobidy
 */
@Table(name = "coupures")
public class Etat {
    
    boolean tapaka;
    Luminosite luminosite;
    @Column(foreign = true)
    Secteur secteur;
    @Column(name = "temps")
    Timestamp times;
    
    public int nbr = 0;
    int realMinutes;
    
    public void updateTimesWithRealTime(){
              this.setTimes( this.getTimes().toLocalDateTime().plusMinutes(this.getRealMinute()) );
    }
    
    public void setTimes( LocalDateTime time ){
              this.setTimes( Timestamp.valueOf(time) );
    }
    
    public void setRealMinutes( int minute ){
              this.realMinutes = minute;
    }
    public int getRealMinute(){
              return this.realMinutes;
    }
    
    
    double besoin;

          public double getBesoinEleve() {
                    return besoin;
          }

          public void setBesoin(double besoin) {
                    this.besoin = besoin;
          }
    
    
    double currentBatterie;

          public double getCurrentBatterie() {
                    return currentBatterie;
          }

          public void setCurrentBatterie(double currentBatterie) {
                    this.currentBatterie = currentBatterie;
          }
    
    
    
    public boolean isTapaka() {
        return tapaka;
    }

    public void setTapaka(boolean tapaka) {
        this.tapaka = tapaka;
    }

    public Luminosite getLuminosite() {
        return luminosite;
    }

    public void setLuminosite(Luminosite luminosite) {
        this.luminosite = luminosite;
    }

    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    public Timestamp getTimes() {
        return times;
    }

    public void setTimes(Timestamp times) {
        this.times = times;
    }
    
    public Etat getCoupureAt( Connection connection, String date ) throws Exception{
        String sql = "select * from coupures where date = '%s' and secteur ='%s'";
        sql = String.format( sql, date, this.getSecteur().getId() );
        Etat[] etats = Dao.fetch(connection, this, sql);
        return ( etats.length > 0 ) ? etats[0] : null;
    }
    
    public Etat createEtatFor( double matin, double apres, Luminosite luminosite ) throws CoupureException, Exception{
        double diff = luminosite.calculateDiff(this.getSecteur(), matin, apres);
        Batterie batterie = (Batterie)this.getSecteur().getSources()[1];
        this.setLuminosite(luminosite);
        this.setTimes(this.getLuminosite().getTime());
        this.setBesoin(  (luminosite.getTimeRepresentation() < 12) ? matin : apres  );
//              System.out.println("----------2 ( Etat ligne 104 ) > Luminosite is ::: " + luminosite.getTime());
//              System.out.println("-------------3 ( Etat ligne 105 ) > Paneau is ::: " + luminosite.outputOf((Panneau)this.getSecteur().getSources()[0]));
//              System.out.println("-------------3 ( Etat ligne 106 ) > Besoin is ::: " + ((luminosite.getTime().toLocalDateTime().getHour() < 12) ? matin : apres));
//              System.out.println("-------------3 ( Etat ligne 107 ) > Batterie is ::: " + diff);
        if( diff < 0 && this.isTapaka() == false){
                diff = Math.abs(diff);
                try{
                       batterie.affineResults( this,diff );
//                    batterie.deduire(diff);
//                    this.setCurrentBatterie( batterie.getReste() );
                }catch(CoupureException exception){
                      this.setTapaka(true);
                      this.setTimes( Timestamp.valueOf(this.getTimes().toLocalDateTime().plusMinutes(exception.getMinute())) );
                }
//                
//                try{
//                    batterie.deduire(diff);
////                    this.setCurrentBatterie( batterie.getReste() );
//                }catch(CoupureException exception){
//                    try{
//                        batterie.affineResults( diff );
////                        this.setCurrentBatterie( batterie.getReste() );
//                    }catch(CoupureException coupure){
//                        this.setTapaka(true);
//                        this.setTimes( Timestamp.valueOf(this.getTimes().toLocalDateTime().plusMinutes(coupure.getMinute())) );
//                    }
//                }
          }
        else if( diff >= 0 ){
                    batterie.recharger( diff );
                    this.setTapaka(false);
          }
        
        this.setCurrentBatterie( ( batterie == null ) ? 0 : batterie.getReste() );
//        System.out.println( "===================== reste Batterie ==== > " + batterie.getReste() );
        return this;
    }
    
    public boolean isEquals( Etat etat ) throws Exception{
        if( etat == null ) return false;
        long minuteMarge = 15;
        Timestamp time = this.getTimes();
        Timestamp temp = etat.getTimes();
        Timestamp before = Timestamp.valueOf( time.toLocalDateTime().minusMinutes(minuteMarge) );
        Timestamp after = Timestamp.valueOf( time.toLocalDateTime().plusMinutes(minuteMarge) );
        if( temp.getTime() >= before.getTime() && temp.getTime() <= after.getTime()){
            return true;
        }
        return false;
    }
    
    public boolean isEquals(){
        Etat[] etats = this.getSecteur().getEtatAsArray();
        for( Etat etat : etats ){
            if( etat.isTapaka() ){
                if( etat.getTimes().compareTo(this.getTimes()) == 0 ){
                    return true;
                }
            }
        }
        return false;
    }
    
    public double getOutputPanneau(){
       // Inona no atao ato
       // ALaina le panneau
       Panneau p = (Panneau)this.getSecteur().getSources()[0];
       return this.getLuminosite().outputOf(p);
    }
    
    public Etat getCoupureOf( Connection connection, Secteur secteur, String date ) throws Exception{
        Etat etat = new Etat();
        etat.setSecteur(secteur);
        etat = etat.getCoupureAt(connection, date);
        return etat;
    }
    
    public boolean isAfter( Timestamp toCompare ){
        long t = this.getTimes().getTime();
        long e = toCompare.getTime();
        return ( t >= e );
    }
    
    public boolean isEquals( Timestamp timestamp ){
        long t = this.getTimes().getTime();
        long e = timestamp.getTime();
        // 1 s -> 1000
        // 60 s -> 60000
        long marge =60000; // 60 secondes
        return ( e >= t - marge && e <= t + marge || t - e == 0 );
    }
    
    
}
