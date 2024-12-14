package com.crudops.skylark.service.impl;

import com.crudops.skylark.model.Info;
import com.crudops.skylark.repository.InfosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final InfosRepository infosRepository;

    @Autowired
    public CustomUserDetailsService(InfosRepository infosRepository) {
        this.infosRepository = infosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the repository based on the provided username (email)
        Optional<Info> infos = infosRepository.findByEmail(username);  // Assuming your user has an email field

        if (infos == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

            return User.builder()
                    .username("user")
                    .password("{noop}password") // Use NoOpPasswordEncoder for testing (use BCrypt in production)
                    .roles("USER")
                    .build();
    }
}
