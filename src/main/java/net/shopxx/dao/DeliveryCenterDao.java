package net.shopxx.dao;

import net.shopxx.entity.DeliveryCenter;

public abstract interface DeliveryCenterDao
  extends BaseDao<DeliveryCenter, Long>
{
  public abstract DeliveryCenter findDefault();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.DeliveryCenterDao
 * JD-Core Version:    0.7.0.1
 */