package net.shopxx.service;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Coupon;

public abstract interface CouponService
  extends BaseService<Coupon, Long>
{
  public abstract Page<Coupon> findPage(Boolean paramBoolean1, Boolean paramBoolean2, Boolean paramBoolean3, Pageable paramPageable);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.CouponService
 * JD-Core Version:    0.7.0.1
 */