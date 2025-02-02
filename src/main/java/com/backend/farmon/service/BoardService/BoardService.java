package com.backend.farmon.service.BoardService;

import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.post.PostRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {

    void save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles)throws Exception;


    void save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles)throws Exception;

    // 분야 필수 에러 전문가 칼럼 과 qna 게시판에 추가
    void save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception;
}
