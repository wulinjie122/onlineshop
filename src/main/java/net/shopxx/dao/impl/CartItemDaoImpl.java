package net.shopxx.dao.impl;

import net.shopxx.dao.CartItemDao;
import net.shopxx.entity.CartItem;
import org.springframework.stereotype.Repository;

@Repository("cartItemDaoImpl")
public class CartItemDaoImpl
  extends BaseDaoImpl<CartItem, Long>
  implements CartItemDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.CartItemDaoImpl
 * JD-Core Version:    0.7.0.1
 */