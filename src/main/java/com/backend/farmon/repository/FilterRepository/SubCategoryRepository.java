package com.backend.farmon.repository.FilterRepository;

import com.backend.farmon.domain.SubCategory;
import com.backend.farmon.dto.Filter.FieldCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    // 선택된 하위 카테고리만 필터링해서 가져오는 쿼리 (필요에 따라 사용)
    List<SubCategory> findByIsSelectedTrue();

    // 선택된 하위 카테고리의 id 목록을 반환하는 쿼리 추가 (필요에 따라 사용)
    @Query("SELECT sc.id FROM SubCategory sc WHERE sc.isSelected = true")
    List<Long> findSelectedSubCategoryIds();

    // 선택된 하위 카테고리들의 FieldCategory를 반환하는 쿼리 추가 (**핵심**)
    @Query("SELECT DISTINCT sc.fieldCategory FROM SubCategory sc WHERE sc.isSelected = true")
    List<FieldCategory> findSelectedSubCategoryFieldCategories();

    // 선택된 하위 카테고리들을 category id로 묶어서 반환하는 쿼리 추가 (필요에 따라 사용)
    @Query("SELECT sc FROM SubCategory sc WHERE sc.isSelected = true AND sc.fieldCategory.id IN :fieldCategoryIds") // 수정됨
    List<SubCategory> findSelectedSubCategoriesByFieldCategoryId(@Param("fieldCategoryIds") List<Long> fieldCategoryIds);
}