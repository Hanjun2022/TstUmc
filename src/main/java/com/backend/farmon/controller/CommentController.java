package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import com.backend.farmon.service.CommentService.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API", description = "댓글 및 대댓글 관리 API")
@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 저장", description = "사용자가 부모 댓글을 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_TYPE4002", description = "댓글을 저장할 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    public ApiResponse saveComment(
            @Parameter(description = "게시물 ID", required = true) @PathVariable Long postId,
            @RequestBody @Valid CommentRequestDTO.CommentSaveParentRequestDto request) {
        String resultCode;
       // String resultMsg;

        try{
            commentService.saveParentComment(postId, request);
            resultCode = SuccessStatus._OK.getCode(); // 성공 상태 코드 가져오기
          //  resultMsg = SuccessStatus._OK.getMessage(); // 성공 메시지 가져오기
            return ApiResponse.onSuccess(resultCode);

        }catch (Exception e){
            resultCode= ErrorStatus.COMMENT_NOT_SAVED.getCode();
           // resultMsg = ErrorStatus.COMMENT_NOT_SAVED.getMessage();
            return ApiResponse.onSuccess(resultCode);
        }

    }




    @Operation(summary = "대댓글 저장", description = "사용자가 대댓글을 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_TYPE4002", description = "댓글을 저장할 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/{commentId}/replies")
    public ApiResponse saveChildComment(
            @Parameter(description = "게시물 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "부모 댓글 ID", required = true) @PathVariable Long parentId,
            @RequestBody @Valid CommentRequestDTO.CommentSaveChildRequestDto request) {

        String resultCode;
        // String resultMsg;

        try{
            commentService.saveChildComment(postId,parentId, request);
            resultCode = SuccessStatus._OK.getCode(); // 성공 상태 코드 가져오기
            //  resultMsg = SuccessStatus._OK.getMessage(); // 성공 메시지 가져오기
            return ApiResponse.onSuccess(resultCode);
        }catch (Exception e){
            resultCode= ErrorStatus.COMMENT_NOT_SAVED.getCode();
            // resultMsg = ErrorStatus.COMMENT_NOT_SAVED.getMessage();
            return ApiResponse.onSuccess(resultCode);
        }

    }


    @Operation(summary = "게시글의 모든 댓글 조회", description = "사용자가 게시글의 모든 댓글을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_TYPE4002", description = "댓글을 저장할 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping
    public ApiResponse<CommentResponseDTO.CommentResponseDto> getAllComments(@PathVariable Long postId) {

        // 댓글 조회 서비스 호출
        CommentResponseDTO.CommentResponseDto response = commentService.getAllCommentsByPost(postId);

        return ApiResponse.onSuccess(response);

    }



    @Operation(summary = "게시글의 댓글 삭제", description = "부모 댓글 삭제시 밑에 모두 삭제 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_TYPE4003", description = "댓글을 지울 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping("/{commentId}")
    public ApiResponse<CommentResponseDTO.CommentDeleteDTO> deleteComment(
            @Parameter(description = "게시물 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "부모 댓글 ID", required = true) @PathVariable Long parentId) {

        CommentResponseDTO.CommentDeleteDTO comment=commentService.deleteComment(postId,parentId);

        return ApiResponse.onSuccess(comment);
    }


    @Operation(summary = "대댓글 삭제", description = "사용자가 대댓글을 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMENT_TYPE4003", description = "대댓글을 지울 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping("/{commentId}/replies/{childCommentId}")
    public ApiResponse<CommentResponseDTO.CommentDeleteDTO>deleteChildComment(
            @Parameter(description = "게시물 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "부모 댓글 ID", required = true) @PathVariable Long parentId,
            @Parameter(description = "대댓글 ID", required = true) @PathVariable Long childCommentId) {

        CommentResponseDTO.CommentDeleteDTO comment= commentService.deleteChildComment(postId,parentId,childCommentId);

        return ApiResponse.onSuccess(comment);
    }
}