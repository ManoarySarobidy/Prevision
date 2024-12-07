/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.departement;

import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import mg.sarobidy.prevision.energie.Luminosite;
import mg.sarobidy.prevision.presence.Pointage;

public class Presence {
    
    Pointage[] pointages;
    Date date;
    int matin;
    int apres;
    Luminosite[] meteo;
    Secteur secteur;
    Double consomation;

    public Double getConsomation() {
        return consomation;
    }

    public void setConsomation(Double consomation) {
        this.consomation = consomation;
    }
    
    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public Pointage[] getPointages() {
        return pointages;
    }

    public void setPointages(Pointage[] pointages) {
        this.pointages = pointages;
    }
    
    public int getMatin() {
        return matin;
    }

    public void setMatin(int matin) {
        this.matin = matin;
    }

    public int getApres() {
        return apres;
    }

    public void setApres(int apres) {
        this.apres = apres;
    }
    
    public void setEffectif(){
        for( Pointage pointage : this.getPointages() ){
            if( pointage.getTime().toLocalDateTime().getHour() < 12 ){
                this.setMatin( this.getMatin() + pointage.getEffectif() );
            }else{
                this.setApres( this.getApres() + pointage.getEffectif() );
            }
        }
    }
    
    public List<Presence> testPointages( String sql , Connection connection ) throws Exception {
        Pointage[] pointagess = new Pointage().getPointages(connection, sql);
        List<Presence> presences = new ArrayList<>();
        for( int i = 0 ; i < pointagess.length ; i++ ){
            Presence presence = new Presence();
            presence.setDate(pointagess[i].getDate());
            List<Pointage> ps = new ArrayList<>();
            ps.add(pointagess[i]);
            while( i + 1 < pointagess.length &&  pointagess[ i + 1 ].getDate().compareTo(presence.getDate()) == 0 ){
                ps.add(pointagess[i+1]);
                i = i + 1;
            }
            presence.setPointages(ps.toArray(new Pointage[ 0 ] ));
            presence.setLuminosite(connection);
            presence.setEffectif();
            presences.add(presence);
//            System.out.println("i is " + i);
        }
        return presences;
    }
    
    
    public void affineResults( Connection connection ) throws Exception{
        // 1 - Affiner le resultat du secteur
//        this.getSecteur().affineResults(connection, this.getMeteo(), this.getDate().toString());
        this.getSecteur().affineResults(connection, this);
        // 2 - Sauvegarder la moyenne du secteur pour la journée
        this.setConsomation(this.getSecteur().getMoyenne());
        
        System.out.println("Secteur ::: " + this.getSecteur().getId() + " Jour : " + this.getDate() + " iteration = " + this.getConsomation());

    }

    public Luminosite[] getMeteo() {
        return meteo;
    }

    public void setMeteo(Luminosite[] meteo) {
        this.meteo = meteo;
    }
    
    public void setLuminosite( Connection connection ) throws Exception{
        Luminosite[] lms = new Luminosite().getLuminositeAt(connection, this.getDate());
        this.setMeteo(lms);
    }
    
}
