package com.backend.farmon.repository.ExpertReposiotry;

import com.backend.farmon.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long>, ExpertRepositoryCustom {
    // 유저 아이디로 전문가 조회
    Optional<Expert>findExpertByUserId(Long userId);

    // 유저 아이디로 전문가 존재 여부 확인
    boolean existsByUserId(Long userId);
}
