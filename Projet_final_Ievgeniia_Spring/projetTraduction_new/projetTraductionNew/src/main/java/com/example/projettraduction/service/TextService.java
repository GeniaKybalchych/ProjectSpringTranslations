package com.example.projettraduction.service;

import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;

import java.util.List;

/**
 *  Interface de service pour effectuer des opérations liées aux entités Texte.
 */
public interface TextService {
    /**
     * Récupère tous les textes.
     * @return Une liste de tous les textes.
    */
    List<Text> findAll();
    /**
     * Récupère un texte par son identifiant.
     *
     * @param id L'identifiant du texte.
     * @return Le texte correspondant à l'identifiant spécifié.
     */
    Text findTextById(int id);
    /**
     * Enregistre un nouveau projet.
     *
     * @param text Le projet à enregistrer.
     * @return Le texte enregistré.
     */
    Text save(Text text, int projetId, int traducteurId);
    /**
     * Supprime un texte.
     *
     * @param textId L'identifiant du texte à supprimer.
     * @return true si le texte a été supprimé avec succès, false sinon.
     */
    boolean deleteText(int textId);
    /**
     * Met à jour les informations d'un texte.
     *
     * @param id           L'identifiant du texte à mettre à jour.
     * @param contenu          Le nouveau contenu du texte.
     * @param langueSource  La nouvelle langue source du texte.
     * @param langueCible La nouvelle langue cible  du texte.
     *  @param statut Le nouveau statut du texte
     * @return Le texte mis à jour.
     */
    Text updateText(int id, String contenu, String langueSource, String langueCible, String statut);
    /**
     * Filtre les textes par traducteur.
     * @param traducteurId L'id du traducteur associe au texte.
     * @return Une liste de textes correspondant au traducteur.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le traducteur spécifié.
     */
    List<Text> filtrerTextParTraducteur(int traducteurId);
    /**
     * Filtre les textes par traducteur.
     * @param projetId L'id du projet associe au texte.
     * @return Une liste de textes correspondant au projet.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le projet spécifié.
     */
    List<Text> filtrerTextParProjet(int projetId);
    /**
     * Filtre les textes par statut.
     * @param statut Le statut du texte.
     * @return Une liste de textes correspondant au statut.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le statut spécifié.
     */
    List<Text> filtrerTextParStatut(String statut);
    /**
     * Affiche le traducteur du texte
     * @param textId L'id du texte pour lequel on veut afficher le traducteur.
     * @return Le traducteur correspondant au texte.
     * @throws RuntimeException Si aucun traducteur n'est trouvé pour le texte spécifié.
     */
    Traducteur afficherTraducteurDeText(int textId);

    /**
     * Affiche le projet du texte
     *
     * @param textId L'id du texte pour lequel on veut afficher le projet.
     * @return Le projet correspondant au texte.
     * @throws RuntimeException Si aucun projet n'est trouvé pour le texte spécifié.
     */
    Projet afficherProjetDeText(int textId);
}