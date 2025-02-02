package com.backend.farmon.strategy.postType;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.PostRepository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

// Q&A, 전문가 칼럼 게시글 3개씩 조회
@Slf4j
@RequiredArgsConstructor
@Component
public class CategoryPostFetchStrategy implements PostFetchStrategy{
    private final PostRepository postRepository;
    @Override
    public List<Post> fetchPosts(PostType postType) {
        log.info("홈 화면 {} 게시글 조회", postType.name());
        return postRepository.findTop3PostsByPostTYpe(postType);
    }
}
