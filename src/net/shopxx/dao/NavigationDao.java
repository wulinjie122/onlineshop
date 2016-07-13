package net.shopxx.dao;

import java.util.List;
import net.shopxx.entity.Navigation;
import net.shopxx.entity.Navigation.Position;

public abstract interface NavigationDao
  extends BaseDao<Navigation, Long>
{
  public abstract List<Navigation> findList(Navigation.Position paramPosition);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.NavigationDao
 * JD-Core Version:    0.7.0.1
 */