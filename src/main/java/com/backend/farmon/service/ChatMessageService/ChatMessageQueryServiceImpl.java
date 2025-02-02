package com.backend.farmon.service.ChatMessageService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    private static final Integer PAGE_SIZE=12;

    // 페이지 정렬, 최신순
    private Pageable pageRequest(Integer pageNumber) {
        return PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
    }

    // 채팅 메시지 내역 조회 & 안 읽은 메시지 읽음 처리
    @Transactional
    @Override
    public ChatResponse.ChatMessageListDTO findChatMessageList(Long userId, Long chatRoomId, Integer pageNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 안 읽은 메시지들을 읽음 처리
        chatMessageRepository.updateMessagesToReadByChatRoomId(chatRoomId, userId);
        log.info("안 읽은 메시지들 읽음 처리 완료 - 채팅방 아아디: {}", chatRoomId);

        // 모든 채팅 메시지 내역 조회 (EXIT, COMPLETE 제외)
        Slice<ChatMessage> chatMessageList = chatMessageRepository.findNonExitCompleteMessagesByChatRoomId(chatRoomId, pageRequest(pageNumber));
        log.info("모든 채팅 메시지 내역 조회 완료 - 채팅방 아아디: {}", chatRoomId);

        return ChatConverter.toChatMessageListDTO(chatMessageList, userId);
    }
}
