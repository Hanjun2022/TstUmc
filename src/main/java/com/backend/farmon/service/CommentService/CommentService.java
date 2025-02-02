package com.backend.farmon.service.CommentService;

import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;

public interface CommentService  {


   void saveParentComment(Long postId, CommentRequestDTO.CommentSaveParentRequestDto dto);
    //댓글저장

    void saveChildComment(Long postId, Long parentId, CommentRequestDTO.CommentSaveChildRequestDto dto);
    //대댓글 저장


    CommentResponseDTO.CommentDeleteDTO deleteComment(Long postId, Long parentId);
    // 댓글 + 대댓글 삭제 (마지막 부모 댓글이 삭제되면, 부모댓글만 삭제처리 글자 뜸

    CommentResponseDTO.CommentDeleteDTO deleteChildComment(Long postId, Long parentId, Long childId);
    //대댓글 삭제

    CommentResponseDTO.CommentResponseDto getAllCommentsByPost(Long postId);
    //(댓글+대댓글) 모두 조회
}
