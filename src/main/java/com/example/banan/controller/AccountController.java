package com.example.banan.controller;

import com.example.banan.model.*;
import com.example.banan.repository.ProductRepository;
import com.example.banan.repository.PublicationRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.ImageService;
import com.example.banan.service.PublicationService;
import com.example.banan.service.SongService;
import com.example.banan.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

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

    @GetMapping("")
    public String getAccount(Principal principal,Model model) {
        return "account";
    }


    @GetMapping("/changePassword")
    public String changePassword() {
        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(Model model, Principal principal,String newPassword,String oldPassword) {
        User user = userRepository.findByUsername(principal.getName());
        if (userService.changePassword(newPassword,oldPassword,user)) {
            model.addAttribute("message","successfully");
            return "changePassword";
        }
        model.addAttribute("message","old password is wrong");
        return "changePassword";
    }

    @GetMapping("/searchUser/{username}")
    public String searchUser(Model model, @PathVariable String username,Principal principal) {
        if (username.equals(principal.getName())) {
            return "account";
        }

        User user = userService.searchUser(username);
        if (user == null||!user.isActive()) {
            model.addAttribute("message","user not found");
            return "account";
        }
        model.addAttribute("user1",user);

        return "searchResult";
    }

    @GetMapping("/searchUser")
    public String searchUser(String username) {

        return "redirect:/account/searchUser/" + username;
    }

    @PostMapping("/addFriend/{username}")
    public String addFriend(@PathVariable String username,Model model,Principal principal) {
        if (username.equals(principal.getName())) {
            return "account";
        }
        if (userService.addFreindRequest(username,principal.getName())) {
            model.addAttribute("message","friend request sent");
            return "account";
        }
        model.addAttribute("message","error");
        return "account";
    }

    @PostMapping("/acceptFriend/{username}")
    public String acceptFriend(@PathVariable String username,Principal principal,Model model) {
        userService.acceptFriend(username,principal.getName());
        return  "redirect:/account";
    }

    @PostMapping("/declineFriend/{username}")
    public String declineFriend(@PathVariable String username,Principal principal,Model model) {
        userService.declineFriend(principal.getName(),username);
        return  "redirect:/account";
    }


    @GetMapping("/deleteFriend/{username}")
    public String deleteFriend(@PathVariable String username,Principal principal) {
        System.out.println("deleted");
        userService.deleteFriend(principal.getName(),username);
        return "account";
    }

    @PostMapping("/createPublication")
    public String createPublication(Principal principal, Model model,String message) {
        User user = userRepository.findByUsername(principal.getName());
        userService.createPublication(message,user);
        model.addAttribute("message","publication created");
        return "account";
    }

    @GetMapping("/getPublications/{username}")
    public String getPublications(@PathVariable String username,Principal principal,Model model) {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getPublications/my";
        }

        if ((userService.isFriend(principal.getName(),username))) {
            User user = userRepository.findByUsername(username);
            List<Publication> publications = user.getPublications();
            model.addAttribute("publications",publications);
            model.addAttribute("user1",user);
            return "publications";
        } else {
            model.addAttribute("message","the user is not your friend");
            return "account";
        }
    }

    @GetMapping("/getPublications/my")
    public String myPublications(Principal principal, Model model) {
        model.addAttribute("publications",userRepository.findByUsername(principal.getName()).getPublications());
        return "myPublications";
    }

    @PostMapping("/deletePublication/{id}")
    public String deletePublication(@PathVariable long id, Principal principal) {
        publicationService.deletePublication(id,principal.getName());
        return "redirect:/account/getPublications/my";


    }

    @PostMapping("/addSong")
    public String addSong(@RequestParam("f") MultipartFile f, Model model,Principal principal) throws IOException {
        if (songService.addSong(f,principal.getName())) {
            model.addAttribute("message","Song added in your list");
            return "account";
        } else {
            model.addAttribute("message","error");
        }

        return "account";
    }

    @GetMapping("/getSongs/my")
    public String mySongs(Principal principal,Model model) throws IOException {
        model.addAttribute("songs",songService.getSongs(principal.getName()));
        return "mySongs";
    }

    @GetMapping("/getSongs/{username}")
    public String getSongs(@PathVariable String username,Principal principal,Model model) throws IOException {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getSongs/my";
        }

        if ((userService.isFriend(principal.getName(),username))) {
            User user = userRepository.findByUsername(username);
            model.addAttribute("songs",songService.getSongs(user.getUsername()));
            model.addAttribute("user1",user);
            return "songs";
        } else {
            model.addAttribute("message","the user is not your friend");
            return "account";
        }
    }


    @PostMapping("/deleteSong/{name}")
    public String deleteSong(@PathVariable String name, Principal principal) {
        songService.deleteSong(name,principal.getName());
        return "redirect:/account/getSongs/my";
    }


    @PostMapping("/addImage")
    public String addImage(@RequestParam("f") MultipartFile f, Model model,Principal principal) throws IOException {

        if (f.getOriginalFilename().equals("")) {
            return "account";
        }
        if (imageService.addImage(f,principal.getName())) {
            model.addAttribute("message","Image added in your list");
            return "account";
        } else {
            model.addAttribute("message","error");
        }
        return "account";
    }

    @GetMapping("/getImages/my")
    public String myImages(Principal principal,Model model) throws IOException {
        model.addAttribute("images",imageService.getImages(principal.getName()));
        return "myImages";
    }


    @GetMapping("/getImages/{username}")
    public String getImages(@PathVariable String username,Principal principal,Model model) throws IOException {
        if (username.equals(principal.getName())) {
            return "redirect:/account/getImages/my";
        }

        if ((userService.isFriend(principal.getName(),username))) {
            User user = userRepository.findByUsername(username);
            model.addAttribute("images",imageService.getImages(user.getUsername()));
            model.addAttribute("user1",user);
            return "images";
        } else {
            model.addAttribute("message","the user is not your friend");
            return "account";
        }
    }

    @PostMapping("/deleteImage/{name}")
    public String deleteImage(@PathVariable String name, Principal principal) {
        imageService.deleteImage(name,principal.getName());
        return "redirect:/account/getImages/my";
    }

    @GetMapping("/getAllProducts")
    public String getAllProducts(Model model) {
        List products = productRepository.findAll();
        model.addAttribute("products",products);
        return null;
    }
    @GetMapping("/getProducts/{username}")
    public String getProducts(@PathVariable String username, Principal principal,Model model) {
        User user = userRepository.findByUsername(username);
        List products = user.getProducts();
        model.addAttribute("products",products);
        return null;

    }
    @GetMapping("/getProducts/my")
    public String myProducts(Principal principal,Model model) throws IOException {
        model.addAttribute("products",userRepository.findByUsername(principal.getName()).getProducts());
        return null;
    }
    @PostMapping("/addProduct")
    public String addProduct(Principal principal, Product product) {
        User user = userRepository.findByUsername(principal.getName());
        product.setSellerName(user.getUsername());
        user.getProducts().add(product);
        userRepository.save(user);
        productRepository.save(product);
        return null;
    }
    @PostMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable String id, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Long productId = Long.getLong(id);
        List products = user.getProducts();
        Product product = productRepository.getById(productId);
        if (product != null) {
            products.remove(product);
            user.setProducts(products);
            userRepository.save(user);
            productRepository.delete(product);
        }
        return null;
    }

    @ModelAttribute("user")
    public User attribute(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return user;
    }
    @ModelAttribute("friendRequests")
    public List<String> atribute2(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return user.getFriendRequests();

    }

    @ModelAttribute("friendUsernames")
    public List<String> atribute3(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        return user.getFriendUsernames();

    }





}
