package net.shopxx.service;

import net.shopxx.entity.Log;

public abstract interface LogService
  extends BaseService<Log, Long>
{
  public abstract void clear();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.LogService
 * JD-Core Version:    0.7.0.1
 */