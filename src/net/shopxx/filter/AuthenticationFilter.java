package net.shopxx.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.shopxx.service.RSAService;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.apache.shiro.authc.AuthenticationToken;


public class AuthenticationFilter extends FormAuthenticationFilter {
	private static final String ENPASSWORD = "enPassword";
	private static final String CAPTCHA_ID = "captchaId";
	private static final String CAPTCHA = "captcha";
	private String enPassword = "enPassword";
	private String captchaId = "captchaId";
	private String captcha = "captcha";
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaServiceImpl;

	/**
	 * 创建认证信息
	 */
	protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
		String username = getUsername(servletRequest);		//用户名
		String password = getPassword(servletRequest);		//密码
		String captchaId = getCaptchaId(servletRequest);	//验证码图片
		String captcha = getCaptcha(servletRequest);		//验证码输入
		boolean rememberMe = isRememberMe(servletRequest);	//是否保存登陆信息
		String host = getHost(servletRequest);				//登陆IP
		return new net.shopxx.AuthenticationToken(username, password, captchaId, captcha, rememberMe,host);
	}

	/**
	 * 登陆拒绝
	 */
	protected boolean onAccessDenied(ServletRequest servletRequest,ServletResponse servletResponse) {
		HttpServletRequest localHttpServletRequest = null;
		HttpServletResponse localHttpServletResponse = null;
		try {
			localHttpServletRequest = (HttpServletRequest) servletRequest;
			localHttpServletResponse = (HttpServletResponse) servletResponse;
			String str = localHttpServletRequest.getHeader("X-Requested-With");
			if ((str != null) && (str.equalsIgnoreCase("XMLHttpRequest"))) {
				localHttpServletResponse.addHeader("loginStatus",
						"accessDenied");
				localHttpServletResponse.sendError(403);
				return false;
			}
			return super.onAccessDenied(localHttpServletRequest,localHttpServletResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 登陆成功
	 */
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
			ServletRequest servletRequest, ServletResponse servletResponse) {
		Session localSession = subject.getSession();
		HashMap localHashMap = new HashMap();
		Collection localCollection = localSession.getAttributeKeys();
		Iterator localIterator = localCollection.iterator();
		Object localObject;
		while (localIterator.hasNext()) {
			localObject = localIterator.next();
			localHashMap.put(localObject,
					localSession.getAttribute(localObject));
		}
		localSession.stop();
		localSession = subject.getSession();
		localIterator = localHashMap.entrySet().iterator();
		while (localIterator.hasNext()) {
			localObject = (Map.Entry) localIterator.next();
			localSession.setAttribute(((Map.Entry) localObject).getKey(),
					((Map.Entry) localObject).getValue());
		}
		
		try {
			return super.onLoginSuccess(token, subject, servletRequest,servletResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
		
	}

	protected String getPassword(ServletRequest servletRequest) {
		HttpServletRequest localHttpServletRequest = (HttpServletRequest) servletRequest;
		String str = this.rsaServiceImpl.decryptParameter(this.enPassword,
				localHttpServletRequest);
		this.rsaServiceImpl.removePrivateKey(localHttpServletRequest);
		return str;
	}

	protected String getCaptchaId(ServletRequest paramServletRequest) {
		String str = WebUtils.getCleanParam(paramServletRequest, this.captchaId);
		if (str == null) {
			str = ((HttpServletRequest) paramServletRequest).getSession().getId();
		}
		return str;
	}

	protected String getCaptcha(ServletRequest paramServletRequest) {
		return WebUtils.getCleanParam(paramServletRequest, this.captcha);
	}

	public String getEnPasswordParam() {
		return this.enPassword;
	}

	public void setEnPasswordParam(String enPasswordParam) {
		this.enPassword = enPasswordParam;
	}

	public String getCaptchaIdParam() {
		return this.captchaId;
	}

	public void setCaptchaIdParam(String captchaIdParam) {
		this.captchaId = captchaIdParam;
	}

	public String getCaptchaParam() {
		return this.captcha;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captcha = captchaParam;
	}
}