package com.backend.farmon.dto.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class HomeResponse {
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 게시글 리스트 정보")
    public static class PostListDTO {
        @Schema(description = "커뮤니티 카테고리에 따른 게시글 리스트")
        List <PostDetailDTO> postList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 커뮤니티 게시글 상세 정보")
    public static class PostDetailDTO {
        
        @Schema(description = "커뮤니티 게시글 아이디", example = "1")
        Long postId;

        @Schema(description = "커뮤니티 게시글 제목")
        String postTitle;

        @Schema(description = "커뮤니티 게시글 내용")
        String postContent;

        @Schema(description = "게시글 좋아요 횟수", example = "5")
        Integer likeCount;

        @Schema(description = "게시글 댓글 횟수", example = "2")
        Integer commentCount;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 인기 칼럼 리스트 정보")
    public static class PopularPostListDTO {
        @Schema(description = "인기 칼럼 리스트")
        List <PopularPostDetailDTO> popularPostList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 인기 칼럼 정보")
    public static class PopularPostDetailDTO {

        @Schema(description = "인기 칼럼 게시글 아이디", example = "1")
        Long popularPostId;

        @Schema(description = "인기 칼럼 제목")
        String popularPostTitle;

        @Schema(description = "인기 칼럼 내용")
        String popularPostContent;

        @Schema(description = "작성자 이름")
        String writer;

        @Schema(description = "작성자 프로필 이미지")
        String profileImage;

        @Schema(description = "인기 칼럼 썸네일 이미지")
        String popularPostImage;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 최근 검색어 정보")
    public static class RecentSearchListDTO {

        @Schema(description = "최근 검색어 리스트")
        List <String> recentSearchList;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 추천 검색어 정보")
    public static class RecommendSearchListDTO {

        @Schema(description = "추천 검색어 리스트")
        List <String> recommendSearchList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "검색어 삭제 시 반환 정보")
    public static class SearchDeleteDTO {

        @Schema(description = "검색어 삭제 성공 여부", example = "true")
        Boolean isSearchDelete;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 자동 완성 검색어 정보")
    public static class AutoCompleteSearchDTO  {

        @Schema(description = "자동 완성 검색어 리스트")
        List <String> searchList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 자동 완성 검색어 저장 반환 정보")
    public static class AutoCompleteSearchPostDTO {

        @Schema(description = "검색어 저장 여부", example = "true")
        Boolean isSearchSave;
    }
}
