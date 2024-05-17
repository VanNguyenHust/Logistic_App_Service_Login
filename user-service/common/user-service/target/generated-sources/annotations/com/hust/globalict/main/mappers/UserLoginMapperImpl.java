package com.hust.globalict.main.mappers;

import com.hust.globalict.main.dtos.UserLoginDTO;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.modules.UserLogin.UserLoginBuilder;
import com.hust.globalict.main.responses.UserLoginResponse;
import com.hust.globalict.main.responses.UserLoginResponse.UserLoginResponseBuilder;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.processing.Generated;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-14T21:40:05+0700",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class UserLoginMapperImpl implements UserLoginMapper {

    @Override
    public UserLoginResponse mapTopUserLoginResponse(UserLogin userLogin) {
        if ( userLogin == null ) {
            return null;
        }

        UserLoginResponseBuilder userLoginResponse = UserLoginResponse.builder();

        userLoginResponse.accountNonExpired( userLogin.isAccountNonExpired() );
        userLoginResponse.accountNonLocked( userLogin.isAccountNonLocked() );
        Collection<? extends GrantedAuthority> collection = userLogin.getAuthorities();
        if ( collection != null ) {
            userLoginResponse.authorities( new ArrayList<GrantedAuthority>( collection ) );
        }
        userLoginResponse.createdAt( userLogin.getCreatedAt() );
        userLoginResponse.credentialsNonExpired( userLogin.isCredentialsNonExpired() );
        userLoginResponse.enabled( userLogin.isEnabled() );
        userLoginResponse.id( userLogin.getId() );
        userLoginResponse.loginType( userLogin.getLoginType() );
        userLoginResponse.role( userLogin.getRole() );
        userLoginResponse.updatedAt( userLogin.getUpdatedAt() );
        userLoginResponse.user( userLogin.getUser() );
        userLoginResponse.username( userLogin.getUsername() );

        return userLoginResponse.build();
    }

    @Override
    public UserLogin mapToUserLoginEntity(UserLoginDTO userLoginDTO) {
        if ( userLoginDTO == null ) {
            return null;
        }

        UserLoginBuilder userLogin = UserLogin.builder();

        userLogin.loginType( userLoginDTO.getLoginType() );
        userLogin.password( userLoginDTO.getPassword() );
        userLogin.username( userLoginDTO.getUsername() );

        return userLogin.build();
    }
}
