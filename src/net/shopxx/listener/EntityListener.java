package net.shopxx.listener;

import java.util.Date;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import net.shopxx.entity.BaseEntity;

public class EntityListener
{
  @PrePersist
  public void prePersist(BaseEntity entity)
  {
    entity.setCreateDate(new Date());
    entity.setModifyDate(new Date());
  }
  
  @PreUpdate
  public void preUpdate(BaseEntity entity)
  {
    entity.setModifyDate(new Date());
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.listener.EntityListener
 * JD-Core Version:    0.7.0.1
 */