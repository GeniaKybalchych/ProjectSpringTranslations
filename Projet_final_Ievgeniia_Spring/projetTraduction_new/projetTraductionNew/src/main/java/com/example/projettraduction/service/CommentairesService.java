package com.example.projettraduction.service;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Text;

import java.util.List;

/**
 * Service pour l'entité Commentaires.
 * Fournit des méthodes pour gérer les commentaires.
 */

public interface CommentairesService {
    /**
     * Récupère tous les commentaires.
     *
     * @return Une liste de commentaires.
     */
    List<Commentaires> findAll();

    /**
     * Récupère un commentaire par son identifiant.
     *
     * @param id L'identifiant du commentaire.
     * @return Le commentaire correspondant à l'identifiant.
     */
    Commentaires findCommentairesById(int id);

    /**
     * Enregistre un nouveau commentaire.
     *
     * @param commentaire Le commentaire à enregistrer.
     * @param textId      L'identifiant du texte associé au commentaire.
     * @return Le commentaire enregistré.
     */
    Commentaires save(Commentaires commentaire, int textId);

    /**
     * Supprime un commentaire.
     *
     * @param commentairesId L'identifiant du commentaire à supprimer.
     * @return Vrai si le commentaire a été supprimé avec succès, sinon faux.
     */
    boolean deleteCommentaire(int commentairesId);

    /**
     * Met à jour un commentaire existant.
     *
     * @param id      L'identifiant du commentaire à mettre à jour.
     * @param auteur  Le nouvel auteur du commentaire.
     * @param contenu Le nouveau contenu du commentaire.
     * @param date    La nouvelle date du commentaire.
     * @return Le commentaire mis à jour.
     */
    Commentaires updateCommentaires(int id, String auteur, String contenu, String date);

    /**
     * Filtre les commentaires par auteur.
     *
     * @param auteur Le nom de l'auteur pour filtrer les commentaires.
     * @return Une liste de commentaires correspondant à l'auteur spécifié.
     */
    List<Commentaires> filtrerCommentairesParAuteur(String auteur);

    /**
     * Filtre les commentaires par date.
     *
     * @param date La date pour filtrer les commentaires.
     * @return Une liste de commentaires correspondant à la date spécifiée.
     */
    List<Commentaires> filtrerCommentairesParDate(String date);

    /**
     * Filtre les commentaires par contenu.
     *
     * @param motCle Le mot-clé pour filtrer les commentaires.
     * @return Une liste de commentaires contenant le mot-clé spécifié dans leur contenu.
     */
    List<Commentaires> filtrerCommentairesParContenu(String motCle);

    /**
     * Filtre les commentaires par texte.
     *
     * @param textId L'identifiant du texte pour filtrer les commentaires.
     * @return Une liste de commentaires associés au texte spécifié.
     */
    List<Commentaires> filtrerCommentairesParText(int textId);

    /**
     * Affiche le texte associé à un commentaire.
     *
     * @param commentairesId L'identifiant du commentaire.
     * @return Le texte associé au commentaire.
     */
    Text afficherTextDeCommentaire(int commentairesId);
}
