package com.icloud.security.user.service;

import com.icloud.security.user.domain.Authority;
import com.icloud.security.user.domain.User;
import com.icloud.security.user.service.helper.UserTestHelper;
import com.icloud.security.user.service.helper.WithUserTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.icloud.security.user.domain.Authority.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <u>
 * <li>사용자 생성</li>
 * <li>이름 수정</li>
 * <li>권한 부여</li>
 * <li>권한 취소</li>
 * <li>email 검색</li>
 * <li>role 중복해서 추가되지 않는다.</li>
 * <li>email이 중복되서 들어가는가</li>
 * </u>
 */
@DataJpaTest
public class UserTest extends WithUserTest {

    @BeforeEach
    protected void before() {
        prepareUserServices();
    }

    @DisplayName("1. 사용자 생성")
    @Test
    void 사용자_생성() throws Exception {
        userTestHelper.createUser(school, "user1");
        ArrayList<User> list = new ArrayList<>(userRepository.findAll());
        assertEquals(1, list.size());
        UserTestHelper.assertUser(school, list.get(0), "user1");
    }

    @DisplayName("2. 이름 수정")
    @Test
    void 이름_수정() throws Exception {
        User user = userTestHelper.createUser(school, "user1");
        userService.updateUsername(user.getUserId(), "user2");
        List<User> list = userRepository.findAll();
        assertEquals(1, list.size());
        assertEquals("user2", list.get(0).getName());
    }

    @DisplayName("3. 권한 부여")
    @Test
    void 권한_부여() throws Exception {
        User user = userTestHelper.createUser(school, "user1", ROLE_STUDENT);
        userService.addAuthority(user.getUserId(), ROLE_TEACHER);
        User savedUser = userService.findUser(user.getUserId()).get();
        UserTestHelper.assertUser(school, savedUser, "user1", ROLE_STUDENT, ROLE_TEACHER);
    }

    @DisplayName("4. 권한 취소")
    @Test
    void 권한_취소하기() throws Exception {
        User user = userTestHelper.createUser(school, "admin", ROLE_STUDENT, ROLE_TEACHER);
        userService.removeAuthority(user.getUserId(), ROLE_STUDENT);
        User findUser = userService.findUser(user.getUserId()).get();
        UserTestHelper.assertUser(school, findUser, "admin", ROLE_TEACHER);
    }

    @DisplayName("5. email 검색.")
    @Test
    void email_검색기능() throws Exception {
        User user = userTestHelper.createUser(school, "user1", ROLE_STUDENT);
        User findUser = (User) userSecurityService.loadUserByUsername("user1@test.com");
        UserTestHelper.assertUser(school, user, "user1");
    }

    @DisplayName("6. role 중복해서 추가되지 않는다.")
    @Test
    void Role_중복_문제() throws Exception {
        User user = userTestHelper.createUser(school, "user1", ROLE_STUDENT);
        userService.addAuthority(user.getUserId(), ROLE_STUDENT);
        userService.addAuthority(user.getUserId(), ROLE_STUDENT);
        User savedUser = userService.findUser(user.getUserId()).get();
        assertEquals(1, savedUser.getAuthorities().size());
        UserTestHelper.assertUser(school, savedUser, "user1", ROLE_STUDENT);
    }


    @DisplayName("7. email 이 중복되어서 들어가는가")
    @Test
    void email_중복문제() throws Exception {
        userTestHelper.createUser(school, "user1");
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userTestHelper.createUser(school, "user1");
        });
        System.out.println("exception = " + exception);
    }

}
