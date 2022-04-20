package com.example.banan.service;

import com.example.banan.model.Publication;
import org.springframework.stereotype.Service;

@Service
public interface PublicationService {
    void deletePublication(long id, String username);
}
