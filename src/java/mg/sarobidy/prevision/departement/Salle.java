/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.departement;

import java.sql.Connection;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.presence.Pointage;

@Table(name="salle")
public class Salle {
    
    @Column(isPrimaryKey = true )
    String id;
    @Column
    String nom;
    Secteur secteur;
    Pointage[] presences;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Secteur getSecteur() {
        return secteur;
    }

    public void setSecteur(Secteur secteur) {
        this.secteur = secteur;
    }
     
    public void getPresenceAt( Connection connection, String date )  throws Exception{
       Pointage[] pointages = new Pointage[2];
       pointages[0] = this.getPointageMatin( connection, date );
       pointages[1] = this.getPointageHariva(connection, date );
        this.setPresences(pointages);
    }
    

    public Pointage getPointageMatin( Connection connection, String date ) throws Exception{
        String matin = "select * from v_pointage_matin where date = '%s' and salle = '%s'";
        matin = String.format(matin, date, this.getId());
        Pointage[] pointages = new Pointage().getPointages(connection, matin);
        return (pointages.length == 0) ? null : pointages[0];
       // Okey eto no mila mandinika
    }
    
    public Pointage getPointageHariva(Connection connection, String date) throws Exception{
       String apres = "select * from v_pointage_apres where date = '%s' and salle = '%s'";
       apres = String.format(apres, date, this.getId());
       Pointage[] pointages = new Pointage().getPointages(connection, apres);
      return (pointages.length == 0) ? null : pointages[0];   
    }
    
    public Pointage[] getPresences() {
        return presences;
    }

    public void setPresences(Pointage[] presences) {
        this.presences = presences;
    }
    
    public int getNombrePresent(){
    
        int m = this.getPresentMatin();
        int apm = this.getPresentHariva();
        
        int diff = apm - m;
        
        return m + diff;
    }
    
    public int getPresentMatin(){
//        return ( this.getPresences()[0] == null ) ? 0 :this.getPresences()[0].getEffectif();
        return ( this.getPresences()[0] == null ) ? 0 : 200;
    }
    
    public int getPresentHariva(){
//        return (this.getPresences()[1] == null ) ? 0 : this.getPresences()[1].getEffectif();
        return (this.getPresences()[1] == null ) ? 0 : 100;
    }
    
    public String getId(){
        return this.id;
    }
    public void setId(String id){
        this.id = id;
    }
    
    public Salle[] getSalleFor( Connection connection, Secteur secteur ) throws Exception{
        String sql = "select id_salle as id, nom_salle as nom, ref_salle as ref from salle_secteur where id_secteur = '%s'";
        sql = String.format(sql , secteur.getId());
        //System.out.println("Salle query = " + sql);
        Salle[] ss = Dao.fetch(connection, this, sql);
        return ss;
    }
        
}
