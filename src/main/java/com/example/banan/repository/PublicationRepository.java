package com.example.banan.repository;

import com.example.banan.model.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication,Long> {


    void deleteById(long id);
    Publication findById(long id);
}
