package net.shopxx;

import java.io.Serializable;

public class LogConfig
  implements Serializable
{
  private static final long serialVersionUID = -1108848647938408402L;
  private String IIIllIlI;
  private String IIIllIll;
  
  public String getOperation()
  {
    return this.IIIllIlI;
  }
  
  public void setOperation(String operation)
  {
    this.IIIllIlI = operation;
  }
  
  public String getUrlPattern()
  {
    return this.IIIllIll;
  }
  
  public void setUrlPattern(String urlPattern)
  {
    this.IIIllIll = urlPattern;
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.LogConfig
 * JD-Core Version:    0.7.0.1
 */