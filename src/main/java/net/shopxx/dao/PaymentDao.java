package net.shopxx.dao;

import net.shopxx.entity.Payment;

public abstract interface PaymentDao
  extends BaseDao<Payment, Long>
{
  public abstract Payment findBySn(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.PaymentDao
 * JD-Core Version:    0.7.0.1
 */