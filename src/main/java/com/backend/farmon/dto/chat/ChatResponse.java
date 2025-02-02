package com.backend.farmon.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class ChatResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 목록 정보")
    public static class ChatRoomListDTO { 
        @Schema(description = "채팅 대화방 세부 정보 목록")
        List <ChatRoomDetailDTO> chatRoomInfoList;

        @Schema(description = "현재 페이지의 채팅 대화방 목록 개수", example = "10")
        Integer chatRoomInfoListSize;

        @Schema(description = "총 페이지 수", example = "8")
        Integer totalPage;

        @Schema(description = "총 채팅 대화 내역 개수", example = "80")
        Long totalElements;

        @Schema(description = "페이지 처음 여부", example = "true")
        Boolean isFirst;

        @Schema(description = "페이지 마지막 여부", example = "false")
        Boolean isLast;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅 대화방 세부 정보")
    public static class ChatRoomDetailDTO {
        @Schema(description = "채팅방 아이디", example = "1")
        Long chatRoomId;

        @Schema(description = "채팅 중인 상대방 이름", example = "김팜온")
        String name;

        @Schema(description = "채팅 중인 상대방 닉네임 (채팅 상대가 전문가일 경우에만 해당) / 채팅 상대가 농업인이면 null", example = "병해충전문가")
        String nickName;

        @Schema(description = "채팅 중인 상대가 전문가일 경우, 닉네임만 보이게 활성화 여부 / 채팅 상대가 농업인이면 null", example = "true")
        Boolean isExpertNickNameOnly;

        @Schema(description = "채팅 상대 역할, 농업인 또는 전문가", example = "농업인")
        String type;

        @Schema(description = "채팅 중인 상대방 프로필 이미지")
        String profileImage;

        @Schema(description = "신청 견적 예산", example = "500만원 ~ 1,000만원")
        String estimateBudget;

        @Schema(description = "신청 견적 종류", example = "스마트팜")
        String estimateCategory;

        @Schema(description = "신청 견적 주소", example = "경기")
        String estimateAreaName;

        @Schema(description = "신청 견적 주소 디테일", example = "이천시")
        String estimateAreaDetail;

        @Schema(description = "안 읽은 채팅 개수", example = "3")
        Integer unreadMessageCount;

        @Schema(description = "마지막 채팅 내용", example = "추가로 궁금한 점이 생기면 언제든 문의하세요.")
        String lastMessageContent;

        @Schema(description = "마지막 채팅 날짜", example = "2025.01.10")
        String lastMessageDate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅 대화방의 세부 정보 및 메시지 정보")
    public static class ChatMessageListDTO { 
        @Schema(description = "채팅 대화방의 메시지 목록")
        List <ChatMessageDetailDTO> chatMesageList;

        @Schema(description = "현재 페이지의 채팅 대화 내역 개수", example = "20")
        Integer chatMessageListSize;

        @Schema(description = "페이지 처음 여부", example = "true")
        Boolean isFirst;

        @Schema(description = "페이지 마지막 여부", example = "false")
        Boolean isLast;

        @Schema(description = "다음 페이지가 있는지 여부", example = "true")
        Boolean hasNext;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅 대화방의 메시지 세부 정보, 채팅방 조회시 List로 전달")
    public static class ChatMessageDetailDTO {
        @Schema(description = "메시지 내용", example = "안녕하세요.")
        String messageContent;

        @Schema(description = "상대방이 메시지 읽음 여부", example = "false")
        Boolean isOtherRead;

        @Schema(description = "내가 보낸 메시지인지 상대방이 보낸 메시지인지 여부 / 내가 보낸 메시지라면 true, 상대방이 보낸 메시지라면 false", example = "true")
        Boolean isMine;

        @Schema(description = "메시지 전송 시간", example = "오후 5:11")
        String sendTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 생성 시(전문가가 농업인이 올린 견적을 보고 채팅 신청 시) 응답 정보")
    public static class ChatRoomCreateDTO {
        @Schema(description = "생성된 채팅방 아이디", example = "1")
        Long chatRoomId;

        @Schema(description = "채팅 중인 상대방 이름", example = "김팜온")
        String name;

        @Schema(description = "채팅 상대 역할, 채팅방 생성은 전문가만 가능하므로 항상 농업인 반환", example = "농업인")
        String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 정보")
    public static class ChatRoomDataDTO {

        @Schema(description = "채팅 중인 상대방 이름", example = "김팜온")
        String name;

        @Schema(description = "채팅 중인 상대방 닉네임 (채팅 상대가 전문가일 경우에만 해당)", example = "병해충전문가")
        String nickName;

        @Schema(description = "채팅 중인 상대가 전문가일 경우, 닉네임만 보이게 활성화 여부", example = "true")
        Boolean isExpertNickNameOnly;

        @Schema(description = "채팅 중인 상대방 프로필 이미지")
        String profileImage;

        @Schema(description = "채팅 상대 역할, 농업인 또는 전문가", example = "농업인")
        String type;

        @Schema(description = "채팅 상대의 마지막 채팅방 접속 시간", example = "28분")
        String lastEnterTime;

        @Schema(description = "채팅 상대의 평균 메시지 응답 시간, 정보가 없는 사용자이면 null", example = "1시간")
        String averageResponseTime;

        @Schema(description = "채팅 상대의 컨설팅 완료 여부", example = "true")
        Boolean isOtherComplete;

        @Schema(description = "사용자의 컨설팅 완료 여부", example = "true")
        Boolean isComplete;

        @Schema(description = "견적 완료 여부, 농업인 전문가 모두 컨설팅 완료 시 true", example = "true")
        Boolean isEstimateComplete;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 삭제 응답 정보")
    public static class ChatRoomDeleteDTO {
        @Schema(description = "삭제 여부 / 삭제에 성공했다면 true, 실패했다면 false", example = "true")
        Boolean isDeleteSuccess;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 견적 응답 정보")
    public static class ChatRoomEstimateDTO {
        @Schema(description = "견적 작물 카테고리", example = "곡물, 채소작물, 과일 등")
        String estimateCropCategory;

        @Schema(description = "견적 작물 이름", example = "쌀, 고구마, 사과 등")
        String estimateCropName;

        @Schema(description = "견적 신청자")
        String estimateApplyName;

        @Schema(description = "견적 카테고리", example = "작물 관리, 스마트팜 등")
        String estimateCategory;

        @Schema(description = "신청 견적 주소", example = "경기")
        String estimateAreaName;

        @Schema(description = "신청 견적 주소 디테일", example = "이천시")
        String estimateAreaDetail;

        @Schema(description = "견적 예산", example = "50만원 ~ 100만원")
        String estimateBudget;

        @Schema(description = "컨설팅 내용")
        String estimateContent;

        @Schema(description = "견적 이미지 리스트")
        List<String> estimateImageList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "채팅방 컨설팅 완료 정보")
    public static class ChatRoomCompleteDTO {
        @Schema(description = "사용자의 컨설팅 완료 여부", example = "true")
        Boolean isComplete;

        @Schema(description = "채팅 상대의 컨설팅 완료 여부", example = "true")
        Boolean isOtherComplete;

        @Schema(description = "견적 완료 여부, 농업인 전문가 모두 컨설팅 완료 시 true", example = "true")
        Boolean isEstimateComplete;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "수신할 채팅 메시지 정보")
    public static class ChatMessageReceiveDTO {

        @Schema(description = "보낸 사람 아이디, 현재 로그인한 사용자의 userId와 동일", example = "1")
        Long senderId;

        @Schema(description = "보낸 사람 타입", example = "농업인")
        String senderType;

        @Schema(description = "메시지 내용, 이미지일 경우 클라이언트에서 이미지 파일을 Base64로 인코딩하여 전송 필요", example = "안녕하세요. 견적 신청하셨나요?")
        String messageContent;

        @Schema(description = "메시지 타입, " +
                "텍스트 메시지 전송이라면 TEXT, 이미지 전송이라면 IMAGE, 컨설팅 완료라면 COMPLETE, 채팅방 퇴장이라면 EXIT", example = "TEXT")
        String messageType;

        @Schema(description = "보낸 시간", example = "2025-01-10")
        String sendTime;

        @Schema(description = "내가 보낸 메시지인지 여부, 내가 보낸 메시지라면 true, 받은 메시지라면 false", example = "true")
        Boolean isMine;

        @Schema(description = "상대방이 메시지 읽음 여부", example = "false")
        Boolean isOtherRead;

        @Schema(description = "상대방이 채팅방에 접속해 있는지 여부", example = "false")
        Boolean isOtherEnter;
    }
}
