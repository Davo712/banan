package com.example.banan.repository;

import com.example.banan.model.Image;
import com.example.banan.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

    Image findByName(String name);
}
