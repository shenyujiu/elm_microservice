package com.eleme.user.controller;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.dto.user.AddressSaveRequest;
import com.eleme.entity.dto.user.UserLoginRequest;
import com.eleme.entity.dto.user.UserRegisterRequest;
import com.eleme.entity.vo.user.AddressVO;
import com.eleme.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<Long> register(@RequestBody @Valid UserRegisterRequest request) {
        Long userId = userService.register(request);
        if (userId == null) {
            return ApiResponse.fail(40001, "用户名已存在");
        }
        return ApiResponse.ok(userId);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid UserLoginRequest request) {
        String token = userService.login(request);
        if (token == null) {
            return ApiResponse.fail(40002, "用户名或密码错误");
        }
        return ApiResponse.ok(token);
    }

    @GetMapping("/me")
    public ApiResponse<Long> me(@RequestHeader(value = "X-Token", required = false) String token,
                                @RequestHeader(value = "Authorization", required = false) String authorization) {
        Long userId = userService.resolveUserId(token, authorization);
        if (userId == null) {
            return ApiResponse.fail(401, "未登录");
        }
        return ApiResponse.ok(userId);
    }

    @GetMapping("/{userId}/addresses/default")
    public ApiResponse<AddressVO> getDefaultAddress(@PathVariable("userId") Long userId) {
        return ApiResponse.ok(userService.getDefaultAddress(userId));
    }

    @GetMapping("/{userId}/addresses")
    public ApiResponse<List<AddressVO>> listAddresses(@PathVariable("userId") Long userId) {
        return ApiResponse.ok(userService.listAddresses(userId));
    }

    @GetMapping("/{userId}/addresses/{addressId}")
    public ApiResponse<AddressVO> getAddress(@PathVariable("userId") Long userId,
                                             @PathVariable("addressId") Long addressId) {
        AddressVO address = userService.getAddress(userId, addressId);
        if (address == null) {
            return ApiResponse.fail(404, "地址不存在");
        }
        return ApiResponse.ok(address);
    }

    @PostMapping("/{userId}/addresses")
    public ApiResponse<Long> createAddress(@PathVariable("userId") Long userId,
                                           @RequestBody @Valid AddressSaveRequest request) {
        return ApiResponse.ok(userService.createAddress(userId, request));
    }

    @PutMapping("/{userId}/addresses/{addressId}")
    public ApiResponse<Void> updateAddress(@PathVariable("userId") Long userId,
                                           @PathVariable("addressId") Long addressId,
                                           @RequestBody @Valid AddressSaveRequest request) {
        if (!userService.updateAddress(userId, addressId, request)) {
            return ApiResponse.fail(404, "地址不存在");
        }
        return ApiResponse.ok();
    }

    @PutMapping("/{userId}/addresses/{addressId}/default")
    public ApiResponse<Void> setDefaultAddress(@PathVariable("userId") Long userId,
                                               @PathVariable("addressId") Long addressId) {
        if (!userService.setDefaultAddress(userId, addressId)) {
            return ApiResponse.fail(404, "地址不存在");
        }
        return ApiResponse.ok();
    }

    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ApiResponse<Void> deleteAddress(@PathVariable("userId") Long userId,
                                           @PathVariable("addressId") Long addressId) {
        if (!userService.deleteAddress(userId, addressId)) {
            return ApiResponse.fail(404, "地址不存在");
        }
        return ApiResponse.ok();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @PostMapping("/_unauthorized")
    public void unauthorized() {
    }
}

