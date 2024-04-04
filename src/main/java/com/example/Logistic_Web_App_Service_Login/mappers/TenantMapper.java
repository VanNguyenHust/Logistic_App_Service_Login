package com.example.Logistic_Web_App_Service_Login.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.example.Logistic_Web_App_Service_Login.dtos.TenantDTO;
import com.example.Logistic_Web_App_Service_Login.models.Tenant;
import com.example.Logistic_Web_App_Service_Login.responses.TenantResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TenantMapper {
	TenantMapper iNSTANCE = Mappers.getMapper(TenantMapper.class);
	
	Tenant mapToTenantEntity(TenantDTO tenantDTO);
	
	Tenant mapToTenantEntity(TenantResponse tenantResponse);
	
	TenantResponse mapToTenantResponse(Tenant tenant);
}
