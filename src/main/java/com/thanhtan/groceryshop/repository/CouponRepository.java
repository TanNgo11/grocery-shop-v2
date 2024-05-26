package com.thanhtan.groceryshop.repository;

import com.thanhtan.groceryshop.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCode(String code);

    long deleteByCode(String code);
}