package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.like.LikeRequestDTO;
import com.backend.farmon.dto.like.LikeResponseDTO;
import com.backend.farmon.service.LikeService.LikeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "좋아요 페이지", description = "좋아요 기능에 관한 API")
@RestController
@RequestMapping("/api/posts/{boardId}/list/{postId}/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeServiceImpl likeService;

    /**
     * 좋아요 추가
     */
    @Operation(summary = "좋아요 추가", description = "사용자가 특정 게시글에 좋아요를 추가합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_TYPE4001", description = "좋아요를 누를 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping
    public ApiResponse<LikeResponseDTO> addLike(
            @PathVariable("boardId") Long boardId,
            @RequestBody LikeRequestDTO likeRequestDTO) throws IllegalAccessException {

        Long userId = likeRequestDTO.getUserId();
        Long postId = likeRequestDTO.getPostId();

        likeService.postLikeUp(userId, postId);

        int  likeCount = likeService.getLikeCount(postId);
        LikeResponseDTO response = LikeResponseDTO.builder()
                .postId(postId)
                .userId(userId)
                .likeCount(likeCount)
                .build();

        return ApiResponse.onSuccess(response);
    }

    /**
     * 좋아요 삭제
     */
    @Operation(summary = "좋아요 삭제", description = "사용자가 특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "LIKE_TYPE4001", description = "좋아요를 누를 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping
    public ApiResponse<Integer> removeLike(
            @PathVariable("boardId")  Long boardId,
            @RequestBody LikeRequestDTO likeRequestDTO) throws IllegalAccessException {

        Long userId = likeRequestDTO.getUserId();
        Long postId = likeRequestDTO.getPostId();

        likeService.postLikeDown(userId, postId);

        int updatedLikeCount = likeService.getLikeCount(postId);
        return ApiResponse.onSuccess(updatedLikeCount);
    }
}
