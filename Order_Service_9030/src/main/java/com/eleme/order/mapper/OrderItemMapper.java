package com.eleme.order.mapper;

import com.eleme.order.model.OrderItemDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderItemMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "odId")
    int insert(OrderItemDO orderItem);

    List<OrderItemDO> selectByOrderId(@Param("orderId") Long orderId);
}
