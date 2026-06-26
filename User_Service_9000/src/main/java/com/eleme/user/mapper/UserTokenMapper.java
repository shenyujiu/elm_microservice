package com.eleme.user.mapper;

import com.eleme.user.model.UserTokenDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserTokenMapper {
    int insert(UserTokenDO userToken);

    UserTokenDO selectByToken(@Param("token") String token);
}
