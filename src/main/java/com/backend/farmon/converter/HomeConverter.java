package com.backend.farmon.converter;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.home.HomeResponse;

import java.util.List;

public class HomeConverter {
    public static HomeResponse.PostListDTO toPostListDTO(List<Post> postList, List<Integer> likeCountList, List<Integer> commentCountList) {
        // 각 Post와 대응하는 likeCount와 commentCount를 매핑하여 DTO 리스트 생성
        List<HomeResponse.PostDetailDTO> postDetailDTOList =
                postList.stream()
                        .map(post -> {
                            int index = postList.indexOf(post);
                            return toPostDetailDTO(post, likeCountList.get(index), commentCountList.get(index));
                        })
                        .toList();

        return new HomeResponse.PostListDTO(postDetailDTOList);
    }

    public static HomeResponse.PostDetailDTO toPostDetailDTO(Post post, Integer likeCount, Integer commentCount){
        return HomeResponse.PostDetailDTO.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

    public static HomeResponse.PopularPostListDTO toPopularPostListDTO(List<Post> postList) {
        List<HomeResponse.PopularPostDetailDTO> popularPostDetailDTOList = postList.stream()
                .map(HomeConverter::toPopularPostDetailListDTO)
                .toList();

        return HomeResponse.PopularPostListDTO.builder()
                .popularPostList(popularPostDetailDTOList)
                .build();
    }

    public static HomeResponse.PopularPostDetailDTO toPopularPostDetailListDTO(Post post) {
        return HomeResponse.PopularPostDetailDTO.builder()
                .popularPostId(post.getId())
                .popularPostTitle(post.getPostTitle())
                .popularPostContent(post.getPostContent())
                .writer(post.getUser().getUserName())
                .profileImage(post.getUser().getExpert().getProfileImageUrl())
                // 게시글 이미지 가져오기
                .popularPostImage(
                        (post.getPostImgs() != null && !post.getPostImgs().isEmpty())
                                ? post.getPostImgs().get(0).getStoredFileName()
                                : null
                )
                .build();
    }

    public static HomeResponse.AutoCompleteSearchPostDTO toAutoCompleteSearchPostDTO(){
        return HomeResponse.AutoCompleteSearchPostDTO.builder()
                .isSearchSave(true)
                .build();
    }

    public static HomeResponse.SearchDeleteDTO toSearchDeleteDTO(){
        return HomeResponse.SearchDeleteDTO.builder()
                .isSearchDelete(true)
                .build();
    }
}
