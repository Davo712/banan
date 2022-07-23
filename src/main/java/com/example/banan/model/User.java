package com.example.banan.model;

import lombok.Data;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private boolean active;
    private String activationCode;
    private long balance;
    @ElementCollection
    private List<String> friendRequests;
    @ElementCollection
    private List<String> friendUsernames;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Publication> publications;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Song> songs;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Image> images;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Product> products;


}
