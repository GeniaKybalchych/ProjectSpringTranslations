package com.example.projettraduction.service;

import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.repository.TraducteurRepository;
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
 * Classe de test pour la classe `TraducteurServiceImpl'.
 */
class TraducteurServiceImplTest {
    @InjectMocks
    private TraducteurServiceImpl traducteurService;

    @Mock
    private TraducteurRepository traducteurRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    /**
     * Teste la méthode `findAll()` en vérifiant si elle retourne une liste de traducteurs.
     */
    void testFindAllEtantDonneTraducteursReturnListeTraducteurs() {
        List<Traducteur> expectedTraducteur = new ArrayList<>();
        expectedTraducteur.add(new Traducteur());
        expectedTraducteur.add(new Traducteur());
        Mockito.when(traducteurRepository.findAll()).thenReturn(expectedTraducteur);
        List<Traducteur> result = traducteurService.findAll();
        assertEquals(expectedTraducteur, result);
    }

    @Test
    /**
     * Teste la méthode `findTraducteurById()` en vérifiant si elle retourne le traducteur correspondant à l'ID donné.
     */
    public void testFindTraducteurByIdEtantDonneTraducteurExistsReturnTraducteur() {
        int traducteurId = 1;
        Traducteur expectedTraducteur = new Traducteur();
        Mockito.when(traducteurRepository.findById(traducteurId)).thenReturn(Optional.of(expectedTraducteur));

        Traducteur result = traducteurService.findTraducteurById(traducteurId);

        assertEquals(expectedTraducteur, result);
    }

    @Test
    /**
     * Teste la méthode `findTraducteurById()` en vérifiant si elle lance une exception lorsque le traducteur n'existe pas.
     */
    public void testFindTraducteurByIdEtantDonneTraducteurInExistantReturnMessageException() {
        // Arrange
        int traducteurId = 1;
        Mockito.when(traducteurRepository.findById(traducteurId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> traducteurService.findTraducteurById(traducteurId));
        assertEquals("Traducteur id " + traducteurId + " n'existe pas", exception.getMessage());
    }

    @Test
    /**
     * Teste la méthode `saveTraducteur()` en vérifiant si elle retourne le traducteur enregistré.
     */
    void testSaveTraducteurEtantDonneTraducteurReturnsSavedTraducteur() {
        Traducteur traducteur = new Traducteur();
        when(traducteurRepository.save(traducteur)).thenReturn(traducteur);

        Traducteur result = traducteurService.saveTraducteur(traducteur);

        assertNotNull(result);
        assertEquals(traducteur, result);
        verify(traducteurRepository).save(traducteur);
    }

    @Test
    /**
     * Teste la méthode `deleteTraducteur()` en vérifiant si elle supprime le traducteur existant et affecte `null` aux textes associés.
     */
    void testDeleteTraducteurEntantDonneTraducteurExistsReturnDeleteTraducteurEtSetNullPourAssociatedTexts() {
        int traducteurId = 1;
        Traducteur traducteur = new Traducteur();

        Text text1 = new Text();
        Text text2 = new Text();

        traducteur.assignerTextTraducteur(text1);
        traducteur.assignerTextTraducteur(text2);

        when(traducteurRepository.findById(traducteurId)).thenReturn(Optional.of(traducteur));

        boolean result = traducteurService.deleteTraducteur(traducteurId);

        assertTrue(result);
        assertNull(text1.getTraducteur());
        assertNull(text2.getTraducteur());

        verify(traducteurRepository, times(1)).findById(traducteurId);
        verify(traducteurRepository, times(1)).deleteById(traducteurId);
    }

    @Test
    /**
     * Teste la méthode `deleteTraducteur()` en vérifiant si elle lance une exception lorsque le traducteur n'existe pas.
     */
    void testDeleteTraducteurEtantDonneTraducteurNonExistantReturnFalse() {
        int traducteurId = 1;
        Optional<Traducteur> optionalTraducteur = Optional.empty();
        when(traducteurRepository.findById(traducteurId)).thenReturn(optionalTraducteur);

        assertThrows(RuntimeException.class, () -> traducteurService.deleteTraducteur(traducteurId));
    }

    @Test
    /**
     * Teste la méthode `updateTraducteur()` en vérifiant si elle met à jour le traducteur existant.
     */
    void testUpdateTraducteurEtantDonneTraducteurExisteReturnUpdatedTraducteur() {
        int traducteurId = 1;
        String newNom = "Nouveau Nom";
        String newEmail = "nouveauemail@example.com";

        Traducteur existingTraducteur = new Traducteur("Ancien Nom", "ancienemail@example.com");

        when(traducteurRepository.existsById(traducteurId)).thenReturn(true);
        when(traducteurRepository.findById(traducteurId)).thenReturn(Optional.of(existingTraducteur));
        when(traducteurRepository.save(existingTraducteur)).thenReturn(existingTraducteur);

        Traducteur updatedTraducteur = traducteurService.updateTraducteur(traducteurId, newNom, newEmail);

        assertNotNull(updatedTraducteur);
        assertEquals(newNom, updatedTraducteur.getNom());
        assertEquals(newEmail, updatedTraducteur.getEmail());

        verify(traducteurRepository, times(1)).existsById(traducteurId);
        verify(traducteurRepository, times(1)).findById(traducteurId);
        verify(traducteurRepository, times(1)).save(existingTraducteur);
    }

    @Test
    /**
     * Teste la méthode `updateTraducteur()` en vérifiant si elle retourne `null` lorsque le traducteur n'existe pas.
     */
    void testUpdateTraducteurEtantDonneTraducteurNonExistantReturnNull() {
        int traducteurId = 1;
        String newNom = "Nouveau Nom";
        String newEmail = "nouveauemail@example.com";

        when(traducteurRepository.existsById(traducteurId)).thenReturn(false);

        Traducteur updatedTraducteur = traducteurService.updateTraducteur(traducteurId, newNom, newEmail);

        assertNull(updatedTraducteur);

        verify(traducteurRepository, times(1)).existsById(traducteurId);
        verify(traducteurRepository, never()).findById(traducteurId);
        verify(traducteurRepository, never()).save(any(Traducteur.class));
    }

    @Test
    /**
     * Teste la méthode `filtrerTraducteursParNom()` en vérifiant si elle renvoie une liste de traducteurs filtrés par nom.
     */
    void testFiltrerTraducteursParNomEtantDonneTraducteursExistantsReturnFilteredTraducteurs() {
        String nom = "Luise Coderre";

        Traducteur traducteur1 = new Traducteur(nom, "corederre.luise@example.com");
        Traducteur traducteur2 = new Traducteur(nom, "jdoe@example.com");

        List<Traducteur> traducteurs = new ArrayList<>();
        traducteurs.add(traducteur1);
        traducteurs.add(traducteur2);

        when(traducteurRepository.findAll()).thenReturn(traducteurs);

        // Act
        List<Traducteur> result = traducteurService.filtrerTraducteursParNom(nom);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(traducteur1));
        assertTrue(result.contains(traducteur2));

        verify(traducteurRepository, times(1)).findAll();
    }

    @Test
    /**
     * Teste la méthode `filtrerTraducteursParNom()` en vérifiant si elle lance une exception lorsque aucun traducteur n'est trouvé.
     */
    void testFiltrerTraducteursParNomEtantDonneTraducteurPasTrouveReturnThrowRuntimeException() {
        String nom = "John Doe";

        List<Traducteur> traducteurs = new ArrayList<>();

        when(traducteurRepository.findAll()).thenReturn(traducteurs);

        assertThrows(RuntimeException.class, () -> traducteurService.filtrerTraducteursParNom(nom));

        verify(traducteurRepository, times(1)).findAll();
    }

    @Test
    /**
     * Teste la méthode `filtrerTraducteursParEmail()` en vérifiant si elle renvoie une liste de traducteurs filtrés par email.
     */
    void testFiltrerTraducteursParEmailDEtantDonneTraducteursExistentReturnTraducteursTrouves() {
        // Arrange
        String email = "coderre.louise@example.com";

        Traducteur traducteur1 = new Traducteur("Louise Coderre", email);
        Traducteur traducteur2 = new Traducteur("Jane Smith", email);

        List<Traducteur> traducteurs = new ArrayList<>();
        traducteurs.add(traducteur1);
        traducteurs.add(traducteur2);

        when(traducteurRepository.findAll()).thenReturn(traducteurs);

        List<Traducteur> result = traducteurService.filtrerTraducteursParEmail(email);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(traducteur1));
        assertTrue(result.contains(traducteur2));

        verify(traducteurRepository, times(1)).findAll();
    }

    @Test
    /**
     * Teste la méthode `filtrerTraducteursParEmail()` en vérifiant si elle lance une exception lorsque aucun traducteur n'est trouvé.
     */
    void testFiltrerTraducteursParEmailEtantDonneTraducteuNonTrouveReturnThrowRuntimeException() {

        String email = "luise.coderre@example.com";

        List<Traducteur> traducteurs = new ArrayList<>();

        when(traducteurRepository.findAll()).thenReturn(traducteurs);

        assertThrows(RuntimeException.class, () -> traducteurService.filtrerTraducteursParEmail(email));

        verify(traducteurRepository, times(1)).findAll();
    }
}