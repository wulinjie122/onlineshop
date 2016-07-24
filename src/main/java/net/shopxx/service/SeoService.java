package net.shopxx.service;

import net.shopxx.entity.Seo;
import net.shopxx.entity.Seo.Type;

public abstract interface SeoService
  extends BaseService<Seo, Long>
{
  public abstract Seo find(Seo.Type paramType);
  
  public abstract Seo find(Seo.Type paramType, String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.SeoService
 * JD-Core Version:    0.7.0.1
 */