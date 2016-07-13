package net.shopxx.service;

public abstract interface CacheService
{
  public abstract String getDiskStorePath();
  
  public abstract int getCacheSize();
  
  public abstract void clear();
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.CacheService
 * JD-Core Version:    0.7.0.1
 */