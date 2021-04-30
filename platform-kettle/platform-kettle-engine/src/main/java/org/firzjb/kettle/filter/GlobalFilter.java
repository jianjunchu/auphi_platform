package org.firzjb.kettle.filter;

import org.firzjb.kettle.utils.JsonUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
public class GlobalFilter implements Filter {

	@Override
	public void destroy() {

	}

	/**
	 * 过滤器解决前端跨域问题
	 * @param req
	 * @param res
	 * @param fc
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {

		if(res instanceof HttpServletResponse) {
			HttpServletResponse response = (HttpServletResponse) res;
			JsonUtils.put(response);

			HttpServletRequest request = (HttpServletRequest) req;
	        JsonUtils.put(request);

	        response.setHeader("Access-Control-Allow-Origin", "*");
	        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
	        response.setHeader("Access-Control-Max-Age", "3600");
	        response.setHeader("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization");
	        response.setHeader("Access-Control-Allow-Credentials", "true");

		}

		fc.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {

	}
}
