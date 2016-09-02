package net.shopxx.controller.shop;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.shopxx.CommonAttributes;
import net.shopxx.Message;
import net.shopxx.Principal;
import net.shopxx.Setting;
import net.shopxx.entity.Area;
import net.shopxx.entity.BaseEntity;
import net.shopxx.entity.Cart;
import net.shopxx.entity.Member;
import net.shopxx.entity.MemberAttribute;
import net.shopxx.service.AreaService;
import net.shopxx.service.CaptchaService;
import net.shopxx.service.CartService;
import net.shopxx.service.MemberAttributeService;
import net.shopxx.service.MemberRankService;
import net.shopxx.service.MemberService;
import net.shopxx.service.RSAService;
import net.shopxx.util.CookieUtils;
import net.shopxx.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("shopRegisterController")
@RequestMapping( { "/register" })
public class RegisterController extends BaseController {
	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;
	@Resource(name = "areaServiceImpl")
	private AreaService IIlIIIlI;
	@Resource(name = "cartServiceImpl")
	private CartService IIlIIIll;

	@RequestMapping(value = { "/check_username" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		return (!this.memberService.usernameDisabled(username))
				&& (!this.memberService.usernameExists(username));
	}

	@RequestMapping(value = { "/check_email" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public boolean checkEmail(String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		return !this.memberService.emailExists(email);
	}

	@RequestMapping(method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String index(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		model.addAttribute("genders", Member.Gender.values());
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/shop/register/index";
	}

	@RequestMapping(value = { "/submit" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public Message submit(String captchaId, String captcha, String username,String email, 
			HttpServletRequest request,HttpServletResponse response, HttpSession session) {
		
		//解密用户密码
		String password = this.rsaService.decryptParameter("enPassword", request);
		
		//删除私钥
		this.rsaService.removePrivateKey(request);
		
		//校验验证码是否正确
		if (!this.captchaService.isValid(Setting.CaptchaType.memberRegister,captchaId, captcha)) {
			return Message.error("shop.captcha.invalid", new Object[0]);
		}
		
		//是否开放注册？？暂时没看懂
		Setting settings = SettingUtils.get();
		if (!settings.getIsRegisterEnabled().booleanValue()) {
			return Message.error("shop.register.disabled", new Object[0]);
		}
		
		//校验用户名、密码和邮箱是否符合约束要求
		if(!validateValue(Member.class, "username", username, new Class[] { BaseEntity.Save.class }) ||
			!validateValue(Member.class, "password", password,new Class[] { BaseEntity.Save.class }) ||
			!validateValue(Member.class, "email", email,new Class[] { BaseEntity.Save.class })
		) {
			return Message.error("shop.common.invalid", new Object[0]);
		}
		
		if((username.length() < settings.getUsernameMinLength().intValue()) || (username.length() > settings.getUsernameMaxLength().intValue())) {
			return Message.error("shop.common.invalid", new Object[0]);
		}
		
		if ((password.length() < settings.getPasswordMinLength().intValue())
				|| (password.length() > settings.getPasswordMaxLength()
						.intValue())) {
			return Message.error("shop.common.invalid", new Object[0]);
		}
		if ((this.memberService.usernameDisabled(username))
				|| (this.memberService.usernameExists(username))) {
			return Message.error("shop.register.disabledExist", new Object[0]);
		}
		if ((!settings.getIsDuplicateEmail().booleanValue())
				&& (this.memberService.emailExists(email))) {
			return Message.error("shop.register.emailExist", new Object[0]);
		}
		Member localMember = new Member();
		List localList = this.memberService.findList();
		Object localObject2 = localList.iterator();
		Object localObject4;
		while (((Iterator) localObject2).hasNext()) {
			MemberAttribute localObject1 = (MemberAttribute) ((Iterator) localObject2).next();
			String s1 = request.getParameter("memberAttribute_"+ ((MemberAttribute) localObject1).getId());
			if ((((MemberAttribute) localObject1).getType() == MemberAttribute.Type.name)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.address)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.zipCode)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.phone)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.mobile)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.text)
					|| (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.select)) {
				if ((((MemberAttribute) localObject1).getIsRequired()
						.booleanValue())
						&& (StringUtils.isEmpty(s1))) {
					return Message.error("shop.common.invalid", new Object[0]);
				}
				localMember.setAttributeValue((MemberAttribute) localObject1,s1);
			} else {
				Member.Gender localGender;
				if (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.gender) {
					localGender = StringUtils.isNotEmpty(s1) ? Member.Gender.valueOf(s1) : null;
					if ((((MemberAttribute) localObject1).getIsRequired().booleanValue())
							&& (localGender == null)
					) {
						return Message.error("shop.common.invalid",
								new Object[0]);
					}
					localMember.setGender(localGender);
				} else if (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.birth) {
					try {
						Date date = StringUtils.isNotEmpty(s1) ? DateUtils.parseDate(s1,CommonAttributes.DATE_PATTERNS) : null;
						if ((((MemberAttribute) localObject1).getIsRequired()
								.booleanValue()) && (date == null)) {
							return Message.error("shop.common.invalid",new Object[0]);
						}
						localMember.setBirth(date);
					} catch (ParseException localParseException1) {
						return Message.error("shop.common.invalid",
								new Object[0]);
					}
				} else if (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.area) {
					localObject4 = StringUtils.isNotEmpty(s1) ? (Area) this.IIlIIIlI.find(Long.valueOf(s1)) : null;
					if (localObject4 != null) {
						localMember.setArea((Area) localObject4);
					} else if (((MemberAttribute) localObject1).getIsRequired()
							.booleanValue()) {
						return Message.error("shop.common.invalid",
								new Object[0]);
					}
				} else if (((MemberAttribute) localObject1).getType() == MemberAttribute.Type.checkbox) {
					String[] as = request.getParameterValues("memberAttribute_"+ ((MemberAttribute) localObject1).getId());
					List list1  = as != null ? Arrays.asList(as) : null;
					if ((((MemberAttribute) localObject1).getIsRequired().booleanValue())&& ((list1 == null) || ((list1).isEmpty()))) {
						return Message.error("shop.common.invalid",
								new Object[0]);
					}
					localMember.setAttributeValue(
							(MemberAttribute) localObject1, list1);
				}
			}
		}
		localMember.setUsername(username.toLowerCase());
		localMember.setPassword(DigestUtils.md5Hex(password));
		localMember.setEmail(email);
		localMember.setPoint(settings.getRegisterPoint());
		localMember.setAmount(new BigDecimal(0));
		localMember.setBalance(new BigDecimal(0));
		localMember.setIsEnabled(Boolean.valueOf(true));
		localMember.setIsLocked(Boolean.valueOf(false));
		localMember.setLoginFailureCount(Integer.valueOf(0));
		localMember.setLockedDate(null);
		localMember.setRegisterIp(request.getRemoteAddr());
		localMember.setLoginIp(request.getRemoteAddr());
		localMember.setLoginDate(new Date());
		localMember.setSafeKey(null);
		localMember.setMemberRank(this.memberRankService.findDefault());
		localMember.setFavoriteProducts(null);
		this.memberService.save(localMember);
		Object localObject1 = this.IIlIIIll.getCurrent();
		if ((localObject1 != null)
				&& (((Cart) localObject1).getMember() == null)) {
			this.IIlIIIll.merge(localMember, (Cart) localObject1);
			CookieUtils.removeCookie(request, response, "cartId");
			CookieUtils.removeCookie(request, response, "cartKey");
		}
		localObject2 = new HashMap();
		Object localObject3 = session.getAttributeNames();
		while (((Enumeration) localObject3).hasMoreElements()) {
			localObject4 = (String) ((Enumeration) localObject3).nextElement();
			((Map) localObject2).put(localObject4, session
					.getAttribute((String) localObject4));
		}
		session.invalidate();
		session = request.getSession();
		Object localObject5 = ((Map) localObject2).entrySet().iterator();
		while (((Iterator) localObject5).hasNext()) {
			localObject4 = (Map.Entry) ((Iterator) localObject5).next();
			session.setAttribute((String) ((Map.Entry) localObject4).getKey(),
					((Map.Entry) localObject4).getValue());
		}
		session.setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, new Principal(
				localMember.getId(), localMember.getUsername()));
		CookieUtils.addCookie(request, response, "username", localMember
				.getUsername());
		return Message.success("shop.register.success", new Object[0]);
	}
}