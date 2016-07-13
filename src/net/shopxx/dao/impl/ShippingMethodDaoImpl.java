package net.shopxx.dao.impl;

import net.shopxx.dao.ShippingMethodDao;
import net.shopxx.entity.ShippingMethod;
import org.springframework.stereotype.Repository;

@Repository("shippingMethodDaoImpl")
public class ShippingMethodDaoImpl
  extends BaseDaoImpl<ShippingMethod, Long>
  implements ShippingMethodDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.ShippingMethodDaoImpl
 * JD-Core Version:    0.7.0.1
 */