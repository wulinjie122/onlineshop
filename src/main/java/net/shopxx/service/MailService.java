package net.shopxx.service;

import java.util.Map;
import net.shopxx.entity.ProductNotify;
import net.shopxx.entity.SafeKey;

public abstract interface MailService
{
  public abstract void send(String paramString1, String paramString2, Integer paramInteger, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, Map<String, Object> paramMap, boolean paramBoolean);
  
  public abstract void send(String paramString1, String paramString2, String paramString3, Map<String, Object> paramMap, boolean paramBoolean);
  
  public abstract void send(String paramString1, String paramString2, String paramString3, Map<String, Object> paramMap);
  
  public abstract void send(String paramString1, String paramString2, String paramString3);
  
  public abstract void sendTestMail(String paramString1, String paramString2, Integer paramInteger, String paramString3, String paramString4, String paramString5);
  
  public abstract void sendFindPasswordMail(String paramString1, String paramString2, SafeKey paramSafeKey);
  
  public abstract void sendProductNotifyMail(ProductNotify paramProductNotify);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.MailService
 * JD-Core Version:    0.7.0.1
 */