package com.backend.farmon.dto.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "게시판 타입")
public enum PostType {
    ALL, POPULAR, QNA, FREE, EXPERT_COLUMN;


}
