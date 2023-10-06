package com.example.projettraduction.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.Objects;
/**

 Représente un commentaire sur un texte.
 */
@Entity
@Table(name = "commentaires")
public class Commentaires {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; //(identifiant unique du commentaire)

    @Column(name = "auteur")
    private String auteur;
    @Column(name = "contenu")
    private String contenu;
    @Column(name = "date")
    private String date;

    //Si on supprime un commentaire, on supprime pas le texte
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.PERSIST, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "texte_id")
    @JsonBackReference
    private Text text;

    public Commentaires() {
    }

    public Commentaires(String auteur, String contenu, String date) {
        this.auteur = auteur;
        this.contenu = contenu;
        this.date = date;
    }

    public Commentaires(int id, String auteur, String contenu, String date, Text text) {
        this.id = id;
        this.auteur = auteur;
        this.contenu = contenu;
        this.date = date;
        this.text = text;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Commentaires that)) return false;
        return getId() == that.getId() && Objects.equals(getAuteur(), that.getAuteur()) && Objects.equals(getContenu(), that.getContenu()) && Objects.equals(getDate(), that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuteur(), getContenu(), getDate());
    }

    @Override
    public String toString() {
        return "Commentaires{" +
                "id=" + id +
                ", auteur='" + auteur + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
