package com.eleme.order.service;

import com.eleme.entity.dto.order.OrderCreateRequest;
import com.eleme.entity.enums.OrderStatus;
import com.eleme.entity.feign.CartFeignClient;
import com.eleme.entity.feign.MerchantFeignClient;
import com.eleme.entity.feign.UserFeignClient;
import com.eleme.entity.vo.cart.CartItemVO;
import com.eleme.entity.vo.cart.CartSummaryVO;
import com.eleme.entity.vo.merchant.MerchantVO;
import com.eleme.entity.vo.order.OrderConfirmVO;
import com.eleme.entity.vo.order.OrderItemVO;
import com.eleme.entity.vo.order.OrderVO;
import com.eleme.entity.vo.user.AddressVO;
import com.eleme.order.mapper.OrderInfoMapper;
import com.eleme.order.mapper.OrderItemMapper;
import com.eleme.order.model.OrderInfoDO;
import com.eleme.order.model.OrderItemDO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {
    private final UserFeignClient userFeignClient;
    private final CartFeignClient cartFeignClient;
    private final MerchantFeignClient merchantFeignClient;
    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderService(UserFeignClient userFeignClient,
                        CartFeignClient cartFeignClient,
                        MerchantFeignClient merchantFeignClient,
                        OrderInfoMapper orderInfoMapper,
                        OrderItemMapper orderItemMapper) {
        this.userFeignClient = userFeignClient;
        this.cartFeignClient = cartFeignClient;
        this.merchantFeignClient = merchantFeignClient;
        this.orderInfoMapper = orderInfoMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderConfirmVO confirm(Long userId, Long merchantId) {
        AddressVO address = userFeignClient.getDefaultAddress(userId).data();
        List<CartItemVO> cartItems = cartFeignClient.getMerchantCartItems(userId, merchantId).data();
        CartSummaryVO summary = cartFeignClient.getMerchantCartSummary(userId, merchantId).data();
        MerchantVO merchant = merchantFeignClient.getMerchant(merchantId).data();
        return new OrderConfirmVO(userId, merchantId, merchant, address, cartItems, summary);
    }

    @Transactional
    public OrderVO create(OrderCreateRequest request) {
        List<CartItemVO> cartItems = cartFeignClient.getMerchantCartItems(request.userId(), request.merchantId()).data();
        if (CollectionUtils.isEmpty(cartItems)) {
            return null;
        }

        CartSummaryVO summary = cartFeignClient.getMerchantCartSummary(request.userId(), request.merchantId()).data();
        MerchantVO merchant = merchantFeignClient.getMerchant(request.merchantId()).data();

        BigDecimal goodsAmount = summary == null ? BigDecimal.ZERO : summary.goodsAmount();
        BigDecimal deliveryFee = merchant == null || merchant.deliveryFee() == null ? BigDecimal.ZERO : merchant.deliveryFee();
        BigDecimal payAmount = goodsAmount.add(deliveryFee);

        OrderInfoDO orderInfo = new OrderInfoDO();
        orderInfo.setUserId(request.userId());
        orderInfo.setMerchantId(request.merchantId());
        orderInfo.setMerchantName(merchant == null ? null : merchant.name());
        orderInfo.setAddressId(request.addressId());
        orderInfo.setGoodsAmount(goodsAmount);
        orderInfo.setDeliveryFee(deliveryFee);
        orderInfo.setPayAmount(payAmount);
        orderInfo.setStatus(OrderStatus.UNPAID.name());
        orderInfo.setRemark(request.remark());
        orderInfo.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        orderInfoMapper.insert(orderInfo);

        for (CartItemVO cartItem : cartItems) {
            OrderItemDO item = new OrderItemDO();
            item.setOrderId(orderInfo.getId());
            item.setFoodId(cartItem.foodId());
            item.setFoodName(cartItem.foodName());
            item.setFoodPrice(cartItem.foodPrice());
            item.setQuantity(cartItem.quantity());
            item.setAmount(cartItem.foodPrice().multiply(BigDecimal.valueOf(cartItem.quantity())));
            orderItemMapper.insert(item);
        }

        cartFeignClient.clearMerchantCart(request.userId(), request.merchantId());
        return getOrder(orderInfo.getId());
    }

    public OrderVO getOrder(Long orderId) {
        OrderInfoDO orderInfo = orderInfoMapper.selectById(orderId);
        return orderInfo == null ? null : toOrderVO(orderInfo);
    }

    public List<OrderVO> listUserOrders(Long userId) {
        return orderInfoMapper.selectByUserId(userId).stream()
                .map(this::toOrderVO)
                .toList();
    }

    @Transactional
    public boolean updatePayStatus(Long orderId, OrderStatus status) {
        OrderInfoDO existing = orderInfoMapper.selectById(orderId);
        if (existing == null) {
            return false;
        }
        LocalDateTime paidAt = status == OrderStatus.PAID ? LocalDateTime.now() : existing.getPaidAt();
        orderInfoMapper.updatePayStatus(orderId, status.name(), paidAt);
        return true;
    }

    private OrderVO toOrderVO(OrderInfoDO orderInfo) {
        List<OrderItemVO> items = orderItemMapper.selectByOrderId(orderInfo.getId()).stream()
                .map(item -> new OrderItemVO(
                        item.getId(),
                        item.getFoodId(),
                        item.getFoodName(),
                        item.getFoodPrice(),
                        item.getQuantity(),
                        item.getAmount()
                ))
                .toList();

        return new OrderVO(
                orderInfo.getId(),
                orderInfo.getOrderNo(),
                orderInfo.getUserId(),
                orderInfo.getMerchantId(),
                orderInfo.getMerchantName(),
                orderInfo.getGoodsAmount(),
                orderInfo.getDeliveryFee(),
                orderInfo.getPayAmount(),
                OrderStatus.valueOf(orderInfo.getStatus()),
                toOffsetDateTime(orderInfo.getCreatedAt()),
                toOffsetDateTime(orderInfo.getPaidAt()),
                items
        );
    }

    private OffsetDateTime toOffsetDateTime(LocalDateTime time) {
        return time == null ? null : time.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }
}
