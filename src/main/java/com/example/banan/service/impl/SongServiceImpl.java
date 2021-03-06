package com.example.banan.service.impl;

import com.example.banan.model.Song;
import com.example.banan.model.User;
import com.example.banan.repository.SongRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.SongService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    public SongRepository songRepository;
    @Autowired
    public UtilService utilService;

    @Override
    public boolean addSong(MultipartFile f, String username) throws IOException {
        Song song = new Song();
        song.setName(RandomStringUtils.randomAlphabetic(15) + ".mp3");
        song.setLink("C:\\Users\\User\\Desktop\\MusicsForBanan\\" + song.getName());
        User user = userRepository.findByUsername(username);
        List<Song> songs = user.getSongs();
        songs.add(song);
        user.setSongs(songs);
        songRepository.save(song);
        userRepository.save(user);

        byte[] array = f.getBytes();
        File file = new File(song.getLink());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(array);
        fileOutputStream.close();

        return true;
    }

    @Override
    public List<Song> getSongs(String username) {
        User user = userRepository.findByUsername(username);
        return user.getSongs();

    }

    @Override
    public void deleteSong(String name, String username) {
        User user = userRepository.findByUsername(username);
        List<Song> songs = user.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).getName().equals(name)) {
                songs.remove(i);
            }
        }
        Song song = songRepository.findByName(name);
        songRepository.delete(song);

        File file = new File(song.getLink());
        file.delete();

    }


}
