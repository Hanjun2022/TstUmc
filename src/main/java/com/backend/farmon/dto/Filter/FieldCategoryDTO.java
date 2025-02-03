package com.backend.farmon.dto.Filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Schema(description = "분야에 대한 필터링 DTO")
public class FieldCategoryDTO {

    @Schema(description = "작물ID")
    Long categoryId;
    @Schema(description = "상위작물이름")
    String name;

    @Schema(description = "상위작물이름")
    String displayName;

    @Schema(description = "상위분야 ")
    @Setter
    private FieldCategory fieldCategory;

    @Schema(description = "하위분야 ")
    @Getter
    private List<SubCategoryDTO> subCategories;

    // 유효성 검증 메서드 추가
    public boolean isValid() {
        return fieldCategory != null && subCategories != null && !subCategories.isEmpty();
    }



}