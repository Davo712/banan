package com.example.banan.service.impl;

import com.example.banan.model.Image;
import com.example.banan.model.Song;
import com.example.banan.model.User;
import com.example.banan.repository.ImageRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.ImageService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public ImageRepository imageRepository;
    @Autowired
    public UtilService utilService;

    @Override
    public boolean addImage(MultipartFile f, String username,String nameByUser) throws IOException {

        Image image = new Image();
        image.setName(RandomStringUtils.randomAlphabetic(20) + ".jpg");
        image.setLink("C:\\Users\\User\\Desktop\\ImagesForBanana\\" + image.getName());
        image.setNameByUser(nameByUser);
        User user = userRepository.findByUsername(username);
        List<Image> images = user.getImages();
        images.add(image);
        user.setImages(images);
        imageRepository.save(image);
        userRepository.save(user);


        byte[] array = f.getBytes();
        File file = new File(image.getLink());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(array);
        fileOutputStream.close();
        return true;
    }

    @Override
    public List<Image> getImages(String username){
        User user = userRepository.findByUsername(username);
        return user.getImages();
    }

    @Override
    public void deleteImage(String name, String username) {

        User user = userRepository.findByUsername(username);
        List<Image> images = user.getImages();
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getName().equals(name)) {
                images.remove(i);
            }
        }
        Image image = imageRepository.findByName(name);
        imageRepository.delete(image);
        File file = new File(image.getLink());
        file.delete();

    }
}
