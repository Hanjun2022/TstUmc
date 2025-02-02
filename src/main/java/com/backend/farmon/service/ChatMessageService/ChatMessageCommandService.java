package com.backend.farmon.service.ChatMessageService;

import com.backend.farmon.dto.chat.ChatRequest;

public interface ChatMessageCommandService {

    // 메시지 저장
    void saveChatMessage(Long chatRoomId, ChatRequest.ChatMessageDTO dto);
}
