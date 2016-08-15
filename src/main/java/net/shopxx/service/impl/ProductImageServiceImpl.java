package net.shopxx.service.impl;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.shopxx.Setting;
import net.shopxx.entity.ProductImage;
import net.shopxx.plugin.StoragePlugin;
import net.shopxx.service.ProductImageService;
import net.shopxx.util.FreemarkerUtils;
import net.shopxx.util.ImageUtils;
import net.shopxx.util.SettingUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

@Service("productImageServiceImpl")
public class ProductImageServiceImpl
  implements ProductImageService, ServletContextAware
{
  private static final String IIIllIlI = "jpg";
  private static final String IIIllIll = "image/jpeg";
  private ServletContext IIIlllII;
  @Resource(name="taskExecutor")
  private TaskExecutor IIIlllIl;
  @Resource
  private List<StoragePlugin> IIIllllI;
  
  public void setServletContext(ServletContext servletContext)
  {
    this.IIIlllII = servletContext;
  }
  
  private void IIIllIlI(String paramString1, String paramString2, String paramString3, String paramString4, File paramFile, String paramString5)
  {
    try
    {
      this.IIIlllIl.execute(new _cls1(this, paramFile, paramString1, paramString5, paramString2, paramString3, paramString4));
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
  
  public void build(ProductImage productImage)
  {
    MultipartFile localMultipartFile = productImage.getFile();
    if ((localMultipartFile != null) && (!localMultipartFile.isEmpty())) {
      try
      {
        Setting localSetting = SettingUtils.get();
        HashMap localHashMap = new HashMap();
        localHashMap.put("uuid", UUID.randomUUID().toString());
        String str1 = FreemarkerUtils.process(localSetting.getImageUploadPath(), localHashMap);
        String str2 = UUID.randomUUID().toString();
        String str3 = str1 + str2 + "-source." + FilenameUtils.getExtension(localMultipartFile.getOriginalFilename());
        String str4 = str1 + str2 + "-large." + "jpg";
        String str5 = str1 + str2 + "-medium." + "jpg";
        String str6 = str1 + str2 + "-thumbnail." + "jpg";
        Collections.sort(this.IIIllllI);
        Iterator localIterator = this.IIIllllI.iterator();
        while (localIterator.hasNext())
        {
          StoragePlugin localStoragePlugin = (StoragePlugin)localIterator.next();
          if (localStoragePlugin.getIsEnabled())
          {
            File localFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
            if (!localFile.getParentFile().exists()) {
              localFile.getParentFile().mkdirs();
            }
            localMultipartFile.transferTo(localFile);
            IIIllIlI(str3, str4, str5, str6, localFile, localMultipartFile.getContentType());
            productImage.setSource(localStoragePlugin.getUrl(str3));
            productImage.setLarge(localStoragePlugin.getUrl(str4));
            productImage.setMedium(localStoragePlugin.getUrl(str5));
            productImage.setThumbnail(localStoragePlugin.getUrl(str6));
          }
        }
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
    }
  }
  
  class _cls1 implements Runnable{
	  
	  final ProductImageServiceImpl productImageServiceImpl;
      private final File file;
      private final String str1;
      private final String str2;
      private final String str3;
      private final String str4;
      private final String str5;
      
	  _cls1(ProductImageServiceImpl paramProductImageServiceImpl, 
			  File paramFile, String paramString1, String paramString2,
			  String paramString3, String paramString4, String paramString5) {
		  
		  productImageServiceImpl = paramProductImageServiceImpl;
		  file = paramFile;
		  str1 = paramString1;
		  str2 = paramString2;
		  str3 = paramString3;
		  str4 = paramString4;
		  str5 = paramString5;
	  }
  
	  public void run(){
		  //last modify by linjie.wu
		  /*
		  Collections.sort(ProductImageServiceImpl.entityManager(this.productImageServiceImpl));
		  Iterator localIterator = ProductImageServiceImpl.entityManager(this.productImageServiceImpl).iterator();
		  while (localIterator.hasNext()){
			  StoragePlugin localStoragePlugin = (StoragePlugin)localIterator.next();
			  if (localStoragePlugin.getIsEnabled()){
				  Setting localSetting = SettingUtils.get();
				  String str = System.getProperty("java.io.tmpdir");
				  File localFile1 = new File(ProductImageServiceImpl.IIIllIll(this.productImageServiceImpl).getRealPath(localSetting.getWatermarkImage()));
				  File localFile2 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
				  File localFile3 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
				  File localFile4 = new File(str + "/upload_" + UUID.randomUUID() + "." + "jpg");
				  try {
					  ImageUtils.zoom(this.file, localFile2, localSetting.getLargeProductImageWidth().intValue(), localSetting.getLargeProductImageHeight().intValue());
					  ImageUtils.addWatermark(localFile2, localFile2, localFile1, localSetting.getWatermarkPosition(), localSetting.getWatermarkAlpha().intValue());
					  ImageUtils.zoom(this.file, localFile3, localSetting.getMediumProductImageWidth().intValue(), localSetting.getMediumProductImageHeight().intValue());
					  ImageUtils.addWatermark(localFile3, localFile3, localFile1, localSetting.getWatermarkPosition(), localSetting.getWatermarkAlpha().intValue());
					  ImageUtils.zoom(this.file, localFile4, localSetting.getThumbnailProductImageWidth().intValue(), localSetting.getThumbnailProductImageHeight().intValue());
					  localStoragePlugin.upload(this.str1, this.file, this.str2);
					  localStoragePlugin.upload(this.str3, localFile2, "image/jpeg");
					  localStoragePlugin.upload(this.str4, localFile3, "image/jpeg");
					  localStoragePlugin.upload(this.str5, localFile4, "image/jpeg");
				  }
				  finally
				  {
					  FileUtils.deleteQuietly(this.file);
					  FileUtils.deleteQuietly(localFile2);
					  FileUtils.deleteQuietly(localFile3);
					  FileUtils.deleteQuietly(localFile4);
				  }
				  break;
			  }
		  }
		   */
	  }
  }
}