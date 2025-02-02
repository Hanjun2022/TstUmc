package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findAllByBoardId(Long boardId, Pageable pageable);
    Page<Post> findAllByUserId(Long userId, Pageable pageable);
    List<Post> findAllByUserId(Long userId);
    List<Post> findByUserIdIn(List<Long> userIds);

    @Query("SELECT COUNT(l) FROM LikeCount l WHERE l.post.id = :postId")
    int getLikeCount(@Param("postId") Long postId);


    // 게시판 ID와 필터링된 FieldCategory ID로 게시글 조회
    Page<Post> findAllByBoardIdAndFieldCategoryIds(Long boardId, List<Long> selectedFieldCategoryIds, Pageable pageable);

    Page<Post> findPopularPostsByBoardIdAndFieldCategoryIds(Long boardId, List<Long> ids, Pageable pageable);

}
