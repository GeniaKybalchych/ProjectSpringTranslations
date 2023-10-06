package com.example.projettraduction.repository;

import com.example.projettraduction.entities.Projet;
import com.example.projettraduction.entities.Traducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**

 Repository pour l'entité Projet.
 Gère la persistance et la récupération des projets dans la base de données.
 */
@Repository
public interface ProjetRepository extends JpaRepository<Projet,Integer> {
}
