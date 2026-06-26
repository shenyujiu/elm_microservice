package com.eleme.payment.mapper;

import com.eleme.payment.model.PaymentLogDO;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PaymentLogMapper {
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(PaymentLogDO paymentLog);

    PaymentLogDO selectByOrderId(@Param("orderId") Long orderId);

    int updateStatus(@Param("orderId") Long orderId,
                     @Param("payStatus") String payStatus,
                     @Param("transactionNo") String transactionNo,
                     @Param("callbackAt") LocalDateTime callbackAt);
}
