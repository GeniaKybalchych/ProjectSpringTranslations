package com.example.projettraduction.service;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.repository.ProjetRepository;
import com.example.projettraduction.repository.TextRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour ProjetServiceImpl.
 */
class ProjetServiceImplTest {
    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private TextRepository textRepository;

    @InjectMocks
    private ProjetServiceImpl projetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Teste la méthode findAll() lorsqu'une liste de projets est retournée.
     */
    @Test
    void testFindAllEtantDonneLIsteProjetsReturnsAllProjets() {

        List<Projet> allProjets = new ArrayList<>();
        allProjets.add(new Projet());
        allProjets.add(new Projet());
        when(projetRepository.findAll()).thenReturn(allProjets);


        List<Projet> result = projetService.findAll();


        assertEquals(allProjets.size(), result.size());
        verify(projetRepository).findAll();
    }

    /**
     * Teste la méthode findProjetById() lorsqu'un projet existant est retourné.
     */
    @Test
    void testFindProjetByIdEtantDonneProjetExistantReturnsProjet() {
        int id = 1;
        Projet projet = new Projet();
        projet.setId(id);
        Optional<Projet> projetOptional = Optional.of(projet);
        when(projetRepository.findById(id)).thenReturn(projetOptional);

        Projet result = projetService.findProjetById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(projetRepository).findById(id);
    }

    /**
     * Teste la méthode findProjetById() lorsqu'aucun projet n'est trouvé.
     */
    @Test
    void testFindProjetByIdEtantDonneProjetInExisatntThrowsException() {
        int id = 1;
        Optional<Projet> projetOptional = Optional.empty();
        when(projetRepository.findById(id)).thenReturn(projetOptional);

        assertThrows(RuntimeException.class, () -> projetService.findProjetById(id));
        verify(projetRepository).findById(id);
    }

    /**
     * Teste la méthode saveProjet() lorsqu'un projet est sauvegardé avec succès.
     */
    @Test
    void testSaveProjetEtantDonneProjetReturnsSavedProjet() {
        Projet projet = new Projet();
        when(projetRepository.save(projet)).thenReturn(projet);

        Projet result = projetService.saveProjet(projet);

        assertNotNull(result);
        assertEquals(projet, result);
        verify(projetRepository).save(projet);
    }

    /**
     * Teste la méthode deleteProjet() lorsqu'un projet existant est supprimé avec succès.
     */
    @Test
    void testDeleteProjetEtantDonneExistantIdReturnDeletedProjet() {
        // Arrange
        int projetId = 1;


        Projet projet = new Projet();
        Text text1 = new Text();
        Text text2 = new Text();
        Commentaires commentaires1 = new Commentaires();
        Commentaires commentaires2 = new Commentaires();
        Traducteur traducteur = new Traducteur();

        text1.setCommentaires(new ArrayList<>());
        text2.setCommentaires(new ArrayList<>());
        text1.getCommentaires().add(commentaires1);
        text2.getCommentaires().add(commentaires2);

        traducteur.setTexts(new ArrayList<>());
        traducteur.getTexts().add(text1);
        traducteur.getTexts().add(text2);

        text1.setTraducteur(traducteur);
        text2.setTraducteur(traducteur);
        text1.setProjet(projet);
        text2.setProjet(projet);

        projet.setTexts(new ArrayList<>());
        projet.getTexts().add(text1);
        projet.getTexts().add(text2);

        Optional<Projet> optionalProjet = Optional.of(projet);
        when(projetRepository.findById(projetId)).thenReturn(optionalProjet);

        // Act
        boolean result = projetService.deleteProjet(projetId);

        // Assert
        assertTrue(result);
        assertNull(text1.getTraducteur());
        assertNull(text2.getTraducteur());
        assertNull(text1.getProjet());
        assertNull(text2.getProjet());
        assertNull(commentaires1.getText());
        assertNull(commentaires2.getText());
        assertTrue(traducteur.getTexts().isEmpty());

        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(projetRepository, times(1)).findById(idCaptor.capture());
        verify(projetRepository, times(1)).deleteById(projetId);

        assertEquals(projetId, idCaptor.getValue());
    }

    /**
     * Teste la méthode deleteProjet() lorsqu'aucun projet n'est trouvé avec l'ID donné.
     */
    @Test
    void testDeleteProjetEtantDonneNonExistingProjetReturnProjetNonTrouve() {

        int projetId = 1;

        when(projetRepository.findById(projetId)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> projetService.deleteProjet(projetId));
        verify(projetRepository, times(1)).findById(projetId);
        verify(projetRepository, never()).deleteById(projetId);
    }

    /**
     * Teste la méthode updateProjet() lorsqu'un projet existant est modifié avec succès.
     */
    @Test
    void testUpdateProjetEtantDonneProjetExistantReturnProjetModifie() {
        int projetId = 1;
        String newNom = "Nouveau nom";
        String newDescription = "Nouvelle description";
        String newDateCreation = "Nouvelle date de création";
        Projet existingProjet = new Projet();
        existingProjet.setId(projetId);
        existingProjet.setNom("Ancien nom");
        existingProjet.setDescription("Ancienne description");
        existingProjet.setDateCreation("Ancienne date de création");
        Optional<Projet> optionalProjet = Optional.of(existingProjet);
        when(projetRepository.existsById(projetId)).thenReturn(true);
        when(projetRepository.findById(projetId)).thenReturn(optionalProjet);
        when(projetRepository.save(existingProjet)).thenReturn(existingProjet);

        Projet updatedProjet = projetService.updateProjet(projetId, newNom, newDescription, newDateCreation);

        assertNotNull(updatedProjet);
        assertEquals(newNom, updatedProjet.getNom());
        assertEquals(newDescription, updatedProjet.getDescription());
        assertEquals(newDateCreation, updatedProjet.getDateCreation());
        verify(projetRepository).save(existingProjet);
    }

    /**
     * Teste la méthode updateProjet() lorsqu'aucun projet n'est trouvé avec l'ID donné.
     */
    @Test
    void testUpdateProjetEtantDonneProjetInexistantReturnsNull() {
        int projetId = 1;
        when(projetRepository.existsById(projetId)).thenReturn(false);

        Projet updatedProjet = projetService.updateProjet(projetId, "Nouveau nom", "Nouvelle description", "Nouvelle date de création");

        assertNull(updatedProjet);
        verify(projetRepository, never()).save(any(Projet.class));
    }

    /**
     * Teste la méthode filtrerProjetsParNom() lorsqu'un projet avec le nom donné existe.
     */
    @Test
    void testFiltrerProjetsParNomEtantDonneNomExistantReturnsFilteredProjets() {
        // Arrange
        String nom = "Projet 1";
        Projet projet1 = new Projet();
        projet1.setNom("Projet 2");
        Projet projet2 = new Projet();
        projet2.setNom(nom);
        List<Projet> allProjets = new ArrayList<>();
        allProjets.add(projet1);
        allProjets.add(projet2);
        when(projetRepository.findAll()).thenReturn(allProjets);


        List<Projet> result = projetService.filtrerProjetsParNom(nom);


        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(projet2, result.get(0));
        verify(projetRepository).findAll();
    }

    /**
     * Teste la méthode filtrerProjetsParNom() lorsqu'aucun projet n'est trouvé avec le nom donné.
     */
    @Test
    void testFiltrerProjetsParNomEtantDonneNomInexistantThrowsException() {

        String nom = "Projet 1";
        Projet projet = new Projet();
        projet.setNom("Projet 2");
        List<Projet> allProjets = new ArrayList<>();
        allProjets.add(projet);
        when(projetRepository.findAll()).thenReturn(allProjets);


        assertThrows(RuntimeException.class, () -> projetService.filtrerProjetsParNom(nom));
        verify(projetRepository).findAll();
    }

    /**
     * Teste la méthode filtrerProjetsParDateCreation() lorsqu'un projet avec la date de création donnée existe.
     */
    @Test
    void testFiltrerProjetsParDateCreationEtantDonneDateExuisteReturnsFilteredProjets() {

        String dateCreation = "2023-01-01";
        Projet projet1 = new Projet();
        projet1.setDateCreation("2022-01-01");
        Projet projet2 = new Projet();
        projet2.setDateCreation(dateCreation);
        List<Projet> allProjets = new ArrayList<>();
        allProjets.add(projet1);
        allProjets.add(projet2);
        when(projetRepository.findAll()).thenReturn(allProjets);


        List<Projet> result = projetService.filtrerProjetsParDateCreation(dateCreation);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(projet2, result.get(0));
        verify(projetRepository).findAll();
    }

    /**
     * Teste la méthode filtrerProjetsParDateCreation() lorsqu'aucun projet n'est trouvé avec la date de création donnée.
     */
    @Test
    void testFiltrerProjetsParDateCreationEtantDonneDateInexistantThrowsException() {

        String dateCreation = "2023-01-01";
        Projet projet = new Projet();
        projet.setDateCreation("2022-01-01");
        List<Projet> allProjets = new ArrayList<>();
        allProjets.add(projet);
        when(projetRepository.findAll()).thenReturn(allProjets);


        assertThrows(RuntimeException.class, () -> projetService.filtrerProjetsParDateCreation(dateCreation));
        verify(projetRepository).findAll();
    }

    /**
     * Teste la méthode filtrerProjetsParDescription lorsque des projets existent correspondant au mot-clé donné.
     */
    @Test
    void testFiltrerProjetsParDescriptionEtantDonneProjetsExistentReturnProjetsFiltres() {
        // Arrange
        String motCle = "important";
        Projet projet1 = new Projet("Projet 1", "Description importante","Date1");
        Projet projet2 = new Projet("Projet 2", "Description non pertinente","Date2");
        Projet projet3 = new Projet("Projet 3", "Description importante","Date3");

        List<Projet> projets = new ArrayList<>();
        projets.add(projet1);
        projets.add(projet2);
        projets.add(projet3);

        when(projetRepository.findAll()).thenReturn(projets);

        List<Projet> result = projetService.filtrerProjetsParDescription(motCle);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(projet1));
        assertTrue(result.contains(projet3));

        verify(projetRepository, times(1)).findAll();
    }

    /**
     * Teste la méthode filtrerProjetsParDescription lorsque aucun projet n'est trouvé pour le mot-clé donné.
     * Doit lever une exception de type RuntimeException.
     */
    @Test
    void testFiltrerProjetsParDescriptionEtantDonneAucunProjetTrouveReturnThrowRuntimeException() {
        String motCle = "important";
        List<Projet> projets = new ArrayList<>();

        when(projetRepository.findAll()).thenReturn(projets);

        assertThrows(RuntimeException.class, () -> projetService.filtrerProjetsParDescription(motCle));

        verify(projetRepository, times(1)).findAll();
    }
}