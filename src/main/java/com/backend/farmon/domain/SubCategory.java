package com.backend.farmon.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 하위 항목 이름

    @Column(nullable = false)
    private boolean isSelected; // 선택 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_category_id", nullable = false)
    private FieldCategoryEntity  fieldCategory; // 상위 카테고리와의 관계 설정 (**수정됨**)
}