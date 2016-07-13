package net.shopxx.dao.impl;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import net.shopxx.dao.CartDao;
import net.shopxx.entity.Cart;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

@Repository("cartDaoImpl")
public class CartDaoImpl
  extends BaseDaoImpl<Cart, Long>
  implements CartDao
{
  public void evictExpired()
  {
    String str = "delete from Cart cart where cart.modifyDate <= :expire";
    this.entityManager.createQuery(str).setFlushMode(FlushModeType.COMMIT).setParameter("expire", DateUtils.addSeconds(new Date(), -604800)).executeUpdate();
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.CartDaoImpl
 * JD-Core Version:    0.7.0.1
 */