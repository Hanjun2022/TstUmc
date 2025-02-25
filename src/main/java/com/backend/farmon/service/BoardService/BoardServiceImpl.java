package com.backend.farmon.service.BoardService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.FieldCategory;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.Filter.SubCategoryDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository; // 게시글 데이터를 저장하기 위한 Repository
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service; // 파일 업로드를 위한 S3 서비스



    @Override
    public void save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) throws Exception {
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if(board.getPostType() != PostType.FREE){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        } //생각해보니까 board에서 id 찾는게 동일

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        // 전체,인기 게시판에 저장
        savePostToAllAndPopular(post, user);


        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }

    }


    // 분야 선택 안 할 시 에러가 일어나게 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public void save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getFieldCategory()); // 유효성 검증
        log.info(String.valueOf(postDto.getUserId()));
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.QNA) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        log.info(postDto.getPostContent().toString());
        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        savePostToAllAndPopular(post, user);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
    }

    @Override
    public void save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getFieldCategory()); // 유효성 검증
        log.info("검증은 완료");
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.EXPERT_COLUMN) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        // 인기 , 전체 게시판 에 저장  근데 이건  왜 저장이 같이 안돼지?
        savePostToAllAndPopular(post, user);

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
    }


    private void validateFieldCategory(FieldCategoryDTO fieldCategoryDTO) {
        if (fieldCategoryDTO == null || !fieldCategoryDTO.isValid()) {
            throw new GeneralException(ErrorStatus.FIELD_CATEGORY_REQUIRED);
        }
        log.info("뭐가 문제야");
        List<SubCategoryDTO> subCategories = fieldCategoryDTO.getSubCategories();
        if (subCategories == null || subCategories.isEmpty()) {
            throw new GeneralException(ErrorStatus.SUB_CATEGORIES_EMPTY);
        }

        boolean hasSelectedSubCategory = subCategories.stream()
                .anyMatch(SubCategoryDTO::getIsSelected);

        if (!hasSelectedSubCategory) {
            throw new GeneralException(ErrorStatus.FIELD_CATEGORY_REQUIRED);
        }
        log.info("인증은 끝");
    }




    // 자유게시판
    private Post createPostByBoardType(BoardRequestDto.FreePost postDTO,User user,Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }



    // 전문가 칼럼 게시판
//    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO, User user, Board board) throws Exception {
//        List<String> selectedSubCategories = postDTO.getFieldCategory().getSubCategories().stream()
//                .filter(FieldCategoryDTO.SubCategoryDTO::getIsSelected) // isSelected = true인 항목만 필터링
//                .map(FieldCategoryDTO.SubCategoryDTO::getSubCategoryName) // 이름만 추출
//                .collect(Collectors.toList());
//
//        return Post.builder()
//                .postTitle(postDTO.getPostTitle())
//                .postContent(postDTO.getPostContent())
//                .fieldCategory(postDTO.getFieldCategory().getFieldCategory().getDisplayName()) // 상위 카테고리 설정
//                .user(user)
//                .board(board)
//                .selectedSubCategories(selectedSubCategories) // 체크된 하위 카테고리 추가
//                .build();
//    }

    private Post createPostByBoardType(BoardRequestDto.QnaPost postDTO, User user, Board board) {
        List<String> selectedSubCategories = postDTO.getFieldCategory().getSubCategories().stream()
                .filter(SubCategoryDTO::getIsSelected)
                .map(SubCategoryDTO::getSubCategoryName)
                .collect(Collectors.toList());

        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .fieldCategory(postDTO.getFieldCategory().getFieldCategory()) // Enum 값 직접 설정
                .selectedSubCategories(selectedSubCategories)
                .user(user)
                .board(board)
                .build();
    }
    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO, User user, Board board) {
        List<String> selectedSubCategories = postDTO.getFieldCategory().getSubCategories().stream()
                .filter(SubCategoryDTO::getIsSelected)
                .map(SubCategoryDTO::getSubCategoryName)
                .collect(Collectors.toList());

        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .fieldCategory(postDTO.getFieldCategory().getFieldCategory()) // Enum 값 직접 설정
                .selectedSubCategories(selectedSubCategories)
                .user(user)
                .board(board)
                .build();
    }

    // 전체 게시판 과 인기 게시판에 각각 저장 (좋아요 수로 정렬 이니 인기 게시판에는 추가)
    private void savePostToAllAndPopular(Post originalPost, User user) {
        // 전체 게시판 가져오기
        Board allBoard = boardRepository.findByPostType(PostType.ALL)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // 인기 게시판 가져오기
        Board popularBoard = boardRepository.findByPostType(PostType.POPULAR)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // 전체 게시판에 저장할 게시글 생성
        Post allPost = Post.builder()
                .postTitle(originalPost.getPostTitle())
                .postContent(originalPost.getPostContent())
                .fieldCategory(originalPost.getFieldCategory())
                .user(user)
                .board(allBoard)
                .selectedSubCategories(originalPost.getSelectedSubCategories())
                .build();
        postRepository.save(allPost);
        log.info("두 게시판에 모두 저장됨");
        // 인기 게시판에 저장할 게시글 생성 (초기 좋아요 1 설정)
        Post popularPost = Post.builder()
                .postTitle(originalPost.getPostTitle())
                .postContent(originalPost.getPostContent())
                .fieldCategory(originalPost.getFieldCategory())
                .user(user)
                .board(popularBoard)
                .postLikes(0) // 인기 게시판은 기본 좋아요 추가
                .selectedSubCategories(originalPost.getSelectedSubCategories())
                .build();
        postRepository.save(popularPost);
    }


}
