package com.example.projettraduction.service;

import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.repository.ProjetRepository;
import com.example.projettraduction.repository.TextRepository;
import com.example.projettraduction.repository.TraducteurRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de test pour la classe TextServiceImpl.
 */
class TextServiceImplTest {

    @Mock
    private TextRepository textRepository;
    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private TraducteurRepository traducteurRepository;

    @InjectMocks
    private TextServiceImpl textService;
    @InjectMocks
    private TraducteurServiceImpl traducteurService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    /**
     * Teste la méthode findAll() en vérifiant si elle retourne la liste de Text attendue.
     */
    @Test
    public void testFindAllEtantDonneTextsReturnListeTexts() {

        List<Text> expectedText = new ArrayList<>();
        expectedText.add(new Text());
        expectedText.add(new Text());
        Mockito.when(textRepository.findAll()).thenReturn(expectedText);

        // Act
        List<Text> result = textService.findAll();

        // Assert
        assertEquals(expectedText, result);
    }

    /**
     * Teste la méthode findTextById() en vérifiant si elle retourne le Text attendu pour l'ID spécifié.
     */
    @Test
    public void testFindTextByIdEtantDonneTextExistsTextIdUnReturnText() {
        int textId = 1;
        Text expectedText = new Text();
        Mockito.when(textRepository.findById(textId)).thenReturn(Optional.of(expectedText));

        Text result = textService.findTextById(textId);

        assertEquals(expectedText, result);
    }

    /**
     * Teste la méthode findTextById() en vérifiant si elle lance une exception lorsque le Text pour l'ID spécifié n'existe pas.
     */
    @Test
    public void testFindTextByIdEtantDonneTextIdUnInexistantReturnPasTRouve() {
        // Arrange
        int textId = 1;
        Mockito.when(textRepository.findById(textId)).thenReturn(Optional.empty());

        // Act and Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> textService.findTextById(textId));
        assertEquals("Text id " + textId + " n'existe pas", exception.getMessage());
    }

    /**
     * Teste la méthode save() en vérifiant si elle retourne le Text ajouté avec le projet et le traducteur associés.
     */
    @Test
    void saveEtantDonneTextReturnTextAjoute() {
        // Arrange
        Text text = new Text();
        int projetId = 1;
        int traducteurId = 1;

        Projet projet = new Projet();
        when(projetRepository.findById(projetId)).thenReturn(Optional.of(projet));

        Traducteur traducteur = new Traducteur();
        when(traducteurRepository.findById(traducteurId)).thenReturn(Optional.of(traducteur));

        when(textRepository.save(text)).thenReturn(text);

        Text savedText = textService.save(text, projetId, traducteurId);

        assertEquals(text, savedText);
        assertEquals(projet, savedText.getProjet());
        assertEquals(traducteur, savedText.getTraducteur());
        verify(projetRepository, times(1)).findById(projetId);
        verify(traducteurRepository, times(1)).findById(traducteurId);
        verify(textRepository, times(1)).save(text);
    }

    /**
     * Teste la méthode deleteText() en vérifiant si elle supprime le Text spécifié et le dissocie du projet et du traducteur associés.
     */
    @Test
    void testDeleteTextEtantDonneTextReturnTextSupprime() {
        // Arrange
        int textId = 1;
        Text text = new Text();
        Projet projet = new Projet();
        Traducteur traducteur = new Traducteur();

        projet.ajouterTextProjet(text);
        traducteur.assignerTextTraducteur(text);

        when(textRepository.findById(textId)).thenReturn(Optional.of(text));


        boolean result = textService.deleteText(textId);


        assertTrue(result);

        assertEquals(projet, text.getProjet());
        assertEquals(traducteur, text.getTraducteur());
        verify(textRepository, times(1)).deleteById(textId);
    }

    /**
     * Teste la méthode deleteText() en vérifiant si elle retourne false lorsque le Text spécifié n'est pas trouvé.
     */
    @Test
    void testDeleteTextEtantDonneTextPasTRouveReturnsFalse() {
        int textId = 1;

        when(textRepository.findById(textId)).thenReturn(Optional.empty());

        boolean result = textService.deleteText(textId);

        assertFalse(result);
        verify(textRepository, never()).deleteById(anyInt());
    }

    /**
     * Teste la méthode updateText() en vérifiant si elle retourne le Text modifié avec les nouvelles valeurs spécifiées.
     */
    @Test
    void testUpdateTextEtantDonneTextReturnTextMOdifie() {
        // Arrange
        int textId = 1;
        String contenu = "Nouveau contenu";
        String langueSource = "fr";
        String langueCible = "en";
        String statut = "En cours";

        Text text = new Text();
        text.setId(textId);

        when(textRepository.existsById(textId)).thenReturn(true);
        when(textRepository.findById(textId)).thenReturn(Optional.of(text));
        when(textRepository.save(text)).thenReturn(text);

        // Act
        Text updatedText = textService.updateText(textId, contenu, langueSource, langueCible, statut);

        // Assert
        assertNotNull(updatedText);
        assertEquals(contenu, updatedText.getContenu());
        assertEquals(langueSource, updatedText.getLangueSource());
        assertEquals(langueCible, updatedText.getLangueCible());
        assertEquals(statut, updatedText.getStatut());
        verify(textRepository, times(1)).save(text);
    }

    /**
     * Teste la méthode updateText() en vérifiant si elle retourne null lorsque le Text spécifié n'existe pas.
     */
    @Test
    void testUpdateTextEtantDonneTextInexistantReturnsNull() {

        int textId = 1;
        String contenu = "Nouveau contenu";
        String langueSource = "fr";
        String langueCible = "en";
        String statut = "En cours";

        when(textRepository.existsById(textId)).thenReturn(false);

        Text updatedText = textService.updateText(textId, contenu, langueSource, langueCible, statut);

        assertNull(updatedText);
        verify(textRepository, never()).save(any(Text.class));
    }

    @Test
/**
 * Teste la méthode filtrerTextParProjet() en vérifiant si elle retourne les textes filtrés correspondant à l'ID du projet donné.
 */
    void testFiltrerTextParProjetEtantDonneMatchingProjetIdReturnsFilteredTexts() {
        int projetId = 1;

        Projet projet = new Projet();
        projet.setId(projetId);

        Text text1 = new Text();
        text1.setId(1);
        text1.setProjet(projet);

        Text text2 = new Text();
        text2.setId(2);
        text2.setProjet(null);

        List<Text> texts = new ArrayList<>();
        texts.add(text1);
        texts.add(text2);

        when(textRepository.findAll()).thenReturn(texts);

        List<Text> filteredTexts = textService.filtrerTextParProjet(projetId);

        assertNotNull(filteredTexts);
        assertEquals(1, filteredTexts.size());
        assertEquals(text1, filteredTexts.get(0));
    }

    @Test
/**
 * Teste la méthode filtrerTextParProjet() en vérifiant si elle lance une exception lorsque les textes ne sont pas trouvés.
 */
    void testFiltrerTextParProjetEtantDonneTextsPasTrouvesThrowsException() {
        int projetId = 1;

        List<Text> texts = new ArrayList<>();

        when(textRepository.findAll()).thenReturn(texts);

        Assertions.assertThrows(RuntimeException.class, () -> {
            textService.filtrerTextParProjet(projetId);
        });
    }

    @Test
/**
 * Teste la méthode filtrerTextParStatut() en vérifiant si elle retourne les textes filtrés correspondant au statut donné.
 */
    void testFiltrerTextParStatutEtantDonneStatutTrouveReturnsFilteredTexts() {
        String statut = "En cours";

        Text text1 = new Text();
        text1.setId(1);
        text1.setStatut(statut);

        Text text2 = new Text();
        text2.setId(2);
        text2.setStatut(null);

        List<Text> texts = new ArrayList<>();
        texts.add(text1);
        texts.add(text2);

        when(textRepository.findAll()).thenReturn(texts);


        List<Text> filteredTexts = textService.filtrerTextParStatut(statut);

        assertNotNull(filteredTexts);
        assertEquals(1, filteredTexts.size());
        assertEquals(text1, filteredTexts.get(0));
    }

    @Test
/**
 * Teste la méthode filtrerTextParStatut() en vérifiant si elle lance une exception lorsque le texte n'est pas trouvé.
 */
    void testFiltrerTextParStatutEtantDonneTextPasTrouveThrowsException() {
        String statut = "Terminé";

        List<Text> texts = new ArrayList<>();

        when(textRepository.findAll()).thenReturn(texts);

        Assertions.assertThrows(RuntimeException.class, () -> {
            textService.filtrerTextParStatut(statut);
        });
    }


    /**
     * Teste la méthode filtrerTextParTraducteur() pour vérifier si elle renvoie les textes correspondant à l'ID du traducteur donné.
     */
    @Test
    void testFiltrerTextParTraducteurEtantDonneTraducteurIdMatchingReturnsFilteredTexts() {
        // Arrange
        int traducteurId = 1;

        Traducteur traducteur = new Traducteur();
        traducteur.setId(traducteurId);

        Text text1 = new Text();
        text1.setId(1);
        text1.setTraducteur(traducteur);

        Text text2 = new Text();
        text2.setId(2);
        text2.setTraducteur(null);

        List<Text> texts = new ArrayList<>();
        texts.add(text1);
        texts.add(text2);

        when(textRepository.findAll()).thenReturn(texts);

        List<Text> filteredTexts = textService.filtrerTextParTraducteur(traducteurId);

        assertNotNull(filteredTexts);
        assertEquals(1, filteredTexts.size());
        assertEquals(text1, filteredTexts.get(0));
    }

    /**
     * Teste la méthode filtrerTextParTraducteur() lorsqu'aucun texte correspondant à l'ID du traducteur n'est trouvé.
     */
    @Test
    void testFiltrerTextParTraducteurEtantDonneTexteCorresppondantPasTrouveThrowsException() {
        int traducteurId = 1;

        List<Text> texts = new ArrayList<>();

        when(textRepository.findAll()).thenReturn(texts);

        Assertions.assertThrows(RuntimeException.class, () -> {
            textService.filtrerTextParTraducteur(traducteurId);
        });
    }

    /**
     * Teste la méthode afficherTraducteurDeText() pour vérifier si elle renvoie le traducteur correspondant à l'ID du texte donné.
     */
    @Test
    void testAfficherTraducteurDeTextEtantDonneTraducteurExistePourTexteIdReturnsTraducteurTrouve() {
        int textId = 1;

        Traducteur traducteur = new Traducteur();
        traducteur.setId(1);

        Text text = new Text();
        text.setId(textId);
        text.setTraducteur(traducteur);

        when(textRepository.findById(textId)).thenReturn(Optional.of(text));

        Traducteur result = textService.afficherTraducteurDeText(textId);

        assertNotNull(result);
        assertEquals(traducteur, result);
    }

    /**
     * Teste la méthode afficherTraducteurDeText() lorsqu'aucun texte n'est trouvé avec l'ID donné.
     */
    @Test
    void testAfficherTraducteurDeTextEtantDonneTexteInExistantThrowsException() {

        int textId = 1;

        when(textRepository.findById(textId)).thenReturn(Optional.empty());


        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            textService.afficherTraducteurDeText(textId);
        });
    }

    /**
     * Teste la méthode afficherProjetDeText() pour vérifier si elle renvoie le projet correspondant à l'ID du texte donné.
     */
    @Test
    void testAfficherProjetDeTextEtantDonneTexteIdExisatntReturnsMatchingProjet() {
        // Arrange
        int textId = 1;

        Projet projet = new Projet();
        projet.setId(1);

        Text text = new Text();
        text.setId(textId);
        text.setProjet(projet);

        when(textRepository.findById(textId)).thenReturn(Optional.of(text));

        Projet result = textService.afficherProjetDeText(textId);

        assertNotNull(result);
        assertEquals(projet, result);
    }

    /**
     * Teste la méthode afficherProjetDeText() lorsqu'aucun texte n'est trouvé avec l'ID donné.
     */
    @Test
    void testAfficherProjetDeTextEtantDonneTexteInExisatntThrowsException() {
        int textId = 1;

        when(textRepository.findById(textId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            textService.afficherProjetDeText(textId);
        });
    }
}