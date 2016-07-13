package net.shopxx.service;

import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;
import net.shopxx.entity.Message;

public abstract interface MessageService
  extends BaseService<Message, Long>
{
  public abstract Page<Message> findPage(Member paramMember, Pageable paramPageable);
  
  public abstract Page<Message> findDraftPage(Member paramMember, Pageable paramPageable);
  
  public abstract Long count(Member paramMember, Boolean paramBoolean);
  
  public abstract void delete(Long paramLong, Member paramMember);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.MessageService
 * JD-Core Version:    0.7.0.1
 */