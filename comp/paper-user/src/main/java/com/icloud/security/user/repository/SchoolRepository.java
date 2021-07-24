package com.icloud.security.user.repository;


import com.icloud.security.user.domain.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface SchoolRepository extends JpaRepository<School, Long> {

    @Query( "SELECT DISTINCT c.city " +
            "FROM School c")
    List<String> getCities();

    List<School> findAllByCity(String city);
}
