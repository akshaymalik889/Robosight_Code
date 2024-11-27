package com.example.TestingJWT.services;


import com.example.TestingJWT.entities.Regular;
import com.example.TestingJWT.entities.User;
import com.example.TestingJWT.repositories.RegularRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegularService {

    private final RegularRepository regularRepository;

    public Regular createRegularUser(User user) {

        Regular regular = Regular
                .builder()
                .user(user)
                .build();

        return regularRepository.save(regular);

    }

}
