package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.converter.ConvertTime;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;

    // 채팅방 생성
    @Transactional
    @Override
    public ChatResponse.ChatRoomCreateDTO addChatRoom(Long userId, Long estimateId) {
        // 채팅 신청한 전문가
        Expert expert = expertRepository.findById(userId)
                .orElseThrow(()-> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 채팅 신청한 견적
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.ESTIMATE_NOT_FOUND));

        // 견적을 신청한 농업인
        User farmer = userRepository.findById(estimate.getUser().getId())
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = ChatConverter.toChatRoom(expert, estimate, farmer);
        chatRoom.setExpert(expert);
        chatRoom.setFarmer(farmer);
        chatRoom.setEstimate(estimate);

        chatRoomRepository.save(chatRoom);

        log.info("채팅방 생성 완료 - 채팅방 아아디: {}, 생성한 전문가 expertId: {}", chatRoom.getId(), expert.getId());

        return ChatConverter.toChatRoomCreateDTO(chatRoom, farmer);
    }

    // 채팅방 삭제
    @Transactional
    @Override
    public ChatResponse.ChatRoomDeleteDTO removeChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        chatMessageRepository.deleteByChatRoomId(chatRoomId); // 채팅 메시지 삭제
        chatRoomRepository.delete(chatRoom); // 채팅방 삭제
        log.info("채팅방 삭제 완료 - 채팅방 아아디: {}", chatRoomId);

        return ChatConverter.toChatRoomDeleteDTO();
    }

    // 채팅방 컨설팅 완료
    @Transactional
    @Override
    public void exchangeChatRoomUserComplete(Long userId, Long chatRoomId, ChatRequest.ChatMessageDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 채팅방에서의 전문가 여부
        boolean isExpert = chatRoom.getExpert().getUser().getId().equals(userId);

        boolean isOtherComplete = false;
        // 컨설팅 완료 여부 변경 및 상대 거래 완료 여부 조회
        if(isExpert){
            chatRoom.setIsExpertComplete(true);
            isOtherComplete = chatRoom.getIsFarmerComplete();
        }
        else{
            chatRoom.setIsFarmerComplete(true);
            isOtherComplete = chatRoom.getIsExpertComplete();
        }

        // 농업인, 전문가 둘 다 컨설팅 완료 일 시 견적 상태 변경
        if(chatRoom.getIsFarmerComplete() && chatRoom.getIsExpertComplete()){
            Estimate estimate = chatRoom.getEstimate();
            estimate.setStatus(1); // 견적 상태 진행 완료로 변경
            estimate.setExpert(chatRoom.getExpert()); // 견적 전문가를 채팅방의 전문가로 변경

            dto.setIsEstimateComplete(true);
        }

        log.info("채팅방 컨설팅 완료 - 유저 아이디: {}, 채팅방 아아디: {}", userId, chatRoomId);
    }

    // 사용자 여부에 따른 채팅 입장 시간 변경
    @Transactional
    @Override
    public void changeChatRoomEnterTime(Long userId, ChatRoom chatRoom) {
        // 전문가라면 전문가 접속 시간 뱐걍
        if(chatRoom.getExpert().getUser().getId().equals(userId))
            chatRoom.setExpertLastEnter(LocalDateTime.now());
        else
            chatRoom.setFarmerLastEnter(LocalDateTime.now());
    }
}
