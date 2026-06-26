package com.eleme.merchant.service;

import com.eleme.entity.enums.MerchantSortBy;
import com.eleme.entity.vo.merchant.CategoryVO;
import com.eleme.entity.vo.merchant.FoodVO;
import com.eleme.entity.vo.merchant.MerchantVO;
import com.eleme.merchant.mapper.CategoryMapper;
import com.eleme.merchant.mapper.FoodMapper;
import com.eleme.merchant.mapper.MerchantMapper;
import com.eleme.merchant.model.CategoryDO;
import com.eleme.merchant.model.FoodDO;
import com.eleme.merchant.model.MerchantDO;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {
    private final CategoryMapper categoryMapper;
    private final MerchantMapper merchantMapper;
    private final FoodMapper foodMapper;

    public MerchantService(CategoryMapper categoryMapper,
                           MerchantMapper merchantMapper,
                           FoodMapper foodMapper) {
        this.categoryMapper = categoryMapper;
        this.merchantMapper = merchantMapper;
        this.foodMapper = foodMapper;
    }

    public List<CategoryVO> listCategories() {
        return categoryMapper.selectAll().stream()
                .map(this::toCategoryVO)
                .toList();
    }

    public List<MerchantVO> listMerchants(Long categoryId, String keyword, MerchantSortBy sortBy) {
        List<MerchantVO> merchants = merchantMapper.selectByCondition(categoryId, keyword == null ? null : keyword.trim()).stream()
                .map(this::toMerchantVO)
                .toList();

        Comparator<MerchantVO> comparator = Comparator.comparing(MerchantVO::id);
        if (sortBy != null) {
            comparator = switch (sortBy) {
                case sales -> Comparator.comparing(MerchantVO::monthlySales, Comparator.nullsLast(Integer::compareTo)).reversed();
                case rating -> Comparator.comparing(MerchantVO::rating, Comparator.nullsLast(Double::compareTo)).reversed();
                case deliveryTime -> Comparator.comparing(MerchantVO::deliveryTimeMin, Comparator.nullsLast(Integer::compareTo));
                case deliveryFee -> Comparator.comparing(MerchantVO::deliveryFee, Comparator.nullsLast(java.math.BigDecimal::compareTo));
                default -> Comparator.comparing(MerchantVO::id);
            };
        }
        return merchants.stream().sorted(comparator).toList();
    }

    public MerchantVO getMerchant(Long merchantId) {
        MerchantDO merchant = merchantMapper.selectById(merchantId);
        return merchant == null ? null : toMerchantVO(merchant);
    }

    public List<FoodVO> listFoods(Long merchantId) {
        return foodMapper.selectByMerchantId(merchantId).stream()
                .map(this::toFoodVO)
                .toList();
    }

    private CategoryVO toCategoryVO(CategoryDO category) {
        Long id = category.getId();
        String name = switch (id == null ? 0 : id.intValue()) {
            case 1 -> "美食";
            case 2 -> "早餐";
            case 3 -> "跑腿代购";
            case 4 -> "汉堡披萨";
            case 5 -> "甜品饮品";
            case 6 -> "速食简餐";
            case 7 -> "地方小吃";
            case 8 -> "米粉面食";
            case 9 -> "包子粥铺";
            case 10 -> "烧烤炸串";
            default -> category.getName() != null ? category.getName() : "分类" + id;
        };
        String icon = switch (id == null ? 0 : id.intValue()) {
            case 1 -> "🍱";
            case 2 -> "🥐";
            case 3 -> "🛵";
            case 4 -> "🍔";
            case 5 -> "🥤";
            case 6 -> "🍕";
            case 7 -> "🍜";
            case 8 -> "🍝";
            case 9 -> "🥟";
            case 10 -> "🍢";
            default -> "🍽️";
        };
        return new CategoryVO(id, name, icon, id == null ? 0 : id.intValue());
    }

    private MerchantVO toMerchantVO(MerchantDO merchant) {
        return new MerchantVO(
                merchant.getId(),
                merchant.getName(),
                merchant.getCategoryId(),
                merchant.getCoverUrl(),
                merchant.getRating(),
                merchant.getMonthlySales(),
                merchant.getDeliveryFee(),
                merchant.getStartPrice(),
                merchant.getDistanceKm(),
                merchant.getDeliveryTimeMin(),
                merchant.getAnnouncement()
        );
    }

    private FoodVO toFoodVO(FoodDO food) {
        return new FoodVO(
                food.getId(),
                food.getMerchantId(),
                food.getName(),
                food.getDescription(),
                food.getPrice(),
                food.getOriginPrice(),
                food.getImageUrl(),
                food.getSales(),
                food.getSortNo()
        );
    }
}
