package com.example.projettraduction.service;

import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.repository.TraducteurRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Implémentation de l'interface {@link TraducteurService} pour les opérations liées aux traducteurs.
 */
@Transactional
@Service
public class TraducteurServiceImpl implements TraducteurService {
    @Autowired
    private TraducteurRepository traducteurRepository;

    /**
     * Récupère tous les traducteurs.
     *
     * @return la liste de tous les traducteurs
     */
    @Override
    public List<Traducteur> findAll() {
        return traducteurRepository.findAll();
    }

    /**
     * Récupère un traducteur par son ID.
     *
     * @param id l'ID du traducteur
     * @return le traducteur avec l'ID spécifié
     * @throws RuntimeException si le traducteur n'existe pas
     */
    @Override
    public Traducteur findTraducteurById(int id) {
        Optional<Traducteur> traducteurOptional = traducteurRepository.findById(id);
        Traducteur traducteur = null;
        if (traducteurOptional.isPresent()) {
            traducteur = traducteurOptional.get();
        } else {
            throw new RuntimeException("Traducteur id " + id + " n'existe pas");
        }
        return traducteur;
    }

    /**
     * Enregistre un nouveau traducteur.
     *
     * @param traducteur le traducteur à enregistrer
     * @return le traducteur enregistré
     */
    @Transactional
    @Override
    public Traducteur saveTraducteur(Traducteur traducteur) {
        return traducteurRepository.save(traducteur);

    }

    /**
     * Supprime un traducteur par son ID.
     *
     * @param traducteurId l'ID du traducteur à supprimer
     * @return true si le traducteur a été supprimé avec succès, false sinon
     */
    @Override
    public boolean deleteTraducteur(int traducteurId) {
        Traducteur traducteur = traducteurRepository.findById(traducteurId).get();
        if (traducteur != null) {
            for (Text text : traducteur.getTexts()) {
                text.setTraducteur(null);
            }
            traducteurRepository.deleteById(traducteurId);
            return true;
        }
        return false;
    }

    /**
     * Met à jour un traducteur avec un nouveau nom et une nouvelle adresse e-mail.
     *
     * @param id    l'ID du traducteur à mettre à jour
     * @param nom   le nouveau nom du traducteur
     * @param email la nouvelle adresse e-mail du traducteur
     * @return le traducteur mis à jour
     */
    public Traducteur updateTraducteur(int id, String nom, String email) {
        if (this.traducteurRepository.existsById(id)) {
            Traducteur traducteur = traducteurRepository.findById(id).get();
            traducteur.setNom(nom);
            traducteur.setEmail(email);
            traducteurRepository.save(traducteur);
            return traducteur;
        }
        return null;
    }

    /**
     * Filtre les traducteurs par nom.
     *
     * @param nom le nom à utiliser pour le filtrage
     * @return la liste des traducteurs filtrés par nom
     * @throws RuntimeException si aucun traducteur n'est trouvé avec le nom spécifié
     */
    @Override
    public List<Traducteur> filtrerTraducteursParNom(String nom) {
        List<Traducteur> traducteurs = traducteurRepository.findAll();
        List<Traducteur> traducteursFiltres = new ArrayList<>();

        for (Traducteur traducteur : traducteurs) {
            if (traducteur.getNom() != null && traducteur.getNom().equals(nom)) {
                traducteursFiltres.add(traducteur);
            }
        }

        if (traducteursFiltres.isEmpty()) {
            throw new RuntimeException("Aucun traducteur trouvé avec le nom : " + nom);
        }

        return traducteursFiltres;
    }

    /**
     * Filtre les traducteurs par adresse e-mail.
     *
     * @param email l'adresse e-mail à utiliser pour le filtrage
     * @return la liste des traducteurs filtrés par adresse e-mail
     * @throws RuntimeException si aucun traducteur n'est trouvé avec l'adresse e-mail spécifiée
     */
    @Override
    public List<Traducteur> filtrerTraducteursParEmail(String email) {
        List<Traducteur> traducteurs = traducteurRepository.findAll();
        List<Traducteur> traducteursFiltres = new ArrayList<>();

        for (Traducteur traducteur : traducteurs) {
            if (traducteur.getEmail() != null && traducteur.getEmail().equals(email)) {
                traducteursFiltres.add(traducteur);
            }
        }

        if (traducteursFiltres.isEmpty()) {
            throw new RuntimeException("Aucun traducteur trouvé avec l'e-mail : " + email);
        }

        return traducteursFiltres;
    }
}