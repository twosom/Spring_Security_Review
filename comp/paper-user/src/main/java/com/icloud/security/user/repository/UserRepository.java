package com.icloud.security.user.repository;

import com.icloud.security.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User " +
            "SET name = :username " +
            "WHERE userId = :userId")
    void updateUserName(@Param("userId") Long userId, @Param(value = "username") String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN Authority a " +
            "ON u.userId = a.userId " +
            "WHERE a.authority = :authority")
    List<User> findAllByAuthoritiesIn(@Param(value = "authority") String authority);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN Authority a " +
            "ON u.userId = a.userId " +
            "WHERE a.authority = :authority")
    Page<User> findAllByAuthoritiesIn(@Param(value = "authority") String authority, Pageable pageable);

    @Query("SELECT u " +
            "FROM User u " +
            "JOIN Authority a " +
            "ON u.userId = a.userId " +
            "WHERE u.school.schoolId = :schoolId " +
            "AND a.authority = :authority")
    List<User> findAllBySchool(@Param(value = "schoolId") Long schoolId, @Param(value = "authority") String authority);


    @Query("SELECT u " +
            "FROM User u " +
            "JOIN User t " +
            "ON u.teacher.userId = t.userId " +
            "AND t.userId = :userId")
    List<User> findAllByTeacherUserId(@Param(value = "userId") Long userId);


    @Query("SELECT COUNT(u) " +
            "FROM User u " +
            "JOIN User t " +
            "ON u.teacher.userId = t.userId " +
            "WHERE t.userId = :userId")
    Long countByAllTeacherUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(u) " +
            "FROM User u " +
            "JOIN Authority a " +
            "ON u.userId = a.userId " +
            "AND a.authority = :authority")
    Long countAllByAuthoritiesIn(@Param(value = "authority") String authority);

    @Query("SELECT COUNT(u) " +
            "FROM User u " +
            "JOIN Authority a " +
            "ON u.userId = a.userId " +
            "WHERE u.school.schoolId = :schoolId " +
            "AND a.authority = :authority")
    Long countAllByAuthoritiesIn(@Param(value = "schoolId") Long schoolId, @Param(value = "authority") String authority);


}
