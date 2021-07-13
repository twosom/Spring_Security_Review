package com.icloud.security.config;

import com.icloud.security.user.domain.SpUser;
import com.icloud.security.user.service.SpUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DbInit implements InitializingBean {

    public static final String TEST_USER_1 = "user1";
    public static final String TEST_USER_2 = "user2";
    public static final String ADMIN = "admin";
    private final SpUserService userService;
    private final PasswordEncoder encoder;


    @Override
    public void afterPropertiesSet() throws Exception {
        createUserIfNotExists(TEST_USER_1, "ROLE_USER");
        createUserIfNotExists(TEST_USER_2, "ROLE_USER");
        createUserIfNotExists(ADMIN, "ROLE_ADMIN");
    }

    private void createUserIfNotExists(String username, String roleName) {
        if (userService.findUser(username).isEmpty()) {
            SpUser user = SpUser.builder()
                    .email(username)
                    .password(encoder.encode("1111"))
                    .enabled(true)
                    .build();
            userService.save(user);
            userService.addAuthority(user.getUserId(), roleName);
        }
    }
}
