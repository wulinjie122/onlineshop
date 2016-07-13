package net.shopxx.dao;

import net.shopxx.entity.Cart;

public abstract interface CartDao
  extends BaseDao<Cart, Long>
{
  public abstract void evictExpired();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.CartDao
 * JD-Core Version:    0.7.0.1
 */