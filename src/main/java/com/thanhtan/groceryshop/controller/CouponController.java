package com.thanhtan.groceryshop.controller;

import com.thanhtan.groceryshop.dto.request.CouponRequest;
import com.thanhtan.groceryshop.dto.response.ApiResponse;
import com.thanhtan.groceryshop.dto.response.CouponResponse;
import com.thanhtan.groceryshop.service.ICouponService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_COUPONS;


@RestController
@RequestMapping(API_V1_COUPONS)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CouponController {

    ICouponService couponService;

    @GetMapping
    public ApiResponse<List<CouponResponse>> getAllCoupons() {
        return ApiResponse.<List<CouponResponse>>builder()
                .result(couponService.getAllCoupons())
                .build();
    }

    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(@RequestBody CouponRequest couponRequest) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.createCoupon(couponRequest))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CouponResponse> getCouponById(@PathVariable Long id) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.getCouponById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ApiResponse.<String>builder()
                .result("Coupon deleted successfully")
                .build();
    }

    @GetMapping("/code/{code}")
    public ApiResponse<CouponResponse> getCouponByCode(@PathVariable String code) {
        return ApiResponse.<CouponResponse>builder()
                .result(couponService.getCouponByCode(code))
                .build();
    }


}
