package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    // 커뮤니티 전체 게시글 3개 조회
    public List<Post> findTop3Posts();

    // 커뮤니티 인기 게시글 3개 조회
    public List<Post> findTop3PostsByLikes();

    // 커뮤니티 카테고리별 게시글 3개 조회
    public List<Post> findTop3PostsByPostTYpe(PostType postType);

    // 인기 전문가 칼럼 6개 조회
    public List<Post> findTop6ExpertColumnPostsByPostId(List<Long> popularPostsIdList);

    // 인기 게시판 목록 조회
    Page<Post> findPopularPosts(Long boardId, Pageable pageable);
}
