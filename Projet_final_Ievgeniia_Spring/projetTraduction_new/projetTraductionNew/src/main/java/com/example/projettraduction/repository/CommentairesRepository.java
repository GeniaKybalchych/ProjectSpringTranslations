package com.example.projettraduction.repository;

import com.example.projettraduction.entities.Commentaires;
import com.example.projettraduction.entities.Traducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**

 Repository pour l'entité Commentaires.
 Gère la persistance et la récupération des commentaires dans la base de données.
 */
@Repository
public interface CommentairesRepository extends JpaRepository<Commentaires,Integer> {
}
