package com.example.banan.controller;

import com.example.banan.model.Message;
import com.example.banan.model.User;
import com.example.banan.repository.MessageRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    public MessageRepository messageRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserService userService;

    public User globalUser;



    @GetMapping("/getMessages/{username}")
    public ResponseEntity test(Principal principal,@PathVariable String username) {
        System.out.println(true);
        List<Message> messages = messageRepository.getMessages(principal.getName(), username);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("/account/sendMessage/{username}")
    public String getSendMessagePage(Principal principal, Model model, @PathVariable String username) {
        if (principal.getName().equals(username)) {
            return "redirect:/account/";
        }
        if (!userService.isFriend(principal.getName(), username)) {
            model.addAttribute("message", "the user is not your friend");
            return "account";
        }
        model.addAttribute("fromUsername", principal.getName());
        model.addAttribute("toUsername", username);
        return "message";
    }

    @PostMapping("/account/sendMessage/{username}")
    public String sendMessage(Principal principal, @PathVariable String username, String messageText) {
        if (principal.getName().equals(username)) {
            return "account";
        }
        Message message = new Message();
        message.setMessageFrom(principal.getName());
        message.setMessageTo(username);
        message.setMessageText(messageText);
        messageRepository.save(message);
        return "redirect:/account/sendMessage/" + username;
    }





    @ModelAttribute("user")
    public User attribute(Principal principal) {
        globalUser = userRepository.findByUsername(principal.getName());
        return globalUser;
    }

    @ModelAttribute("friendRequests")
    public List<String> atribute2() {
        if (globalUser.getFriendRequests() != null) {
            return globalUser.getFriendRequests();
        }
        return new ArrayList<>();
    }

    @ModelAttribute("friendUsernames")
    public List<String> atribute3() {
        if (globalUser.getFriendUsernames() != null) {
            return globalUser.getFriendUsernames();
        }
        return new ArrayList<>();
    }
}
