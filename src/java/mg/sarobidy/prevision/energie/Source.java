/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mg.sarobidy.prevision.energie;

import mg.sarobidy.generic.annotation.Column;
import mg.sarobidy.generic.annotation.Table;

/**
 *
 * @author sarobidy
 */
@Table( name =  "sources")
public class Source {
    
    @Column( name = "idsource" )
    String id;
    @Column(name = "name")
    String nom;
    @Column
    Double puissance;
    @Column
    String reference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPuissance() {
        return puissance;
    }

    public void setPuissance(Double puissance) {
        this.puissance = puissance;
    }
    
}
