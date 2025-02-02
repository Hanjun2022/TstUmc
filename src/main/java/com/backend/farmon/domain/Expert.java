package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.mapping.ExpertArea;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
public class Expert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickName; // 전문가만 가지는 닉네임

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Boolean isNickNameOnly; // 닉네임만 보이기 활성화 여부, true이면 전문가일 시 닉네임만 반환되도록

    private String profileImageUrl; // 전문기 프로필 이미지

    private String expertDescription; // 전문가 한줄소개

    private String additionalInformation; // 전문가 추가정보

    private Float rating;
    private Integer careerYears;

    private String availableRange; //활동 가능 범위

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isAvailableEverywhere; // 전국 어디든 가능 여부

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isExcludeIsland; // 도서 지방 제외 여부

    private String serviceDetail1;
    private String serviceDetail2;
    private String serviceDetail3;
    private String serviceDetail4;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // user와 1:1관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crop_id")
    private Crop crop; // 전문가와 작물은 1:N관계

    // 매핑 테이블없이 바로 지역 테이블과 매핑되도록 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    private Area area; // 전문가와 지역은 1:N관계

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<Estimate> estimateList = new ArrayList<>();

//    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ExpertArea> expertAreaList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolioList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertCareer> expertCareerList = new ArrayList<>();

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomList = new ArrayList<>();

    public void setCrop(Crop crop) {
        // 기존값이 있으면 crop엔티티의 ExpertList에서 삭제시킴
        if(this.crop != null) {
            crop.getExpertList().remove(this);
        }
        this.crop = crop;
        crop.getExpertList().add(this);
    }

    public void setArea(Area area) {
        if (this.area != null) {
            area.getExpertList().remove(this);
        }
        this.area = area;
        area.getExpertList().add(this);
    }

    // 유저엔티티와 양방향 매핑
    public void setUser(User user) {
        if (this.user != null) {
            this.user.setExpert(null); // 기존 관계 제거
        }
        this.user = user;
        if (user != null) {
            user.setExpert(this);  // 반대편도 설정
        }
    }
}