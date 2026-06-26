package com.eleme.payment.controller;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.dto.payment.PaymentCreateRequest;
import com.eleme.entity.vo.payment.PaymentVO;
import com.eleme.payment.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ApiResponse<PaymentVO> create(@RequestBody @Valid PaymentCreateRequest request) {
        return ApiResponse.ok(paymentService.create(request));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<PaymentVO> getByOrderId(@PathVariable("orderId") Long orderId) {
        return ApiResponse.ok(paymentService.getByOrderId(orderId));
    }

    @PostMapping("/callback/mock")
    public ApiResponse<Void> mockCallback(@RequestBody @Valid MockCallbackRequest request) {
        if (!paymentService.mockCallback(request.getOrderId(), request.isSuccess())) {
            return ApiResponse.fail(404, "支付单不存在");
        }
        return ApiResponse.ok();
    }

    public static final class MockCallbackRequest {
        @NotNull
        private Long orderId;
        private boolean success;

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
