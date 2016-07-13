package net.shopxx.dao;

import net.shopxx.entity.DeliveryTemplate;

public abstract interface DeliveryTemplateDao
  extends BaseDao<DeliveryTemplate, Long>
{
  public abstract DeliveryTemplate findDefault();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.DeliveryTemplateDao
 * JD-Core Version:    0.7.0.1
 */