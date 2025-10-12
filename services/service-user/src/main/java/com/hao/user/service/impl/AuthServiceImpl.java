package com.hao.user.service.impl;

import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.UserException;
import com.hao.dto.LoginRequest;
import com.hao.entity.User;
import com.hao.user.service.AuthService;
import com.hao.user.service.AuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private Map<String, AuthenticationStrategy> strategies;

    @Override
    public User login(LoginRequest request) {
        String loginType = request.getLoginType(); // "phone"表示手机号登录，"number"表示学号登录
        // 构造策略Bean的名族
        String strategyBeanName = buildStrategyBeanName(loginType);
        // 从Spring容器中获取对应的策略
        AuthenticationStrategy strategy = strategies.get(strategyBeanName);
        if (strategy == null) {
            throw new UserException(MessageConstant.LOGIN_ERROR);
        }
        return strategy.authenticate(request);
    }

    private String buildStrategyBeanName(String loginType) {
        return loginType + "AuthenticationStrategy";
    }
}
