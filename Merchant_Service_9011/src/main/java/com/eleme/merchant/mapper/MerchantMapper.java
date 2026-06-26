package com.eleme.merchant.mapper;

import com.eleme.merchant.model.MerchantDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MerchantMapper {
    List<MerchantDO> selectByCondition(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);

    MerchantDO selectById(@Param("merchantId") Long merchantId);
}
