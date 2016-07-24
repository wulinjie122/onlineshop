package net.shopxx.service;

import net.shopxx.entity.DeliveryCenter;

public abstract interface DeliveryCenterService
  extends BaseService<DeliveryCenter, Long>
{
  public abstract DeliveryCenter findDefault();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.DeliveryCenterService
 * JD-Core Version:    0.7.0.1
 */