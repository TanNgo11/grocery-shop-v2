package com.thanhtan.groceryshop.mapper;

import com.thanhtan.groceryshop.dto.request.CouponRequest;
import com.thanhtan.groceryshop.dto.response.CouponResponse;
import com.thanhtan.groceryshop.entity.Coupon;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CouponMapper {


    Coupon toCoupon(CouponRequest couponRequest);

    CouponResponse toCouponResponse(Coupon coupon);

    List<CouponResponse> toOrderResponseList(List<Coupon> allCoupons);

}
