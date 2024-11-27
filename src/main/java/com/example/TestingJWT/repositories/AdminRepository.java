package com.example.TestingJWT.repositories;

import com.example.TestingJWT.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

}
