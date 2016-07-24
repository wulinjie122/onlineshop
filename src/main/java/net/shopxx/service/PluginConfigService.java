package net.shopxx.service;

import net.shopxx.entity.PluginConfig;

public abstract interface PluginConfigService
  extends BaseService<PluginConfig, Long>
{
  public abstract boolean pluginIdExists(String paramString);
  
  public abstract PluginConfig findByPluginId(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.PluginConfigService
 * JD-Core Version:    0.7.0.1
 */