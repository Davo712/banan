package com.example.banan.repository;

import com.example.banan.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * FROM message WHERE message_from = :username1 AND message_to = :username2 OR message_from = :username2 AND message_to = :username1",nativeQuery = true)
    List<Message> getMessages(@Param("username1") String username1, @Param("username2") String username2);
}
