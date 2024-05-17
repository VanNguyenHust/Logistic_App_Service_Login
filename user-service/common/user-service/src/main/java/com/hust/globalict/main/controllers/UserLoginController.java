package com.hust.globalict.main.controllers;

import java.util.Random;
import java.util.UUID;

import com.hust.globalict.main.constants.PatternConstants;
import com.hust.globalict.main.constants.StatusRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.dtos.ChangePasswordDTO;
import com.hust.globalict.main.dtos.RefreshTokenDTO;
import com.hust.globalict.main.dtos.UserLoginDTO;
import com.hust.globalict.main.constants.Uri;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.exceptions.InvalidPasswordException;
import com.hust.globalict.main.mappers.UserLoginMapper;
import com.hust.globalict.main.mappers.UserMapper;
import com.hust.globalict.main.modules.Token;
import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.modules.UserLogin;
import com.hust.globalict.main.responses.LoginResponse;
import com.hust.globalict.main.responses.UserLoginResponse;
import com.hust.globalict.main.services.role.IRoleService;
import com.hust.globalict.main.services.token.ITokenService;
import com.hust.globalict.main.services.user.IUserService;
import com.hust.globalict.main.services.userlogin.IUserLoginService;
import com.hust.globalict.main.utils.MessageKeys;
import com.hust.globalict.main.utils.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.hust.globalict.main.constants.PatternConstants.SPECIAL_CHARACTERS;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Uri.USER_LOGIN)
public class UserLoginController {
    private final IUserService userService;
    private final IRoleService roleService;
    private final IUserLoginService userLoginService;
    private final UserLoginMapper userLoginMapper;

    private final ITokenService tokenService;
    private final AuthenticationManager authenticationManager;

    private final LocalizationUtils localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> createUserLogin(@Valid @RequestBody UserLoginDTO userLoginDTO)
            throws Exception {
        if (!userLoginDTO.getPassword().equals(userLoginDTO.getRetypePassword())) {
            throw new BadCredentialsException(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_PASSWORD_NOT_MATCH));
        }

        UserLogin newUserLogin = userLoginMapper.mapToUserLoginEntity(userLoginDTO);

        if (userLoginDTO.getUserId() != null) {
            User user = userService.getUserById(userLoginDTO.getUserId());
            newUserLogin.setUser(user);
        } else {
            User newUser = new User();
            userService.createUser(newUser);
            newUserLogin.setUser(newUser);
        }

        if (userLoginDTO.getRoleId() == null) {
            newUserLogin.setRole(roleService.getRoleByName(StatusRole.USER.toLowerCase()));
        } else {
            newUserLogin.setRole(roleService.getRoleById(userLoginDTO.getRoleId()));
        }

        userLoginService.createUserLogin(newUserLogin);

        UserLoginResponse userLoginResponse = userLoginMapper.mapTopUserLoginResponse(newUserLogin);

        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_CREATE_SUCCESSFULLY))
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
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody UserLoginDTO userLoginDTO,
                                                HttpServletRequest request) throws Exception {
        // Kiểm tra thông tin đăng nhập và sinh token
        String token = userLoginService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword(),
                userLoginDTO.getLoginType());

        String userLoginAgent = request.getHeader("User-Agent");
        UserLogin userLoginDetail = userLoginService.getUserLoginDetailsFromToken(token, userLoginDTO.getLoginType());
        Token jwtToken = tokenService.addToken(userLoginDetail, token, isMobileDevice(userLoginAgent));

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(userLoginDetail.getUsername())
                .roles(userLoginDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(jwtToken.getId()).build();

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_LOGIN_SUCCESSFULLY))
                        .data(loginResponse)
                        .status(HttpStatus.OK)
                        .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseObject> refreshToken(@Valid @RequestBody RefreshTokenDTO refreshTokenDTO)
            throws Exception {
        UserLogin userLoginDetail = userLoginService.getUserLoginDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken(), refreshTokenDTO.getTokenType());

        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userLoginDetail);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .userName(userLoginDetail.getUsername())
                .roles(userLoginDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(jwtToken.getId())
                .build();

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .data(loginResponse)
                        .message(localizationUtils.getLocalizedMessage(MessageKeys.REFRESH_TOKEN_SUCCESSFULLY))
                        .status(HttpStatus.OK)
                        .build());
    }

    public static String generatePassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Sinh một ký tự chữ hoa ngẫu nhiên cho ký tự đầu tiên
        password.append((char) (random.nextInt(26) + 'A'));

        // Sử dụng lambda để chọn một ký tự đặc biệt cho ký tự thứ hai
        char randomChar = SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()));
        password.append(randomChar);

        // Sinh các ký tự tiếp theo ngẫu nhiên
        for (int i = 2; i < length; i++) {
            // Sinh một ký tự ngẫu nhiên
            randomChar = (char) (random.nextInt(26) + 'a');
            password.append(randomChar);
        }

        return password.toString();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(@RequestHeader(name = "user_login_id") long userLoginId)
            throws Exception {
        try {
            String newPassword = generatePassword(6);
            userLoginService.resetPassword(userLoginId, newPassword);

            return ResponseEntity.ok(ResponseObject.builder()
                    .message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_RESET_PASSWORD_SUCCESSFULLY))
                    .data(newPassword)
                    .status(HttpStatus.OK)
                    .build());

        } catch (InvalidPasswordException e) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Invalid password")
                    .data("")
                    .build());
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_NOT_FOUND));
        }
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> changePassword(@RequestHeader(name = "user_login_id") long userLoginId,
                                                         @Valid @RequestBody ChangePasswordDTO changePasswordDTO) throws Exception {
        UserLogin existingUserLogin = userLoginService.getUserLoginById(userLoginId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                existingUserLogin.getUsername(), changePasswordDTO.getOldPassword());

        authenticationManager.authenticate(authenticationToken);

        String newPassword = changePasswordDTO.getNewPassword();

        userLoginService.resetPassword(userLoginId, newPassword);

        return ResponseEntity.ok(ResponseObject.builder()
                .message(localizationUtils.getLocalizedMessage(MessageKeys.USER_LOGIN_CHANGE_PASSWORD_SUCCESSFULLY))
                .data(newPassword)
                .status(HttpStatus.OK)
                .build());
    }
}
