package com.example.banan.controller;

import com.example.banan.model.Message;
import com.example.banan.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    public MessageRepository messageRepository;


    @GetMapping("/account/sendMessage/{username}")
    public String getSendMessagePage(Principal principal, Model model, @PathVariable String username) {
        model.addAttribute("fromUsername",principal.getName());
        model.addAttribute("toUsername",username);
        List<Message> messages = messageRepository.getMessages(principal.getName(),username);
        model.addAttribute("messages",messages);
        return "sendMessagePage";
    }

    @PostMapping("/account/sendMessage/{username}")
    public String sendMessage(Principal principal,@PathVariable String username,String messageText) {
        Message message = new Message();
        message.setMessageFrom(principal.getName());
        message.setMessageTo(username);
        message.setMessageText(messageText);
        messageRepository.save(message);
        return "redirect:/account/sendMessage/" + username;
    }

}
