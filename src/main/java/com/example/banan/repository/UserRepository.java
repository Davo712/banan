package com.example.banan.repository;

import com.example.banan.model.Publication;
import com.example.banan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);
    void deleteById(int id);
    User findByActivationCode(String activationCode);



}
