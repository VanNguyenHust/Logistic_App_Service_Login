package com.hust.globalict.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.globalict.main.modules.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{
    boolean existsByNameOrCode(String name, String code);
}
