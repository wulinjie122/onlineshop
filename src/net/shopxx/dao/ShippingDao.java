package net.shopxx.dao;

import net.shopxx.entity.Shipping;

public abstract interface ShippingDao
  extends BaseDao<Shipping, Long>
{
  public abstract Shipping findBySn(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.ShippingDao
 * JD-Core Version:    0.7.0.1
 */