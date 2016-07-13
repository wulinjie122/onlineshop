package net.shopxx.dao;

import net.shopxx.entity.PluginConfig;

public abstract interface PluginConfigDao
  extends BaseDao<PluginConfig, Long>
{
  public abstract boolean pluginIdExists(String paramString);
  
  public abstract PluginConfig findByPluginId(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.PluginConfigDao
 * JD-Core Version:    0.7.0.1
 */