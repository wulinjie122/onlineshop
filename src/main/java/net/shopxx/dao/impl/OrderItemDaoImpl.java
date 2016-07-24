package net.shopxx.dao.impl;

import net.shopxx.dao.OrderItemDao;
import net.shopxx.entity.OrderItem;
import org.springframework.stereotype.Repository;

@Repository("orderItemDaoImpl")
public class OrderItemDaoImpl
  extends BaseDaoImpl<OrderItem, Long>
  implements OrderItemDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.OrderItemDaoImpl
 * JD-Core Version:    0.7.0.1
 */