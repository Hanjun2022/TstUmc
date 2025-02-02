package com.backend.farmon.service.ChatMessageService;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatMessageHandler;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.aws.s3.AmazonS3Manager;
import com.backend.farmon.aws.s3.UuidRepository;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.converter.ConvertTime;
import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.ChatRoomService.ChatRoomCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatMessageCommandServiceImpl implements ChatMessageCommandService {

    private final ChatRoomCommandService chatRoomCommandService;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final UuidRepository uuidRepository;

    // 메시지 저장
    @Transactional
    @Override
    public void saveChatMessage(Long chatRoomId, ChatRequest.ChatMessageDTO dto){
        Long userId = dto.getSenderId();

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        switch (dto.getMessageType()){
            case "ENTER": // 입장 - 접속시간 변경
                log.info("채팅방 입장 ENTER - chatRoomId: {}", chatRoomId);
                chatRoomCommandService.changeChatRoomEnterTime(userId, chatRoom);
                break;
            case "TEXT": // 텍스트 로직
                log.info("채팅방 메시지 전송 TEXT - chatRoomId: {}", chatRoomId);
                break;
            case "IMAGE": // 이미지 로직
                log.info("채팅방 이미지 전송 IMAGE - chatRoomId: {}", chatRoomId);
                break;
            case "COMPLETE": // 컨설팅 완료
                log.info("채팅방 컨설팅 완료 COMPLETE - chatRoomId: {}", chatRoomId);
                chatRoomCommandService.exchangeChatRoomUserComplete(userId, chatRoomId, dto);
                break;
            case "EXIT": // 퇴장 - 접속시간 변경
                log.info("채팅방 퇴장 EXIT - chatRoomId: {}", chatRoomId);
                chatRoomCommandService.changeChatRoomEnterTime(userId, chatRoom);
                break;
            default:
                throw new ChatMessageHandler(ErrorStatus.MESSAGE_TYPE_NOT_FOUND);
        }

        ChatMessage chatMessage = ChatConverter.toChatMessage(dto, chatRoom);
        chatMessageRepository.save(chatMessage);

        dto.setSendTime(ConvertTime.convertToAmPmFormat(chatMessage.getCreatedAt())); // 전송 시간 변경
        dto.setIsMine(false); // 내가 보낸 메시지 여부 변경

        log.info("채팅 메시지 저장 완료 - chatMessageId: {}, senderId: {}, messageType: {}, chatRoomId: {}",
                chatMessage.getId(), dto.getSenderId(), dto
                        .getMessageType(), chatRoomId);
    }
}
