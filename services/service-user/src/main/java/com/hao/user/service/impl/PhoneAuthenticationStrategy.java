package com.hao.user.service.impl;

import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.PhoneException;
import com.hao.dto.LoginRequest;
import com.hao.entity.User;
import com.hao.user.service.AuthenticationStrategy;
import com.hao.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Qualifier("phoneAuth")
public class PhoneAuthenticationStrategy implements AuthenticationStrategy {
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public User authenticate(LoginRequest request) {
        // 参数校验
        if (!isValidPhone(request.getPhone())) throw new PhoneException(MessageConstant.PHONE_ERROR);
        if (request.getVerifyCode().length() != 6) throw new PhoneException(MessageConstant.VERIFY_CODE_ERROR);
        String redisKey = "sms:code:" + request.getPhone();
        String verifyCode = (String) redisTemplate.opsForValue().get(redisKey);
        if (verifyCode == null) throw new PhoneException(MessageConstant.VERIFY_CODE_ERROR);
        if (!verifyCode.equals(request.getVerifyCode())) throw new PhoneException(MessageConstant.VERIFY_CODE_ERROR);
        return userService.getUserByPhoneWithRole(request.getPhone(), request.getRole());
    }

    private boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        phone = phone.trim();
        if (!phone.matches("\\d+")) return false;
        return phone.matches(PHONE_REGEX);
    }
}
