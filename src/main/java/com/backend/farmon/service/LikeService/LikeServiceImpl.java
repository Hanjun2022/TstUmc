package com.backend.farmon.service.LikeService;


import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.LikeCount;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeCountRepository likeCountRepository;



    // 좋아요 추가
    @Transactional
    public void postLikeUp(Long userId, Long postId) throws IllegalAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (likeCountRepository.findByUserIdAndPostId(userId, postId) !=null) {
            throw new IllegalAccessException("이미 좋아요를 눌렀습니다!");
        }

        LikeCount likeCount = LikeCount.builder()
                .user(user)
                .post(post)
                .build();
        likeCountRepository.save(likeCount);

        post.increaseLikes();
        // 좋아요를 일단 Post엔티티에 구현하여 하나를 더해줌
        postRepository.save(post);
        postRepository.flush();
    }

    // 좋아요 감소
    @Transactional
    public void postLikeDown(Long userId, Long postId) throws IllegalAccessException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        LikeCount like = likeCountRepository.findByUserIdAndPostId(userId, postId);
        likeCountRepository.delete(like);
        post.decreaseLikes();
        postRepository.save(post);
        postRepository.flush();
        // 좋아요를 일단 Post엔티티에 구현하여 하나를 더해줌
    }
    @Transactional
    public int getLikeCount(Long postId) {
        return postRepository.getLikeCount(postId);
    }


}
