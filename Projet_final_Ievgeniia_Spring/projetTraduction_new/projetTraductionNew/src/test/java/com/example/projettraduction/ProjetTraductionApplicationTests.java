package com.example.projettraduction;

import com.example.projettraduction.controleur.TraducteurProjetControleur;
import com.example.projettraduction.entities.Traducteur;
import com.example.projettraduction.service.TraducteurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




@WebMvcTest(TraducteurProjetControleur.class)
class ProjetTraductionApplicationTests {


	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TraducteurService traducteurService;

	@Test
	void testFindAllTraducteurs() throws Exception {
		List<Traducteur> traducteurs = new ArrayList<>();
		traducteurs.add(new Traducteur("John Doe", "doe@hotmail.com"));
		traducteurs.add(new Traducteur("Jane Smith", "smith@hotmail.com"));

		when(traducteurService.findAll()).thenReturn(traducteurs);

		mockMvc.perform(get("/traducteurs"))
				.andExpect(status().isOk());
	}
}




