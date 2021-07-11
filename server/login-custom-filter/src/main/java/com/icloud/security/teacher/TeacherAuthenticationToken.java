package com.icloud.security.teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherAuthenticationToken implements Authentication {

    private Teacher principal;
    private String credentials;
    private String details;
    private boolean authenticated;
    private Set<GrantedAuthority> authorities;

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return principal == null ? new HashSet<>() : principal.getRole();
//    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getUsername();
    }

}
