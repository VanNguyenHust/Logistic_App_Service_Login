package com.hust.globalict.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hust.globalict.main.dtos.TenantDTO;
import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.responses.TenantResponse;

@Mapper(componentModel = "spring")
public interface TenantMapper {
	TenantMapper iNSTANCE = Mappers.getMapper(TenantMapper.class);

	Tenant mapToTenantEntity(TenantDTO tenantDTO);

	Tenant mapToTenantEntity(TenantResponse tenantResponse);

	TenantResponse mapToTenantResponse(Tenant tenant);
}
