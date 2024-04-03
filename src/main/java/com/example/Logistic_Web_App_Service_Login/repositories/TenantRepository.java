package com.example.Logistic_Web_App_Service_Login.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Logistic_Web_App_Service_Login.models.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long>{

}
