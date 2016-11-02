package net.shopxx.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import net.shopxx.dao.SnDao;
import net.shopxx.entity.Sn;
import net.shopxx.util.FreemarkerUtils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

@Repository("snDaoImpl")
public class SnDaoImpl implements SnDao, InitializingBean {

  private SnDaoImpl.HiloOptimizer productPrefixHiloOptimizer;
  private SnDaoImpl.HiloOptimizer IIIllIll;
  private SnDaoImpl.HiloOptimizer IIIlllII;
  private SnDaoImpl.HiloOptimizer IIIlllIl;
  private SnDaoImpl.HiloOptimizer IIIllllI;
  private SnDaoImpl.HiloOptimizer IIIlllll;

  @PersistenceContext
  private EntityManager entityManager;
  @Value("${sn.product.prefix}")
  private String productPrefix;
  @Value("${sn.product.maxLo}")
  private int productMaxLo;
  @Value("${sn.order.prefix}")
  private String orderPrefix;
  @Value("${sn.order.maxLo}")
  private int orderMaxLo;
  @Value("${sn.payment.prefix}")
  private String paymentPrefix;
  @Value("${sn.payment.maxLo}")
  private int paymentMaxLo;
  @Value("${sn.refunds.prefix}")
  private String refundsPrefix;
  @Value("${sn.refunds.maxLo}")
  private int refundsMaxLo;
  @Value("${sn.shipping.prefix}")
  private String shippingPrefix;
  @Value("${sn.shipping.maxLo}")
  private int shippingMaxLo;
  @Value("${sn.returns.prefix}")
  private String returnsPrefix;
  @Value("${sn.returns.maxLo}")
  private int returnsMaxLo;
  
  public void afterPropertiesSet()
  {
    this.productPrefixHiloOptimizer = new SnDaoImpl.HiloOptimizer(this, Sn.Type.product, this.productPrefix, this.productMaxLo);
    this.IIIllIll = new SnDaoImpl.HiloOptimizer(this, Sn.Type.order, this.orderPrefix, this.orderMaxLo);
    this.IIIlllII = new SnDaoImpl.HiloOptimizer(this, Sn.Type.payment, this.paymentPrefix, this.paymentMaxLo);
    this.IIIlllIl = new SnDaoImpl.HiloOptimizer(this, Sn.Type.refunds, this.refundsPrefix, this.refundsMaxLo);
    this.IIIllllI = new SnDaoImpl.HiloOptimizer(this, Sn.Type.shipping, this.shippingPrefix, this.shippingMaxLo);
    this.IIIlllll = new SnDaoImpl.HiloOptimizer(this, Sn.Type.returns, this.returnsPrefix, this.returnsMaxLo);
  }
  
  public String generate(Sn.Type type)
  {
    Assert.notNull(type);
    if (type == Sn.Type.product) {
      return this.productPrefixHiloOptimizer.generate();
    }
    if (type == Sn.Type.order) {
      return this.IIIllIll.generate();
    }
    if (type == Sn.Type.payment) {
      return this.IIIlllII.generate();
    }
    if (type == Sn.Type.refunds) {
      return this.IIIlllIl.generate();
    }
    if (type == Sn.Type.shipping) {
      return this.IIIllllI.generate();
    }
    if (type == Sn.Type.returns) {
      return this.IIIlllll.generate();
    }
    return null;
  }
  
  private long IIIllIlI(Sn.Type paramType) {

    String str = "select sn from Sn sn where sn.type = :type";
    Sn localSn = (Sn)this.entityManager.createQuery(str, Sn.class).setFlushMode(FlushModeType.COMMIT).setParameter("type", paramType).setLockMode(LockModeType.PESSIMISTIC_WRITE).getSingleResult();
    long l = localSn.getLastValue().longValue();
    localSn.setLastValue(Long.valueOf(l + 1L));
    this.entityManager.merge(localSn);
    return l;

  }
  
  class HiloOptimizer {

    private Sn.Type IIIllIll;
    private String IIIlllII;
    private int IIIlllIl;
    private int IIIllllI;
    private long IIIlllll;
    private long IIlIIIII;
    
    public HiloOptimizer(SnDaoImpl paramSnDaoImpl, Sn.Type type, String prefix, int maxLo)
    {
      this.IIIllIll = type;
      this.IIIlllII = (prefix != null ? prefix.replace("{", "${") : "");
      this.IIIlllIl = maxLo;
      this.IIIllllI = (maxLo + 1);
    }
    
    public synchronized String generate()
    {
      if (this.IIIllllI > this.IIIlllIl) {
        //this.entityManager = SnDaoImpl.entityManager(this.entityManager, this.getNotifyUrl);
        this.IIIllllI = (this.IIlIIIII == 0L ? 1 : 0);
        this.IIIlllll = (this.IIlIIIII * (this.IIIlllIl + 1));

      }

      return FreemarkerUtils.process(this.IIIlllII, null) + (this.IIIlllll + this.IIIllllI++);
    }
  }
  
}