package com.example.banan.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {

    public User() {
    }

    public User(long id, String username, String password, String name, String surname, boolean active, String activationCode, long balance, Role role, List<String> friendRequests, List<String> friendUsernames, List<Publication> publications, List<Song> songs, List<Image> images) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.active = active;
        this.activationCode = activationCode;
        this.balance = balance;
        this.role = role;
        this.friendRequests = friendRequests;
        this.friendUsernames = friendUsernames;
        this.publications = publications;
        this.songs = songs;
        this.images = images;
    }

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
    private Role role;
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
