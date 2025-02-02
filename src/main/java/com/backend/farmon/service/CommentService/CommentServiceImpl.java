package com.backend.farmon.service.CommentService;

import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.backend.farmon.converter.CommentConverter.toChildCommentEntity;
import static com.backend.farmon.converter.CommentConverter.toParentSaveEntity;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;



    @Override
    @Transactional
    public void saveParentComment(Long postId, CommentRequestDTO.CommentSaveParentRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 확인되지 않습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Comment comment = commentRepository.save(toParentSaveEntity(dto, user, post));
    }

    @Override
    @Transactional
    public void saveChildComment(Long postId, Long parentId, CommentRequestDTO.CommentSaveChildRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 확인되지 않습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다."));

        Comment childComment = toChildCommentEntity(dto, user, post, parentComment);

        commentRepository.save(childComment);
    }

    // 댓글 삭제시 댓글 삭제 상태로 전환
    @Override
    @Transactional
    public CommentResponseDTO.CommentDeleteDTO deleteComment(Long postId, Long parentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 확인되지 않습니다."));

        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글이 확인되지 않습니다."));

        // 부모 댓글을 삭제된 상태로 설정 (isDeleted 값을 true로 설정)
        parentComment.setIsDeleted(true);
        commentRepository.save(parentComment);  // 업데이트된 댓글을 저장

        return CommentResponseDTO.CommentDeleteDTO.builder()
                .isDeleteSuccess(true)
                .build();
    }

    // 대댓글 삭제
    @Override
    @Transactional
    public CommentResponseDTO.CommentDeleteDTO deleteChildComment(Long postId, Long parentId, Long childId) {
        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 확인되지 않습니다."));

        // 부모 댓글 조회
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("부모 댓글이 확인되지 않습니다."));

        // 대댓글 조회
        Comment childComment = commentRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("대댓글이 확인되지 않습니다."));

        // 부모 댓글이 대댓글을 가지고 있는지 확인
        if (parentComment.getChildren().contains(childComment)) {
            // 대댓글을 삭제된 상태로 설정 (isDeleted 값을 true로 설정)
            childComment.setIsDeleted(true);
            commentRepository.save(childComment);  // 대댓글 업데이트

            // 대댓글 삭제 후 Post의 댓글 리스트에서 제거 (Post의 상태 갱신)
            post.getComments().remove(childComment); // 부모 댓글이 달린 대댓글을 포스트의 댓글 리스트에서 제거

            // Post를 업데이트하여 댓글 상태 갱신
            postRepository.save(post);
        } else {
            throw new RuntimeException("대댓글이 부모 댓글에 포함되지 않습니다.");
        }

        // 대댓글 삭제가 성공적으로 처리되었음을 반환
        return CommentResponseDTO.CommentDeleteDTO.builder()
                .isDeleteSuccess(true)
                .build();
    }

    // 부모 댓글과 대댓글 모두 조회
    @Override
    @Transactional(readOnly = true)
    public CommentResponseDTO.CommentResponseDto getAllCommentsByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 확인되지 않습니다."));

        // 부모 댓글 조회
        List<Comment> parentComments = commentRepository.findParentCommentsByPostId(postId);

        List<CommentResponseDTO.CommentParentResponseDto> parentCommentDtos = parentComments.stream()
                .map(parent -> {
                    List<CommentResponseDTO.CommentChildResponseDto> childCommentDtos = parent.getChildren().stream()
                            .map(child -> CommentResponseDTO.CommentChildResponseDto.builder()
                                    .commentId(child.getId())
                                    .commentContent(child.getCommentContent())
                                    .userId(child.getUser().getId())
                                    .parentId(parent.getId())
                                    .build())
                            .toList();

                    return CommentResponseDTO.CommentParentResponseDto.builder()
                            .commentId(parent.getId())
                            .commentContent(parent.getCommentContent())
                            .userId(parent.getUser().getId())
                            .expertCategory(String.valueOf(parent.getUser().getExpert().getCrop()))
                            .childComments(childCommentDtos)
                            .build();
                })
                .toList();

        return CommentResponseDTO.CommentResponseDto.builder()
                .parentComments(parentCommentDtos)
                .hasNext(false) // 페이징 적용 시 수정 필요
                .build();
    }
}
