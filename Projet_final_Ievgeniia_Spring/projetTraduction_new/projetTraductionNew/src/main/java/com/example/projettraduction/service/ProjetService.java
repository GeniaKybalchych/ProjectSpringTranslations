package com.example.projettraduction.service;

import com.example.projettraduction.entities.Projet;
import jakarta.transaction.Transactional;

import java.util.List;

/**
 * Interface de service pour les opérations liées aux projets.
 */
public interface ProjetService {

    /**
     * Récupère tous les projets.
     *
     * @return Une liste contenant tous les projets.
     */
    List<Projet> findAll();

    /**
     * Récupère un projet par son identifiant.
     *
     * @param id L'identifiant du projet.
     * @return Le projet correspondant à l'identifiant spécifié.
     */
    Projet findProjetById(int id);

    /**
     * Enregistre un nouveau projet.
     *
     * @param projet Le projet à enregistrer.
     * @return Le projet enregistré.
     */
    Projet saveProjet(Projet projet);

    /**
     * Supprime un projet.
     *
     * @param projetId L'identifiant du projet à supprimer.
     * @return true si le projet a été supprimé avec succès, false sinon.
     */
    @Transactional
    boolean deleteProjet(int projetId);

    /**
     * Met à jour les informations d'un projet.
     *
     * @param id           L'identifiant du projet à mettre à jour.
     * @param nom          Le nouveau nom du projet.
     * @param description  La nouvelle description du projet.
     * @param dateCreation La nouvelle date de création du projet.
     * @return Le projet mis à jour.
     */
    Projet updateProjet(int id, String nom, String description, String dateCreation);

    /**
     * Filtre les projets par nom.
     *
     * @param nom Le nom du projet à filtrer.
     * @return Une liste de projets correspondant au nom spécifié.
     */
    List<Projet> filtrerProjetsParNom(String nom);

    /**
     * Filtre les projets par date de création.
     *
     * @param dateCreation La date de création du projet à filtrer.
     * @return Une liste de projets correspondant à la date de création spécifiée.
     */
    List<Projet> filtrerProjetsParDateCreation(String dateCreation);

    /**
     * Filtre les projets par description.
     *
     * @param motCle Le mot-clé pour filtrer les projets par description.
     * @return Une liste de projets contenant le mot-clé spécifié dans leur description.
     */
    List<Projet> filtrerProjetsParDescription(String motCle);
}
