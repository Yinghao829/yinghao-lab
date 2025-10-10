package com.hao.user.mapper;


import com.hao.common.annotation.AutoFill;
import com.hao.entity.User;
import com.hao.common.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @AutoFill(value = OperationType.INSERT)
    void save(User user);

    @Select("select id from lab_user_db.user where phone like #{phone}")
    Long getByPhone(String phone);
}
