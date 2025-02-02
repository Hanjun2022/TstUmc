package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    // userId와 연관된 검색어와 일치하는 채팅방 페이징 조회
    Page<ChatRoom> findChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable);

    // userId와 연관된 검색어와 일치하는 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    Page<ChatRoom> findUnReadChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable);
}
