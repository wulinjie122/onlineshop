package net.shopxx.service.impl;

import javax.annotation.Resource;
import net.shopxx.dao.CartItemDao;
import net.shopxx.entity.CartItem;
import net.shopxx.service.CartItemService;
import org.springframework.stereotype.Service;

@Service("cartItemServiceImpl")
public class CartItemServiceImpl
  extends BaseServiceImpl<CartItem, Long>
  implements CartItemService
{
  @Resource(name="cartItemDaoImpl")
  public void setBaseDao(CartItemDao cartItemDao)
  {
    super.setBaseDao(cartItemDao);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.impl.CartItemServiceImpl
 * JD-Core Version:    0.7.0.1
 */