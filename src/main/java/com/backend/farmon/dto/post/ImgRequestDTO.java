package com.backend.farmon.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "이미지 요청 DTO")
public class ImgRequestDTO {


    @Schema(description = "여러 개의 이미지 파일을 업로드할 때 사용", required = true)
    private List<MultipartFile> multipartFiles;

    @Schema(description = "단일 이미지 파일을 업로드할 때 사용")
    private MultipartFile multipartFile;

    @Schema(description = "원본 파일 이름", example = "image.jpg")
    private String originalFileName;

    @Schema(description = "저장된 파일 경로",example="xxx ")
    private String savePath;



}