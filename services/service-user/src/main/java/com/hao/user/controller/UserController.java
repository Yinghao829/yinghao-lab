package com.hao.user.controller;

import com.hao.common.constant.JwtClaimsConstant;
import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.UserException;
import com.hao.common.properties.JwtProperties;
import com.hao.common.utils.JwtUtil;
import com.hao.dto.LoginRequest;
import com.hao.dto.UserRegisterDTO;
import com.hao.common.result.Result;
import com.hao.entity.User;
import com.hao.user.service.AuthService;
import com.hao.user.service.UserService;
import com.hao.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("新增用户:{}", userRegisterDTO);
        userService.save(userRegisterDTO);
        return Result.success();
    }

    @PostMapping("/sendCode")
    @ApiOperation("获取验证码")
    public Result sendCode(@RequestParam String phone) {
        userService.sendCode(phone);
        return Result.success();
    }

    @PostMapping("/login/auth")
    public Result<UserLoginVO> login(@RequestBody LoginRequest loginRequest) {
        User user = authService.login(loginRequest);
        if (user == null) throw new UserException(MessageConstant.USER_NOT_EXIST);
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        claims.put(JwtClaimsConstant.USER_ROLE, user.getRole());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .number(user.getNumber())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

}
