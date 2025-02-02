package com.backend.farmon.dto.Answer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "답변 요청 DTO")
public class AnswerRequestDTO {
    @Schema(description = "답변 제목")
    private String title;

    @Schema(description = "답변 내용")
    private String content;

    @Schema(description = "답변자 ID")
    private Long answeredUserId; // 답변한 사람의 ID 추가

    @Schema(description="사진 목록")
    private List<MultipartFile> Img;
}
