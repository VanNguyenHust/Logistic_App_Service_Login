package com.example.Logistic_Web_App_Service_Login.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import com.example.Logistic_Web_App_Service_Login.dtos.UserDTO;
import com.example.Logistic_Web_App_Service_Login.models.User;
import com.example.Logistic_Web_App_Service_Login.responses.UserResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
	UserMapper iNSTANCE = Mappers.getMapper(UserMapper.class);

	User mapToUserEntity(UserDTO userDTO);

	UserResponse mapToUserReponse(User user);
}
