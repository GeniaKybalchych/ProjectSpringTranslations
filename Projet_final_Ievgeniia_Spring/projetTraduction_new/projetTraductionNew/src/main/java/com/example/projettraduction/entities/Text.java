package com.example.projettraduction.entities;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Représente un text du projet.
 */
@Entity
@Table(name = "texte")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//si on veut avoir la liste des étudiants
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "contenu")
    private String contenu;
    @Column(name = "langue_source")
    private String langueSource;
    @Column(name = "langue_cible")
    private String langueCible;
    @Column(name = "statut")
    private String statut; // (statut du texte : en cours de traduction, traduit, révisé, etc.)
    @ManyToOne(optional = true, cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "traducteur_id")
    @JsonIgnore

    private Traducteur traducteur;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "text", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Commentaires> commentaires;

    //Si on supprime un texte, on supprime pas le projet
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH}, optional = true)
    @JoinColumn(name = "projet_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnore
    private Projet projet;

    public Text() {
    }

    public Text(int id) {
        this.id = id;
    }

    public Text(String contenu, String langueSource, String langueCible, String statut) {
        this.contenu = contenu;
        this.langueSource = langueSource;
        this.langueCible = langueCible;
        this.statut = statut;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<Commentaires> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaires> commentaires) {
        this.commentaires = commentaires;
    }

    public Traducteur getTraducteur() {
        return traducteur;
    }

    public void setTraducteur(Traducteur traducteur) {
        this.traducteur = traducteur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getLangueSource() {
        return langueSource;
    }

    public void setLangueSource(String langueSource) {
        this.langueSource = langueSource;
    }

    public String getLangueCible() {
        return langueCible;
    }

    public void setLangueCible(String langueCible) {
        this.langueCible = langueCible;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Text text)) return false;
        return getId() == text.getId() && Objects.equals(getContenu(), text.getContenu()) && Objects.equals(getLangueSource(), text.getLangueSource()) && Objects.equals(getLangueCible(), text.getLangueCible()) && Objects.equals(getStatut(), text.getStatut());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContenu(), getLangueSource(), getLangueCible(), getStatut());
    }

    @Override
    public String toString() {
        return "Text{" + "id=" + id + ", contenu='" + contenu + '\'' + ", langueSource='" + langueSource + '\'' + ", langueCible='" + langueCible + '\'' + ", statut='" + statut + '\'' + '}';
    }

    /**
     * Ajout d'une méthode qui permet d'ajouter le commentaire à la liste et de setter
     * le commentaire au texte
     *
     * @param tempCommentaires cours à ajouter
     */
    public void add(Commentaires tempCommentaires) {
        if (commentaires == null) {
            commentaires = new ArrayList<Commentaires>();
        }
        commentaires.add(tempCommentaires);
        //et on fait le lien avec le texte
        tempCommentaires.setText(this);
    }
}
