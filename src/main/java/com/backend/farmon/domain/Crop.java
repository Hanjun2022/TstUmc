package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.enums.CropCategory;
import com.backend.farmon.domain.mapping.ExpertArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Crop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    //    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(55)")
    private String category;

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Estimate> estimateList = new ArrayList<>();

    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<Expert> expertList = new ArrayList<>();
}
