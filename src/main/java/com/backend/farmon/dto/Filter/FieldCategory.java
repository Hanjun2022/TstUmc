package com.backend.farmon.dto.Filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "분야에 대한 필터링 상위분야 ")
public enum FieldCategory {
    COMMON("공통"),
    GRAIN("곡물"),
    VEGETABLE("채소"),
    FRUIT("과일"),
    SPECIAL_CROP("특용"),
    FLOWER("화훼"),
    FEED("사료"),
    ANY("기타");

    private final String displayName;

    FieldCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
