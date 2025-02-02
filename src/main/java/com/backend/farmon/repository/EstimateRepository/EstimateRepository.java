package com.backend.farmon.repository.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.Expert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long>, EstimateRepositoryCustom {
    @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.estimateImageList WHERE e.id = :estimateId")
    Optional<Estimate> findEstimateWithImages(@Param("estimateId") Long estimateId);

    // 1) 특정 cropId 에게 매핑된 견적서 목록 - 상태가 0인것만
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.crop.id = :cropId AND e.status = 0 " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findByCropIdAndStatus(@Param("cropId") Long cropId, Pageable pageable);

    // 1-1) 특정 cropName 에게 매핑된 견적서 목록 - 상태가 0인것만
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.crop.name = :cropName AND e.status = 0 " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findByCropNameAndStatus(@Param("cropName") String cropName, Pageable pageable);

    // 2) 특정 cropCategory 에게 매핑된 견적서 목록 - 상태가 0인것만
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.crop.category = :cropCategory AND e.status = 0 " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findByCropCategoryAndStatus(@Param("cropCategory") String cropCategory, Pageable pageable);

    // 3) estimate 랑 chatRoom 테이블 조인해서 전달한 expertId가
    // chatRoom 테이블의 expert_id와 일치하는 estimates 목록 모두 조회
    @Query("SELECT e FROM Estimate e " +
            "JOIN ChatRoom c ON e.id = c.estimate.id " +
            "WHERE c.expert.id = :expertId " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findAllEstimatesByExpertId(@Param("expertId") Long expertId, Pageable pageable);

    // 4) estimate  expert_id와 일치하는 estimates 목록 중
    // status 가 1인 목록을 전달
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.expert.id = :expertId AND e.status = 1 " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findCompletedEstimatesByExpertId(@Param("expertId") Long expertId, Pageable pageable);

    // 5) estimate user_id와 일치하는 estimates 목록 모두 조회
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.user.id = :userId " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findAllEstimatesByUserId(@Param("userId") Long userId, Pageable pageable);

    // 6) estimate  user_id와 일치하는 estimates 목록 중 status 가 1인 목록을 전달
    @Query("SELECT e FROM Estimate e " +
            "WHERE e.user.id = :userId AND e.status = 1 " +
            "ORDER BY e.createdAt DESC")
    Page<Estimate> findCompletedEstimatesByUserId(@Param("userId") Long userId, Pageable pageable);

    // 7) estimate  user_id와 일치하는 estimates 목록 중 최신순 5개 목록을 전달
    @Query("SELECT e FROM Estimate e WHERE e.user.id = :userId " +
            "ORDER BY e.createdAt DESC")
    List<Estimate> findTop5ByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);

    // 특정 전문가에 대한 모든 Estimate 수를 반환하는 메서드
    long countByExpert(Expert expert);
}
