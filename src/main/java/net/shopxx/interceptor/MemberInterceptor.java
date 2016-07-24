package net.shopxx.interceptor;

import java.net.URLEncoder;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.shopxx.Principal;
import net.shopxx.entity.Member;
import net.shopxx.service.MemberService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MemberInterceptor extends HandlerInterceptorAdapter {
	private static final String REDIRECT = "redirect:";
	private static final String REDIRECT_URL = "redirectUrl";
	private static final String MEMBER = "member";
	private static final String LOGIN_HTML = "/login.jhtml";
	private String loginHtml = "/login.jhtml";
	
	@Value("${url_escaping_charset}")
	private String IIIlllll;
	
	@Resource(name = "memberServiceImpl")
	private MemberService IIlIIIII;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
		HttpSession localHttpSession = request.getSession();
		Principal localPrincipal = (Principal) localHttpSession.getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
		if (localPrincipal != null) {
			return true;
		}
		String str1 = request.getHeader("X-Requested-With");
		if ((str1 != null) && (str1.equalsIgnoreCase("XMLHttpRequest"))) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(403);
			return false;
		}
		if (request.getMethod().equalsIgnoreCase("GET")) {
			String str2 = request.getQueryString() != null ? request
					.getRequestURI()
					+ "?" + request.getQueryString() : request.getRequestURI();
			response.sendRedirect(request.getContextPath() + this.loginHtml
					+ "?" + "redirectUrl" + "="
					+ URLEncoder.encode(str2, this.IIIlllll));
		} else {
			response.sendRedirect(request.getContextPath() + this.loginHtml);
		}
		return false;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
		if (modelAndView != null) {
			String str = modelAndView.getViewName();
			if (!StringUtils.startsWith(str, "redirect:")) {
				modelAndView.addObject("member", this.IIlIIIII.getCurrent());
			}
		}
	}

	public String getLoginUrl() {
		return this.loginHtml;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginHtml = loginUrl;
	}
}

/*
 * Location: D:\workspace\shopxx\WEB-INF\classes\
 * 
 * Qualified Name: net.shopxx.interceptor.MemberInterceptor
 * 
 * JD-Core Version: 0.7.0.1
 * 
 */