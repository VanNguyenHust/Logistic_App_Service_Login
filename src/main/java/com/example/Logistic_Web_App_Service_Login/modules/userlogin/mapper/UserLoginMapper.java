  package com.example.Logistic_Web_App_Service_Login.modules.userlogin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.dto.UserLoginDTO;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.response.UserLoginResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserLoginMapper {
	UserLoginMapper iNSTANCE = Mappers.getMapper(UserLoginMapper.class);

	UserLoginResponse mapTopUserLoginResponse(UserLogin userLogin);
	
	UserLogin mapToUserLoginEntity(UserLoginDTO userLoginDTO);
}
