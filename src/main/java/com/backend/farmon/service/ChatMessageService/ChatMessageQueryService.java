package com.backend.farmon.service.ChatMessageService;

import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatMessageQueryService {
    // 채팅 메시지 내역 조회 (무한스크롤)
    ChatResponse.ChatMessageListDTO findChatMessageList(Long userId, Long chatRoomId, Integer pageNumber);
}
