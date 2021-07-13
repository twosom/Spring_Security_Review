package com.icloud.security.user.repository;

import com.icloud.security.user.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpUserRepository extends JpaRepository<SpUser, Long> {

    Optional<SpUser> findByEmail(String email);


}
