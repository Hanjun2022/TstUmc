package com.backend.farmon.repository.UserRepository;

import com.backend.farmon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
