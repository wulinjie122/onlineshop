package net.shopxx.dao;

import java.util.List;
import java.util.Set;
import net.shopxx.entity.Parameter;
import net.shopxx.entity.ParameterGroup;

public abstract interface ParameterDao
  extends BaseDao<Parameter, Long>
{
  public abstract List<Parameter> findList(ParameterGroup paramParameterGroup, Set<Parameter> paramSet);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.ParameterDao
 * JD-Core Version:    0.7.0.1
 */