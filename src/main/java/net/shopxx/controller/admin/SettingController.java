package net.shopxx.controller.admin;

import java.io.FileOutputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.MessagingException;

import net.shopxx.Message;
import net.shopxx.Setting;
import net.shopxx.service.CacheService;
import net.shopxx.service.FileService;
import net.shopxx.service.MailService;
import net.shopxx.service.StaticService;
import net.shopxx.util.SettingUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.mail.smtp.SMTPSendFailedException;
import com.sun.mail.smtp.SMTPSenderFailedException;

@Controller("adminstingController")
@RequestMapping({"/admin/setting"})
public class SettingController extends BaseController
{

  @javax.annotation.Resource(name="fileServiceImpl")
  private FileService IIIlllIl; 

  @javax.annotation.Resource(name="mailServiceImpl")
  private MailService IIIllllI;

  @javax.annotation.Resource(name="cacheServiceImpl")
  private CacheService IIIlllll;

  @javax.annotation.Resource(name="staticServiceImpl")
  private StaticService IIlIIIII;

  @RequestMapping(value={"/mail_test"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message mailTest(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail)
  {
    if (StringUtils.isEmpty(toMail))
      return ERROR;
    Setting localSetting = SettingUtils.get();
    if (StringUtils.isEmpty(smtpPassword))
      smtpPassword = localSetting.getSmtpPassword();
    try
    {
      if ((!IIIllIlI(Setting.class, "smtpFromMail", smtpFromMail, new Class[0])) || (!IIIllIlI(Setting.class, "smtpHost", smtpHost, new Class[0])) || (!IIIllIlI(Setting.class, "smtpPort", smtpPort, new Class[0])) || (!IIIllIlI(Setting.class, "smtpUsername", smtpUsername, new Class[0])))
        return ERROR;
      this.IIIllllI.sendTestMail(smtpFromMail, smtpHost, smtpPort, smtpUsername, smtpPassword, toMail);
    }
    catch (MailSendException localMailSendException)
    {
      Exception[] arrayOfException1 = localMailSendException.getMessageExceptions();
      if (arrayOfException1 != null)
        for (Exception localException1 : arrayOfException1)
        {
          Object localObject;
          Exception localException2;
          if ((localException1 instanceof SMTPSendFailedException))
          {
            localObject = (SMTPSendFailedException)localException1;
            localException2 = ((SMTPSendFailedException)localObject).getNextException();
            if ((localException2 instanceof SMTPSenderFailedException))
              return Message.error("admin.setting.mailTestSenderFailed", new Object[0]);
          }
          else
          {
            if (!(localException1 instanceof MessagingException))
              continue;
            localObject = (MessagingException)localException1;
            localException2 = ((MessagingException)localObject).getNextException();
            if ((localException2 instanceof UnknownHostException))
              return Message.error("admin.setting.mailTestUnknownHost", new Object[0]);
            if ((localException2 instanceof ConnectException))
              return Message.error("admin.setting.mailTestConnect", new Object[0]);
          }
        }
      return Message.error("admin.setting.mailTestError", new Object[0]);
    }
    catch (MailAuthenticationException localMailAuthenticationException)
    {
      return Message.error("admin.setting.mailTestAuthentication", new Object[0]);
    }
    catch (Exception localException)
    {
      return Message.error("admin.setting.mailTestError", new Object[0]);
    }
    return (Message)Message.success("admin.setting.mailTestSuccess", new Object[0]);
  }

  @RequestMapping(value={"/edit"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String edit(ModelMap model)
  {
    model.addAttribute("watermarkPositions", Setting.WatermarkPosition.values());
    model.addAttribute("roundTypes", Setting.RoundType.values());
    model.addAttribute("captchaTypes", Setting.CaptchaType.values());
    model.addAttribute("accountLockTypes", Setting.AccountLockType.values());
    model.addAttribute("stockAllocationTimes", Setting.StockAllocationTime.values());
    model.addAttribute("reviewAuthorities", Setting.ReviewAuthority.values());
    model.addAttribute("consultationAuthorities", Setting.ConsultationAuthority.values());
    return "/admin/setting/edit";
  }

  @RequestMapping(value={"/update"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
	public String update(Setting setting, MultipartFile watermarkImageFile,
			RedirectAttributes redirectAttributes) {
		FileOutputStream fileoutputstream;
		if (!IIIllIlI(setting, new Class[0]))
			return "/admin/common/error";
		if (setting.getUsernameMinLength().intValue() > setting
				.getUsernameMaxLength().intValue()
				|| setting.getPasswordMinLength().intValue() > setting
						.getPasswordMinLength().intValue())
			return "/admin/common/error";
		Setting setting1 = SettingUtils.get();
		if (StringUtils.isEmpty(setting.getSmtpPassword()))
			setting.setSmtpPassword(setting1.getSmtpPassword());
		if (watermarkImageFile != null && !watermarkImageFile.isEmpty()) {
			if (!IIIlllIl.isValid(net.shopxx.FileInfo.FileType.image,
					watermarkImageFile)) {
				IIIllIlI(redirectAttributes,
						Message.error("admin.upload.invalid", new Object[0]));
				return "redirect:edit.jhtml";
			}
			String s = IIIlllIl.uploadLocal(net.shopxx.FileInfo.FileType.image,
					watermarkImageFile);
			setting.setWatermarkImage(s);
		} else {
			setting.setWatermarkImage(setting1.getWatermarkImage());
		}
		setting.setCnzzSiteId(setting1.getCnzzSiteId());
		setting.setCnzzPassword(setting1.getCnzzPassword());
		SettingUtils.set(setting);
		IIIlllll.clear();
		IIlIIIII.buildIndex();
		IIlIIIII.buildOther();
		fileoutputstream = null;
		try {
			ClassPathResource classpathresource = new ClassPathResource(
					"/shopxx.properties");
			Properties properties = PropertiesLoaderUtils
					.loadProperties(classpathresource);
			String s1 = properties.getProperty("template.update_delay");
			String s2 = properties.getProperty("message.cache_seconds");
			if (setting.getIsDevelopmentEnabled().booleanValue()) {
				if (!s1.equals("0") || !s2.equals("0")) {
					fileoutputstream = new FileOutputStream(
							classpathresource.getFile());
					properties.setProperty("template.update_delay", "0");
					properties.setProperty("message.cache_seconds", "0");
					properties.store(fileoutputstream, "SHOP++ PROPERTIES");
				}
			} else if (s1.equals("0") || s2.equals("0")) {
				fileoutputstream = new FileOutputStream(
						classpathresource.getFile());
				properties.setProperty("template.update_delay", "3600");
				properties.setProperty("message.cache_seconds", "3600");
				properties.store(fileoutputstream, "SHOP++ PROPERTIES");
			}
			// break MISSING_BLOCK_LABEL_416;
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fileoutputstream);
		}
		IOUtils.closeQuietly(fileoutputstream);
		// break MISSING_BLOCK_LABEL_421;
		// Exception exception1;
		// exception1;
		// IOUtils.closeQuietly(fileoutputstream);
		// throw exception1;
		// IOUtils.closeQuietly(fileoutputstream);
		IIIllIlI(redirectAttributes, SUCCESS);
		return "redirect:edit.jhtml";
	}
}