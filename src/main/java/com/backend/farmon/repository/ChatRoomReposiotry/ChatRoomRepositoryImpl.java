package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.QChatMessage;
import com.backend.farmon.domain.QChatRoom;
import com.backend.farmon.domain.enums.ChatMessageType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatMessage chatMessage = QChatMessage.chatMessage;

    @Override
    public Page<ChatRoom> findChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // userId와 연관된 채팅방 조회
        builder.and(chatRoom.farmer.id.eq(userId).or(chatRoom.expert.user.id.eq(userId)));

        // role에 따른 조건 추가
        if ("FARMER".equalsIgnoreCase(role)) {
            builder.and(chatRoom.farmer.id.eq(userId));
        } else if ("EXPERT".equalsIgnoreCase(role)) {
            builder.and(chatRoom.expert.user.id.eq(userId));
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        // 텍스트, 이미지 메시지만 조회
        builder.and(chatMessage.type.in(ChatMessageType.TEXT, ChatMessageType.IMAGE));

        // 검색어 조건 (여섯 가지 중 하나라도 포함되면 조회)
        if (searchName != null && !searchName.trim().isEmpty()) {
            BooleanBuilder searchCondition = new BooleanBuilder();
            searchCondition.or(chatRoom.farmer.userName.containsIgnoreCase(searchName)); // 농업인 사용자명 검색
            searchCondition.or(chatRoom.expert.nickName.containsIgnoreCase(searchName)); // 전문가 닉네임 검색
            searchCondition.or(chatRoom.expert.isNickNameOnly.eq(false).and(chatRoom.expert.user.userName.containsIgnoreCase(searchName))); // 전문가가 닉네임만 표시하지 않을 경우, 원래 사용자명도 검색
            searchCondition.or(chatRoom.estimate.crop.category.containsIgnoreCase(searchName)); // 작물 카테고리로 검색
            searchCondition.or(chatRoom.estimate.crop.name.containsIgnoreCase(searchName)); // 작물 디테일 이름으로 검색
            searchCondition.or(chatRoom.estimate.category.containsIgnoreCase(searchName)); // 견적 분야로 검색

            builder.and(searchCondition); // 전체 조건에 추가
        }

        // 데이터 조회 쿼리
        QueryResults<ChatRoom> results = queryFactory
                .selectFrom(chatRoom)
                .join(chatMessage).on(chatMessage.chatRoom.id.eq(chatRoom.id))
                .where(builder)
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        // Page 객체 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // userId와 연관된 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    @Override
    public Page<ChatRoom> findUnReadChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // role에 따른 조건 추가
        if ("FARMER".equalsIgnoreCase(role)) {
            builder.and(chatRoom.farmer.id.eq(userId));
        } else if ("EXPERT".equalsIgnoreCase(role)) {
            builder.and(chatRoom.expert.user.id.eq(userId));
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        // 안 읽은 텍스트, 이미지 메시지만 조회
        builder.and(chatMessage.isRead.isFalse());
        builder.and(chatMessage.type.in(ChatMessageType.TEXT, ChatMessageType.IMAGE));

        // 검색어 조건 (여섯 가지 중 하나라도 포함되면 조회)
        if (searchName != null && !searchName.trim().isEmpty()) {
            BooleanBuilder searchCondition = new BooleanBuilder();
            searchCondition.or(chatRoom.farmer.userName.containsIgnoreCase(searchName)); // 농업인 사용자명 검색
            searchCondition.or(chatRoom.expert.nickName.containsIgnoreCase(searchName)); // 전문가 닉네임 검색
            searchCondition.or(chatRoom.expert.isNickNameOnly.eq(false).and(chatRoom.expert.user.userName.containsIgnoreCase(searchName))); // 전문가가 닉네임만 표시하지 않을 경우, 원래 사용자명도 검색
            searchCondition.or(chatRoom.estimate.crop.category.containsIgnoreCase(searchName)); // 작물 카테고리로 검색
            searchCondition.or(chatRoom.estimate.crop.name.containsIgnoreCase(searchName)); // 작물 디테일 이름으로 검색
            searchCondition.or(chatRoom.estimate.category.containsIgnoreCase(searchName)); // 견적 분야로 검색

            builder.and(searchCondition); // 전체 조건에 추가
        }

        // 데이터 조회 쿼리
        QueryResults<ChatRoom> results = queryFactory
                .selectFrom(chatRoom)
                .join(chatMessage).on(chatMessage.chatRoom.id.eq(chatRoom.id))
                .where(builder)
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        // Page 객체 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
