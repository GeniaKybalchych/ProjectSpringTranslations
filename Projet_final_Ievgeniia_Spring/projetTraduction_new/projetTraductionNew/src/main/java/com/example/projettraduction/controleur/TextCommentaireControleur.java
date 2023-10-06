package com.example.projettraduction.controleur;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.service.CommentairesService;
import com.example.projettraduction.service.ProjetService;
import com.example.projettraduction.service.TextService;
import com.example.projettraduction.service.TraducteurService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur pour gérer les opérations liées aux textes et commentaires.
 */
@RestController
public class TextCommentaireControleur {

    @Autowired
    private TextService textService;

    @Autowired
    private CommentairesService commentairesService;

    @Autowired
    private ProjetService projetService;

    @Autowired
    private TraducteurService traducteurService;

    /**
     * Récupère tous les textes.
     *
     * @return la liste des textes
     */
    @GetMapping("/texts")
    public List<Text> findAllTexts() {
        return textService.findAll();
    }

    /**
     * Récupère tous les commentaires.
     *
     * @return la liste des commentaires
     */
    @GetMapping("/commentaires")
    public List<Commentaires> findAllCommentaires() {
        return commentairesService.findAll();
    }

    /**
     * Récupère un texte par son identifiant.
     *
     * @param id l'identifiant du texte
     * @return le texte correspondant à l'identifiant
     */
    @GetMapping("/texts/{id}")
    public Text getTextById(@PathVariable("id") int id) {
        return textService.findTextById(id);
    }

    /**
     * Récupère un commentaire par son identifiant.
     *
     * @param id l'identifiant du commentaire
     * @return le commentaire correspondant à l'identifiant
     */
    @GetMapping("/commentaires/{id}")
    public Commentaires getCommentairesById(@PathVariable("id") int id) {
        return commentairesService.findCommentairesById(id);
    }

    /**
     * Enregistre un commentaire pour un texte donné.
     *
     * @param textId      l'identifiant du texte
     * @param commentaire le commentaire à enregistrer
     * @return la réponse HTTP avec le commentaire enregistré
     */
    @PostMapping("/textes/{textId}/commentaires")
    public ResponseEntity<Commentaires> saveCommentaire(@PathVariable("textId") int textId, @RequestBody Commentaires commentaire) {
        commentairesService.save(commentaire, textId);
        return ResponseEntity.ok(commentaire);
    }

    /**
     * Enregistre un texte pour un projet et un traducteur donnés.
     *
     * @param texte        le texte à enregistrer
     * @param projetId     l'identifiant du projet
     * @param traducteurId l'identifiant du traducteur
     * @return la réponse HTTP avec le texte enregistré
     */
    @Transactional
    @PostMapping("/textes/{projetId}/{traducteurId}")
    public ResponseEntity<Text> saveTexte(@RequestBody Text texte, @PathVariable int projetId, @PathVariable int traducteurId) {
        Text savedTexte = textService.save(texte, projetId, traducteurId);
        return ResponseEntity.ok(savedTexte);
    }

    /**
     * Supprime un commentaire par son identifiant.
     *
     * @param commentairesId l'identifiant du commentaire à supprimer
     * @return la réponse HTTP avec un message indiquant si le commentaire a été supprimé avec succès ou non
     */
    @DeleteMapping("/commentaires/{commentairesId}")
    public ResponseEntity<String> deleteCommentairesById(@PathVariable int commentairesId) {
        boolean deleted = commentairesService.deleteCommentaire(commentairesId);
        if (deleted) {
            String message = "Commentaires avec ID " + commentairesId + " supprimé.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Commentaires avec ID " + commentairesId + " n'existe pas.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * Supprime un texte par son identifiant.
     *
     * @param textId l'identifiant du texte à supprimer
     * @return la réponse HTTP avec un message indiquant si le texte a été supprimé avec succès ou non
     */
    @Transactional
    @DeleteMapping("/texts/{textId}")
    public ResponseEntity<String> deleteTextById(@PathVariable int textId) {
        boolean deleted = textService.deleteText(textId);
        if (deleted) {
            String message = "Text avec ID " + textId + " supprimé.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Text avec ID " + textId + " n'existe pas.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * Met à jour un commentaire.
     *
     * @param id           l'identifiant du commentaire à mettre à jour
     * @param commentaires les nouvelles informations du commentaire
     * @return le commentaire mis à jour
     */
    @PutMapping("/commmentaires/{id}")
    public Commentaires updateCommentaires(@PathVariable int id, @RequestBody Commentaires commentaires) {
        return commentairesService.updateCommentaires(id, commentaires.getAuteur(), commentaires.getContenu(), commentaires.getDate());
    }

    /**
     * Met à jour un texte.
     *
     * @param id   l'identifiant du texte à mettre à jour
     * @param text les nouvelles informations du texte
     * @return le texte mis à jour
     */
    @PutMapping("/texts/{id}")
    public Text updateText(@PathVariable int id, @RequestBody Text text) {
        return textService.updateText(id, text.getContenu(), text.getLangueSource(), text.getLangueCible(), text.getStatut());
    }

    /**
     * Filtre les commentaires par auteur.
     *
     * @param auteur l'auteur des commentaires à filtrer
     * @return la liste des commentaires correspondant à l'auteur
     */
    @GetMapping("/commentaires/filtrer/auteur/{auteur}")
    public List<Commentaires> filtrerCommentairesParAuteur(@PathVariable String auteur) {
        return commentairesService.filtrerCommentairesParAuteur(auteur);
    }

    /**
     * Filtre les commentaires par date.
     *
     * @param date la date des commentaires à filtrer
     * @return la liste des commentaires correspondant à la date
     */
    @GetMapping("/commentaires/filtrer/date/{date}")
    public List<Commentaires> filtrerCommentairesParDate(@PathVariable String date) {
        return commentairesService.filtrerCommentairesParDate(date);
    }

    /**
     * Filtre les commentaires par identifiant de texte.
     *
     * @param textId l'identifiant du texte des commentaires à filtrer
     * @return la liste des commentaires correspondant à l'identifiant de texte
     */
    @GetMapping("/commentaires/filtrer/text/{textId}")
    public List<Commentaires> filtrerCommentairesParText(@PathVariable int textId) {
        return commentairesService.filtrerCommentairesParText(textId);
    }

    /**
     * Filtre les commentaires par contenu.
     *
     * @param motCle le mot clé du contenu des commentaires à filtrer
     * @return la liste des commentaires correspondant au contenu
     */
    @GetMapping("/commentaires/filtrer/contenu/{motCle}")
    public List<Commentaires> filtrerCommentairesParContenu(@PathVariable String motCle) {
        return commentairesService.filtrerCommentairesParContenu(motCle);
    }

    /**
     * Filtre les textes par identifiant de traducteur.
     *
     * @param traducteurId l'identifiant du traducteur des textes à filtrer
     * @return la liste des textes correspondant à l'identifiant de traducteur
     */
    @GetMapping("/texts/filtrer/traducteur/{traducteurId}")
    public List<Text> filtrerTextParTraducteur(@PathVariable int traducteurId) {
        return textService.filtrerTextParTraducteur(traducteurId);
    }

    /**
     * Filtre les textes par identifiant de projet.
     *
     * @param projetId l'identifiant du projet des textes à filtrer
     * @return la liste des textes correspondant à l'identifiant de projet
     */
    @GetMapping("/texts/filtrer/projet/{projetId}")
    public List<Text> filtrerTextParProjet(@PathVariable int projetId) {
        return textService.filtrerTextParProjet(projetId);
    }

    /**
     * Filtre les textes par statut.
     *
     * @param statut le statut des textes à filtrer
     * @return la liste des textes correspondant au statut
     */
    @GetMapping("/texts/filtrer/statut/{statut}")
    public List<Text> filtrerTextParStatut(@PathVariable String statut) {
        return textService.filtrerTextParStatut(statut);
    }

    /**
     * Affiche le projet associé à un texte.
     *
     * @param textId l'identifiant du texte
     * @return la réponse HTTP avec le projet associé ou une réponse NOT FOUND si le projet n'existe pas
     */
    @GetMapping("/texts/projet/{textId}")
    public ResponseEntity<Projet> afficherProjetDeTexte(@PathVariable int textId) {
        Projet projet = textService.afficherProjetDeText(textId);
        if (projet != null) {
            return ResponseEntity.ok().body(projet);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Affiche le texte associé à un commentaire.
     *
     * @param commentaireId l'identifiant du commentaire
     * @return la réponse HTTP avec le texte associé ou une réponse NOT FOUND si le texte n'existe pas
     */
    @GetMapping("/commentaires/text/{commentaireId}")
    public ResponseEntity<Text> afficherTextDeCommentaire(@PathVariable int commentaireId) {
        Text text = commentairesService.afficherTextDeCommentaire(commentaireId);
        if (text != null) {
            return ResponseEntity.ok().body(text);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}