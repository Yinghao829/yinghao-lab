package com.hao.user.service.impl;

import com.hao.common.constant.MessageConstant;
import com.hao.common.exception.UserException;
import com.hao.dto.UserRegisterDTO;
import com.hao.entity.User;
import com.hao.common.exception.PhoneException;
import com.hao.common.exception.VerifyCodeException;
import com.hao.user.mapper.UserMapper;
import com.hao.user.service.UserService;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(UserRegisterDTO userRegisterDTO) {
        // 校验验证码
        String redisKey = "sms:code:" + userRegisterDTO.getPhone();
        String code = (String) redisTemplate.opsForValue().get(redisKey);
        if (code == null) {
            throw new VerifyCodeException(MessageConstant.VERIFY_CODE_NOT_FOUND);
        }
        if (!code.equals(userRegisterDTO.getVerifyCode())) {
            throw new VerifyCodeException(MessageConstant.VERIFY_CODE_ERROR);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setPassword(DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes()));

        userMapper.save(user);
    }

    @Override
    public void sendCode(String phone) {

        // 查询手机号是否已经被注册
        User user = userMapper.getByPhone(phone);
        if (user != null ) {
            throw new PhoneException(MessageConstant.PHONE_EXISTED);
        }
        if (!isValidPhone(phone)) {
            // 抛出异常 “手机号为空或格式不正确”
            throw new PhoneException(MessageConstant.PHONE_ERROR);
        }

        // 随机生成6位验证码
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("验证码为：" + code);

        String redisKey = "sms:code:" + phone;

        redisTemplate.opsForValue().set(redisKey, code, 5, TimeUnit.MINUTES);

    }

    @Override
    public User getUserByPhoneWithRole(String phone, Integer role) {
        User user = userMapper.getByPhone(phone);
        if (user == null) {
            throw new UserException(MessageConstant.USER_NOT_EXIST);
        }
        return user;
    }

    @Override
    public User getUserByNumberWithRole(String number, Integer role) {
        User user = userMapper.getByNumber(number);
        return user;
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
