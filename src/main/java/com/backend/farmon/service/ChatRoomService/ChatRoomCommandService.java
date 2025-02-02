package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatRoomCommandService {

    // 채팅방 생성
    ChatResponse.ChatRoomCreateDTO addChatRoom(Long userId, Long estimateId);

    // 채팅방 삭제
    ChatResponse.ChatRoomDeleteDTO removeChatRoom(Long userId, Long chatRoomId);

    // 채팅방 컨설팅 완료
    void exchangeChatRoomUserComplete(Long userId, Long chatRoomId, ChatRequest.ChatMessageDTO dto);

    // 사용자 여부에 따른 채팅 입장 시간 변경
    void changeChatRoomEnterTime(Long userId, ChatRoom chatRoom);
}
