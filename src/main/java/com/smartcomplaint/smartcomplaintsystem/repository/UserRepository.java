package com.smartcomplaint.smartcomplaintsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartcomplaint.smartcomplaintsystem.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}