package com.sha.springbootjwtauthorization.security;

import com.sha.springbootjwtauthorization.model.User;
import com.sha.springbootjwtauthorization.service.UserService;
import com.sha.springbootjwtauthorization.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found with the username:"+username));
        Set<GrantedAuthority> authorities=Set.of(SecurityUtils.ConvertToAuthority(user.getRole().name()));
        return UserPrincipal.builder()
                .user(user)
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
