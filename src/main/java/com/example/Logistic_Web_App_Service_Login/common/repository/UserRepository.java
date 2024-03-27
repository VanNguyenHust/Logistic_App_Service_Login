package com.example.Logistic_Web_App_Service_Login.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Logistic_Web_App_Service_Login.common.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
