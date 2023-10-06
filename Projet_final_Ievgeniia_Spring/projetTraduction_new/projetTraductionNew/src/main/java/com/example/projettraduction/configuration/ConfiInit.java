package com.example.projettraduction.configuration;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.service.ProjetService;
import com.example.projettraduction.service.TraducteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Classe de configuration pour initialiser les données de l'application.
 */
@Component
public class ConfiInit implements CommandLineRunner {

    @Autowired
    private TraducteurService traducteurService;

    @Autowired
    private ProjetService projetService;

    /**
     * Méthode appelée lors du démarrage de l'application.
     * Elle vérifie si la base de données est vide et initialise les données si nécessaire.
     *
     * @param args les arguments de ligne de commande
     * @throws Exception si une erreur se produit lors de l'exécution
     */
    @Override
    public void run(String... args) throws Exception {
        // Initialiser les données si la base de données est vide
        if (traducteurService.findAll().isEmpty() && projetService.findAll().isEmpty()) {
            // Créer les projets avec les textes et les textes avec les commentaires
            createProjetWithTextsWithCommentaires();
        }
    }

    /**
     * Crée des projets avec des textes et des commentaires.
     * Ces données sont utilisées pour l'initialisation de l'application.
     */
    private void createProjetWithTextsWithCommentaires() {
        // Créer les projets
        Projet projet1 = new Projet("Projet de traduction de documents médicaux", "Ce projet vise à traduire des documents médicaux du français vers l'espagnol.", "1 juillet 2019");
        Projet projet2 = new Projet("Projet de localisation d'une application mobile", "Ce projet consiste à adapter une application mobile pour différentes langues et cultures.", "1 septembre 2019");
        Projet projet3 = new Projet("Projet de sous-titrage de vidéos", "Ce projet a pour objectif de sous-titrer des vidéos dans différentes langues pour une meilleure accessibilité.", "1 novembre 2019");

        // Créer les textes
        Text tempText1 = new Text("Estoy encantado de verte.", "Espagnol", "Français", "En cours de traduction");
        Text tempText2 = new Text("Welcome to our mobile application!", "Anglais", "Espagnol", "Traduit");
        Text tempText3 = new Text("Rapport d'étude clinique sur le traitement du cancer", "Français", "Espagnol", "Non révisé");

        // Créer les traducteurs
        Traducteur traducteur1 = new Traducteur("John Smith", "smith@example.com");
        Traducteur traducteur2 = new Traducteur("Emilie Leblanc", "leblanc@example.com");
        Traducteur traducteur3 = new Traducteur("Annie Picard", "picard@example.com");


        // Créer les commentaires
        Commentaires commentaires1 = new Commentaires("Jean", "Excellent travail sur cette traduction !", "1 juillet 2020");
        Commentaires commentaires2 = new Commentaires("Émilie", "J'ai une suggestion pour améliorer la traduction.", "3 mars 2020");
        Commentaires commentaires3 = new Commentaires("Michel", "Cette traduction nécessite une clarification supplémentaire.", "2 septembre 2020");

        traducteur1.assignerTextTraducteur(tempText1);
        traducteur2.assignerTextTraducteur(tempText2);
        traducteur3.assignerTextTraducteur(tempText3);

        // Ajouter les commentaires aux textes
        tempText1.add(commentaires1);
        System.out.println("Commentaire1 sauvegardé");
        tempText2.add(commentaires2);
        tempText2.add(commentaires3);
        System.out.println("Commentaires 2 et 3 sauvegardés");


        // Ajouter les textes aux projets
        projet3.ajouterTextProjet(tempText1);
        System.out.println("Text1 ajouté au projet3");

        projet2.ajouterTextProjet(tempText2);
        System.out.println("Text2 ajouté au projet2");

        projet1.ajouterTextProjet(tempText3);
        System.out.println("Text3 ajouté au projet1");

        // Sauvegarder les projets
        projetService.saveProjet(projet1);
        projetService.saveProjet(projet2);
        projetService.saveProjet(projet3);
        System.out.println("Projets sauvegardés");

        // Assigner les textes aux traducteurs
        tempText1.setTraducteur(traducteur1);
        tempText2.setTraducteur(traducteur2);
        tempText3.setTraducteur(traducteur3);

        // Sauvegarder les traducteurs
        traducteurService.saveTraducteur(traducteur1);
        traducteurService.saveTraducteur(traducteur2);
        traducteurService.saveTraducteur(traducteur3);

        System.out.println("Traducteurs sauvegardés");


    }

}