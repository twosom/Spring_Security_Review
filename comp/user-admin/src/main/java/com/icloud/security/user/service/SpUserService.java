package com.icloud.security.user.service;

import com.icloud.security.user.domain.SpAuthority;
import com.icloud.security.user.domain.SpUser;
import com.icloud.security.user.repository.SpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SpUserService implements UserDetailsService {

    private final SpUserRepository spUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return spUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<SpUser> findUser(String email) {
        return spUserRepository.findByEmail(email);
    }

    public SpUser save(SpUser user) {
        return spUserRepository.save(user);
    }


    public void addAuthority(Long userId, String authority) {
        spUserRepository.findById(userId).ifPresent(user -> {
            SpAuthority newRole = new SpAuthority(user.getUserId(), authority);
            if (user.getAuthorities().isEmpty()) {
                user.getAuthorities().add(newRole);
                save(user);
            } else if (!user.getAuthorities().contains(newRole)) {
                user.getAuthorities().add(newRole);
                save(user);
            }
        });
    }

    public void removeAuthority(Long userId, String authority) {
        spUserRepository.findById(userId).ifPresent(user -> {

            if (user.getAuthorities().isEmpty()) return;

            SpAuthority targetRole = new SpAuthority(user.getUserId(), authority);
            if (user.getAuthorities().contains(targetRole)) {
                user.getAuthorities().remove(targetRole);
                save(user);
            }
        });
    }


}
