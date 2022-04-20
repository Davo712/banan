package com.example.banan.repository;

import com.example.banan.model.Song;
import com.example.banan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {

    Song findByName(String name);
}
