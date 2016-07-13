package net.shopxx.dao.impl;

import net.shopxx.dao.SpecificationDao;
import net.shopxx.entity.Specification;
import org.springframework.stereotype.Repository;

@Repository("specificationDaoImpl")
public class SpecificationDaoImpl
  extends BaseDaoImpl<Specification, Long>
  implements SpecificationDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.SpecificationDaoImpl
 * JD-Core Version:    0.7.0.1
 */