package com.hust.globalict.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hust.globalict.main.dtos.UserDTO;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.responses.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper iNSTANCE = Mappers.getMapper(UserMapper.class);

	User mapToUserEntity(UserDTO userDTO);
	
	User mapToUserEntity(UserResponse userResponse);

	UserResponse mapToUserResponse(User user);
}
