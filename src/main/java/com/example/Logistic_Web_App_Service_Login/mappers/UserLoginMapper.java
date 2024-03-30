  package com.example.Logistic_Web_App_Service_Login.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.example.Logistic_Web_App_Service_Login.dtos.UserLoginDTO;
import com.example.Logistic_Web_App_Service_Login.models.UserLogin;
import com.example.Logistic_Web_App_Service_Login.responses.UserLoginResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserLoginMapper {
	UserLoginMapper iNSTANCE = Mappers.getMapper(UserLoginMapper.class);

	UserLoginResponse mapTopUserLoginResponse(UserLogin userLogin);
	
	UserLogin mapToUserLoginEntity(UserLoginDTO userLoginDTO);
}
