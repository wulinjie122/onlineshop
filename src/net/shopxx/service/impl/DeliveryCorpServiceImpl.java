package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.DeliveryCorpDao;
import net.shopxx.entity.DeliveryCorp;
import net.shopxx.service.DeliveryCorpService;
import org.springframework.stereotype.Service;

@Service("deliveryCorpServiceImpl")
public class DeliveryCorpServiceImpl
  extends BaseServiceImpl<DeliveryCorp, Long>
  implements DeliveryCorpService
{
  @Resource(name="deliveryCorpDaoImpl")
  public void setBaseDao(DeliveryCorpDao deliveryCorpDao)
  {
    super.setBaseDao(deliveryCorpDao);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.impl.DeliveryCorpServiceImpl
 * JD-Core Version:    0.7.0.1
 */