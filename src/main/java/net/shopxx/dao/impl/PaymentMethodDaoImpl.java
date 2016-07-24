package net.shopxx.dao.impl;

import net.shopxx.dao.PaymentMethodDao;
import net.shopxx.entity.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository("paymentMethodDaoImpl")
public class PaymentMethodDaoImpl
  extends BaseDaoImpl<PaymentMethod, Long>
  implements PaymentMethodDao
{}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.impl.PaymentMethodDaoImpl
 * JD-Core Version:    0.7.0.1
 */