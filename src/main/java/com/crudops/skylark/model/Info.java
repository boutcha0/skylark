package com.crudops.skylark.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name = "Informations", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Info {

    @Id
    private String id;

    private String name;

    private String adresse;

    @Column(unique = true, nullable = false)  // unique and not null
    private String email;
}
