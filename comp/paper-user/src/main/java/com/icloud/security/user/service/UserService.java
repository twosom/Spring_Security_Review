package com.icloud.security.user.service;

import com.icloud.security.user.domain.Authority;
import com.icloud.security.user.domain.User;
import com.icloud.security.user.repository.SchoolRepository;
import com.icloud.security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.icloud.security.user.domain.Authority.ROLE_STUDENT;
import static com.icloud.security.user.domain.Authority.ROLE_TEACHER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    public Page<User> listUser(int pageNum, int size) {
        return userRepository.findAll(PageRequest.of(pageNum - 1, size));
    }

    public Map<Long, User> getUsers(List<Long> userIds) {
        return userRepository
                .findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));
    }

    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority newRole = new Authority(user.getUserId(), authority);
            if (!user.getAuthorities().contains(newRole)) {
                user.getAuthorities().add(newRole);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(user -> {
            Authority targetRole = new Authority(userId, authority);
            if (!user.getAuthorities().isEmpty() && user.getAuthorities().contains(targetRole)) {
                user.getAuthorities().remove(targetRole);
                save(user);
            }
        });
    }

    public void updateUsername(Long userId, String username) {
        userRepository.updateUserName(userId, username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findTeacherList() {
        return userRepository.findAllByAuthoritiesIn(ROLE_TEACHER);
    }

    public List<User> findStudentList() {
        return userRepository.findAllByAuthoritiesIn(ROLE_STUDENT);
    }

    public List<User> findTeacherStudentList(Long userId) {
        return userRepository.findAllByTeacherUserId(userId);
    }

    public Long findTeacherStudentCount(Long userId) {
        return userRepository.countByAllTeacherUserId(userId);
    }

    public List<User> findBySchoolStudentList(Long schoolId) {
        return userRepository.findAllBySchool(schoolId, ROLE_STUDENT);
    }

    public List<User> findBySchoolTeacherList(Long schoolId) {
        return userRepository.findAllBySchool(schoolId, ROLE_TEACHER);
    }

    public void updateUserSchoolTeacher(Long userId, Long schoolId, Long teacherId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (!user.getSchool().getSchoolId().equals(schoolId)) {
                schoolRepository.findById(schoolId).ifPresent(user::setSchool);
            }
            if (!user.getTeacher().getUserId().equals(teacherId)) {
                findUser(teacherId).ifPresent(user::setTeacher);
            }
            if (user.getSchool().getSchoolId() != user.getTeacher().getSchool().getSchoolId()) {
                throw new IllegalArgumentException("해당 학교의 선생이 아닙니다.");
            }
            save(user);
        });
    }

    public Long countTeacher() {
        return userRepository.countAllByAuthoritiesIn(ROLE_TEACHER);
    }

    public Long countTeacher(Long schoolId) {
        return userRepository.countAllByAuthoritiesIn(schoolId, ROLE_TEACHER);
    }

    public Long countStudent() {
        return userRepository.countAllByAuthoritiesIn(ROLE_STUDENT);
    }

    public Long countStudent(Long schoolId) {
        return userRepository.countAllByAuthoritiesIn(schoolId, ROLE_STUDENT);
    }

    public Page<User> listStudents(int pageNum, int size) {
        return userRepository.findAllByAuthoritiesIn(ROLE_STUDENT, PageRequest.of(pageNum - 1, size));
    }

    public Page<User> listTeachers(int pageNum, int size) {
        return userRepository.findAllByAuthoritiesIn(ROLE_TEACHER, PageRequest.of(pageNum - 1, size));
    }


}
