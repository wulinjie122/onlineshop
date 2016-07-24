package net.shopxx.dao;

import net.shopxx.entity.Admin;

public abstract interface AdminDao
  extends BaseDao<Admin, Long>
{
  public abstract boolean usernameExists(String paramString);
  
  public abstract Admin findByUsername(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.AdminDao
 * JD-Core Version:    0.7.0.1
 */