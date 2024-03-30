package com.example.Logistic_Web_App_Service_Login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
