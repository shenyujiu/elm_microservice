package com.eleme.payment.service;

import com.eleme.entity.dto.order.PayStatusUpdateRequest;
import com.eleme.entity.dto.payment.PaymentCreateRequest;
import com.eleme.entity.enums.OrderStatus;
import com.eleme.entity.enums.PaymentStatus;
import com.eleme.entity.feign.OrderFeignClient;
import com.eleme.entity.vo.payment.PaymentVO;
import com.eleme.payment.mapper.PaymentLogMapper;
import com.eleme.payment.model.PaymentLogDO;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    private final PaymentLogMapper paymentLogMapper;
    private final OrderFeignClient orderFeignClient;

    public PaymentService(PaymentLogMapper paymentLogMapper, OrderFeignClient orderFeignClient) {
        this.paymentLogMapper = paymentLogMapper;
        this.orderFeignClient = orderFeignClient;
    }

    @Transactional
    public PaymentVO create(PaymentCreateRequest request) {
        PaymentLogDO paymentLog = paymentLogMapper.selectByOrderId(request.orderId());
        if (paymentLog == null) {
            paymentLog = new PaymentLogDO();
            paymentLog.setOrderId(request.orderId());
            paymentLog.setOrderNo(request.orderNo());
            paymentLog.setPayChannel(request.payChannel() == null ? "MOCK" : request.payChannel());
            paymentLog.setPayStatus(PaymentStatus.INIT.name());
            paymentLog.setAmount(request.amount());
            paymentLogMapper.insert(paymentLog);
        }
        return toPaymentVO(paymentLogMapper.selectByOrderId(request.orderId()));
    }

    public PaymentVO getByOrderId(Long orderId) {
        PaymentLogDO paymentLog = paymentLogMapper.selectByOrderId(orderId);
        return paymentLog == null ? null : toPaymentVO(paymentLog);
    }

    @Transactional
    public boolean mockCallback(Long orderId, boolean success) {
        PaymentLogDO existing = paymentLogMapper.selectByOrderId(orderId);
        if (existing == null) {
            return false;
        }

        PaymentStatus status = success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        paymentLogMapper.updateStatus(
                orderId,
                status.name(),
                UUID.randomUUID().toString().replace("-", ""),
                LocalDateTime.now()
        );

        if (success) {
            orderFeignClient.updatePayStatus(orderId, new PayStatusUpdateRequest(OrderStatus.PAID));
        }
        return true;
    }

    private PaymentVO toPaymentVO(PaymentLogDO paymentLog) {
        return new PaymentVO(
                paymentLog.getId(),
                paymentLog.getOrderId(),
                paymentLog.getOrderNo(),
                paymentLog.getPayChannel(),
                PaymentStatus.valueOf(paymentLog.getPayStatus()),
                paymentLog.getTransactionNo(),
                paymentLog.getAmount(),
                toOffsetDateTime(paymentLog.getCreatedAt()),
                toOffsetDateTime(paymentLog.getCallbackAt())
        );
    }

    private OffsetDateTime toOffsetDateTime(LocalDateTime time) {
        return time == null ? null : time.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
}
