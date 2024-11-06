package com.crudops.skylark.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Informations")
public class Infos {

    @Id
    private String Id;
    private String Name;
    private String Adresse;

    public Infos() {
    }

    public Infos(String id, String name, String adresse) {
        Id = id;
        Name = name;
        Adresse = adresse;

        //        return  new Infos(
//                "1",
//                "Hamza" ,
//                "Sale");
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }


}
