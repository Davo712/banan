package com.example.banan.controller;

import com.example.banan.model.User;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class MainController {


    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserService userService;

    @GetMapping("/")
    public String getHome() {

        return "home";
    }

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/account";
        }
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {

        return "registration";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        model.addAttribute("message", "Activate your account, message sent to your email");
        return "login";
    }


    @GetMapping("/activate/")
    public String activate() {
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivate = userService.activateUser(code);
        if (isActivate) {
            model.addAttribute("message","Successful activation");
            return "login";
        }
        model.addAttribute("message","Something is wrong");
        return "login";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(String username,Model model) {
        if (userService.forgotPassword(username)) {
            model.addAttribute("message","sms sent in your email");
            return "forgotPassword";
        }
        model.addAttribute("message","wrong username");
        return "forgotPassword";

    }
    @GetMapping("/forgotPassword1/{code}")
    public String forgotPassword1(@PathVariable String code, Model model) {
        if (userRepository.findByActivationCode(code) == null) {
            model.addAttribute("message","invalide link");
            return "login";
        }
        code = "/forgotPassword1/" + code;
        System.out.println(code);
        model.addAttribute("code",code);
        return "forgotPassword1";
    }

    @PostMapping("/forgotPassword1/{code}")
    public String forgotPassword1(Model model, @PathVariable String code,String newPassword,String newPassword1) {
        System.out.println("saassasa");
        if (userService.forgotPassword1(code,newPassword,newPassword1)) {
            model.addAttribute("message", "password changed");
            return "login";
        } else {
            model.addAttribute("message","error");
            return "forgotPassword1";
        }

    }


}
