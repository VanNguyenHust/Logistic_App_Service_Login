package com.hust.globalict.main.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.hust.globalict.main.dtos.UserLoginDTO;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.responses.UserLoginResponse;

@Mapper(componentModel = "spring")
public interface UserLoginMapper {
	UserLoginMapper iNSTANCE = Mappers.getMapper(UserLoginMapper.class);

	UserLoginResponse mapTopUserLoginResponse(UserLogin userLogin);

	UserLogin mapToUserLoginEntity(UserLoginDTO userLoginDTO);
}
