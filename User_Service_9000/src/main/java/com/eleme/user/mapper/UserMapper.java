package com.eleme.user.mapper;

import com.eleme.user.model.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserDO selectByUsername(@Param("username") String username);

    UserDO selectById(@Param("id") Long id);

    int insert(UserDO user);
}
