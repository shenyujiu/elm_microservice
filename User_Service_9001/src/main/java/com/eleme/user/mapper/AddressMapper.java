package com.eleme.user.mapper;

import com.eleme.user.model.AddressDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AddressMapper {
    int insert(AddressDO address);

    List<AddressDO> selectByUserId(@Param("userId") Long userId);

    AddressDO selectById(@Param("userId") Long userId, @Param("addressId") Long addressId);

    AddressDO selectFirstByUserId(@Param("userId") Long userId);

    AddressDO selectDefaultByUserId(@Param("userId") Long userId);

    int clearDefaultByUserId(@Param("userId") Long userId);

    int update(AddressDO address);

    int updateDefaultById(@Param("userId") Long userId, @Param("addressId") Long addressId);

    int deleteById(@Param("userId") Long userId, @Param("addressId") Long addressId);
}
