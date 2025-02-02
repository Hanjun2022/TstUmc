package com.backend.farmon.apiPayload.code.status;

import com.backend.farmon.apiPayload.code.BaseErrorCode;
import com.backend.farmon.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","해당 경로는 인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 유저 관려 에러
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "아이디와 일치하는 사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4002", "닉네임은 필수 입니다."),
    EXCHANGE_ROLE_SAME(HttpStatus.BAD_REQUEST, "USER4003", "전환하려는 역할과 현재 로그인한 역할이 일치합니다."),
    WITHDRAW_USER_LOGIN(HttpStatus.BAD_REQUEST, "USER4004", "회원탈퇴한 유저입니다."),

    // 전문가 관련 에러
    EXPERT_NOT_FOUND(HttpStatus.BAD_REQUEST, "EXPERT4001", "아이디와 일치하는 전문가가 없습니다."),
    EXPERT_NOT_REGISTER(HttpStatus.BAD_REQUEST, "EXPERT4002", "전문가로 등록되어 있지 않은 농업인 입니다."),
    EXPERT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "EXPERT4003", "이미 전문가로 등록된 농업인 입니다."),

    // 견적 관련 에러
    ESTIMATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "ESTIMATE4001", "견적 아이디와 일치하는 견적이 없습니다."),

    // 채팅방 관련 에러
    CHATROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHATROOM4001", "채팅방 아이디와 일치하는 채팅방이 없습니다."),

    // 페이지 번호
    PAGE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PAGE4001", "페이지 번호는 1 이상이어야 합니다."),

    // 회원가입 에러
    EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "PAGE4001", "이미 가입된 이메일주소입니다."),

    // 커뮤니티 게시판   
    POST_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_TYPE4001", "지원되지 않는 게시판 타입 입니다."),
    POST_NOT_SAVED(HttpStatus.BAD_REQUEST, "POST_TYPE4002", "게시글이 저장되지 않았습니다."),
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "POST_TYPE4003", "게시글을 찾을 수 없습니다."),

    //댓글
    COMMENT_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "COMMENT_TYPE4001", "댓글을 찾을 수 없습니다."),
    COMMENT_NOT_SAVED(HttpStatus.BAD_REQUEST, "COMMENT_TYPE4002", "댓글(대댓글)이 저장되지 않았습니다."),
    COMMENT_NOT_DELETED(HttpStatus.BAD_REQUEST, "COMMENT_TYPE4003", "댓글이 삭제되지 않았습니다."),

    //좋아요
    Like_TYPE_NOT_SAVED(HttpStatus.BAD_REQUEST, "LIKE_TYPE4001", "좋아요가 눌리지 않았습니다."),

    // 사용자 인증 정보
    AUTHORIZATION_NOT_EQUALS(HttpStatus.FORBIDDEN, "AUTHORIZATION4031", "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)"),

    // 작물 관련 에러
    CROP_NOT_FOUND(HttpStatus.BAD_REQUEST, "CROP4001", "해당 작물 카테고리가 존재하지 않습니다."),

    // 지역 관련 에러
    AREA_NOT_FOUND(HttpStatus.BAD_REQUEST, "AREA4001", "해당 위치 카테고리가 존재하지 않습니다."),

    // 전문가 경력 관련 에러
    EXPERT_CAREER_NOT_FOUND(HttpStatus.BAD_REQUEST, "EXPERT_CAREER4001", "해당 경력 아이디와 일치하는 경력이 존재하지 않습니다."),

    // 채팅 메시지 타입
    MESSAGE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "MESSAGE_TYPE4001", "지원되지 않는 채팅 메시지 타입입니다."),

    // 검색어 관련 에러
    SEARCH_NOT_EMPTY(HttpStatus.BAD_REQUEST, "SEARCH_4001", "검색어가 비어 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
