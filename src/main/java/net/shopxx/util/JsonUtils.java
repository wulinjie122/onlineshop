package net.shopxx.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;

public final class JsonUtils
{
  private static ObjectMapper IIIllIlI = new ObjectMapper();
  
  public static String toJson(Object value)
  {
    try
    {
      return IIIllIlI.writeValueAsString(value);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public static void toJson(HttpServletResponse response, String contentType, Object value)
  {
    Assert.notNull(response);
    Assert.hasText(contentType);
    try
    {
      response.setContentType(contentType);
      IIIllIlI.writeValue(response.getWriter(), value);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public static void toJson(HttpServletResponse response, Object value)
  {
    Assert.notNull(response);
    PrintWriter localPrintWriter = null;
    try
    {
      localPrintWriter = response.getWriter();
      IIIllIlI.writeValue(localPrintWriter, value);
      localPrintWriter.flush();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      IOUtils.closeQuietly(localPrintWriter);
    }
  }
  
  public static <T> T toObject(String json, Class<T> valueType)
  {
    Assert.hasText(json);
    Assert.notNull(valueType);
    try
    {
      return IIIllIlI.readValue(json, valueType);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public static <T> T toObject(String json, TypeReference<?> typeReference)
  {
    Assert.hasText(json);
    Assert.notNull(typeReference);
    try
    {
      return IIIllIlI.readValue(json, typeReference);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public static <T> T toObject(String json, JavaType javaType)
  {
    Assert.hasText(json);
    Assert.notNull(javaType);
    try
    {
      return IIIllIlI.readValue(json, javaType);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.util.JsonUtils
 * JD-Core Version:    0.7.0.1
 */