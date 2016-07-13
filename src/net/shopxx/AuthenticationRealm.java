package net.shopxx;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import net.shopxx.entity.Admin;
import net.shopxx.service.AdminService;
import net.shopxx.service.CaptchaService;
import net.shopxx.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.pam.UnsupportedTokenException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class AuthenticationRealm extends AuthorizingRealm {
	
  @Resource(name="captchaServiceImpl")
  private CaptchaService captchaService;
  @Resource(name="adminServiceImpl")
  private AdminService adminService;
  
  /**
   * 认证回调函数，登录时调用
   */
  protected AuthenticationInfo doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken token)
  {
    AuthenticationToken authToken = (AuthenticationToken)token;
    String username = authToken.getUsername();
    String password = new String(authToken.getPassword());
    String captchaId = authToken.getCaptchaId();
    String captcha = authToken.getCaptcha();
    String host = authToken.getHost();
    
    //验证码是否正确
    if (!this.captchaService.isValid(Setting.CaptchaType.adminLogin, captchaId, captcha)) {
      throw new UnsupportedTokenException();
    }
    
    if ((username != null) && (password != null)){
    	
      Admin admin = this.adminService.findByUsername(username);
      if (admin == null) {	//用户不存在
        throw new UnknownAccountException();
      }
      if (!admin.getIsEnabled().booleanValue()){	//用户是否有效
        throw new DisabledAccountException();
      }
      Setting settings = SettingUtils.get();
      int i;
      if (admin.getIsLocked().booleanValue()) {	//是否锁定
        if (ArrayUtils.contains(settings.getAccountLockTypes(), Setting.AccountLockType.admin))
        {
          i = settings.getAccountLockTime().intValue();
          if (i == 0) {
            throw new LockedAccountException();
          }
          Date localDate1 = admin.getLockedDate();
          Date localDate2 = DateUtils.addMinutes(localDate1, i);
          if (new Date().after(localDate2))
          {
            admin.setLoginFailureCount(Integer.valueOf(0));
            admin.setIsLocked(Boolean.valueOf(false));
            admin.setLockedDate(null);
            this.adminService.update(admin);
          }
          else
          {
            throw new LockedAccountException();
          }
        }
        else
        {
          admin.setLoginFailureCount(Integer.valueOf(0));
          admin.setIsLocked(Boolean.valueOf(false));
          admin.setLockedDate(null);
          this.adminService.update(admin);
        }
      }
      if (!DigestUtils.md5Hex(password).equals(admin.getPassword()))
      {
        i = admin.getLoginFailureCount().intValue() + 1;
        if (i >= settings.getAccountLockCount().intValue())
        {
          admin.setIsLocked(Boolean.valueOf(true));
          admin.setLockedDate(new Date());
        }
        admin.setLoginFailureCount(Integer.valueOf(i));
        this.adminService.update(admin);
        throw new IncorrectCredentialsException();
      }
      admin.setLoginIp(host);
      admin.setLoginDate(new Date());
      admin.setLoginFailureCount(Integer.valueOf(0));
      this.adminService.update(admin);
      return new SimpleAuthenticationInfo(new Principal(admin.getId(), username), password, getName());
    }
    throw new UnknownAccountException();
  }
  
  /**
   * 授权查询回调函数，进行鉴权但缓存中没有用户的授权信息时调用
   */
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals)
  {
    Principal principal = (Principal)principals.fromRealm(getName()).iterator().next();
    if (principal != null)
    {
      List list = this.adminService.findAuthorities(principal.getId());
      if (list != null)
      {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(list);
        return simpleAuthorizationInfo;
      }
    }
    return null;
  }
}