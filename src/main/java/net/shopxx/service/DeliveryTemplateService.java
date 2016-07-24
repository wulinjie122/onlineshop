package net.shopxx.service;

import net.shopxx.entity.DeliveryTemplate;

public abstract interface DeliveryTemplateService
  extends BaseService<DeliveryTemplate, Long>
{
  public abstract DeliveryTemplate findDefault();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.DeliveryTemplateService
 * JD-Core Version:    0.7.0.1
 */