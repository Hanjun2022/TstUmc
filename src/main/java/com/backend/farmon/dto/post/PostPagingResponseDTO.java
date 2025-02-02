package com.backend.farmon.dto.post;

import com.backend.farmon.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "각 게시판 별 페이징")
public class PostPagingResponseDTO {

    private Long id;
    private String postTitle;
    private String postContent;
    private String category;
    private int postlike;
    private int postcomment;


    private List<String> imgUrls;


    @Builder
    public PostPagingResponseDTO(Post post, List<String> imgUrls) {
        this.id = post.getId();
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        this.category = String.valueOf(post.getFieldCategory());
        this.postlike = post.getLikeCount();
        this.postcomment = post.getComments().size();
        this.imgUrls = imgUrls;
    }



}
