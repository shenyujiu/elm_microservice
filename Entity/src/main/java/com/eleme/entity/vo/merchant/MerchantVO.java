package com.eleme.entity.vo.merchant;

import java.math.BigDecimal;

public record MerchantVO(
        Long id,
        String name,
        Long categoryId,
        String coverUrl,
        Double rating,
        Integer monthlySales,
        BigDecimal deliveryFee,
        BigDecimal startPrice,
        Double distanceKm,
        Integer deliveryTimeMin,
        String announcement
) {
}
