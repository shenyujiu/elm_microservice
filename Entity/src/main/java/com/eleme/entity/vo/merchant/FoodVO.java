package com.eleme.entity.vo.merchant;

import java.math.BigDecimal;

public record FoodVO(
        Long id,
        Long merchantId,
        String name,
        String description,
        BigDecimal price,
        BigDecimal originPrice,
        String imageUrl,
        Integer sales,
        Integer sortNo
) {
}
