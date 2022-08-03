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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
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
        List<Message> messages = messageRepository.getMessages(principal.getName(), username);
        model.addAttribute("messages", messages);
        return "sendMessagePage";
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
    public List<String> atribute2(Principal principal) {
        return globalUser.getFriendRequests();
    }

    @ModelAttribute("friendUsernames")
    public List<String> atribute3(Principal principal) {
        return globalUser.getFriendUsernames();
    }
}
