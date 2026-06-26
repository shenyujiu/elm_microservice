package com.eleme.order.mapper;

import com.eleme.order.model.OrderInfoDO;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderInfoMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "orderId")
    int insert(OrderInfoDO orderInfo);

    OrderInfoDO selectById(@Param("orderId") Long orderId);

    List<OrderInfoDO> selectByUserId(@Param("userId") Long userId);

    int updatePayStatus(@Param("orderId") Long orderId, @Param("status") String status, @Param("paidAt") LocalDateTime paidAt);
}
