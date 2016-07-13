package net.shopxx.dao;

import net.shopxx.entity.Log;

public abstract interface LogDao
  extends BaseDao<Log, Long>
{
  public abstract void removeAll();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.LogDao
 * JD-Core Version:    0.7.0.1
 */