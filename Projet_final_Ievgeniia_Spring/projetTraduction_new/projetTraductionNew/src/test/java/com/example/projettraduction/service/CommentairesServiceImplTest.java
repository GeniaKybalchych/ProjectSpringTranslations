package com.example.projettraduction.service;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Text;
import com.example.projettraduction.repository.CommentairesRepository;
import com.example.projettraduction.repository.TextRepository;
import org.junit.jupiter.api.AfterEach;
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
 * Classe de test pour CommentairesServiceImpl.
 */
public class CommentairesServiceImplTest {

    @Mock
    private CommentairesRepository commentairesRepository;

    @Mock
    private TextRepository textRepository;

    @InjectMocks
    private CommentairesServiceImpl commentairesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    /**
     * Teste la méthode findAll() qui retourne une liste de commentaires.
     */
    @Test
    void testFindAllEtantDonneCommentairesReturnsListCommentaires() {
        // Arrange
        List<Commentaires> expectedCommentaires = new ArrayList<>();
        expectedCommentaires.add(new Commentaires());
        expectedCommentaires.add(new Commentaires());
        Mockito.when(commentairesRepository.findAll()).thenReturn(expectedCommentaires);

        List<Commentaires> result = commentairesService.findAll();

        assertEquals(expectedCommentaires, result);
    }

    /**
     * Teste la méthode findCommentairesById() avec un ID existant.
     */
    @Test
    void testFindCommentairesByIdEtantDonneIdExistantUnReturnCommentaires() {

        int commentairesId = 1;
        Commentaires expectedCommentaires = new Commentaires();
        Mockito.when(commentairesRepository.findById(commentairesId)).thenReturn(Optional.of(expectedCommentaires));

        Commentaires result = commentairesService.findCommentairesById(commentairesId);

        assertEquals(expectedCommentaires, result);
    }

    /**
     * Teste la méthode findCommentairesById() avec un ID inexistant.
     */
    @Test
    void testFindCommentairesByIdNonExistantUnIdThrowsException() {

        int commentairesId = 1;
        Mockito.when(commentairesRepository.findById(commentairesId)).thenReturn(Optional.empty());


        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentairesService.findCommentairesById(commentairesId));
        assertEquals("Commentaire id " + commentairesId + " n'existe pas", exception.getMessage());
    }

    /**
     * Teste la méthode save() pour ajouter un commentaire à un texte existant.
     */

    @Test
    void testCommentairesAuTextEtantDonneTextId1ExistantReturnCommenatireAjoute() {
        int textId = 1;
        Text texte = new Text();
        Commentaires commentaire = new Commentaires();

        when(textRepository.findById(textId)).thenReturn(Optional.of(texte));
        when(commentairesRepository.save(commentaire)).thenReturn(commentaire);

        Commentaires savedCommentaire = commentairesService.save(commentaire, textId);

        assertEquals(texte, commentaire.getText());
        assertEquals(commentaire, savedCommentaire);
        verify(commentairesRepository,
                times(1)).save(any(Commentaires.class));

    }

    /**
     * Teste la méthode save() lorsque le texte n'est pas trouvé.
     */
    @Test
    void testSaveEtantDonneTexteNotTrouveReturnThrowsException() {
        int textId = 1;
        Commentaires commentaire = new Commentaires();

        when(textRepository.findById(textId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentairesService.save(commentaire, textId));

        assertEquals("Texte introuvable", exception.getMessage());
        verify(commentairesRepository, never()).save(commentaire);
    }


    /**
     * Teste la méthode deleteCommentaire() avec un commentaire existant.
     */

    @Test
    void testDeleteCommentaireEtantDonneCommentairesExistReturnsTrue() {
        // Arrange
        int commentairesId = 1;
        Commentaires commentaires = new Commentaires();
        Text text = new Text();
        commentaires.setText(text);
        Optional<Commentaires> optionalCommentaires = Optional.of(commentaires);
        when(commentairesRepository.findById(commentairesId)).thenReturn(optionalCommentaires);

        List<Commentaires> commentairesList = new ArrayList<>();
        commentairesList.add(commentaires);
        text.setCommentaires(commentairesList);


        boolean result = commentairesService.deleteCommentaire(commentairesId);


        assertTrue(result);
        verify(commentairesRepository).findById(commentairesId);
        verify(commentairesRepository).delete(commentaires);
        assertTrue(text.getCommentaires().isEmpty());
    }

    /**
     * Teste la méthode deleteCommentaire() avec un commentaire inexistant.
     */
    @Test
    void testDeleteCommentaireEtantDonneCommentaireNonEXistantReturnFalse() {
        int commentairesId = 1;
        Optional<Commentaires> optionalCommentaires = Optional.empty();
        when(commentairesRepository.findById(commentairesId)).thenReturn(optionalCommentaires);

        boolean result = commentairesService.deleteCommentaire(commentairesId);

        assertFalse(result);

    }

    /**
     * Teste la méthode updateCommentaires() avec un commentaire existant.
     */
    @Test
    void testUpdateCommentairesEtantDonneCommenatireExisteReturnsUpdatedCommentaires() {

        int commentairesId = 1;
        String auteur = "John Doe";
        String contenu = "Contenu mis à jour";
        String date = "2023-05-31";

        Commentaires commentaires = new Commentaires();
        commentaires.setId(commentairesId);
        commentaires.setAuteur("Auteur initial");
        commentaires.setContenu("Contenu initial");
        commentaires.setDate("2023-05-30");

        Optional<Commentaires> optionalCommentaires = Optional.of(commentaires);
        when(commentairesRepository.findById(commentairesId)).thenReturn(optionalCommentaires);
        when(commentairesRepository.save(commentaires)).thenReturn(commentaires);


        Commentaires result = commentairesService.updateCommentaires(commentairesId, auteur, contenu, date);


        assertNotNull(result);
        assertEquals(auteur, result.getAuteur());
        assertEquals(contenu, result.getContenu());
        assertEquals(date, result.getDate());
        verify(commentairesRepository).save(commentaires);
    }

    /**
     * Teste la méthode updateCommentaires() avec un commentaire inexistant.
     */
    @Test
    void testUpdateCommentairesEtantDonneCommenatiresNonExistantReturnThrowsException() {

        int commentairesId = 1;
        String auteur = "John Doe";
        String contenu = "Contenu mis à jour";
        String date = "2023-05-31";

        Optional<Commentaires> optionalCommentaires = Optional.empty();
        when(commentairesRepository.findById(commentairesId)).thenReturn(optionalCommentaires);


        assertThrows(IllegalArgumentException.class, () -> commentairesService.updateCommentaires(commentairesId, auteur, contenu, date));
    }

    /**
     * Teste la méthode filtrerCommentairesParAuteur() avec un auteur donné.
     */
    @Test
    void testFiltrerCommentairesParAuteurEtantDonneAuteurReturnsFilteredCommentaires() {
        String auteur = "John Doe";
        Commentaires commentaire1 = new Commentaires();
        commentaire1.setAuteur(auteur);
        Commentaires commentaire2 = new Commentaires();
        commentaire2.setAuteur("Jane Smith");
        Commentaires commentaire3 = new Commentaires();
        commentaire3.setAuteur(auteur);

        List<Commentaires> commentaires = new ArrayList<>();
        commentaires.add(commentaire1);
        commentaires.add(commentaire2);
        commentaires.add(commentaire3);

        when(commentairesRepository.findAll()).thenReturn(commentaires);


        List<Commentaires> result = commentairesService.filtrerCommentairesParAuteur(auteur);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(commentaire1));
        assertTrue(result.contains(commentaire3));
    }

    /**
     * Teste la méthode filtrerCommentairesParAuteur() lorsque le commentaire n'est pas trouvé.
     */
    @Test
    void testFiltrerCommentairesParAuteurEtantDonneCommenatirePasTrouveThrowsException() {
        String auteur = "John Doe";
        List<Commentaires> commentaires = new ArrayList<>();

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        assertThrows(RuntimeException.class, () -> commentairesService.filtrerCommentairesParAuteur(auteur));
    }

    /**
     * Teste la méthode filtrerCommentairesParDate() avec une date donnée.
     */
    @Test
    void testFiltrerCommentairesParDateEtantDonneDateReturnFilteredCommentaires() {

        String date = "2023-05-31";
        Commentaires commentaire1 = new Commentaires();
        commentaire1.setDate(date);
        Commentaires commentaire2 = new Commentaires();
        commentaire2.setDate("2023-06-01");
        Commentaires commentaire3 = new Commentaires();
        commentaire3.setDate(date);

        List<Commentaires> commentaires = new ArrayList<>();
        commentaires.add(commentaire1);
        commentaires.add(commentaire2);
        commentaires.add(commentaire3);

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        List<Commentaires> result = commentairesService.filtrerCommentairesParDate(date);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(commentaire1));
        assertTrue(result.contains(commentaire3));
    }

    /**
     * Teste la méthode filtrerCommentairesParDate() lorsque le commentaire n'est pas trouvé.
     */
    @Test
    void testFiltrerCommentairesParDateEtantDonneCommenatirePasTRouveThrowsException() {
        String date = "2023-05-31";
        List<Commentaires> commentaires = new ArrayList<>();

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        assertThrows(RuntimeException.class, () -> commentairesService.filtrerCommentairesParDate(date));
    }

    /**
     * Teste la méthode filtrerCommentairesParContenu() avec un mot-clé donné.
     */
    @Test
    void testFiltrerCommentairesParContenuEtantDonneMotCleImportantReturnFilteredCommentaires() {
        // Arrange
        String motCle = "important";
        Commentaires commentaire1 = new Commentaires();
        commentaire1.setContenu("Ce commentaire est important");
        Commentaires commentaire2 = new Commentaires();
        commentaire2.setContenu("Ce commentaire n'est pas pertinent");
        Commentaires commentaire3 = new Commentaires();
        commentaire3.setContenu("Ceci est un commentaire important");

        List<Commentaires> commentaires = new ArrayList<>();
        commentaires.add(commentaire1);
        commentaires.add(commentaire2);
        commentaires.add(commentaire3);

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        List<Commentaires> result = commentairesService.filtrerCommentairesParContenu(motCle);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(commentaire1));
        assertTrue(result.contains(commentaire3));
    }

    /**
     * Teste la méthode filtrerCommentairesParContenu() avec un mot-clé donné.
     */
    @Test
    void testFiltrerCommentairesParContenuEtantDonneMotCleImportantReturnCommenatairePasTrouveThrowsException() {
        String motCle = "important";
        List<Commentaires> commentaires = new ArrayList<>();

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        assertThrows(RuntimeException.class, () -> commentairesService.filtrerCommentairesParContenu(motCle));
    }

    /**
     * Teste la méthode filtrerCommentairesParText() avec un ID de texte donné.
     */
    @Test
    void testFiltrerCommentairesParTextEtantDonneTextIdUnReturnsFilteredCommentaires() {
        // Arrange
        int textId = 1;
        Commentaires commentaire1 = new Commentaires();
        commentaire1.setText(new Text(textId));
        Commentaires commentaire2 = new Commentaires();
        commentaire2.setText(new Text(2));
        Commentaires commentaire3 = new Commentaires();
        commentaire3.setText(new Text(textId));

        List<Commentaires> commentaires = new ArrayList<>();
        commentaires.add(commentaire1);
        commentaires.add(commentaire2);
        commentaires.add(commentaire3);

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        List<Commentaires> result = commentairesService.filtrerCommentairesParText(textId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(commentaire1));
        assertTrue(result.contains(commentaire3));
    }

    /**
     * Teste la méthode filtrerCommentairesParText() lorsque le commentaire n'est pas trouvé.
     */
    @Test
    void testFiltrerCommentairesParTextEtantDonneTextIdUnReturnCommenatirePasTRouveThrowsException() {
        int textId = 1;
        List<Commentaires> commentaires = new ArrayList<>();

        when(commentairesRepository.findAll()).thenReturn(commentaires);

        assertThrows(RuntimeException.class, () -> commentairesService.filtrerCommentairesParText(textId));
    }

    /**
     * Teste la méthode afficherTextDeCommentaire() avec un ID de commentaire existant.
     */
    @Test
    void testAfficherTextDeCommentaireEtantDonneCommentaireIdReturnsText() {
        int commentaireId = 1;
        Commentaires commentaire = new Commentaires();
        Text text = new Text();
        commentaire.setText(text);

        when(commentairesRepository.findById(commentaireId)).thenReturn(Optional.of(commentaire));

        Text texte = commentairesService.afficherTextDeCommentaire(commentaireId);


        assertEquals(text, texte);
    }

    /**
     * Teste la méthode afficherTextDeCommentaire() avec un ID de commentaire inexistant.
     */
    @Test
    void testAfficherTextDeCommentaireEtantDonneCommentaireIdInexistantThrowsException() {

        int commentaireId = 1;
        when(commentairesRepository.findById(commentaireId)).thenReturn(null);


        assertThrows(RuntimeException.class, () -> commentairesService.afficherTextDeCommentaire(commentaireId));
    }


}





