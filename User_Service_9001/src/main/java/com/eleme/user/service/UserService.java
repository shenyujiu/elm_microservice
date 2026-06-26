package com.eleme.user.service;

import com.eleme.entity.dto.user.AddressSaveRequest;
import com.eleme.entity.dto.user.UserLoginRequest;
import com.eleme.entity.dto.user.UserRegisterRequest;
import com.eleme.entity.vo.user.AddressVO;
import com.eleme.user.mapper.AddressMapper;
import com.eleme.user.mapper.UserMapper;
import com.eleme.user.mapper.UserTokenMapper;
import com.eleme.user.model.AddressDO;
import com.eleme.user.model.UserDO;
import com.eleme.user.model.UserTokenDO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final AddressMapper addressMapper;
    private final UserTokenMapper userTokenMapper;

    public UserService(UserMapper userMapper,
                       AddressMapper addressMapper,
                       UserTokenMapper userTokenMapper) {
        this.userMapper = userMapper;
        this.addressMapper = addressMapper;
        this.userTokenMapper = userTokenMapper;
    }

    @Transactional
    public Long register(UserRegisterRequest request) {
        String username = request.username().trim();
        if (userMapper.selectByUsername(username) != null) {
            return null;
        }

        Long userId = parseUserId(username);
        UserDO user = new UserDO();
        user.setId(userId);
        user.setUsername(username);
        user.setPassword(request.password());
        user.setPhone(request.phone());
        user.setNickname(StringUtils.hasText(request.nickname()) ? request.nickname().trim() : username);
        user.setGender(StringUtils.hasText(request.gender()) ? request.gender().trim() : "男");
        user.setStatus("ACTIVE");
        userMapper.insert(user);

        AddressDO address = new AddressDO();
        address.setUserId(userId);
        address.setContactName(user.getNickname());
        address.setContactPhone(StringUtils.hasText(request.phone()) ? request.phone().trim() : "13800000000");
        address.setContactSex("女".equals(user.getGender()) ? 0 : 1);
        address.setDetail("沈阳市浑南区软件园E18楼");
        addressMapper.insert(address);
        addressMapper.updateDefaultById(userId, address.getId());

        return userId;
    }

    @Transactional
    public String login(UserLoginRequest request) {
        UserDO user = userMapper.selectByUsername(request.username().trim());
        if (user == null || !user.getPassword().equals(request.password())) {
            return null;
        }

        UserTokenDO userToken = new UserTokenDO();
        userToken.setUserId(user.getId());
        userToken.setToken(UUID.randomUUID().toString().replace("-", ""));
        userToken.setExpiredAt(LocalDateTime.now().plusDays(7));
        userTokenMapper.insert(userToken);
        return userToken.getToken();
    }

    public Long resolveUserId(String token, String authorization) {
        String actualToken = token;
        if (!StringUtils.hasText(actualToken) && StringUtils.hasText(authorization)) {
            actualToken = authorization.startsWith("Bearer ")
                    ? authorization.substring("Bearer ".length()).trim()
                    : authorization.trim();
        }
        if (!StringUtils.hasText(actualToken)) {
            return null;
        }

        UserTokenDO userToken = userTokenMapper.selectByToken(actualToken);
        if (userToken == null || userToken.getExpiredAt() == null || userToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            return null;
        }
        return userToken.getUserId();
    }

    public AddressVO getDefaultAddress(Long userId) {
        AddressDO address = addressMapper.selectDefaultByUserId(userId);
        if (address == null) {
            return null;
        }
        return toAddressVO(address);
    }

    public List<AddressVO> listAddresses(Long userId) {
        return addressMapper.selectByUserId(userId).stream()
                .map(this::toAddressVO)
                .collect(Collectors.toList());
    }

    public AddressVO getAddress(Long userId, Long addressId) {
        AddressDO address = addressMapper.selectById(userId, addressId);
        return address == null ? null : toAddressVO(address);
    }

    @Transactional
    public Long createAddress(Long userId, AddressSaveRequest request) {
        AddressDO address = new AddressDO();
        address.setUserId(userId);
        fillAddress(address, request);
        addressMapper.insert(address);
        if (Boolean.TRUE.equals(request.isDefault()) || addressMapper.selectDefaultByUserId(userId) == null) {
            addressMapper.updateDefaultById(userId, address.getId());
        }
        return address.getId();
    }

    @Transactional
    public boolean updateAddress(Long userId, Long addressId, AddressSaveRequest request) {
        AddressDO existing = addressMapper.selectById(userId, addressId);
        if (existing == null) {
            return false;
        }

        existing.setId(addressId);
        fillAddress(existing, request);
        addressMapper.update(existing);
        if (Boolean.TRUE.equals(request.isDefault())) {
            addressMapper.updateDefaultById(userId, addressId);
        }
        return true;
    }

    @Transactional
    public boolean setDefaultAddress(Long userId, Long addressId) {
        AddressDO existing = addressMapper.selectById(userId, addressId);
        if (existing == null) {
            return false;
        }

        addressMapper.updateDefaultById(userId, addressId);
        return true;
    }

    @Transactional
    public boolean deleteAddress(Long userId, Long addressId) {
        AddressDO existing = addressMapper.selectById(userId, addressId);
        if (existing == null) {
            return false;
        }

        addressMapper.deleteById(userId, addressId);
        AddressDO currentDefault = addressMapper.selectDefaultByUserId(userId);
        if (currentDefault == null || currentDefault.getId().equals(addressId)) {
            AddressDO firstAddress = addressMapper.selectFirstByUserId(userId);
            if (firstAddress != null) {
                addressMapper.updateDefaultById(userId, firstAddress.getId());
            } else {
                addressMapper.clearDefaultByUserId(userId);
            }
        }
        return true;
    }

    private void fillAddress(AddressDO address, AddressSaveRequest request) {
        address.setContactName(request.contactName().trim());
        address.setContactPhone(request.contactPhone().trim());
        address.setContactSex(1);
        String detail = request.detail().trim();
        String prefix = (StringUtils.hasText(request.province()) ? request.province().trim() : "")
                + (StringUtils.hasText(request.city()) ? request.city().trim() : "")
                + (StringUtils.hasText(request.district()) ? request.district().trim() : "");
        address.setDetail(StringUtils.hasText(prefix) ? prefix + detail : detail);
    }

    private AddressVO toAddressVO(AddressDO address) {
        AddressDO defaultAddress = addressMapper.selectDefaultByUserId(address.getUserId());
        boolean isDefault = defaultAddress != null && defaultAddress.getId().equals(address.getId());
        return new AddressVO(
                address.getId(),
                address.getUserId(),
                address.getContactName(),
                address.getContactPhone(),
                "",
                "",
                "",
                address.getDetail(),
                null,
                isDefault
        );
    }

    private Long parseUserId(String userIdText) {
        try {
            return Long.parseLong(userIdText);
        } catch (NumberFormatException ex) {
            long value = 0;
            for (int i = 0; i < userIdText.length(); i++) {
                value = value * 31 + userIdText.charAt(i);
            }
            return Math.abs(value);
        }
    }
}
