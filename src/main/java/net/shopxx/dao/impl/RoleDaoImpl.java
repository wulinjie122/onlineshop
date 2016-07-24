package net.shopxx.dao.impl;

import net.shopxx.dao.RoleDao;
import net.shopxx.entity.Role;
import org.springframework.stereotype.Repository;

@Repository("roleDaoImpl")
public class RoleDaoImpl
  extends BaseDaoImpl<Role, Long>
  implements RoleDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.RoleDaoImpl
 * JD-Core Version:    0.7.0.1
 */