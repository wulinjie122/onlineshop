package net.shopxx.controller.shop;

import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import net.shopxx.Message;
import net.shopxx.Setting;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Member;
import net.shopxx.entity.SafeKey;
import net.shopxx.service.CaptchaService;
import net.shopxx.service.MailService;
import net.shopxx.service.MemberService;
import net.shopxx.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopPasswordController")
@RequestMapping({"/password"})
public class PasswordController
  extends BaseController
{
  @Resource(name="captchaServiceImpl")
  private CaptchaService captchaService;
  @Resource(name="memberServiceImpl")
  private MemberService memberService;
  @Resource(name="mailServiceImpl")
  private MailService mailService;
  
  @RequestMapping(value={"/find"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String find(Model model)
  {
    model.addAttribute("captchaId", UUID.randomUUID().toString());
    return "/shop/password/find";
  }
  
  @RequestMapping(value={"/find"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message find(String captchaId, String captcha, String username, String email)
  {
	//校验验证码
    if (!this.captchaService.isValid(Setting.CaptchaType.findPassword, captchaId, captcha)) {
      return Message.error("shop.captcha.invalid", new Object[0]);
    }
    //用户名邮箱是否为空
    if ((StringUtils.isEmpty(username)) || (StringUtils.isEmpty(email))) {
      return Message.error("shop.common.invalid", new Object[0]);
    }
    Member member = this.memberService.findByUsername(username);	//获取会员信息
    if (member == null) {	//该用户不存在
      return Message.error("shop.password.memberNotExist", new Object[0]);
    }
    if (!member.getEmail().equalsIgnoreCase(email)) {	//该用户邮箱无效
      return Message.error("shop.password.invalidEmail", new Object[0]);
    }
    
    Setting settings = SettingUtils.get();
    SafeKey safeKey = new SafeKey();
    safeKey.setValue(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
    safeKey.setExpire(settings.getSafeKeyExpiryTime().intValue() != 0 ? DateUtils.addMinutes(new Date(), settings.getSafeKeyExpiryTime().intValue()) : null);
    member.setSafeKey(safeKey);
    this.memberService.update(member);
    this.mailService.sendFindPasswordMail(member.getEmail(), member.getUsername(), safeKey);
    return Message.success("shop.password.mailSuccess", new Object[0]);
  }
  
  @RequestMapping(value={"/reset"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String reset(String username, String key, Model model)
  {
    Member localMember = this.memberService.findByUsername(username);
    if (localMember == null) {
      return "/shop/common/error";
    }
    SafeKey localSafeKey = localMember.getSafeKey();
    if ((localSafeKey == null) || (localSafeKey.getValue() == null) || (!localSafeKey.getValue().equals(key))) {
      return "/shop/common/error";
    }
    if (localSafeKey.hasExpired())
    {
      model.addAttribute("erroInfo", Message.warn("shop.password.hasExpired", new Object[0]));
      return "/shop/common/error";
    }
    model.addAttribute("captchaId", UUID.randomUUID().toString());
    model.addAttribute("member", localMember);
    model.addAttribute("key", key);
    return "/shop/password/reset";
  }
  
  @RequestMapping(value={"reset"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
  @ResponseBody
  public Message reset(String captchaId, String captcha, String username, String newPassword, String key)
  {
    if (!this.captchaService.isValid(Setting.CaptchaType.resetPassword, captchaId, captcha)) {
      return Message.error("shop.captcha.invalid", new Object[0]);
    }
    Member localMember = this.memberService.findByUsername(username);
    if (localMember == null) {
      return MESSAGE_ERROR;
    }
    if (!validateValue(Member.class, "password", newPassword, new Class[] { BaseEntity.Save.class })) {
      return Message.warn("shop.password.invalidPassword", new Object[0]);
    }
    Setting localSetting = SettingUtils.get();
    if ((newPassword.length() < localSetting.getPasswordMinLength().intValue()) || (newPassword.length() > localSetting.getPasswordMaxLength().intValue())) {
      return Message.warn("shop.password.invalidPassword", new Object[0]);
    }
    SafeKey localSafeKey = localMember.getSafeKey();
    if ((localSafeKey == null) || (localSafeKey.getValue() == null) || (!localSafeKey.getValue().equals(key))) {
      return MESSAGE_ERROR;
    }
    if (localSafeKey.hasExpired()) {
      return Message.error("shop.password.hasExpired", new Object[0]);
    }
    localMember.setPassword(DigestUtils.md5Hex(newPassword));
    localSafeKey.setExpire(new Date());
    localSafeKey.setValue(null);
    this.memberService.update(localMember);
    return Message.success("shop.password.resetSuccess", new Object[0]);
  }
}


/* Location:           D:\workspace\shopxx\WEB-INF\classes\
 * Qualified Name:     net.shopxx.controller.shop.PasswordController
 * JD-Core Version:    0.7.0.1
 */