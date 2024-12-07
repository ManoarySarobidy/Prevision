/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.presence;

import java.sql.Timestamp;
import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.prevision.departement.Salle;

/**
 *
 * @author sarobidy
 */

public class DetailPointage extends Pointage {
    
    @Column(isPrimaryKey = true)
    Integer idDetail;
    @Column(foreign = true)
    Salle salle;
    @Column(name = "temps")
    Timestamp time;
    @Column
    Integer effectif;

    public Integer getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(Integer idDetail) {
        this.idDetail = idDetail;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getEffectif() {
        return effectif;
    }

    public void setEffectif(Integer effectif) {
        this.effectif = effectif;
    }
    
}
