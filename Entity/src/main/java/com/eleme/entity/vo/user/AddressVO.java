package com.eleme.entity.vo.user;

public record AddressVO(
        Long id,
        Long userId,
        String contactName,
        String contactPhone,
        String province,
        String city,
        String district,
        String detail,
        String tag,
        Boolean isDefault
) {
}
