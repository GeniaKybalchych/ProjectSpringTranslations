package com.example.projettraduction.controleur;

import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.service.ProjetService;
import com.example.projettraduction.service.TextService;
import com.example.projettraduction.service.TraducteurService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TraducteurProjetControleur {
    @Autowired
    private TraducteurService traducteurService;
    @Autowired
    private ProjetService projetService;
    @Autowired
    private TextService textService;


    /**
     * Récupère la liste de tous les traducteurs.
     *
     * @return la liste de tous les traducteurs
     */
    @GetMapping("/traducteurs")
    public List<Traducteur> findAllTraducteurs() {
        return traducteurService.findAll();
    }

    /**
     * Récupère la liste de tous les projets.
     *
     * @return la liste de tous les projets
     */
    @GetMapping("/projets")
    public List<Projet> findAllProjets() {
        return projetService.findAll();
    }

    /**
     * Récupère un projet par son identifiant.
     *
     * @param id l'identifiant du projet à récupérer
     * @return le projet correspondant à l'identifiant, ou null s'il n'existe pas
     */
    @GetMapping("/projets/{id}")
    public Projet getProjetById(@PathVariable("id") int id) {
        return projetService.findProjetById(id);
    }

    /**
     * Récupère un traducteur par son identifiant.
     *
     * @param id l'identifiant du traducteur à récupérer
     * @return le traducteur correspondant à l'identifiant, ou null s'il n'existe pas
     */
    @GetMapping("/traducteurs/{id}")
    public Traducteur getTraducteurById(@PathVariable("id") int id) {
        return traducteurService.findTraducteurById(id);
    }

    /**
     * Enregistre un traducteur.
     *
     * @param traducteur le traducteur à enregistrer
     * @return la réponse HTTP avec le traducteur enregistré ou une réponse d'erreur en cas d'échec
     */
    @PostMapping("/traducteurs")
    @Transactional
    public ResponseEntity<Traducteur> saveTraducteur(@RequestBody Traducteur traducteur) {
        try {
            Traducteur savedTraducteur = traducteurService.saveTraducteur(traducteur);
            return new ResponseEntity<>(savedTraducteur, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Enregistre un projet.
     *
     * @param projet le projet à enregistrer
     * @return la réponse HTTP avec un message de succès ou d'échec
     */
    @PostMapping("/projets")
    public ResponseEntity<String> saveProjet(@RequestBody Projet projet) {
        Projet savedProjet = projetService.saveProjet(projet);
        if (savedProjet != null) {
            return ResponseEntity.ok("Projet saved successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save projet");
        }
    }

    /**
     * Supprime un projet par son identifiant.
     *
     * @param projetId l'identifiant du projet à supprimer
     * @return la réponse HTTP avec un message de succès ou d'échec
     */
    @Transactional
    @DeleteMapping("/projets/{projetId}")
    public ResponseEntity<String> deleteProjetById(@PathVariable int projetId) {
        boolean deleted = projetService.deleteProjet(projetId);
        if (deleted) {
            String message = "Projet avec ID " + projetId + " supprimé.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Projet avec ID " + projetId + " n'existe pas.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * Supprime un traducteur par son identifiant.
     *
     * @param traducteurId l'identifiant du traducteur à supprimer
     * @return la réponse HTTP avec un message de succès ou d'échec
     */
    @DeleteMapping("/traducteurs/{traducteurId}")
    public ResponseEntity<String> deleteTraducteurById(@PathVariable int traducteurId) {
        boolean deleted = traducteurService.deleteTraducteur(traducteurId);
        if (deleted) {
            String message = "Traducteur avec ID " + traducteurId + " supprimé.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Traducteur avec ID " + traducteurId + " n'existe pas.";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    /**
     * Met à jour un projet par son identifiant.
     *
     * @param id     l'identifiant du projet à mettre à jour
     * @param projet le projet avec les nouvelles données
     * @return le projet mis à jour
     */
    @PutMapping("/projets/{id}")
    public Projet updateProjet(@PathVariable int id, @RequestBody Projet projet) {
        return projetService.updateProjet(id, projet.getNom(), projet.getDescription(), projet.getDateCreation());
    }

    /**
     * Met à jour un traducteur par son identifiant.
     *
     * @param id         l'identifiant du traducteur à mettre à jour
     * @param traducteur le traducteur avec les nouvelles données
     * @return le traducteur mis à jour
     */
    @PutMapping("/traducteurs/{id}")
    public Traducteur updateTraducteur(@PathVariable int id, @RequestBody Traducteur traducteur) {
        return traducteurService.updateTraducteur(id, traducteur.getNom(), traducteur.getEmail());
    }

    /**
     * Filtre les projets par nom.
     *
     * @param nom le nom utilisé pour filtrer les projets
     * @return la liste des projets correspondant au nom
     */
    @GetMapping("/projets/filtrer/nom/{nom}")
    public List<Projet> filtrerProjetsParNom(@PathVariable String nom) {
        return projetService.filtrerProjetsParNom(nom);
    }

    /**
     * Filtre les projets par date de création.
     *
     * @param date la date utilisée pour filtrer les projets (au format "yyyy-MM-dd")
     * @return la liste des projets correspondant à la date de création
     */
    @GetMapping("/projets/filtrer/date/{date}")
    public List<Projet> filtrerProjetsParDateCreation(@PathVariable String date) {
        return projetService.filtrerProjetsParDateCreation(date);
    }

    /**
     * Filtre les projets par description.
     *
     * @param motCle le mot clé utilisé pour filtrer les projets
     * @return la liste des projets correspondant à la description
     */
    @GetMapping("/projets/filtrer/description/{motCle}")
    public List<Projet> filtrerProjetsParDescription(@PathVariable String motCle) {
        return projetService.filtrerProjetsParDescription(motCle);
    }

    /**
     * Filtre les traducteurs par nom.
     *
     * @param nom le nom utilisé pour filtrer les traducteurs
     * @return la liste des traducteurs correspondant au nom
     */
    @GetMapping("/traducteurs/filtrer/nom/{nom}")
    public List<Traducteur> filtrerTraducteursParNom(@PathVariable String nom) {
        return traducteurService.filtrerTraducteursParNom(nom);
    }

    /**
     * Filtre les traducteurs par email.
     *
     * @param email l'email utilisé pour filtrer les traducteurs
     * @return la liste des traducteurs correspondant à l'email
     */
    @GetMapping("/traducteurs/filtrer/email/{email}")
    public List<Traducteur> filtrerTraducteursParEmail(@PathVariable String email) {
        return traducteurService.filtrerTraducteursParEmail(email);
    }

    /**
     * Affiche le traducteur d'un texte donné.
     *
     * @param textId l'identifiant du texte
     * @return la réponse HTTP avec le traducteur du texte ou une réponse d'erreur si non trouvé
     */
    @GetMapping("/texts/traducteur/{textId}")
    public ResponseEntity<Traducteur> afficherTraducteurDeText(@PathVariable int textId) {
        Traducteur traducteur = textService.afficherTraducteurDeText(textId);
        if (traducteur != null) {
            return ResponseEntity.ok().body(traducteur);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}