package com.backend.farmon.repository.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.QChatMessage;
import com.backend.farmon.domain.enums.ChatMessageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QChatMessage chatMessage = QChatMessage.chatMessage;

    // 채팅방 아이디와 일치하는 퇴장, 거래 완료가 아닌 채팅 메시지 무한스크롤 조회
    @Override
    public Slice<ChatMessage> findNonExitCompleteMessagesByChatRoomId(Long chatRoomId, Pageable pageable) {
        // 요청된 페이지 크기보다 1개 더 가져오기
        List<ChatMessage> content = queryFactory.selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.type.in(ChatMessageType.TEXT, ChatMessageType.IMAGE)
                )
                .orderBy(chatMessage.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 추가 데이터 1개 로드
                .fetch();

        // Slice 객체 반환
        boolean hasNext = content.size() > pageable.getPageSize();
        if (hasNext) {
            content.remove(content.size() - 1); // 추가로 가져온 데이터 제거
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    // 채팅방 아이디와 일치하는 채팅 메시지 중, 상대방이 보낸 메시지 중 TEXT, IMAGE 메시지를 읽음 처리
    @Transactional
    @Override
    public void updateMessagesToReadByChatRoomId(Long chatRoomId, Long userId) {
        queryFactory.update(chatMessage)
                .set(chatMessage.isRead, true)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.senderId.notIn(userId),
                        chatMessage.isRead.isFalse(),
                        chatMessage.type.in(ChatMessageType.TEXT, ChatMessageType.IMAGE)
                )
                .execute();
    }
}
