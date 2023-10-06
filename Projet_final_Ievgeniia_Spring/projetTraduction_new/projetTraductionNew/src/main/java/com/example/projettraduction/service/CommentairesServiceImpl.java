package com.example.projettraduction.service;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.repository.CommentairesRepository;
import com.example.projettraduction.repository.TextRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service CommentairesService.
 * Fournit des méthodes pour gérer les commentaires.
 */
@Service
public class CommentairesServiceImpl implements CommentairesService {

    @Autowired
    private CommentairesRepository commentairesRepository;

    @Autowired
    private TextRepository textRepository;

    /**
     * Récupère tous les commentaires.
     *
     * @return Une liste de commentaires.
     */
    @Override
    public List<Commentaires> findAll() {
        return commentairesRepository.findAll();
    }

    /**
     * Récupère un commentaire par son identifiant.
     *
     * @param id L'identifiant du commentaire.
     * @return Le commentaire correspondant à l'identifiant.
     * @throws RuntimeException si le commentaire n'existe pas.
     */
    @Override
    public Commentaires findCommentairesById(int id) {
        Optional<Commentaires> commentairesOptional = commentairesRepository.findById(id);
        Commentaires commentaires = commentairesOptional.orElseThrow(() -> new RuntimeException("Commentaire id " + id + " n'existe pas"));
        return commentaires;
    }

    /**
     * Enregistre un nouveau commentaire associé à un texte.
     *
     * @param commentaire Le commentaire à enregistrer.
     * @param textId      L'identifiant du texte associé au commentaire.
     * @return Le commentaire enregistré.
     * @throws IllegalArgumentException si le texte associé n'existe pas.
     */
    @Override
    @Transactional
    public Commentaires save(Commentaires commentaire, int textId) {
        Text texte = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Texte introuvable"));
        commentaire.setText(texte);
        return commentairesRepository.save(commentaire);
    }

    /**
     * Supprime un commentaire.
     *
     * @param commentairesId L'identifiant du commentaire à supprimer.
     * @return Vrai si le commentaire a été supprimé avec succès, sinon faux.
     */
    @Override
    public boolean deleteCommentaire(int commentairesId) {
        Optional<Commentaires> optionalCommentaires = commentairesRepository.findById(commentairesId);
        if (optionalCommentaires.isPresent()) {
            Commentaires commentaires = optionalCommentaires.get();
            Text text = commentaires.getText();
            if (text != null) {
                text.getCommentaires().remove(commentaires);
            }
            commentairesRepository.delete(commentaires);
            return true;
        }
        return false;
    }

    /**
     * Met à jour un commentaire existant.
     *
     * @param id      L'identifiant du commentaire à mettre à jour.
     * @param auteur  Le nouvel auteur du commentaire.
     * @param contenu Le nouveau contenu du commentaire.
     * @param date    La nouvelle date du commentaire.
     * @return Le commentaire mis à jour.
     * @throws IllegalArgumentException si le commentaire avec l'ID spécifié n'existe pas.
     */
    @Override
    public Commentaires updateCommentaires(int id, String auteur, String contenu, String date) {
        Optional<Commentaires> optionalCommentaires = commentairesRepository.findById(id);
        if (optionalCommentaires.isPresent()) {
            Commentaires commentaires = optionalCommentaires.get();
            commentaires.setAuteur(auteur);
            commentaires.setContenu(contenu);
            commentaires.setDate(date);
            return commentairesRepository.save(commentaires);
        }
        throw new IllegalArgumentException("Commentaire avec l'ID " + id + " introuvable");
    }

    /**
     * Filtre les commentaires par auteur.
     *
     * @param auteur Le nom de l'auteur pour filtrer les commentaires.
     * @return Une liste de commentaires correspondant à l'auteur spécifié.
     * @throws RuntimeException si aucun commentaire n'est trouvé pour l'auteur donné.
     */
    @Override
    public List<Commentaires> filtrerCommentairesParAuteur(String auteur) {
        List<Commentaires> commentaires = commentairesRepository.findAll();
        List<Commentaires> commentairesFiltres = new ArrayList<>();
        for (Commentaires commentaire : commentaires) {
            if (commentaire.getAuteur().equals(auteur)) {
                commentairesFiltres.add(commentaire);
            }
        }
        if (commentairesFiltres.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour l'auteur : " + auteur);
        }
        return commentairesFiltres;
    }

    /**
     * Filtre les commentaires par date.
     *
     * @param date La date pour filtrer les commentaires.
     * @return Une liste de commentaires correspondant à la date spécifiée.
     * @throws RuntimeException si aucun commentaire n'est trouvé pour la date donnée.
     */
    @Override
    public List<Commentaires> filtrerCommentairesParDate(String date) {
        List<Commentaires> commentaires = commentairesRepository.findAll();
        List<Commentaires> commentairesFiltres = new ArrayList<>();
        for (Commentaires commentaire : commentaires) {
            if (commentaire.getDate().equals(date)) {
                commentairesFiltres.add(commentaire);
            }
        }
        if (commentairesFiltres.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour la date : " + date);
        }
        return commentairesFiltres;
    }

    /**
     * Filtre les commentaires par contenu.
     *
     * @param motCle Le mot-clé pour filtrer les commentaires.
     * @return Une liste de commentaires contenant le mot-clé spécifié dans leur contenu.
     * @throws RuntimeException si aucun commentaire n'est trouvé pour le contenu donné.
     */
    @Override
    public List<Commentaires> filtrerCommentairesParContenu(String motCle) {
        List<Commentaires> commentaires = commentairesRepository.findAll();
        List<Commentaires> commentairesFiltres = new ArrayList<>();
        for (Commentaires commentaire : commentaires) {
            if (commentaire.getContenu().contains(motCle)) {
                commentairesFiltres.add(commentaire);
            }
        }
        if (commentairesFiltres.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour le contenu : " + motCle);
        }
        return commentairesFiltres;
    }

    /**
     * Filtre les commentaires par texte.
     *
     * @param textId L'identifiant du texte pour filtrer les commentaires.
     * @return Une liste de commentaires associés au texte spécifié.
     * @throws RuntimeException si aucun commentaire n'est trouvé pour l'ID de texte donné.
     */
    @Override
    public List<Commentaires> filtrerCommentairesParText(int textId) {
        List<Commentaires> commentaires = commentairesRepository.findAll();
        List<Commentaires> commentairesFiltres = new ArrayList<>();
        for (Commentaires commentaire : commentaires) {
            if (commentaire.getText().getId() == textId) {
                commentairesFiltres.add(commentaire);
            }
        }
        if (commentairesFiltres.isEmpty()) {
            throw new RuntimeException("Aucun commentaire trouvé pour l'ID de texte : " + textId);
        }
        return commentairesFiltres;
    }

    /**
     * Affiche le texte associé à un commentaire.
     *
     * @param commentaireId L'identifiant du commentaire.
     * @return Le texte associé au commentaire.
     * @throws IllegalArgumentException si le commentaire avec l'ID spécifié n'existe pas.
     */
    @Override
    public Text afficherTextDeCommentaire(int commentaireId) {
        Optional<Commentaires> commentaireOptional = commentairesRepository.findById(commentaireId);
        if (commentaireOptional.isPresent()) {
            Commentaires commentaire = commentaireOptional.get();
            return commentaire.getText();
        } else {
            throw new IllegalArgumentException("Le commentaire avec l'ID " + commentaireId + " n'existe pas.");
        }
    }
}