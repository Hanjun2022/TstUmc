package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatRoomQueryService {
    // 검색어 별 채팅방 목록 조회
    ChatResponse.ChatRoomListDTO findChatRoomBySearch(Long userId, Integer read, String searchName, Integer pageNumber);

    // 채팅방 정보 조회 & 안 읽음 메시지 읽음 처리
    ChatResponse.ChatRoomDataDTO findChatRoomDataAndChangeUnreadMessage(Long userId, Long chatRoomId);

    // 채팅방의 견적 조회
    ChatResponse.ChatRoomEstimateDTO findChatRoomEstimate(Long userId, Long chatRoomId);
}
