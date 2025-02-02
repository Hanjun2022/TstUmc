package com.backend.farmon.repository.CropRepository;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByName(String name);
}
