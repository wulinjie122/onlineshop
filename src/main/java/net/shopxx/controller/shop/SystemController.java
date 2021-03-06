package net.shopxx.controller.shop;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("shopSystemController")
@RequestMapping({"/system"})
public class SystemController
{
  @RequestMapping(value={"/info"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void info(HttpServletRequest request, HttpServletResponse response)
  {
    String str = " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ().+/;=-";
    int[] arrayOfInt1 = { 30, 15, 34, 30, 67, 26, 22, 11, 19, 24, 68, 13, 18, 11, 28, 29, 15, 30, 69, 57, 56, 42, 70, 9 };
    StringBuffer localStringBuffer1 = new StringBuffer();
    for (int i : arrayOfInt1) {
      localStringBuffer1.append(str.charAt(i));
    }
    int[] arrayOfInt2 = { 55, 44, 51, 52, 66, 66, 0, 58, 4, 65, 1, 0, 39, 25, 26, 35, 28, 19, 17, 18, 30, 0, 63, 13, 64, 0, 3, 1, 2, 3, 0, 29, 18, 25, 26, 34, 34, 65, 24, 15, 30, 0, 37, 22, 22, 0, 54, 19, 17, 18, 30, 29, 0, 54, 15, 29, 15, 28, 32, 15, 14, 65 };
    StringBuffer localStringBuffer2 = new StringBuffer();
    for (int idx : arrayOfInt2) {
      localStringBuffer2.append(str.charAt(idx));
    }
    response.setContentType(localStringBuffer1.toString());
    PrintWriter localPrintWriter = null;
    try
    {
      localPrintWriter = response.getWriter();
      localPrintWriter.write(localStringBuffer2.toString());
      localPrintWriter.flush();
    }
    catch (IOException localIOException1)
    {
      localIOException1.printStackTrace();
    }
    finally
    {
      IOUtils.closeQuietly(localPrintWriter);
    }
  }
}