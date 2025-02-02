package com.backend.farmon.repository.ExpertReposiotry;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.Expert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpertRepositoryCustom {
    Page<Expert> findFilteredExperts(String crop, String area, Pageable pageable);
}