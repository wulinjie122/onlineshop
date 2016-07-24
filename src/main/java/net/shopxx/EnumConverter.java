package net.shopxx;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter
  extends AbstractConverter
{
  private final Class<?> IIIllIlI;
  
  public EnumConverter(Class<?> enumClass)
  {
    this(enumClass, null);
  }
  
  public EnumConverter(Class<?> enumClass, Object defaultValue)
  {
    super(defaultValue);
    this.IIIllIlI = enumClass;
  }
  
  protected Class<?> getDefaultType()
  {
    return this.IIIllIlI;
  }
  
  protected Object convertToType(Class type, Object value)
  {
    String str = value.toString().trim();
    return Enum.valueOf(type, str);
  }
  
  protected String convertToString(Object value)
  {
    return value.toString();
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.EnumConverter
 * JD-Core Version:    0.7.0.1
 */