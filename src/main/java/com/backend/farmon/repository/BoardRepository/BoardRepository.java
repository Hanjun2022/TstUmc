package com.backend.farmon.repository.BoardRepository;

import com.backend.farmon.domain.Board;
import com.backend.farmon.dto.post.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByPostType(PostType postType);
    Optional<Board>findById(Long id);
}