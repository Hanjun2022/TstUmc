package com.backend.farmon.repository.AreaRepository;

import com.backend.farmon.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Optional<Area> findByAreaNameDetail(String areaNameDetail);

    Optional<Area> findByAreaNameAndAreaNameDetail(String areaName, String areaNameDetail);
}
