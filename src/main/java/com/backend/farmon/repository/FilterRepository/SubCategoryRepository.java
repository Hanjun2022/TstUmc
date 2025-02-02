package com.backend.farmon.repository.FilterRepository;

import com.backend.farmon.dto.Filter.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    // 선택된 하위 카테고리만 필터링해서 가져오는 쿼리
    List<SubCategory> findByIsSelectedTrue();
}