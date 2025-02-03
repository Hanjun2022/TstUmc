package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.FieldCategoryEntity;
import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.Filter.FieldCategory; // FieldCategory Enum import
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findAllByBoardId(Long boardId, Pageable pageable);

    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    List<Post> findAllByUserId(Long userId);

    List<Post> findByUserIdIn(List<Long> userIds);

    @Query("SELECT COUNT(l) FROM LikeCount l WHERE l.post.id = :postId")
    int getLikeCount(@Param("postId") Long postId);


    // 게시판 ID와 FieldCategory 목록으로 게시글 조회 (수정됨)
    @Query("SELECT p FROM Post p " +
            "WHERE p.board.id = :boardId " +
            "AND p.fieldCategory IN :fieldCategoryIds")
    Page<Post> findAllByBoardIdAndFieldCategoryIds(
            @Param("boardId") Long boardId,
            @Param("fieldCategoryIds") List<FieldCategory> fieldCategoryIds, // FieldCategory Enum 리스트
            Pageable pageable);

    // FieldCategoryEntity 리스트를 인자로 받는 새로운 메서드 추가
    @Query("SELECT p FROM Post p " +
            "WHERE p.board.id = :boardId " +
            "AND p.fieldCategory IN (SELECT fce.name FROM FieldCategoryEntity fce WHERE fce IN :fieldCategoryEntities)")
    Page<Post> findAllByBoardIdAndFieldCategoryEntities(
            @Param("boardId") Long boardId,
            @Param("fieldCategoryEntities") List<FieldCategoryEntity> fieldCategoryEntities,
            Pageable pageable);

    // 인기 게시글 조회 (수정됨)
    @Query("SELECT p FROM Post p " +
            "WHERE p.board.id = :boardId " +
            "AND p.fieldCategory IN :fieldCategoryIds " +
            "ORDER BY p.postLikes DESC") // 좋아요 수로 내림차순 정렬
    Page<Post> findPopularPostsByBoardIdAndFieldCategoryIds(
            @Param("boardId") Long boardId,
            @Param("fieldCategoryIds") List<FieldCategory> fieldCategoryIds, // FieldCategory Enum 리스트
            Pageable pageable);

    // 인기 게시글 조회 (FieldCategory 필터링 없이)
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId ORDER BY p.postLikes DESC")
    Page<Post> findPopularPosts(@Param("boardId") Long boardId, Pageable pageable);
}