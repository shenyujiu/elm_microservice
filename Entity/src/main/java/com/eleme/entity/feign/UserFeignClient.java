package com.eleme.entity.feign;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.vo.user.AddressVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "eleme-user-service", contextId = "userFeignClient")
public interface UserFeignClient {
    @GetMapping("/api/users/{userId}/addresses/default")
    ApiResponse<AddressVO> getDefaultAddress(@PathVariable("userId") Long userId);
}