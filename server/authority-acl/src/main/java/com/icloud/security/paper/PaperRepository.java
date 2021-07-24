package com.icloud.security.paper;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface PaperRepository extends JpaRepository<Paper, Long> {

//    @Cacheable(value = "papers")
    Optional<Paper> findById(Long id);

}
