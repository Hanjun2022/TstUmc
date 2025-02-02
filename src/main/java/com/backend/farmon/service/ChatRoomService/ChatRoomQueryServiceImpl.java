package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.config.chat.WebSocketSessionManager;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.domain.enums.ChatMessageType;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;

    private static final Integer PAGE_SIZE=10;

    // 페이지 정렬, 최신순
    private Pageable pageRequest(Integer pageNumber) {
        return PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
    }

    // 검색어와 일치하는 채팅방 목록 조회
    @Override
    public ChatResponse.ChatRoomListDTO findChatRoomBySearch(Long userId, Integer read, String searchName, Integer pageNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 현재 로그인한 사용자의 역할
        String role = userAuthorizationUtil.getCurrentUserRole();

        // 안 읽음 필터링에 따른 유저의 모든 채팅방 목록을 페이지네이션으로 조회
        Page<ChatRoom> chatRoomPage = read.equals(1)
                ? chatRoomRepository.findUnReadChatRoomsByUserIdAndRoleAndSearch(userId, role, searchName, pageRequest(pageNumber))
                : chatRoomRepository.findChatRoomsByUserIdAndRoleAndSearch(userId, role, searchName, pageRequest(pageNumber));

        log.info("안 읽음 필터링에 따른 유저의 모든 채팅방 목록 페이지네이션 조회 완료 - userId: {}", userId);

        // 최신 메시지 타입 (썸네일에서는 텍스트만)
        List<String> targetTypes = List.of(ChatMessageType.TEXT.toString());

        // 안 읽은 메시지 개수 조회 타입 (텍스트, 이미지)
        List<ChatMessageType> unReadTargetTypes = List.of(ChatMessageType.TEXT, ChatMessageType.IMAGE);

        // 채팅 대화방 세부 정보 목록 생성
        List<ChatResponse.ChatRoomDetailDTO> chatRoomInfoList = chatRoomPage.stream().map(chatRoom -> {
            // 전문가 여부
            boolean isExpert = chatRoom.getExpert().getId().equals(userId);
            log.info("채팅방에서 전문가 여부: {}", isExpert);

            // 안 읽은 채팅 메시지 개수 조회
            long unReadMessageCount = chatMessageRepository.countByChatRoomIdAndIsReadFalseAndTypeIn(chatRoom.getId(), unReadTargetTypes);
            log.info("안 읽은 채팅 메시지 개수 조회 완료 - userId: {}, 안 읽은 메시지 개수: {}", userId, unReadMessageCount);

            // 채팅방과 일치하는 최신 메시지 조회
            ChatMessage lastMessage = chatMessageRepository
                    .findLatestMessageByTypes(chatRoom.getId(), targetTypes)
                    .orElse(null);

            log.info("채팅방과 일치하는 최신 메시지 조회 완료 - userId: {}", userId);

            // 채팅방의 견적과 연관된 지역
            Area area = chatRoom.getEstimate().getArea();

            // 채팅방 대화방 세부 정보를 dto로 변환
            return ChatConverter.toChatRoomDetailDTO(chatRoom, lastMessage, area, isExpert, unReadMessageCount);
        }).toList();

        // 최종 채팅방 목록 정보 DTO 생성 및 반환
        return ChatConverter.toChatRoomListDTO(chatRoomPage, chatRoomInfoList);
    }

    // 채팅방 정보 조회
    @Transactional
    @Override
    public ChatResponse.ChatRoomDataDTO findChatRoomDataAndChangeUnreadMessage(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 채팅방 입장 시 접속 시간 수정
        boolean isExpert = chatRoom.getExpert().getId().equals(userId);
//        chatRoomCommandService.changeChatRoomEnterTime(userId, chatRoomId, isExpert);
//        log.info("채팅방 입장 접속 시간 변경 - userId: {}, chatRoomId: {}, 전문가 여부: {}", userId, chatRoomId, isExpert);

        return ChatConverter.toChatRoomDataDTO(chatRoom, isExpert);
    }

    // 채팅방의 견적 조회
    @Override
    public ChatResponse.ChatRoomEstimateDTO findChatRoomEstimate(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 견적 이미지와 함께 견적 조회
        Estimate estimate = estimateRepository.findEstimateWithImages(chatRoom.getEstimate().getId())
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.ESTIMATE_NOT_FOUND));

        log.info("채팅방의 견적 조회 완료 - userId: {}, estimateId: {}", userId, estimate.getId());

        return ChatConverter.toChatRoomEstimateDTO(estimate, estimate.getEstimateImageList());
    }
}
