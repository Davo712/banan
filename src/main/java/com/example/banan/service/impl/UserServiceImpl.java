package com.example.banan.service.impl;

import com.example.banan.controller.MainController;
import com.example.banan.model.Publication;
import com.example.banan.model.Role;
import com.example.banan.model.User;
import com.example.banan.repository.PublicationRepository;
import com.example.banan.repository.UserRepository;
import com.example.banan.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    public PublicationRepository publicationRepository;




    @Override
    public boolean addUser(User user) {

        User user1 = userRepository.findByUsername(user.getUsername());
        if (user1 != null) {
            return false;
        }

        user.setActive(false);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setRole(Role.USER);
        while (userRepository.findByActivationCode(user.getActivationCode()) != null) {
            user.setActivationCode(UUID.randomUUID().toString());
        }
        userRepository.save(user);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getUsername());
                message.setSubject("Activate account");
                String message1 = String.format("Please visit next link for account activation:  http://localhost:8080/activate/%s", user.getActivationCode());
                message.setText(message1);
                emailSender.send(message);
            }
        }).start();

        return true;

    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean changePassword(String newPassword, String oldPassword, User user) {
        if (bCryptPasswordEncoder.matches(oldPassword,user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean forgotPassword(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getUsername());
                message.setSubject("Forgot Password");
                String message1 = String.format("Please visit next link for change password:  http://localhost:8080/forgotPassword1/%s",user.getActivationCode());
                System.out.println(message1);
                message.setText(message1);
                emailSender.send(message);
            }
        }).start();

        return true;
    }

    @Override
    public boolean forgotPassword1(String code,String newPassword,String newPassword1) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) {
            return false;
        }

        if (newPassword.equals(newPassword1)) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            user.setActivationCode(UUID.randomUUID().toString());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User searchUser(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    @Transactional
    public boolean addFreindRequest(String username1,String username2) {
        User user = userRepository.findByUsername(username1);
        User user1 = userRepository.findByUsername(username2);
        List<String> l = user.getFriendUsernames();
        List<String> friends = user.getFriendRequests();
        for (int i = 0; i < l.size() ; i++) {
            if (l.get(i).equals(user1.getUsername()))
                return false;
        }
        if (user == null) {
            return false;
        }
        if (!user.isActive()){
            return false;
        }
        for (int i = 0; i < friends.size() ; i++) {
            if (friends.get(i).equals(username2))
                return false;
        }
        friends.add(username2);
        user.setFriendRequests(friends);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean acceptFriend(String username1, String username2) {
        User user = userRepository.findByUsername(username2);
        User user1 = userRepository.findByUsername(username1);
        List<String> fr1 = user1.getFriendRequests();
        fr1.remove(user.getUsername());
        user1.setFriendRequests(fr1);
        List<String> fr = user.getFriendRequests();
        fr.remove(user1.getUsername());
        user.setFriendRequests(fr);
        List<String> list = user.getFriendUsernames();
        list.add(user1.getUsername());
        user.setFriendUsernames(list);
        userRepository.save(user);
        List<String> list1 = user1.getFriendUsernames();
        list1.add(user.getUsername());
        user1.setFriendUsernames(list1);
        userRepository.save(user1);


        return true;
    }

    @Override
    @Transactional
    public boolean declineFriend(String username1,String username2) {
        User user = userRepository.findByUsername(username1);
        user.getFriendRequests().remove(username2);
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void deleteFriend(String username, String usernameFriend) {
        User user = userRepository.findByUsername(username);
        User friend = userRepository.findByUsername(usernameFriend);
        List<String> friendUsernames = user.getFriendUsernames();
        friendUsernames.remove(usernameFriend);
        user.setFriendUsernames(friendUsernames);
        userRepository.save(user);
        List<String> friendUsernames2 = friend.getFriendUsernames();
        friendUsernames2.remove(username);
        friend.setFriendUsernames(friendUsernames2);
        userRepository.save(friend);
    }

    @Override
    @Transactional
    public boolean createPublication(String message, User user) {
        Publication publication = new Publication();
        publication.setMessage(message);
        publication.setDate(new Date());
        user.getPublications().add(publication);
        user.setPublications(user.getPublications());
        publicationRepository.save(publication);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean isFriend(String username, String firendUsername) {
        User user = userRepository.findByUsername(username);
        List<String> l = user.getFriendUsernames();
        for (int i = 0; i < l.size() ; i++) {
            if (l.get(i).equals(firendUsername)) {
                return true;
            }
        }
        return false;
    }


}
