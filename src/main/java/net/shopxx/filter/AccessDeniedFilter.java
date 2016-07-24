package net.shopxx.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class AccessDeniedFilter implements Filter {
	private static final String IIIllIlI = "Access denied!";

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain) {
		//��ʱ�޸�  wulinjie
		try {
			HttpServletResponse localHttpServletResponse = (HttpServletResponse) servletResponse;
			localHttpServletResponse.sendError(403, "Access denied!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}