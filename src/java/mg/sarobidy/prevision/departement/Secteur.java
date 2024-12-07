/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.departement;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;
import mg.sarobidy.generic.classes.Dao;
import mg.sarobidy.prevision.coupure.Coupure;
import mg.sarobidy.prevision.coupure.Etat;
import mg.sarobidy.prevision.energie.Batterie;
import mg.sarobidy.prevision.energie.Luminosite;
import mg.sarobidy.prevision.energie.Panneau;
import mg.sarobidy.prevision.energie.Source;

/**
 *
 * @author sarobidy
 */

@Table(name = "secteur")
public class Secteur {
    
          int DEFAULT_POWER = 60;
          
    @Column(name = "id", isPrimaryKey = true)
    String id;
    @Column
    String nom;
    @Column
    String ref;
    
    Salle[] salles;
    Source[] sources;
    Source[] temps;
    Etat coupe;
    List<Presence> presencess;
    List<Etat> etats = new ArrayList<>();
    double moyenne = 60;
    
    double min = 0;
    double max;

          public List<Presence> getPresencess() {
                    return presencess;
          }

          public void setPresencess(List<Presence> presencess) {
                    this.presencess = presencess;
          }
    
    

    public double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

          public double getMin() {
                    return min;
          }

          public void setMin(double min) {
                    this.min = min;
          }

          public double getMax() {
                    return max;
          }

          public void setMax(double max) {
                    this.max = max;
          }
    
    
    
    public String getRef() {
        return ref;
    }

    public Source[] getTemps() {
        return temps;
    }

    public void setTemps(Source[] temps) {
        this.temps = temps;
    }
    
    public void setRef(String ref) {
        this.ref = ref;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Salle[] getSalles() {
        return salles;
    }

    public void setSalles(Salle[] salles) {
        this.salles = salles;
    }

    public Etat getCoupe() {
        return coupe;
    }

    public void setCoupe(Etat coupe) {
        this.coupe = coupe;
    }

    public Source[] getSources() {
        return sources;
    }
    
    public void setSources( Connection connection ) throws Exception{
        String panneau = "select * from v_secteur_source where type = 20 and id = '%s'";
        String batterie = "select * from v_secteur_source where type = 10 and id = '%s'";
        panneau = String.format(panneau, this.getId());
        batterie = String.format(batterie, this.getId());
        Panneau panneaux = new Panneau().getPanneau( connection, panneau );
        Batterie bat = new Batterie().getBatterie( connection, batterie );
        Source[] srcs = new Source[2];
        srcs[0] = panneaux;
        srcs[1] = bat;
        this.setSources(srcs);
        this.setTemps(srcs);
    }
    
    public void setSources(Source[] sources) {
        this.sources = sources;
    }
    
    public Salle[] getSalle( Connection connection ) throws Exception{
        return new Salle().getSalleFor( connection, this );
    }
    
    public void getTotalEleveAt( Connection connection, String date ) throws Exception{
        String sql = "select * from v_pointages_secteur_salle_2 where date = '" + date + "' and id_secteur = '" + this.getId() +"'";
        System.out.println(sql);
        List<Presence> prs = new Presence().testPointages(sql, connection);
        this.setPresencess(prs);
    }
    
    
//    public void calculePresence( Luminosite[] luminosites ) throws Exception{
//        int totalPresentMatin = 0;
//        int totalPresentHariva = 0;
//        for( Salle salle : this.getSalles() ){
//            totalPresentMatin = totalPresentMatin + salle.getPresentMatin();
//            totalPresentHariva = totalPresentHariva + salle.getPresentHariva();
//        }
//        double besoin_matin  = calculerBesoin(totalPresentMatin);
//        double besoin_hariva  = calculerBesoin(totalPresentHariva);
//        
//        for( Luminosite luminosite : luminosites){
//            Etat etat = new Etat();
//            etat.setSecteur(this);
//            etat = etat.createEtatFor(besoin_matin, besoin_hariva, luminosite);
//
//            this.getEtats().add(etat);
//        }
//    }
    
    public void calculePresence( Presence presence ) throws Exception{
             this.calculePresence(presence.getMatin(), presence.getApres(), presence.getMeteo());
    }
    
    public void calculePresence( double matin, double apres, Luminosite[] luminosites ) throws Exception{
              double besoin_matin = calculerBesoin((int) Math.round(matin));
              double besoin_hariva = calculerBesoin((int)Math.round(apres));
              boolean tapaka = false;
//              System.out.println("---------1 (Calcule Presence ligne 193 ) > Puissance is " + this.getMoyenne());
              for( Luminosite luminosite : luminosites){
                    Etat etat = new Etat();
                    etat.setSecteur(this);
                    etat.setTapaka(tapaka);
                    etat.nbr = ( luminosite.getTime().toLocalDateTime().getHour() < 12 ) ? (int) matin : (int) apres;
                    etat = etat.createEtatFor(besoin_matin, besoin_hariva, luminosite);
                    tapaka = etat.isTapaka();
                    this.getEtats().add(etat);
               }
    }
    
    public void calculePresence( double[] jour, Luminosite[] luminosites ) throws Exception{
              this.calculePresence(jour[0], jour[1], luminosites);
    }
    
     // Ito no mitady ny puissance moyenne
    public void affineResults( Connection connection, Presence presence ) throws Exception{
        Etat etat = new Etat().getCoupureOf(connection, this, presence.getDate().toString());
        newDay();
        int i = 0; // Limite a 150 iterations
        this.setMax( this.getSources()[0].getPuissance() );
        this.setMin(0);
        
        
        while( this.getMax() - this.getMin() >= (double) 1e-6 ){
            double p = this.getDivision();
//            double p = 91.56;
            resetResults();
            resetSources();
            this.setMoyenne(p);
//                  System.out.println("Borne minimum is : " + this.getMin() + ", Borne maximum is : " + this.getMax() );
            calculePresence(presence);
            if(etat != null){
                etat.setSecteur(this);
                 Etat premiereCoupure = this.getFirstCoupure();
                 premiereCoupure.updateTimesWithRealTime();
                  boolean after = premiereCoupure.isAfter(etat.getTimes());
//                  boolean after = etat.isAfter(premiereCoupure.getTimes());
//                  if( etat.getTimes().compareTo( premiereCoupure.getTimes() ) == 0 ){
//                            break;
//                  }
//                    System.out.println( premiecreCoupure.getTimes() + " ;;;;;  base coupure = " + etat.getTimes()  );
                    if( premiereCoupure.getTimes().compareTo(etat.getTimes()) >= 0 ){
                              this.setMin(p);
                    }else{
                              this.setMax( p );
                    }
//                  updateMoyenne( after, p);
            }else{
                break;
            }
            i++;
        }
       
    }
    
    public void testFunction( Connection connection ) throws Exception{
        String sql  = "select * from v_pointages_secteur_salle_2 where id_secteur = '" + this.getId() + "'";
        //System.out.println(sql);
        List<Presence> presences = new Presence().testPointages(sql, connection);
        // 1 - Récuperer tous les pointages (fait)
        // 2 - Récuperer la météo à chaque pointage (fait)
        // 3 - Affiner les résulats pour chaque jour ( pas encore commencer )
        this.setPresencess(presences);
    }
    
    public void findResults( Connection connection ) throws Exception{
              for( Presence presence : this.getPresencess() ){
                        presence.setSecteur(this);
//                        System.out.println("nandalo lc eh");
                        presence.affineResults(connection);
              }
    }
    
    public void test( Connection connection ) throws Exception{
        this.testFunction(connection);
        this.findResults(connection);
        this.setEtats( new ArrayList<Etat>() );
        this.resetSources();
    }
    
    public double getPuissanceMoyen(){
              double total = 0;
              for( Presence presence : this.getPresencess() ){
                        total = total + presence.getConsomation();
              }
              return total / this.getPresencess().size();
    }
    
    public Presence[] getAllDatesFrom( String date ) throws Exception{
              int d = Coupure.extractDate(date);
              List<Presence> sameDate = new ArrayList<>();
              for( Presence presence : this.getPresencess() ){
                        int p_d = Coupure.extractDate(presence.getDate().toString());
                        if( p_d == d )
                                  sameDate.add(presence);
              }
              return sameDate.toArray( new Presence[0] );
    }
    
    public void predict( Luminosite[] luminosites, String date ) throws Exception{
             
              // Recuperer la puissance moyenne à la fin des données
              double puissance_m = this.getPuissanceMoyen();
              // Recuperer les dates au memes jours que la date donnée
              Presence[] ps = this.getAllDatesFrom(date);
              // Calculer la moyenne des eleves le matin et l'apres midi dans les dates
              double[] moyenneGenerale = this.getGeneralMean(ps);
              // Recuperer la meteo à la date voulue
              // Utiliser la meteo, la consommation moyenne, le nombre d'eleve moyenne pour avoir la coupure
             // Mi-calcule zao ny atao
             // andao ary eh
              System.out.println("Puissance :: " + puissance_m);
             this.setMoyenne(puissance_m);
             // calcule amin'izay amin'izao
             this.calculePresence( moyenneGenerale, luminosites );
             this.getFirstCoupure().updateTimesWithRealTime();
    }
    
    public double[] getGeneralMean( Presence[] ps ){
              double matin = 0;
              double hariva = 0;
              double total = 0;
              for( Presence presence : ps ){
                        matin = matin + presence.getMatin();
                        hariva = hariva + presence.getApres();
                        total = total + (presence.getMatin() + presence.getApres()) / 2;
              }
//              System.out.println(" matin is " + matin);
//              System.out.println(" hraiva is " + hariva);
              return new double[]{ matin / ps.length , hariva / ps.length };
//              return new double[]{ total / ps.length , total / ps.length };
    }

    
    void updateMoyenne( boolean after ){
        if( after ){
            this.setMoyenne( this.getMoyenne() + ( this.getMoyenne() / 2 ) );
        }else
            this.setMoyenne(this.getMoyenne() - (this.getMoyenne() / 2));
    }
    
    double getDivision(){
           return (this.getMax() + this.getMin()) / 2;
    }
    
    void updateMoyenne( boolean after, double value ){
        if( after ){
            this.setMin(value);
        }else
            this.setMax(value);
    }
 
   
    void resetResults(){
        this.setEtats(new ArrayList<Etat>());
    }
    
    void newDay(){
        this.setMoyenne(60);
    }
    
    void resetBatterie() {
        if( this.getSources()[1] != null ){
            ((Batterie)this.getSources()[1]).setDeduit(0.0);    
        }
    }
    
    void resetSources(  ) {
        this.setSources(this.getTemps());
        this.resetBatterie();
        
    }
    
    double calculerBesoin( int present  ){
        return present * this.getMoyenne();
    }
    
    public List<Etat> getEtats() {
        return etats;
    }

    public void setEtats(List<Etat> etats) {
        this.etats = etats;
    }
    
    public Secteur[] getSecteurs( Connection connection ) throws Exception{
        Secteur[] secteurs =  Dao.findAll(connection, this);
        for( Secteur s : secteurs ){
                  s.setSources(connection);
        }
        return secteurs;
    }
    
    public Etat[] getEtatAsArray(){
              return this.getEtats().toArray( (new Etat[0]) );
    }
    
    public Etat getFirstCoupure(){
        Etat[] etats = this.getEtatAsArray();
//        Etat coupure = null;
        for( Etat etat : etats ){       
            if( etat.isTapaka() ){
//                etat.updateTimesWithRealTime();
                return etat;
            }
        }
        Etat coupure = ( etats.length > 0 ) ? etats[ etats.length - 1 ] : null;
//        if( coupure != null ){
//                  coupure.updateTimesWithRealTime();
//        }
        return coupure;
    }
    
}
