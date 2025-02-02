package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    @Query("SELECT c FROM ChatRoom c LEFT JOIN FETCH c.messageList WHERE c.id = :chatRoomId")
    Optional<ChatRoom> findChatRoomWithMessages(@Param("chatRoomId") Long chatRoomId);
}
