package com.hust.globalict.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hust.globalict.main.dtos.DepartmentDTO;
import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.responses.DepartmentResponse;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
	DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

	Department mapToDepartmentEntity(DepartmentDTO departmentDTO);

	Department mapToDepartmentEntity(DepartmentResponse departmentResponse);

	DepartmentResponse mapToDepartmentResponse(Department department);
}
