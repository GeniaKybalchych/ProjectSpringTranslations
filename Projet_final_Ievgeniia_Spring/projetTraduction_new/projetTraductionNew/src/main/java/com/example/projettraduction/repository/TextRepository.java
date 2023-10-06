package com.example.projettraduction.repository;

import com.example.projettraduction.entities.Text;
import com.example.projettraduction.entities.Traducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**

 Repository pour l'entité Text.
 Gère la persistance et la récupération des textes dans la base de données.
 */
@Repository
public interface TextRepository extends JpaRepository<Text,Integer> {
}
