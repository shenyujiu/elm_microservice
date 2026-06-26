package com.eleme.merchant.mapper;

import com.eleme.merchant.model.FoodDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodMapper {
    List<FoodDO> selectByMerchantId(@Param("merchantId") Long merchantId);
}
