package com.hust.globalict.main.mappers;

import com.hust.globalict.main.dtos.UserDTO;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.modules.User.UserBuilder;
import com.hust.globalict.main.responses.UserResponse;
import com.hust.globalict.main.responses.UserResponse.UserResponseBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-14T21:40:05+0700",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User mapToUserEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.firstName( userDTO.getFirstName() );
        user.fullName( userDTO.getFullName() );
        user.lastName( userDTO.getLastName() );
        user.phone( userDTO.getPhone() );

        return user.build();
    }

    @Override
    public User mapToUserEntity(UserResponse userResponse) {
        if ( userResponse == null ) {
            return null;
        }

        UserBuilder user = User.builder();

        user.department( userResponse.getDepartment() );
        user.firstName( userResponse.getFirstName() );
        user.fullName( userResponse.getFullName() );
        user.id( userResponse.getId() );
        user.isActive( userResponse.getIsActive() );
        user.lastName( userResponse.getLastName() );
        user.phone( userResponse.getPhone() );
        user.tenant( userResponse.getTenant() );

        return user.build();
    }

    @Override
    public UserResponse mapToUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.createdAt( user.getCreatedAt() );
        userResponse.department( user.getDepartment() );
        userResponse.firstName( user.getFirstName() );
        userResponse.fullName( user.getFullName() );
        userResponse.id( user.getId() );
        userResponse.isActive( user.getIsActive() );
        userResponse.lastName( user.getLastName() );
        userResponse.phone( user.getPhone() );
        userResponse.tenant( user.getTenant() );
        userResponse.updatedAt( user.getUpdatedAt() );

        return userResponse.build();
    }
}
