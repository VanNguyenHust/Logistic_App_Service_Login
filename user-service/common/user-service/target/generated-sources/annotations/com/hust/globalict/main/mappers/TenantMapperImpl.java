package com.hust.globalict.main.mappers;

import com.hust.globalict.main.dtos.TenantDTO;
import com.hust.globalict.main.modules.Tenant;
import com.hust.globalict.main.modules.Tenant.TenantBuilder;
import com.hust.globalict.main.responses.TenantResponse;
import com.hust.globalict.main.responses.TenantResponse.TenantResponseBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-14T21:40:05+0700",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class TenantMapperImpl implements TenantMapper {

    @Override
    public Tenant mapToTenantEntity(TenantDTO tenantDTO) {
        if ( tenantDTO == null ) {
            return null;
        }

        TenantBuilder tenant = Tenant.builder();

        tenant.code( tenantDTO.getCode() );
        tenant.name( tenantDTO.getName() );
        tenant.status( tenantDTO.getStatus() );
        tenant.translateName( tenantDTO.getTranslateName() );

        return tenant.build();
    }

    @Override
    public Tenant mapToTenantEntity(TenantResponse tenantResponse) {
        if ( tenantResponse == null ) {
            return null;
        }

        TenantBuilder tenant = Tenant.builder();

        tenant.code( tenantResponse.getCode() );
        tenant.id( tenantResponse.getId() );
        tenant.name( tenantResponse.getName() );
        tenant.status( tenantResponse.getStatus() );
        tenant.translateName( tenantResponse.getTranslateName() );

        return tenant.build();
    }

    @Override
    public TenantResponse mapToTenantResponse(Tenant tenant) {
        if ( tenant == null ) {
            return null;
        }

        TenantResponseBuilder tenantResponse = TenantResponse.builder();

        tenantResponse.code( tenant.getCode() );
        tenantResponse.createdAt( tenant.getCreatedAt() );
        tenantResponse.id( tenant.getId() );
        tenantResponse.name( tenant.getName() );
        tenantResponse.status( tenant.getStatus() );
        tenantResponse.translateName( tenant.getTranslateName() );
        tenantResponse.updatedAt( tenant.getUpdatedAt() );

        return tenantResponse.build();
    }
}
