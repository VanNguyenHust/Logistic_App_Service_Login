package com.example.Logistic_Web_App_Service_Login.modules.userlogin.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Logistic_Web_App_Service_Login.common.components.LocalizationUtils;
import com.example.Logistic_Web_App_Service_Login.common.entity.Token;
import com.example.Logistic_Web_App_Service_Login.common.entity.UserLogin;
import com.example.Logistic_Web_App_Service_Login.common.enums.Uri;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.DataNotFoundException;
import com.example.Logistic_Web_App_Service_Login.common.exceptions.InvalidPasswordException;
import com.example.Logistic_Web_App_Service_Login.common.utils.MessageKeys;
import com.example.Logistic_Web_App_Service_Login.common.utils.ResponseObject;
import com.example.Logistic_Web_App_Service_Login.modules.token.dto.RefreshTokenDTO;
import com.example.Logistic_Web_App_Service_Login.modules.token.service.TokenService;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.dto.ChangePasswordDTO;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.dto.UserLoginDTO;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.mapper.UserLoginMapper;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.response.UserLoginResponse;
import com.example.Logistic_Web_App_Service_Login.modules.userlogin.service.UserLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER_LOGIN)
public class UserLoginController {
	private final UserLoginService userLoginService;
	private final TokenService tokenService;
	private final AuthenticationManager authenticationManager;
	
	private final LocalizationUtils localizationUtils;
	
	@Autowired
	UserLoginMapper userLoginMapper;
	
	@PostMapping("/{register}")
	public ResponseEntity<ResponseObject> createUserLogin(@Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception{		
		if (!userLoginDTO.getPassword().equals(userLoginDTO.getRetypePassword())) {
			return ResponseEntity.badRequest().body(ResponseObject.builder().status(HttpStatus.BAD_REQUEST).data(null)
					.message(localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH)).build());
		}
		
		UserLogin userLogin = userLoginService.createUserLogin(userLoginDTO);
		
		UserLoginResponse userLoginResponse = userLoginMapper.mapTopUserLoginResponse(userLogin);
		userLoginResponse.setRoles(userLogin.getAuthorities().stream().map(item -> item.getAuthority()).toList());
		
		return ResponseEntity.ok(ResponseObject.builder()
				.message("create success")
				.status(HttpStatus.OK)
				.data(userLoginResponse)
				.build());
	}
	
	private boolean isMobileDevice(String userLoginAgent) {
        // Kiểm tra User-Agent header để xác định thiết bị di động
        // Ví dụ đơn giản:
        return userLoginAgent.toLowerCase().contains("mobile");
    }
	
	@PostMapping("/login")
    public ResponseEntity<ResponseObject> login(
            @Valid @RequestBody UserLoginDTO userLoginDTO,
            HttpServletRequest request
    ) throws Exception {
        // Kiểm tra thông tin đăng nhập và sinh token
        String token = userLoginService.login(
                userLoginDTO.getUserName(),
                userLoginDTO.getPassword(),
                userLoginDTO.getLoginType()
        );
        
        String userLoginAgent = request.getHeader("User-Agent");
        UserLogin userLoginDetail = userLoginService.getUserLoginDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userLoginDetail, token, isMobileDevice(userLoginAgent));

        UserLoginResponse userLoginResponse = UserLoginResponse.builder()
//                .message(localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY))
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(userLoginDetail.getUsername())
                .roles(userLoginDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userLoginDetail.getId())
                .build();
        
        return ResponseEntity.ok().body(ResponseObject.builder()
                        .message("Login successfully")
                        .data(userLoginResponse)
                        .status(HttpStatus.OK)
                .build());
    }
	
	@PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
    ) throws Exception {
        UserLogin userLoginDetail = userLoginService.getUserLoginDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userLoginDetail);
        UserLoginResponse userLoginResponse = UserLoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(userLoginDetail.getUsername())
                .roles(userLoginDetail.getAuthorities().stream().map(item -> item.getAuthority()).toList())
                .id(userLoginDetail.getId())
                .build();
        
        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .data(userLoginResponse)
                        .message(userLoginResponse.getMessage())
                        .status(HttpStatus.OK)
                        .build());
    }
	
	@PutMapping("/reset-password/{userLoginId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> resetPassword(@Valid @PathVariable long userLoginId){
        try {
            String newPassword = UUID.randomUUID().toString().substring(0, 5);
            userLoginService.resetPassword(userLoginId, newPassword);
            return ResponseEntity.ok(ResponseObject.builder()
                            .message("Reset password successfully")
                            .data(newPassword)
                            .status(HttpStatus.OK)
                    .build());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Invalid password")
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("User not found")
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
	
	@PutMapping("/change-password/{userLoginId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> changePassword(@Valid @PathVariable long userLoginId, @Valid @RequestBody ChangePasswordDTO changePasswordDTO){
        try {
        	UserLogin existingUserLogin = userLoginService.getUserLoginById(userLoginId);
        	
        	UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(existingUserLogin.getUsername(),
    				changePasswordDTO.getOldPassword());
    		
    		authenticationManager.authenticate(authenticationToken);
        	    		
        	if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getReNewPassword())) {
        		return ResponseEntity.ok(ResponseObject.builder()
        				.message("Newpassword must equals reNewPassword")
        				.data("")
        				.status(HttpStatus.BAD_REQUEST)
        				.build());
        	}
        	
        	String newPassword = changePasswordDTO.getNewPassword();
        	
            userLoginService.resetPassword(userLoginId, newPassword);
            return ResponseEntity.ok(ResponseObject.builder()
                            .message("Change password successfully")
                            .data(newPassword)
                            .status(HttpStatus.OK)
                    .build());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Invalid password")
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("User not found")
                    .data("")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }
    }
}
