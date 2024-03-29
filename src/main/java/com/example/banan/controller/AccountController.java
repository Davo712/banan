package com.example.banan.controller;

import com.example.banan.model.*;
import com.example.banan.repository.MessageRepository;
import com.example.banan.repository.ProductRepository;
import com.example.banan.repository.PublicationRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.ImageService;
import com.example.banan.service.PublicationService;
import com.example.banan.service.SongService;
import com.example.banan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.function.Consumer;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserService userService;
    @Autowired
    public PublicationRepository publicationRepository;
    @Autowired
    public PublicationService publicationService;
    @Autowired
    public SongService songService;
    @Autowired
    public ImageService imageService;
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public MessageRepository messageRepository;

    public User globalUser = new User();

    final String SUCCESS = "Successfully";
    final String OLD_PASS_WRONG = "Old password is wrong";
    final String USER_NOT_FOUND = "User not found";
    final String FRIEND_RQ_SENT = "Friend request sent";
    final String ERROR = "Error";
    final String PUBLICATION_CREATED = "Publication created";
    final String USER_NOT_FRIEND = "The user is not your friend";
    final String SONG_ADDED = "Song added in your list";
    final String IMAGE_ADDED = "Image added in your list";


    @GetMapping("")
    public String getAccount(Principal principal, Model model) {
        return "account";
    }


    @GetMapping("/changePassword")
    public String changePassword() {
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(Model model, Principal principal, String newPassword, String oldPassword) {
        User user = userRepository.findByUsername(principal.getName());
        if (userService.changePassword(newPassword, oldPassword, user)) {
            model.addAttribute("message", SUCCESS);
            return "changePassword";
        }
        model.addAttribute("message", OLD_PASS_WRONG);
        return "changePassword";
    }

    @GetMapping("/getSearchUser")
    public String getSearchUser() {
        return "searchUser";
    }

    @GetMapping("/searchUser/{username}")
    public String searchUser(Model model, @PathVariable String username, Principal principal) {
        if (username.equals(principal.getName())) {
            return "account";
        }

        User user = userService.searchUser(username);
        if (user == null || !user.isActive()) {
            model.addAttribute("message", USER_NOT_FOUND);
            return "searchUser";
        }
        model.addAttribute("user1", user);

        return "searchResult";
    }

    @GetMapping("/searchUser")
    public String searchUser(String username) {

        return "redirect:/account/searchUser/" + username;
    }

    @GetMapping("/getFriends/my")
    public String getFriends() {
        return "myFriends";
    }

    @GetMapping("/getFriendRequests")
    public String getFriendRequests() {
        return "friendRequests";
    }
    @PostMapping("/addFriend/{username}")
    public String addFriend(@PathVariable String username, Model model, Principal principal) {
        if (username.equals(principal.getName())) {
            return "account";
        }
        if (userService.addFriendRequest(username, principal.getName())) {
            model.addAttribute("message", FRIEND_RQ_SENT);
            return "redirect:/account/searchUser/" + username;
        }
        model.addAttribute("message", ERROR);
        return "account";
    }

    @PostMapping("/acceptFriend/{username}")
    public String acceptFriend(@PathVariable String username, Principal principal, Model model) {
        userService.acceptFriend(username, principal.getName());
        return "redirect:/account/getFriendRequests";
    }

    @PostMapping("/declineFriend/{username}")
    public String declineFriend(@PathVariable String username, Principal principal, Model model) {
        userService.declineFriend(principal.getName(), username);
        return "redirect:/account/getFriendRequests";
    }


    @PostMapping("/deleteFriend/{username}")
    public String deleteFriend(@PathVariable String username, Principal principal) {
        userService.deleteFriend(principal.getName(), username);
        return "redirect:/account/getFriends/my";
    }

    @PostMapping("/createPublication")
    public String createPublication(Principal principal, Model model, String message) {
        User user = userRepository.findByUsername(principal.getName());
        userService.createPublication(message, user);
        model.addAttribute("message", PUBLICATION_CREATED);
        return "redirect:/account";
    }

    @GetMapping("/getPublications/{username}")
    public String getPublications(@PathVariable String username, Principal principal, Model model) {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getPublications/my";
        }

        if ((userService.isFriend(principal.getName(), username))) {
            User user = userRepository.findByUsername(username);
            List<Publication> publications = user.getPublications();
            model.addAttribute("publications", publications);
            model.addAttribute("user1", user);
            return "publications";
        } else {
            model.addAttribute("message", USER_NOT_FRIEND);
            return "account";
        }
    }

    @GetMapping("/getPublications/my")
    public String myPublications(Principal principal, Model model) {
        model.addAttribute("publications", userRepository.findByUsername(principal.getName()).getPublications());
        return "account";
    }

    @PostMapping("/deletePublication/{id}")
    public String deletePublication(@PathVariable long id, Principal principal) {
        publicationService.deletePublication(id, principal.getName());
        return "redirect:/account/getPublications/my";


    }

    @PostMapping("/addSong")
    public String addSong(@RequestParam("f") MultipartFile f, Model model, Principal principal,String nameByUser) throws IOException {
        if (songService.addSong(f, principal.getName(),nameByUser)) {
            model.addAttribute("message", SONG_ADDED);
            return "redirect:/account/getSongs/my";
        } else {
            model.addAttribute("message", ERROR);
        }
        return "redirect:/account/getSongs/my";
    }

    @GetMapping("/getSongs/my")
    public String mySongs(Principal principal, Model model) throws IOException {
        List<Song> songs = songService.getSongs(principal.getName());
        Collections.reverse(songs);
        model.addAttribute("songs", songs);
        return "mySongs";
    }

    @GetMapping("/getSongs/{username}")
    public String getSongs(@PathVariable String username, Principal principal, Model model) throws IOException {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getSongs/my";
        }
        if ((userService.isFriend(principal.getName(), username))) {
            User user = userRepository.findByUsername(username);
            model.addAttribute("songs", songService.getSongs(user.getUsername()));
            model.addAttribute("user1", user);
            return "songs";
        } else {
            model.addAttribute("message", USER_NOT_FRIEND);
            return "account";
        }
    }


    @PostMapping("/deleteSong/{name}")
    public String deleteSong(@PathVariable String name, Principal principal) {
        songService.deleteSong(name, principal.getName());
        return "redirect:/account/getSongs/my";
    }


    @PostMapping("/addImage")
    public String addImage(@RequestParam("f") MultipartFile f, Model model, Principal principal,String nameByUser) throws IOException {

        if (f.getOriginalFilename().equals("")) {
            return "account";
        }
        if (imageService.addImage(f, principal.getName(),nameByUser)) {
            model.addAttribute("message", IMAGE_ADDED);
            return "redirect:/account/getImages/my";
        } else {
            model.addAttribute("message", ERROR);
        }
        return "account";
    }

    @GetMapping("/getImages/my")
    public String myImages(Principal principal, Model model) throws IOException {
        model.addAttribute("images", imageService.getImages(principal.getName()));
        return "myImages";
    }


    @GetMapping("/getImages/{username}")
    public String getImages(@PathVariable String username, Principal principal, Model model) throws IOException {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getImages/my";
        }

        if ((userService.isFriend(principal.getName(), username))) {
            User user = userRepository.findByUsername(username);
            model.addAttribute("images", imageService.getImages(user.getUsername()));
            model.addAttribute("user1", user);
            return "images";
        } else {
            model.addAttribute("message", USER_NOT_FRIEND);
            return "account";
        }
    }

    @PostMapping("/deleteImage/{name}")
    public String deleteImage(@PathVariable String name, Principal principal) {
        imageService.deleteImage(name, principal.getName());
        return "redirect:/account/getImages/my";
    }

    @GetMapping("/chat")
    public String getChat(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "chat";
    }





    @ModelAttribute("user")
    public User attribute(Principal principal) {
        globalUser = userRepository.findByUsername(principal.getName());
        return globalUser;
    }

    @ModelAttribute("friendRequests")
    public List<User> attribute2() {
        if (globalUser.getFriendRequests() != null) {
            List<User> friendRequests = new ArrayList<>();
            globalUser.getFriendRequests().forEach(s -> {
                friendRequests.add(userRepository.findByUsername(s));
            });
            return friendRequests;
        }
        return new ArrayList<>();
    }

    @ModelAttribute("friends")
    public List<User> attribute3() {
        if (globalUser.getFriendUsernames() != null) {
            List<User> friends = new ArrayList<>();
            globalUser.getFriendUsernames().forEach(s -> {
                friends.add(userRepository.findByUsername(s));
            });
            return friends;
        }
        return new ArrayList<>();
    }
    @ModelAttribute("publications")
    public List<Publication> attribute4() {
        List<Publication> publications = globalUser.getPublications();
        publications.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
       return publications;
    }


}
