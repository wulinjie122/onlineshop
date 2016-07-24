package net.shopxx.service;

import java.awt.image.BufferedImage;
import net.shopxx.Setting;

public abstract interface CaptchaService
{
  public abstract BufferedImage buildImage(String paramString);
  
  public abstract boolean isValid(Setting.CaptchaType paramCaptchaType, String paramString1, String paramString2);
}