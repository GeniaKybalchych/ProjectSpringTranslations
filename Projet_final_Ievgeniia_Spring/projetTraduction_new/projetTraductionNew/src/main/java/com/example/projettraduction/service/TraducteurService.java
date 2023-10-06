package com.example.projettraduction.service;

import com.example.projettraduction.entities.Traducteur;

import java.util.List;

/**
 * Interface de service pour la gestion des traducteurs.
 */
public interface TraducteurService {

    /**
     * Récupère tous les traducteurs.
     *
     * @return la liste de tous les traducteurs
     */
    List<Traducteur> findAll();

    /**
     * Récupère un traducteur par son ID.
     *
     * @param id l'ID du traducteur
     * @return le traducteur avec l'ID spécifié
     */
    Traducteur findTraducteurById(int id);

    /**
     * Enregistre un nouveau traducteur.
     *
     * @param traducteur le traducteur à enregistrer
     * @return le traducteur enregistré
     */
    Traducteur saveTraducteur(Traducteur traducteur);

    /**
     * Supprime un traducteur par son ID.
     *
     * @param traducteurId l'ID du traducteur à supprimer
     * @return true si le traducteur a été supprimé avec succès, false sinon
     */
    boolean deleteTraducteur(int traducteurId);

    /**
     * Met à jour un traducteur avec un nouveau nom et une nouvelle adresse e-mail.
     *
     * @param id    l'ID du traducteur à mettre à jour
     * @param nom   le nouveau nom du traducteur
     * @param email la nouvelle adresse e-mail du traducteur
     * @return le traducteur mis à jour
     */
    Traducteur updateTraducteur(int id, String nom, String email);

    /**
     * Filtre les traducteurs par nom.
     *
     * @param nom le nom utilisé pour le filtrage
     * @return la liste des traducteurs correspondant au nom spécifié
     */
    List<Traducteur> filtrerTraducteursParNom(String nom);

    /**
     * Filtre les traducteurs par adresse e-mail.
     *
     * @param email l'adresse e-mail utilisée pour le filtrage
     * @return la liste des traducteurs correspondant à l'adresse e-mail spécifiée
     */
    List<Traducteur> filtrerTraducteursParEmail(String email);
}