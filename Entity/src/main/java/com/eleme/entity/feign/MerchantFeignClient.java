package com.eleme.entity.feign;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.vo.merchant.MerchantVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "eleme-merchant-service", contextId = "merchantFeignClient")
public interface MerchantFeignClient {
    @GetMapping("/api/merchants/{merchantId}")
    ApiResponse<MerchantVO> getMerchant(@PathVariable("merchantId") Long merchantId);
}