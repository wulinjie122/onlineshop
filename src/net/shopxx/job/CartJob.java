package net.shopxx.job;

import javax.annotation.Resource;
import net.shopxx.service.CartService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("cartJob")
@Lazy(false)
public class CartJob
{
  @Resource(name="cartServiceImpl")
  private CartService IIIllIlI;
  
  @Scheduled(cron="${job.cart_evict_expired.cron}")
  public void evictExpired()
  {
    this.IIIllIlI.evictExpired();
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.job.CartJob
 * JD-Core Version:    0.7.0.1
 */