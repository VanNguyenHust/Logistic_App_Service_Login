package com.hust.globalict.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.globalict.main.modules.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
}
