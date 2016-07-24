package net.shopxx.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import net.shopxx.Setting.WatermarkPosition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.Operation;
import org.springframework.util.Assert;
import net.shopxx.Setting.WatermarkPosition;

public final class ImageUtils
{
  //图片工具类型
  private static ImageUtils.Type imageUtilType = ImageUtils.Type.auto;
  private static String graphicsMagickPath;
  private static String convertPath;
  private static final Color color = Color.white;
  private static final int IIIllllI = 88;
  
  static
  {
    Object sOsName;
    Object sPath;
    Object localObject3;
    Object localObject4;
    File localFile1;
    File localFile2;
    if (graphicsMagickPath == null)
    {
      sOsName = System.getProperty("os.name").toLowerCase();
      if(((String)sOsName).indexOf("windows") >= 0){
        sPath = System.getenv("Path");
        if (sPath != null)
        {
          String[] paths = ((String)sPath).split(";");
          for (String s1 : paths)
          {
            localFile1 = new File(s1.trim() + "/gm.exe");
            localFile2 = new File(s1.trim() + "/gmdisplay.exe");
            if ((localFile1.exists()) && (localFile2.exists()))
            {
              graphicsMagickPath = s1.trim();
              break;
            }
          }
        }
      }
    }
    if (convertPath == null)
    {
      sOsName = System.getProperty("os.name").toLowerCase();
      if (((String)sOsName).indexOf("windows") >= 0)
      {
        sPath = System.getenv("Path");
        if (sPath != null)
        {
          String[] paths = ((String)sPath).split(";");
          for (String s1 : paths)
          {
            localFile1 = new File(s1.trim() + "/convert.exe");
            localFile2 = new File(s1.trim() + "/composite.exe");
            if ((localFile1.exists()) && (localFile2.exists()))
            {
              convertPath = s1.trim();
              break;
            }
          }
        }
      }
    }
    
    if (imageUtilType == ImageUtils.Type.auto) {
      try
      {
        sOsName = new IMOperation();
        ((IMOperation)sOsName).version();
        sPath = new IdentifyCmd(true);
        if (graphicsMagickPath != null) {
          ((IdentifyCmd)sPath).setSearchPath(graphicsMagickPath);
        }
        ((IdentifyCmd)sPath).run((Operation)sOsName, new Object[0]);
        imageUtilType = ImageUtils.Type.graphicsMagick;
      }
      catch (Throwable localThrowable1)
      {
        try
        {
          sPath = new IMOperation();
          ((IMOperation)sPath).version();
          localObject3 = new IdentifyCmd(false);
          ((IdentifyCmd)localObject3).run((Operation)sPath, new Object[0]);
          if (convertPath != null) {
            ((IdentifyCmd)localObject3).setSearchPath(convertPath);
          }
          imageUtilType = ImageUtils.Type.imageMagick;
        }
        catch (Throwable localThrowable2)
        {
          imageUtilType = ImageUtils.Type.jdk;
        }
      }
    }
  }
  
  public static void zoom(File srcFile, File destFile, int destWidth, int destHeight)
  {
    Assert.notNull(srcFile);
    Assert.notNull(destFile);
    Assert.state(destWidth > 0);
    Assert.state(destHeight > 0);
    Object localObject1;
    Object localObject2;
    if (imageUtilType == ImageUtils.Type.jdk)
    {
      localObject1 = null;
      localObject2 = null;
      ImageWriter localImageWriter = null;
      try
      {
        BufferedImage localBufferedImage1 = ImageIO.read(srcFile);
        int i = localBufferedImage1.getWidth();
        int j = localBufferedImage1.getHeight();
        int k = destWidth;
        int m = destHeight;
        if (j >= i) {
          k = (int)Math.round(destHeight * 1.0D / j * i);
        } else {
          m = (int)Math.round(destWidth * 1.0D / i * j);
        }
        BufferedImage localBufferedImage2 = new BufferedImage(destWidth, destHeight, 1);
        localObject1 = localBufferedImage2.createGraphics();
        ((Graphics2D)localObject1).setBackground(color);
        ((Graphics2D)localObject1).clearRect(0, 0, destWidth, destHeight);
        ((Graphics2D)localObject1).drawImage(localBufferedImage1.getScaledInstance(k, m, 4), destWidth / 2 - k / 2, destHeight / 2 - m / 2, null);
        localObject2 = ImageIO.createImageOutputStream(destFile);
        localImageWriter = (ImageWriter)ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
        localImageWriter.setOutput(localObject2);
        ImageWriteParam localImageWriteParam = localImageWriter.getDefaultWriteParam();
        localImageWriteParam.setCompressionMode(2);
        localImageWriteParam.setCompressionQuality(0.88F);
        localImageWriter.write(null, new IIOImage(localBufferedImage2, null, null), localImageWriteParam);
        ((ImageOutputStream)localObject2).flush();
      }
      catch (IOException localIOException8)
      {
        localIOException8.printStackTrace();
        if (localObject1 != null) {
          ((Graphics2D)localObject1).dispose();
        }
        if (localImageWriter != null) {
          localImageWriter.dispose();
        }
        if (localObject2 == null) {
          return;
        }
        try
        {
          ((ImageOutputStream)localObject2).close();
        }
        catch (IOException localIOException1) {}
      }
      finally
      {
        if (localObject1 != null) {
          ((Graphics2D)localObject1).dispose();
        }
        if (localImageWriter != null) {
          localImageWriter.dispose();
        }
        if (localObject2 != null) {
          try
          {
            ((ImageOutputStream)localObject2).close();
          }
          catch (IOException localIOException2) {}
        }
      }
      try
      {
        ((ImageOutputStream)localObject2).close();
      }
      catch (IOException localIOException3) {}
    }
    else
    {
      localObject1 = new IMOperation();
      ((IMOperation)localObject1).thumbnail(Integer.valueOf(destWidth), Integer.valueOf(destHeight));
      ((IMOperation)localObject1).gravity("center");
      ((IMOperation)localObject1).background(IIIllIlI(color));
      ((IMOperation)localObject1).extent(Integer.valueOf(destWidth), Integer.valueOf(destHeight));
      ((IMOperation)localObject1).quality(Double.valueOf(88.0D));
      ((IMOperation)localObject1).addImage(new String[] { srcFile.getPath() });
      ((IMOperation)localObject1).addImage(new String[] { destFile.getPath() });
      if (imageUtilType == ImageUtils.Type.graphicsMagick)
      {
        localObject2 = new ConvertCmd(true);
        if (graphicsMagickPath != null) {
          ((ConvertCmd)localObject2).setSearchPath(graphicsMagickPath);
        }
        try
        {
          ((ConvertCmd)localObject2).run((Operation)localObject1, new Object[0]);
        }
        catch (IOException localIOException6)
        {
          localIOException6.printStackTrace();
        }
        catch (InterruptedException localInterruptedException2)
        {
          localInterruptedException2.printStackTrace();
        }
        catch (IM4JavaException localIM4JavaException2)
        {
          localIM4JavaException2.printStackTrace();
        }
      }
      else
      {
        localObject2 = new ConvertCmd(false);
        if (convertPath != null) {
          ((ConvertCmd)localObject2).setSearchPath(convertPath);
        }
        try
        {
          ((ConvertCmd)localObject2).run((Operation)localObject1, new Object[0]);
        }
        catch (IOException localIOException7)
        {
          localIOException7.printStackTrace();
        }
        catch (InterruptedException localInterruptedException3)
        {
          localInterruptedException3.printStackTrace();
        }
        catch (IM4JavaException localIM4JavaException3)
        {
          localIM4JavaException3.printStackTrace();
        }
      }
    }
  }
  
  /**
   * 加水印
   * @param srcFile
   * @param destFile
   * @param watermarkFile
   * @param watermarkPosition
   * @param alpha
   */
  public static void addWatermark(File srcFile, File destFile, File watermarkFile,WatermarkPosition watermarkPosition, int alpha)
  {
    Assert.notNull(srcFile);
    Assert.notNull(destFile);
    Assert.state(alpha >= 0);
    Assert.state(alpha <= 100);
    //如果水印文件不存在，或水印位置为空，则返回
    if ((watermarkFile == null) || (!watermarkFile.exists()) || (watermarkPosition == null) || (watermarkPosition == WatermarkPosition.no))
    {
      try
      {
        FileUtils.copyFile(srcFile, destFile);
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      return;
    }
    Object localObject1;
    Object localObject2;
    
    //if image type is jdk
    if (imageUtilType == ImageUtils.Type.jdk)
    {
      //localIOException = null;
      //localObject1 = null;
      //localObject2 = null;
      Graphics2D graphics2d = null;
      ImageOutputStream imageoutputstream = null;
      ImageWriter imagewriter = null;
      try
      {
    	//读取源图片
        BufferedImage srcImage = ImageIO.read(srcFile);
        int mainWidth = srcImage.getWidth();
        int mainHeight = srcImage.getHeight();
        
        //创建新图片
        BufferedImage newImage = new BufferedImage(mainWidth, mainHeight, 1);
        graphics2d = newImage.createGraphics();
        graphics2d.setBackground(color);	//白色背景
        graphics2d.clearRect(0, 0, mainWidth, mainHeight);		
        graphics2d.drawImage(srcImage, 0, 0, null);
        graphics2d.setComposite(AlphaComposite.getInstance(10, alpha / 100.0F));
        
        //读取水印文件
        BufferedImage watermarkImage = ImageIO.read(watermarkFile);
        int width = watermarkImage.getWidth();
        int height = watermarkImage.getHeight();
        int paddingleft = mainWidth - width;
        int padingtop = mainHeight - height;
        if (watermarkPosition == WatermarkPosition.topLeft)
        {
          paddingleft = 0;
          padingtop = 0;
        }
        else if (watermarkPosition == WatermarkPosition.topRight)
        {
          paddingleft = mainWidth - width;
          padingtop = 0;
        }
        else if (watermarkPosition == WatermarkPosition.center)
        {
          paddingleft = (mainWidth - width) / 2;
          padingtop = (mainHeight - height) / 2;
        }
        else if (watermarkPosition == WatermarkPosition.bottomLeft)
        {
          paddingleft = 0;
          padingtop = mainHeight - height;
        }
        else if (watermarkPosition == WatermarkPosition.bottomRight)
        {
          paddingleft = mainWidth - width;
          padingtop = mainHeight - height;
        }
        
        graphics2d.drawImage(watermarkImage, paddingleft, padingtop, width, height, null);
        imageoutputstream  = ImageIO.createImageOutputStream(destFile);
        imagewriter = (ImageWriter)ImageIO.getImageWritersByFormatName(FilenameUtils.getExtension(destFile.getName())).next();
        imagewriter.setOutput(imageoutputstream);
        ImageWriteParam imagewriteparam = imagewriter.getDefaultWriteParam();
        imagewriteparam.setCompressionMode(2);
        imagewriteparam.setCompressionQuality(0.88F);
        imagewriter.write(null, new IIOImage(newImage, null, null), imagewriteparam);
        imageoutputstream.flush();
      }
      catch (IOException ioException)
      {
        ioException.printStackTrace();
        //释放文件流
        if (graphics2d != null) {
        	graphics2d.dispose();
        }
        if (imagewriter != null) {
        	imagewriter.dispose();
        }
        if (imageoutputstream == null) {
          return;
        }
        try
        {
        	imageoutputstream.close();
        }
        catch (IOException localIOException2) {
        	
        }
        
      }
      finally
      {
        if (graphics2d != null) {
        	graphics2d.dispose();
        }
        if (imagewriter != null) {
        	imagewriter.dispose();
        }
        if (imageoutputstream != null) {
          try
          {
        	  imageoutputstream.close();
          }
          catch (IOException localIOException3) {}
        }
      }
      
      try
      {
    	  imageoutputstream.close();
      }
      catch (IOException localIOException4) {
    	  
      }
    }
    else
    {
      String s = "SouthEast";
      if (watermarkPosition == WatermarkPosition.topLeft) {
        s = "NorthWest";
      } else if (watermarkPosition == WatermarkPosition.topRight) {
        s = "NorthEast";
      } else if (watermarkPosition == WatermarkPosition.center) {
        s = "Center";
      } else if (watermarkPosition == WatermarkPosition.bottomLeft) {
        s = "SouthWest";
      } else if (watermarkPosition == WatermarkPosition.bottomRight) {
        s = "SouthEast";
      }
      IMOperation imoperation = new IMOperation();
      imoperation.gravity(s);
      imoperation.dissolve(Integer.valueOf(alpha));
      imoperation.quality(Double.valueOf(88.0D));
      imoperation.addImage(new String[] { watermarkFile.getPath() });
      imoperation.addImage(new String[] { srcFile.getPath() });
      imoperation.addImage(new String[] { destFile.getPath() });
      
      if (imageUtilType == ImageUtils.Type.graphicsMagick)
      {
    	  CompositeCmd compositecmd = new CompositeCmd(true);
        if (graphicsMagickPath != null) {
        	compositecmd.setSearchPath(graphicsMagickPath);
        }
        try
        {
        	compositecmd.run((Operation)imoperation, new Object[0]);
        }
        catch (IOException localIOException8)
        {
          localIOException8.printStackTrace();
        }
        catch (InterruptedException localInterruptedException2)
        {
          localInterruptedException2.printStackTrace();
        }
        catch (IM4JavaException localIM4JavaException2)
        {
          localIM4JavaException2.printStackTrace();
        }
      }
      else
      {
    	  CompositeCmd compositecmd1 = new CompositeCmd(false);
        if (convertPath != null) {
        	compositecmd1.setSearchPath(convertPath);
        }
        try
        {
        	compositecmd1.run((Operation)imoperation, new Object[0]);
        }
        catch (IOException localIOException9)
        {
          localIOException9.printStackTrace();
        }
        catch (InterruptedException localInterruptedException3)
        {
          localInterruptedException3.printStackTrace();
        }
        catch (IM4JavaException localIM4JavaException3)
        {
          localIM4JavaException3.printStackTrace();
        }
      }
    }
  }
  
  public static void initialize() {}
  
  private static String IIIllIlI(Color paramColor)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str1 = Integer.toHexString(paramColor.getRed());
    String str2 = Integer.toHexString(paramColor.getGreen());
    String str3 = Integer.toHexString(paramColor.getBlue());
    str1 = str1.length() == 1 ? "0" + str1 : str1;
    str2 = str2.length() == 1 ? "0" + str2 : str2;
    str3 = str3.length() == 1 ? "0" + str3 : str3;
    localStringBuffer.append("#");
    localStringBuffer.append(str1);
    localStringBuffer.append(str2);
    localStringBuffer.append(str3);
    return localStringBuffer.toString();
  }
  
  enum Type
  {
    auto,  jdk,  graphicsMagick,  imageMagick;
  }
}