package com.hust.globalict.main.mappers;

import com.hust.globalict.main.dtos.DepartmentDTO;
import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.modules.Department.DepartmentBuilder;
import com.hust.globalict.main.responses.DepartmentResponse;
import com.hust.globalict.main.responses.DepartmentResponse.DepartmentResponseBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-14T21:40:05+0700",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public Department mapToDepartmentEntity(DepartmentDTO departmentDTO) {
        if ( departmentDTO == null ) {
            return null;
        }

        DepartmentBuilder department = Department.builder();

        department.code( departmentDTO.getCode() );
        department.level( departmentDTO.getLevel() );
        department.name( departmentDTO.getName() );
        department.parentDepartmentCode( departmentDTO.getParentDepartmentCode() );
        department.parentDepartmentId( departmentDTO.getParentDepartmentId() );
        department.path( departmentDTO.getPath() );
        department.status( departmentDTO.getStatus() );
        department.translateName( departmentDTO.getTranslateName() );
        department.type( departmentDTO.getType() );

        return department.build();
    }

    @Override
    public Department mapToDepartmentEntity(DepartmentResponse departmentResponse) {
        if ( departmentResponse == null ) {
            return null;
        }

        DepartmentBuilder department = Department.builder();

        department.code( departmentResponse.getCode() );
        department.level( departmentResponse.getLevel() );
        department.name( departmentResponse.getName() );
        department.parentDepartmentCode( departmentResponse.getParentDepartmentCode() );
        department.parentDepartmentId( departmentResponse.getParentDepartmentId() );
        department.path( departmentResponse.getPath() );
        department.status( departmentResponse.getStatus() );
        department.translateName( departmentResponse.getTranslateName() );
        department.type( departmentResponse.getType() );

        return department.build();
    }

    @Override
    public DepartmentResponse mapToDepartmentResponse(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentResponseBuilder departmentResponse = DepartmentResponse.builder();

        departmentResponse.code( department.getCode() );
        departmentResponse.createdAt( department.getCreatedAt() );
        departmentResponse.level( department.getLevel() );
        departmentResponse.name( department.getName() );
        departmentResponse.parentDepartmentCode( department.getParentDepartmentCode() );
        departmentResponse.parentDepartmentId( department.getParentDepartmentId() );
        departmentResponse.path( department.getPath() );
        departmentResponse.status( department.getStatus() );
        departmentResponse.translateName( department.getTranslateName() );
        departmentResponse.type( department.getType() );
        departmentResponse.updatedAt( department.getUpdatedAt() );

        return departmentResponse.build();
    }
}
