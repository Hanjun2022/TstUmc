package com.backend.farmon.domain;


import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.dto.Filter.FieldCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldCategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private FieldCategory name; // Enum 매핑

    @Column(nullable = false)
    private String displayName;

    @OneToMany(mappedBy = "fieldCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "fieldCategory")
    private List<Post> posts = new ArrayList<>();

    // isSelected가 true인 subCategory만 반환하는 메서드 추가
    public List<SubCategory> getSelectedSubCategories() {
        return subCategories.stream()
                .filter(SubCategory::isSelected)
                .collect(Collectors.toList());
    }
}
