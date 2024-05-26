package com.thanhtan.groceryshop.service.impl;

import com.thanhtan.groceryshop.dto.request.CouponRequest;
import com.thanhtan.groceryshop.dto.response.CouponResponse;
import com.thanhtan.groceryshop.entity.Coupon;
import com.thanhtan.groceryshop.mapper.CouponMapper;
import com.thanhtan.groceryshop.repository.CouponRepository;
import com.thanhtan.groceryshop.service.ICouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponService implements ICouponService {

    CouponRepository couponRepository;

    CouponMapper couponMapper;

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponMapper.toOrderResponseList(couponRepository.findAll());
    }

    @Override
    public CouponResponse getCouponById(Long id) {
        return couponMapper.toCouponResponse(couponRepository.getById(id));
    }

    @Override
    public CouponResponse createCoupon(CouponRequest couponRequest) {
        Coupon coupon = new Coupon();
        coupon.setCode(UUID.randomUUID().toString());
        coupon.setDiscount(couponRequest.getDiscount());
        coupon.setExpiryDate(couponRequest.getExpiryDate());
        return couponMapper.toCouponResponse(couponRepository.save(coupon));
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        return couponMapper.toCouponResponse(couponRepository.findByCode(code));
    }
}
