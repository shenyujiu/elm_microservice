package com.eleme.entity.vo.order;

import com.eleme.entity.vo.cart.CartItemVO;
import com.eleme.entity.vo.cart.CartSummaryVO;
import com.eleme.entity.vo.merchant.MerchantVO;
import com.eleme.entity.vo.user.AddressVO;
import java.util.List;

public record OrderConfirmVO(
        Long userId,
        Long merchantId,
        MerchantVO merchant,
        AddressVO address,
        List<CartItemVO> cartItems,
        CartSummaryVO summary
) {
}
