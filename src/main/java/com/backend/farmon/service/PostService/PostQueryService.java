package com.backend.farmon.service.PostService;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostRequestDTO;
import com.backend.farmon.dto.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostQueryService {

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    HomeResponse.PostListDTO findHomePostsByCategory(PostType category);

    // 인기 전문가 칼럼 6개 조회
    HomeResponse.PopularPostListDTO findPopularExpertColumnPosts();



}
