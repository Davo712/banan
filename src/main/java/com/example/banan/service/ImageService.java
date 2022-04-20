package com.example.banan.service;

import com.example.banan.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public interface ImageService {

    boolean addImage(MultipartFile f, String username) throws IOException;
    List<Image> getImages(String username) throws IOException;
    void deleteImage(String name,String username);
}
