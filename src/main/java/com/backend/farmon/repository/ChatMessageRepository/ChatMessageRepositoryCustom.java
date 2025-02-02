package com.backend.farmon.repository.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatMessageRepositoryCustom {
    // 채팅방 아이디와 일치하는 채팅 메시지 중, 안 읽은 메시지를 읽음 처리
    void updateMessagesToReadByChatRoomId(Long chatRoomId, Long userId);

    // 채팅방 아이디와 일치하는 퇴장, 거래 완료가 아닌  채팅 메시지 리스트 조회
    public Slice<ChatMessage> findNonExitCompleteMessagesByChatRoomId(Long chatRoomId, Pageable pageable);
}