package com.backend.farmon.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "게시판 타입")
public enum PostType {
    ALL, POPULAR, QNA, FREE, EXPERT_COLUMN;


}
