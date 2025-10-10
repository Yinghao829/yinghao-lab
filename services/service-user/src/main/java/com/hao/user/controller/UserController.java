package com.hao.user.controller;

import com.hao.dto.UserRegisterDTO;
import com.hao.common.result.Result;
import com.hao.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@Slf4j
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

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
}
