package com.example.projettraduction.service;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.repository.ProjetRepository;
import com.example.projettraduction.repository.TextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**

 Implémentation de l'interface {@link ProjetService} pour les opérations liées aux projets.
 */
@Service
@Transactional
public class ProjetServiceImpl implements ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private TextRepository textRepository;

    @Override
    public List<Projet> findAll() {
        return projetRepository.findAll();
    }

    /**

     Récupère un projet par son identifiant.
     @param id L'identifiant du projet.
     @return Le projet correspondant à l'identifiant spécifié.
     @throws RuntimeException Si le projet n'existe pas.
     */
    public Projet findProjetById(int id) {
        Optional<Projet> projetOptional = projetRepository.findById(id);
        Projet projet = null;
        if (projetOptional.isPresent()) {
            projet = projetOptional.get();
        } else {
            throw new RuntimeException("Projet id " + id + " n'existe pas");
        }
        return projet;
    }
    /**
     * Enregistre un nouveau projet.
     *
     * @param projet Le projet à enregistrer.
     * @return Le projet enregistré.
     */
    @Override
    public Projet saveProjet(Projet projet) {
        return projetRepository.save(projet);
    }

    /**

     Supprime un projet et tous les textes associés.

     @param projetId L'identifiant du projet à supprimer.

     @return true si le projet a été supprimé avec succès, false sinon.

     @throws RuntimeException Si le projet n'existe pas.
     */
    @Override
    public boolean deleteProjet(int projetId) {
        Optional<Projet> optionalProjet = projetRepository.findById(projetId);

        if (optionalProjet.isPresent()) {
            Projet projet = optionalProjet.get();

            // Supprimer les commentaires liés aux textes du projet
            for (Text text : projet.getTexts()) {
                for (Commentaires commentaires : text.getCommentaires()) {
                    commentaires.setText(null);
                }

                if (text.getTraducteur() != null) {
                    text.getTraducteur().getTexts().remove(text);
                    text.setTraducteur(null);
                }
                text.setProjet(null);
            }

            // Supprimer les textes du projet
            projet.getTexts().clear();
            projetRepository.deleteById(projetId);

            return true;
        } else {
            throw new RuntimeException("Projet avec l'ID " + projetId + " n'existe pas");
        }
    }

    /**

     Met à jour les informations d'un projet.
     @param id L'identifiant du projet à mettre à jour.
     @param nom Le nouveau nom du projet.
     @param description La nouvelle description du projet.
     @param dateCreation La nouvelle date de création du projet.
     @return Le projet mis à jour.
     */
    @Override
    public Projet updateProjet(int id, String nom, String description, String dateCreation) {
        if (projetRepository.existsById(id)) {
            Projet projet = projetRepository.findById(id).get();
            projet.setNom(nom);
            projet.setDescription(description);
            projet.setDateCreation(dateCreation);
            return projetRepository.save(projet);
        }
        return null;
    }
    /**

     Filtre les projets par nom.

     @param nom Le nom du projet à filtrer.

     @return Une liste de projets correspondant au nom spécifié.

     @throws RuntimeException Si aucun projet n'est trouvé pour le nom spécifié.
     */
    @Override
    public List<Projet> filtrerProjetsParNom(String nom) {
        List<Projet> projets = projetRepository.findAll();
        List<Projet> projetsFiltres = new ArrayList<>();

        for (Projet projet : projets) {
            if (projet.getNom().equals(nom)) {
                projetsFiltres.add(projet);
            }
        }

        if (projetsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun projet trouvé pour le nom : " + nom);
        }

        return projetsFiltres;
    }

    /**

     Filtre les projets par date de création.

     @param dateCreation La date de création du projet à filtrer.

     @return Une liste de projets correspondant à la date de création spécifiée.

     @throws RuntimeException Si aucun projet n'est trouvé pour la date de création spécifiée.
     */
    @Override
    public List<Projet> filtrerProjetsParDateCreation(String dateCreation) {
        List<Projet> projets = projetRepository.findAll();
        List<Projet> projetsFiltres = new ArrayList<>();

        for (Projet projet : projets) {
            if (projet.getDateCreation().equals(dateCreation)) {
                projetsFiltres.add(projet);
            }
        }

        if (projetsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun projet trouvé pour la date de création : " + dateCreation);
        }

        return projetsFiltres;
    }

    /**

     Filtre les projets par description.

     @param motCle Le mot-clé pour filtrer les projets par description.

     @return Une liste de projets contenant le mot-clé spécifié dans leur description.

     @throws RuntimeException Si aucun projet n'est trouvé pour la description spécifiée.
     */
    @Override
    public List<Projet> filtrerProjetsParDescription(String motCle) {
        List<Projet> projets = projetRepository.findAll();
        List<Projet> projetsFiltres = new ArrayList<>();

        for (Projet projet : projets) {
            if (projet.getDescription().contains(motCle)) {
                projetsFiltres.add(projet);
            }
        }

        if (projetsFiltres.isEmpty()) {
            throw new RuntimeException("Aucun projet trouvé pour la description : " + motCle);
        }

        return projetsFiltres;
    }
}