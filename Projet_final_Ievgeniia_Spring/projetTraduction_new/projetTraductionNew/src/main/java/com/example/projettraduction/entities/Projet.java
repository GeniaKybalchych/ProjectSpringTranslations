package com.example.projettraduction.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un projet.
 */
@Entity
@Table(name = "projet")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Projet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", nullable = true)
    private String description;
    @Column(name = "date_creation")
    private String dateCreation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "projet", cascade = {CascadeType.ALL
    }, orphanRemoval = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private List<Text> texts;

    public Projet() {
    }

    public Projet(String nom, String description, String dateCreation) {
        this.nom = nom;
        this.description = description;
        this.dateCreation = dateCreation;
    }

    public List<Text> getTexts() {
        return texts;
    }

    public void setTexts(List<Text> texts) {
        this.texts = texts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Projet projet)) return false;
        return getId() == projet.getId() && Objects.equals(getNom(), projet.getNom()) && Objects.equals(getDescription(), projet.getDescription()) && Objects.equals(getDateCreation(), projet.getDateCreation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getDescription(), getDateCreation());
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                '}';
    }

    /**
     * Ajout d'une méthode qui permet d'ajouter le texte à la liste et de setter
     * le texte au projet
     *
     * @param tempText cours à ajouter
     */
    public void ajouterTextProjet(Text tempText) {
        if (texts == null) {
            texts = new ArrayList<Text>();
        }
        texts.add(tempText);
        //et on fait le lien avec le projet
        tempText.setProjet(this);
    }


}
