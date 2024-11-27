package com.example.TestingJWT.services;


import com.example.TestingJWT.entities.User;
import com.example.TestingJWT.exceptions.ResourceNotFoundException;
import com.example.TestingJWT.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;


    public User getUserById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("User Not Found With Id: "+id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}
