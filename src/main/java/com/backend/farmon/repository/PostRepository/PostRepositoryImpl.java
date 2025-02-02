package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.QBoard;
import com.backend.farmon.domain.QLikeCount;
import com.backend.farmon.domain.QPost;
import com.backend.farmon.dto.post.PostType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QPost post = QPost.post;
    QLikeCount likeCount = QLikeCount.likeCount;
    QBoard board = QBoard.board;

    // 커뮤니티 전체 게시글 3개 조회
    @Override
    public List<Post> findTop3Posts() {
        return queryFactory.selectFrom(post)
                .orderBy(post.createdAt.desc())
                .limit(3)
                .fetch();
    }

    // 커뮤니티 인기 게시글 3개 조회
    @Override
    public List<Post> findTop3PostsByLikes() {
        return queryFactory.selectFrom(post)
                .leftJoin(post.postlikes, likeCount).fetchJoin()
                .groupBy(post)
                .orderBy(likeCount.count().desc(), post.createdAt.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<Post> findTop3PostsByPostTYpe(PostType postType) {
        return queryFactory.select(post)
                .from(post)
                .join(post.board, board).fetchJoin() // Post와 Board를 조인
                .leftJoin(post.postlikes, likeCount) // Post와 LikeCount를 조인
                .where(board.postType.eq(postType)) // PostType으로 필터링
                .groupBy(post) // Post별로 그룹화
                .orderBy(
                        likeCount.count().desc(), // 좋아요 개수로 정렬
                        post.createdAt.desc() // 최신순 정렬
                )
                .limit(3) // 3개 제한
                .fetch();
    }

    // 인기 전문가 칼럼 6개 조회
    @Override
    public List<Post> findTop6ExpertColumnPostsByPostId(List<Long> popularPostsIdList) {
        return queryFactory.select(post)
                .from(post)
                .join(post.board, board).fetchJoin()
                .leftJoin(post.postlikes, likeCount)
                .where(
                        board.postType.eq(PostType.EXPERT_COLUMN) // 전문가 칼럼 조건
                                .and(
                                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                                ? post.id.in(popularPostsIdList).or(post.id.notIn(popularPostsIdList))
                                                : null
                                )
                )
                .groupBy(post)
                .orderBy(
                        // 인기 게시글 우선 정렬
                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                ? Expressions.stringTemplate("CASE WHEN {0} IN ({1}) THEN 1 ELSE 2 END", post.id, Expressions.constant(popularPostsIdList)).asc()
                                : null,
                        // popularPostsIdList 내부 정렬
                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                ? Expressions.stringTemplate("FIELD({0}, {1})", post.id, Expressions.constant(popularPostsIdList)).asc()
                                : null,
                        likeCount.count().desc(), // 좋아요 개수 내림차순
                        post.createdAt.desc() // 작성일 내림차순
                )
                .limit(6) // 6개 제한
                .fetch();
    }

    @Override
    public Page<Post> findPopularPosts(Long boardId, Pageable pageable) {
        QPost post = QPost.post;

        // QueryDSL을 사용하여 게시판별 인기 게시글 조회 (좋아요 순 정렬)
        List<Post> posts = queryFactory
                .selectFrom(post)
                .where(post.board.id.eq(boardId)) // boardId 기준 필터링
                .orderBy(post.postLikes.desc()) // 좋아요 수 기준 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 게시글 수 조회
        long total = queryFactory
                .selectFrom(post)
                .where(post.board.id.eq(boardId)) // boardId 기준 필터링
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }

}
