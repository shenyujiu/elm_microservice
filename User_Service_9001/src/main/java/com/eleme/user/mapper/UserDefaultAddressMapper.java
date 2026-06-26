package com.eleme.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDefaultAddressMapper {
    @Select("""
            SELECT address_id
            FROM user_default_address
            WHERE user_id = #{userId}
            LIMIT 1
            """)
    Long selectDefaultAddressId(@Param("userId") Long userId);

    int upsertDefault(@Param("userId") Long userId, @Param("addressId") Long addressId);

    int deleteByUserId(@Param("userId") Long userId);
}
