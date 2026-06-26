package com.eleme.cart.mapper;

import com.eleme.cart.model.CartItemDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartItemMapper {
    List<CartItemDO> selectByMerchant(@Param("userId") Long userId, @Param("merchantId") Long merchantId);

    CartItemDO selectByUniqueKey(@Param("userId") Long userId,
                                 @Param("merchantId") Long merchantId,
                                 @Param("foodId") Long foodId);

    CartItemDO selectByIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId);

    int insert(CartItemDO item);

    int update(CartItemDO item);

    int deleteById(@Param("itemId") Long itemId, @Param("userId") Long userId);

    int deleteByMerchant(@Param("userId") Long userId, @Param("merchantId") Long merchantId);
}
