package net.shopxx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import net.shopxx.dao.ShippingDao;
import net.shopxx.entity.Shipping;
import org.springframework.stereotype.Repository;

@Repository("shippingDaoImpl")
public class ShippingDaoImpl
  extends BaseDaoImpl<Shipping, Long>
  implements ShippingDao
{
  public Shipping findBySn(String sn)
  {
    if (sn == null) {
      return null;
    }
    String str = "select shipping from Shipping shipping where lower(shipping.sn) = lower(:sn)";
    try
    {
      return (Shipping)this.entityManager.createQuery(str, Shipping.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
    }
    catch (NoResultException localNoResultException) {}
    return null;
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.ShippingDaoImpl
 * JD-Core Version:    0.7.0.1
 */