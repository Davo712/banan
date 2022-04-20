package com.example.banan.service.impl;

import com.example.banan.model.Publication;
import com.example.banan.model.User;
import com.example.banan.repository.PublicationRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.PublicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicationServiceImpl implements PublicationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    public PublicationRepository publicationRepository;

    @Override
    @Transactional
    public void deletePublication(long id, String username) {
        User user = userRepository.findByUsername(username);
        Publication publication = publicationRepository.findById(id);
        user.getPublications().remove(publication);
        userRepository.save(user);
        publicationRepository.deleteById(id);

    }
}
