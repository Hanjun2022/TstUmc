package com.backend.farmon.dto.Filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Schema(description = "분야에 대한 필터링 DTO")
public class FieldCategoryDTO {
    @Setter
    private FieldCategory fieldCategory;
    @Getter
    private List<SubCategoryDTO> subCategories;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryDTO {
        private String subCategoryName;
        private Boolean isSelected;
    }

    // 생성자, getter, setter 추가
    public FieldCategoryDTO(FieldCategory fieldCategory, List<SubCategoryDTO> subCategories) {
        this.fieldCategory = fieldCategory;
        this.subCategories = subCategories;
    }


}