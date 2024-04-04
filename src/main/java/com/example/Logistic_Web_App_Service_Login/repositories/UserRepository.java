package com.example.Logistic_Web_App_Service_Login.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.example.Logistic_Web_App_Service_Login.models.User;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword%")
	Page<User> findAllUserByKeyword(String keyword, Pageable pageable);
    
    boolean existsByTenant(Tenant tenant);
}
