package com.eleme.merchant.controller;

import com.eleme.entity.common.ApiResponse;
import com.eleme.entity.enums.MerchantSortBy;
import com.eleme.entity.vo.merchant.CategoryVO;
import com.eleme.entity.vo.merchant.FoodVO;
import com.eleme.entity.vo.merchant.MerchantVO;
import java.util.List;
import com.eleme.merchant.service.MerchantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @GetMapping("/api/categories")
    public ApiResponse<List<CategoryVO>> listCategories() {
        return ApiResponse.ok(merchantService.listCategories());
    }

    @GetMapping("/api/merchants")
    public ApiResponse<List<MerchantVO>> listMerchants(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                                       @RequestParam(value = "keyword", required = false) String keyword,
                                                       @RequestParam(value = "sortBy", required = false) MerchantSortBy sortBy) {
        return ApiResponse.ok(merchantService.listMerchants(categoryId, keyword, sortBy));
    }

    @GetMapping("/api/merchants/{merchantId}")
    public ApiResponse<MerchantVO> getMerchant(@PathVariable("merchantId") Long merchantId) {
        return ApiResponse.ok(merchantService.getMerchant(merchantId));
    }

    @GetMapping("/api/merchants/{merchantId}/foods")
    public ApiResponse<List<FoodVO>> listFoods(@PathVariable("merchantId") Long merchantId) {
        return ApiResponse.ok(merchantService.listFoods(merchantId));
    }
}

