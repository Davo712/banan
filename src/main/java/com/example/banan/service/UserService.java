package com.example.banan.service;

import com.example.banan.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean addUser(User user);
    boolean activateUser(String code);
    boolean changePassword(String newPassword, String oldPassword, User user);
    boolean forgotPassword(String username);
    boolean forgotPassword1(String code,String newPassword,String newPassword1);
    User searchUser(String username);
    boolean addFriendRequest(String username1,String username2);
    boolean acceptFriend(String username1,String username2);
    boolean declineFriend(String username1, String username2);
    void deleteFriend(String username, String usernameFriend);
    boolean createPublication(String message,User user);
    boolean isFriend(String username,String firendUsername);

}
