package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.SpecificationDao;
import net.shopxx.entity.Specification;
import net.shopxx.service.SpecificationService;
import org.springframework.stereotype.Service;

@Service("specificationServiceImpl")
public class SpecificationServiceImpl
  extends BaseServiceImpl<Specification, Long>
  implements SpecificationService
{
  @Resource(name="specificationDaoImpl")
  public void setBaseDao(SpecificationDao specificationDao)
  {
    super.setBaseDao(specificationDao);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.impl.SpecificationServiceImpl
 * JD-Core Version:    0.7.0.1
 */