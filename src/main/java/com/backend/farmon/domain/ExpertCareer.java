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
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ExpertCareer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer startYear;

    @Column(nullable = false)
    private Integer startMonth;

    private Integer endYear;

    private Integer endMonth;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isOngoing; // 진행중 여부

    private String detailContent1;
    private String detailContent2;
    private String detailContent3;
    private String detailContent4;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private Expert expert;

    public void setExpert(Expert expert) {
        if(this.expert != null) {
            expert.getExpertCareerList().remove(this);
        }
        this.expert = expert;
        expert.getExpertCareerList().add(this);
    }

}