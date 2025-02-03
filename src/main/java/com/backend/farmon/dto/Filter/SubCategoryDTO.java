package com.backend.farmon.dto.Filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "하위 분야 DTO")
public class SubCategoryDTO {

        @Schema(description = "하위 분야 이름")
        private String subCategoryName;

        @Schema(description = "선택 여부")
        private Boolean isSelected;


        public boolean isSelected() {
                return isSelected;
        }
}
