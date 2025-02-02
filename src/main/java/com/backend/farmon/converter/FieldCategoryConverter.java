package com.backend.farmon.converter;

import com.backend.farmon.dto.Filter.FieldCategory;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class FieldCategoryConverter {

    // FieldCategory -> FieldCategoryDTO 변환
    public static FieldCategoryDTO toDTO(FieldCategory fieldCategory, List<FieldCategoryDTO.SubCategoryDTO> subCategories) {
        return FieldCategoryDTO.builder()
                .fieldCategory(fieldCategory)
                .subCategories(subCategories)
                .build();
    }

    // FieldCategoryDTO -> FieldCategory 변환
    public static FieldCategory toEntity(FieldCategoryDTO dto) {
        if (dto == null || dto.getFieldCategory() == null) {
            throw new IllegalArgumentException("FieldCategoryDTO 또는 필드 카테고리는 null일 수 없습니다.");
        }
        return dto.getFieldCategory();
    }

    // SubCategories 변환 (옵션)
    public static List<FieldCategoryDTO.SubCategoryDTO> toSubCategories(List<String> subCategoryNames) {
        return subCategoryNames.stream()
                .map(name -> new FieldCategoryDTO.SubCategoryDTO(name, false)) // 기본값으로 isSelected = false
                .collect(Collectors.toList());
    }
}