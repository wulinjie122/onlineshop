package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.ReturnsDao;
import net.shopxx.entity.Returns;
import net.shopxx.service.ReturnsService;
import org.springframework.stereotype.Service;

@Service("returnsServiceImpl")
public class ReturnsServiceImpl
  extends BaseServiceImpl<Returns, Long>
  implements ReturnsService
{
  @Resource(name="returnsDaoImpl")
  public void setBaseDao(ReturnsDao returnsDao)
  {
    super.setBaseDao(returnsDao);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.impl.ReturnsServiceImpl
 * JD-Core Version:    0.7.0.1
 */