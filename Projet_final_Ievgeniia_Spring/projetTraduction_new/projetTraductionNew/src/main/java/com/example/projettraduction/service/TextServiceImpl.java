package com.example.projettraduction.service;

import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.repository.ProjetRepository;
import com.example.projettraduction.repository.TextRepository;
import com.example.projettraduction.repository.TraducteurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link TextService} pour les opérations liées aux texts.
 */
@Service
public class TextServiceImpl implements TextService {
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private TraducteurRepository traducteurRepository;


    /**
     * Récupère tous les textes.
     *
     * @return Une liste de tous les textes.
     */
    @Override
    public List<Text> findAll() {
        return textRepository.findAll();
    }

    /**
     * Récupère un texte par son identifiant.
     *
     * @param id L'identifiant du texte.
     * @return Le texte correspondant à l'identifiant spécifié.
     */
    public Text findTextById(int id) {
        Optional<Text> textOptional = textRepository.findById(id);
        Text text = null;
        if (textOptional.isPresent()) {
            text = textOptional.get();
        } else {
            throw new RuntimeException("Text id " + id + " n'existe pas");
        }
        return text;
    }

    /**
     * Enregistre un nouveau projet.
     *
     * @param text Le projet à enregistrer.
     * @return Le texte enregistré.
     */
    @Transactional
    public Text save(Text text, int projetId, int traducteurId) {
        // Récupérer le projet existant à partir de l'ID
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new IllegalArgumentException("Projet introuvable"));

        // Récupérer le traducteur existant à partir de l'ID
        Traducteur traducteur = traducteurRepository.findById(traducteurId)
                .orElseThrow(() -> new IllegalArgumentException("Traducteur introuvable"));

        // Associer le projet et le traducteur au texte
        text.setProjet(projet);
        text.setTraducteur(traducteur);

        // Enregistrer le texte
        Text savedText = textRepository.save(text);

        return savedText;
    }

    /**
     * Supprime un texte.
     *
     * @param textId L'identifiant du texte à supprimer.
     * @return true si le texte a été supprimé avec succès, false sinon.
     */
    @Override
    @Transactional
    public boolean deleteText(int textId) {
        Optional<Text> optionalText = textRepository.findById(textId);
        if (optionalText.isPresent()) {
            Text text = optionalText.get();

            // Dissocier la référence au texte dans le projet
            Projet projet = text.getProjet();
            if (projet != null) {
                projet.getTexts().remove(text);
            }

            // Dissocier la référence au texte dans le traducteur
            Traducteur traducteur = text.getTraducteur();
            if (traducteur != null) {
                traducteur.getTexts().remove(text);
            }

            // Supprimer le texte
            textRepository.deleteById(textId);
            return true;
        }
        return false;
    }

    /**
     * Met à jour les informations d'un texte.
     *
     * @param id           L'identifiant du texte à mettre à jour.
     * @param contenu      Le nouveau contenu du texte.
     * @param langueSource La nouvelle langue source du texte.
     * @param langueCible  La nouvelle langue cible  du texte.
     * @param statut       Le nouveau statut du texte
     * @return Le texte mis à jour.
     */
    @Override
    public Text updateText(int id, String contenu, String langueSource, String langueCible, String statut) {
        // Vérifier si le texte existe
        if (textRepository.existsById(id)) {
            Text text = textRepository.findById(id).get();
            text.setContenu(contenu);
            text.setLangueSource(langueSource);
            text.setLangueCible(langueCible);
            text.setStatut(statut);
            textRepository.save(text);
            return text;
        }
        return null;
    }

    /**
     * Filtre les textes par traducteur.
     *
     * @param traducteurId L'id du traducteur associe au texte.
     * @return Une liste de textes correspondant au traducteur.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le traducteur spécifié.
     */
    @Override
    public List<Text> filtrerTextParTraducteur(int traducteurId) {
        List<Text> texts = textRepository.findAll();
        List<Text> textsFiltres = new ArrayList<>();

        for (Text text : texts) {
            if (text.getTraducteur() != null && text.getTraducteur().getId() == traducteurId) {
                textsFiltres.add(text);
            }
        }

        if (textsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun text trouvé avec le traducteur : " + traducteurId);
        }

        return textsFiltres;
    }

    /**
     * Filtre les textes par traducteur.
     *
     * @param projetId L'id du projet associe au texte.
     * @return Une liste de textes correspondant au projet.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le projet spécifié.
     */
    @Override
    public List<Text> filtrerTextParProjet(int projetId) {
        List<Text> texts = textRepository.findAll();
        List<Text> textsFiltres = new ArrayList<>();

        for (Text text : texts) {
            if (text.getProjet() != null && text.getProjet().getId() == projetId) {
                textsFiltres.add(text);
            }
        }
        if (textsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun text trouvé avec le projet : " + projetId);
        }

        return textsFiltres;
    }

    /**
     * Filtre les textes par statut.
     *
     * @param statut Le statut du texte.
     * @return Une liste de textes correspondant au statut.
     * @throws RuntimeException Si aucun texte n'est trouvé pour le statut spécifié.
     */
    @Override
    public List<Text> filtrerTextParStatut(String statut) {
        List<Text> texts = textRepository.findAll();
        List<Text> textsFiltres = new ArrayList<>();

        for (Text text : texts) {
            if (text.getStatut() != null && text.getStatut().equals(statut)) {
                textsFiltres.add(text);
            }
        }

        if (textsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun text trouvé avec le statut : " + statut);
        }

        return textsFiltres;
    }

    /**
     * Affiche le traducteur du texte
     *
     * @param textId L'id du texte pour lequel on veut afficher le traducteur.
     * @return Le traducteur correspondant au texte.
     * @throws RuntimeException Si aucun traducteur n'est trouvé pour le texte spécifié.
     */
    @Override
    public Traducteur afficherTraducteurDeText(int textId) {
        Optional<Text> textOptional = textRepository.findById(textId);
        if (textOptional.isPresent()) {
            Text text = textOptional.get();
            return text.getTraducteur();
        } else {
            throw new IllegalArgumentException("Le text avec l'ID " + textId + " n'existe pas.");
        }
    }

    /**
     * Affiche le projet du texte
     *
     * @param textId L'id du texte pour lequel on veut afficher le projet.
     * @return Le projet correspondant au texte.
     * @throws RuntimeException Si aucun projet n'est trouvé pour le texte spécifié.
     */
    @Override
    public Projet afficherProjetDeText(int textId) {
        Optional<Text> textOptional = textRepository.findById(textId);
        if (textOptional.isPresent()) {
            Text text = textOptional.get();
            return text.getProjet();
        } else {
            throw new IllegalArgumentException("Le text avec l'ID " + textId + " n'existe pas.");
        }
    }


}


