package net.shopxx.dao;

import java.util.Date;
import java.util.List;
import net.shopxx.Page;
import net.shopxx.Pageable;
import net.shopxx.entity.Member;

public abstract interface MemberDao
  extends BaseDao<Member, Long>
{
  public abstract boolean usernameExists(String paramString);
  
  public abstract boolean emailExists(String paramString);
  
  public abstract Member findByUsername(String paramString);
  
  public abstract List<Member> findListByEmail(String paramString);
  
  public abstract Page<Object> findPurchasePage(Date paramDate1, Date paramDate2, Pageable paramPageable);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.dao.MemberDao
 * JD-Core Version:    0.7.0.1
 */