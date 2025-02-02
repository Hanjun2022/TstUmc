package com.backend.farmon.service.PostService;

import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.SubCategory;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostPagingResponseDTO;
import com.backend.farmon.dto.post.PostRequestDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.FilterRepository.SubCategoryRepository;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.service.AWS.S3Service;
import com.backend.farmon.strategy.postType.PostFetchStrategy;
import com.backend.farmon.strategy.postType.PostFetchStrategyFactory;
import jakarta.persistence.Temporal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostQueryServiceImpl implements PostQueryService {

    private final PostFetchStrategyFactory strategyFactory;
    private final CommentRepository commentRepository;
    private final LikeCountRepository likeCountRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final SubCategoryRepository fieldCatrogryRepository;
    private final S3Service s3Service;

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    // 인기, 전체, QNA, 전문가 칼럼
    @Override
    public HomeResponse.PostListDTO findHomePostsByCategory(PostType category) {

        // 카테고리별 게시글 조회
        PostFetchStrategy strategy = strategyFactory.getStrategy(category);
        List<Post> postList = strategy.fetchPosts(category);
        log.info("홈 화면 카테고리별 게시글 조회 성공");

        // 각 게시물의 좋아요 개수 조회
        List<Integer> likeCountList = postList.stream()
                .map(post -> likeCountRepository.countLikeCountsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 좋아요 개수 조회 성공");

        // 각 게시물의 댓글 개수 조회
        List<Integer> commentCountList = postList.stream()
                .map(post -> commentRepository.countCommentsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 댓글 개수 조회 성공");

        return HomeConverter.toPostListDTO(postList, likeCountList, commentCountList);
    }

    // 인기 전문가 칼럼 6개 조회
    @Override
    public HomeResponse.PopularPostListDTO findPopularExpertColumnPosts() {
        // 별도로 인기 칼럼으로 지정할 지정할 전문가 칼럼 게시글 아이디 리스트
        List<Long> popularPostsIdList = new ArrayList<>();
        popularPostsIdList.add(4L);

        // 인기 전문가 칼럼 6개 조회
        List<Post> expertColumnPostList = postRepository.findTop6ExpertColumnPostsByPostId(popularPostsIdList);
        log.info("홈 화면 인기 전문가 칼럼 조회 성공");

        return HomeConverter.toPopularPostListDTO(expertColumnPostList);
    }



    //전체 게시판 (인기 게시판 말고 전부로) 에서 필터링 분야 적용된 걸로 해서 정렬
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findAllPostsByBoardPK(Long boardId, int page, int size, String sortStr) {

        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 게시판 PK로 게시글 조회
        Page<Post> postPages;

        // 필터링된 필드 카테고리와 하위 카테고리들만 필터링
        List<Long> selectedFieldCategoryIds = getSelectedFieldCategoryIds(); // 선택된 하위 카테고리 기준 필터링

        if (!selectedFieldCategoryIds.isEmpty()) {
            // 선택된 하위 카테고리들만 필터링하여 게시글 조회
            postPages = postRepository.findAllByBoardIdAndFieldCategoryIds(boardId, selectedFieldCategoryIds, pageable);
        } else {
            // 선택된 카테고리가 없으면 전체 게시글 조회
            postPages = postRepository.findAllByBoardId(boardId, pageable);
        }

        // Post 객체를 PostPagingResponseDTO로 변환
        Page<PostPagingResponseDTO> postDTOPages = postPages.map(postPage -> new PostPagingResponseDTO(postPage, s3Service.getFullPath(postPage.getPostImgs())));

        return postDTOPages;
    }


    // 필터링된 FieldCategory에서 선택된 하위 카테고리 ID들만 가져오는 메서드
    @Transactional
    public List<Long> getSelectedFieldCategoryIds() {
        // 모든 FieldCategory 엔티티에서 선택된 SubCategory를 필터링
        return fieldCatrogryRepository.findAll().stream()
                .flatMap(fieldCategory -> fieldCategory.getFieldCategory().getSelectedSubCategories().stream()) // 서브카테고리만 필터링
                .map(subCategory -> subCategory.getFieldCategory().getId()) // 필터링된 서브카테고리의 FieldCategory ID 반환
                .collect(Collectors.toList());
    }

    //인기 게시판 좋아요 순
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findPopularPosts(Long boardId, int pageNum, int size, String sort) {
        // 정렬 방향 설정: "ASC" 또는 "DESC" 기준으로 좋아요 수(postLikes)로 정렬
        Sort.Direction direction = Sort.Direction.fromString(sort); // `sort`는 "ASC" 또는 "DESC" 값입니다.
        Pageable pageable = PageRequest.of(pageNum - 1, size, Sort.by(direction, "postLikes"));

        // 선택된 하위 카테고리 ID들을 필터링하여 가져오기
        List<Long> selectedFieldCategoryIds = getSelectedFieldCategoryIds(); // 선택된 하위 카테고리 기준 필터링

        Page<Post> posts;

        if (!selectedFieldCategoryIds.isEmpty()) {
            // 선택된 하위 카테고리들만 필터링하여 인기 게시글 조회
            posts = postRepository.findPopularPostsByBoardIdAndFieldCategoryIds(boardId, selectedFieldCategoryIds, pageable);
        } else {
            // 필터링된 카테고리가 없으면 전체 인기 게시글 조회
            posts = postRepository.findPopularPosts(boardId, pageable);
        }

        // Post 객체를 PostPagingResponseDTO로 변환
        Page<PostPagingResponseDTO> postDTOPages = posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));

        return postDTOPages;
    }


    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findQnaPostsByBoardPK(Long boardId, int page, int size, String sortStr) {

        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 게시판 PK로 게시글 조회
        Page<Post> postPages;

        // 필터링된 필드 카테고리와 하위 카테고리들만 필터링
        List<Long> selectedFieldCategoryIds = getSelectedFieldCategoryIds(); // 선택된 하위 카테고리 기준 필터링

        // Board 엔티티 조회 (게시판 정보 확인)
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (!boardOptional.isPresent()) {
            // Board가 존재하지 않으면 빈 페이지 반환
            return Page.empty(pageable);
        }

        // Board의 postType이 QNA인 경우 필터링
        Board board = boardOptional.get();
        if ( board.getPostType() != PostType.QNA) {
            // QNA 게시판이 아닌 경우 빈 페이지 반환
            return Page.empty(pageable);
        }

        // 선택된 하위 카테고리들만 필터링하여 게시글 조회
        if (!selectedFieldCategoryIds.isEmpty()) {
            postPages = postRepository.findAllByBoardIdAndFieldCategoryIds(boardId, selectedFieldCategoryIds, pageable);
        } else {
            // 선택된 카테고리가 없으면 전체 게시글 조회
            postPages = postRepository.findAllByBoardId(boardId, pageable);
        }

        // Post 객체를 PostPagingResponseDTO로 변환
        Page<PostPagingResponseDTO> postDTOPages = postPages.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));

        return postDTOPages;
    }



    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findExpertsPostsByBoardPK(Long boardId, int page, int size, String sortStr) {

        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 게시판 PK로 게시글 조회
        Page<Post> postPages;

        // 필터링된 필드 카테고리와 하위 카테고리들만 필터링
        List<Long> selectedFieldCategoryIds = getSelectedFieldCategoryIds(); // 선택된 하위 카테고리 기준 필터링

        // Board 엔티티 조회 (게시판 정보 확인)
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (!boardOptional.isPresent()) {
            // Board가 존재하지 않으면 빈 페이지 반환
            return Page.empty(pageable);
        }

        // Board의 postType이 QNA인 경우 필터링
        Board board = boardOptional.get();
        if ( board.getPostType() != PostType.EXPERT_COLUMN) {
            // QNA 게시판이 아닌 경우 빈 페이지 반환
            return Page.empty(pageable);
        }

        // 선택된 하위 카테고리들만 필터링하여 게시글 조회
        if (!selectedFieldCategoryIds.isEmpty()) {
            postPages = postRepository.findAllByBoardIdAndFieldCategoryIds(boardId, selectedFieldCategoryIds, pageable);
        } else {
            // 선택된 카테고리가 없으면 전체 게시글 조회
            postPages = postRepository.findAllByBoardId(boardId, pageable);
        }

        // Post 객체를 PostPagingResponseDTO로 변환
        Page<PostPagingResponseDTO> postDTOPages = postPages.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));

        return postDTOPages;
    }

}
