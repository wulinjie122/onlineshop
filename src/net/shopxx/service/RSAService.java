package net.shopxx.service;

import java.security.interfaces.RSAPublicKey;
import javax.servlet.http.HttpServletRequest;

public abstract interface RSAService
{
  public abstract RSAPublicKey generateKey(HttpServletRequest paramHttpServletRequest);
  
  public abstract void removePrivateKey(HttpServletRequest paramHttpServletRequest);
  
  public abstract String decryptParameter(String paramString, HttpServletRequest paramHttpServletRequest);
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.service.RSAService
 * JD-Core Version:    0.7.0.1
 */