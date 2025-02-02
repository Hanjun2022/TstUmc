package com.backend.farmon.repository.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.enums.ChatMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
    // 채팅방 아이디와 일치하는 가장 최신 메시지 조회
    @Query(value = "SELECT * FROM chat_message cm WHERE cm.chat_room_id = :chatRoomId AND cm.type IN (:types) ORDER BY cm.created_at DESC LIMIT 1",
            nativeQuery = true)
    Optional<ChatMessage> findLatestMessageByTypes(@Param("chatRoomId") Long chatRoomId,
                                                   @Param("types") List<String> types);

    // 채팅방 아이디와 일치하면서 isRead가 false인 메시지 개수 조회
    long countByChatRoomIdAndIsReadFalseAndTypeIn(Long chatRoomId, List<ChatMessageType> types);

//    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId AND cm.isRead = false AND cm.type IN :types")
//    long countUnreadMessagesByChatRoomIdAndTypes(@Param("chatRoomId") Long chatRoomId, @Param("types") List<ChatMessageType> types);

    // 채팅방 아이디와 일치하는 채팅 메시지들 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId")
    void deleteByChatRoomId(@Param("chatRoomId") Long chatRoomId);
}
