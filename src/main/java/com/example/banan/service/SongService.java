package com.example.banan.service;

import com.example.banan.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public interface SongService {
    boolean addSong(MultipartFile f, String username) throws IOException;
    List<Song> getSongs(String username) throws IOException;
    void deleteSong(String name,String username);
}
