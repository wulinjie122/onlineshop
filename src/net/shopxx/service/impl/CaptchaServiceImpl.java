package net.shopxx.service.impl;

import java.awt.image.BufferedImage;
import javax.annotation.Resource;
import net.shopxx.Setting;
import net.shopxx.util.SettingUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service("captchaServiceImpl")
public class CaptchaServiceImpl
  implements net.shopxx.service.CaptchaService
{
  @Resource(name="imageCaptchaService")
  private com.octo.captcha.service.CaptchaService imageCaptchaService;
  
  public BufferedImage buildImage(String captchaId)
  {
    return (BufferedImage)this.imageCaptchaService.getChallengeForID(captchaId);
  }
  
  public boolean isValid(Setting.CaptchaType captchaType, String captchaId, String captcha)
  {
    Setting settings = SettingUtils.get();
    if ((captchaType == null) || (ArrayUtils.contains(settings.getCaptchaTypes(), captchaType)))
    {
      if ((StringUtils.isNotEmpty(captchaId)) && (StringUtils.isNotEmpty(captcha))) {
        try
        {
          return this.imageCaptchaService.validateResponseForID(captchaId, captcha.toUpperCase()).booleanValue();
        }
        catch (Exception localException)
        {
          return false;
        }
      }
      return false;
    }
    return true;
  }
}