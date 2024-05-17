package com.hust.globalict.main.repositories;

import com.hust.globalict.main.modules.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.modules.User;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.fullName LIKE %:keyword%")
    List<User> findAllUserByKeyword(String keyword, Pageable pageable);

    User findByTenant(Tenant tenant);

    User findByDepartment(Department department);
}
