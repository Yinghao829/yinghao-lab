package com.hao.user.service.impl;

import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.UserException;
import com.hao.dto.LoginRequest;
import com.hao.entity.User;
import com.hao.user.service.AuthenticationStrategy;
import com.hao.user.service.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Qualifier("numberAuth")
public class NumberAuthenticationStrategy implements AuthenticationStrategy {

    @Autowired
    private UserService userService;

    @Override
    public User authenticate(LoginRequest request) {
        // 参数校验
        if (StringUtils.isEmpty(request.getNumber())) throw new UserException(MessageConstant.NUMBER_IS_NULL);
        if (StringUtils.isEmpty(request.getPassword())) throw new UserException(MessageConstant.PASSWORD_IS_NULL);
        // 查询用户
        User user = userService.getUserByNumberWithRole(request.getNumber(), request.getRole());
        if (user == null) throw new UserException(MessageConstant.USER_NOT_EXIST);
        // 校验密码
        String password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!password.equals(user.getPassword())) throw new UserException(MessageConstant.PASSWORD_ERROR);
        return user;
    }
}
