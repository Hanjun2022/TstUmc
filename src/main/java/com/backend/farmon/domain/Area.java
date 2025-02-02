package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.mapping.ExpertArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Area extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String areaName;  // 서비스 지역 이름  ex)서울, 강동구

    @Column(nullable = false, length = 10)
    private String areaNameDetail ; // 서비스 지역 디테일  ex)감남구, 강동구

    // 매핑 테이블없이 바로 전문가테이블과 매핑되도록 수정
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expert> expertList = new ArrayList<>();

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Estimate> estimateList = new ArrayList<>();
}
