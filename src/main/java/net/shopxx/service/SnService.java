package net.shopxx.service;

import net.shopxx.entity.Sn;
import net.shopxx.entity.Sn.Type;

public abstract interface SnService
{
  public abstract String generate(Sn.Type paramType);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.SnService
 * JD-Core Version:    0.7.0.1
 */