package net.shopxx.service;

import java.util.List;
import net.shopxx.entity.MemberAttribute;

public abstract interface MemberAttributeService
  extends BaseService<MemberAttribute, Long>
{
  public abstract Integer findUnusedPropertyIndex();
  
  public abstract List<MemberAttribute> findList();
  
  public abstract List<MemberAttribute> findList(String paramString);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.MemberAttributeService
 * JD-Core Version:    0.7.0.1
 */