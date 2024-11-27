package com.example.TestingJWT.services;

import com.example.TestingJWT.entities.Admin;
import com.example.TestingJWT.entities.Regular;
import com.example.TestingJWT.entities.User;
import com.example.TestingJWT.repositories.AdminRepository;
import com.example.TestingJWT.repositories.RegularRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public Admin createAdminUser(User user) {

        Admin admin = Admin
                .builder()
                .user(user)
                .build();

        return adminRepository.save(admin);

    }
}
