package com.example.projettraduction.repository;

import com.example.projettraduction.entities.Traducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**

 Repository pour l'entité Traducteur.
 Gère la persistance et la récupération des traducteurs dans la base de données.
 */
@Repository
public interface TraducteurRepository extends JpaRepository<Traducteur,Integer> {
}
