package net.shopxx.service;

import net.shopxx.entity.Payment;

public abstract interface PaymentService
  extends BaseService<Payment, Long>
{
  public abstract Payment findBySn(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.PaymentService
 * JD-Core Version:    0.7.0.1
 */