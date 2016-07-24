package net.shopxx.dao.impl;

import net.shopxx.dao.OrderLogDao;
import net.shopxx.entity.OrderLog;
import org.springframework.stereotype.Repository;

@Repository("orderLogDaoImpl")
public class OrderLogDaoImpl
  extends BaseDaoImpl<OrderLog, Long>
  implements OrderLogDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.OrderLogDaoImpl
 * JD-Core Version:    0.7.0.1
 */