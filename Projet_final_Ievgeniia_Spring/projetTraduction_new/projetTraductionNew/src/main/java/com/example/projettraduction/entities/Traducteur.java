package com.example.projettraduction.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Représente un traducter des textes
 */
@Entity
@Table(name = "traducteur")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Traducteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, mappedBy = "traducteur", fetch = FetchType.EAGER //enlever la suppression en cascade
    )
    @JsonIgnore
    private List<Text> texts;

    public Traducteur() {
    }

    public Traducteur(String nom, String email) {
        this.nom = nom;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Traducteur that)) return false;
        return getId() == that.getId() && Objects.equals(getNom(), that.getNom()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getEmail());
    }

    @Override
    public String toString() {
        return "Traducteur{" + "id=" + id + ", nom='" + nom + '\'' + ", email='" + email + '\'' + '}';
    }

    /**
     * Ajout d'une méthode qui permet d'ajouter le texte à la liste et de setter
     * le texte au traducteur
     *
     * @param tempText cours à ajouter
     */
    public void assignerTextTraducteur(Text tempText) {
        if (texts == null) {
            texts = new ArrayList<Text>();
        }
        texts.add(tempText);
        //et on fait le lien avec l'instructeur
        tempText.setTraducteur(this);
    }
}
