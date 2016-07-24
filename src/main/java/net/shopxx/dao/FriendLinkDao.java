package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.FriendLink;
import net.shopxx.entity.FriendLink.Type;

public abstract interface FriendLinkDao
  extends BaseDao<FriendLink, Long>
{
  public abstract List<FriendLink> findList(FriendLink.Type paramType);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.FriendLinkDao
 * JD-Core Version:    0.7.0.1
 */