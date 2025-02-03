package com.backend.farmon.converter;

import com.backend.farmon.domain.FieldCategoryEntity;
import com.backend.farmon.domain.SubCategory; // SubCategory Entity import
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.Filter.SubCategoryDTO; // SubCategoryDTO import

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FieldCategoryConverter {

    // FieldCategory -> FieldCategoryDTO 변환
    public static FieldCategoryDTO toDTO(FieldCategoryEntity fieldCategory) {
        List<SubCategoryDTO> subCategoryDTOs = fieldCategory.getSubCategories().stream()
                .map(FieldCategoryConverter::toDTO) // SubCategory -> SubCategoryDTO 변환
                .collect(Collectors.toList());

        return FieldCategoryDTO.builder()
                .categoryId(fieldCategory.getId()) // FieldCategory ID 설정
                .name(fieldCategory.getName().name()) //Enum name 설정
                .displayName(fieldCategory.getDisplayName()) //displayname 설정
                .subCategories(subCategoryDTOs)
                .build();
    }

    // SubCategory -> SubCategoryDTO 변환
    public static SubCategoryDTO toDTO(SubCategory subCategory) {
        return SubCategoryDTO.builder()
                .subCategoryName(subCategory.getName())
                .isSelected(subCategory.isSelected())
                .build();
    }


    // SubCategoryDTO List -> SubCategory List 변환
    public static List<SubCategory> toSubCategoryEntities(FieldCategoryDTO dto, FieldCategoryEntity fieldCategory) {
        if (dto == null || dto.getSubCategories() == null) {
            return new ArrayList<>(); // or throw an exception if subcategories are mandatory
        }
        return dto.getSubCategories().stream()
                .map(subCategoryDTO -> {
                    SubCategory subCategory = new SubCategory();
                    subCategory.setName(subCategoryDTO.getSubCategoryName());
                    subCategory.setSelected(subCategoryDTO.isSelected());
                    subCategory.setFieldCategory(fieldCategory); // FieldCategory 설정
                    return subCategory;
                })
                .collect(Collectors.toList());
    }
}