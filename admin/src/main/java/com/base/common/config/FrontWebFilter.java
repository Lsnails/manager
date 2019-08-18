//package com.base.common.config;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 自定义过滤器-----未启用（来源于网站前端登录）
// * @ClassName:  FrontWebFilter   
// * @Description:TODO(这里用一句话描述这个类的作用)   
// * @author:  huanw
// * @date:   2019年3月11日 下午2:32:04
// */
//public class FrontWebFilter implements Filter {
//	@Override
//	public void destroy() {
//		
//	}
//
//	@Override
//	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
//			throws IOException, ServletException {
//		System.out.println("测试过滤器222222---请求开始");
//		HttpServletRequest request = (HttpServletRequest) servletRequest;
//		HttpServletResponse response = (HttpServletResponse) servletResponse;
//		String url = request.getRequestURI();//获取request请求的的url
//		boolean flag = url.contains("/mywebsite/");//url中包含“/mywebsite/”需要验证前端是否登录
//		System.out.println("测试过滤器222222---"+url);
//		if(flag) {
//			response.sendRedirect(request.getContextPath()+ "/websiteLogin");
////			response.sendRedirect("/admin/websiteLogin");
////			chain.doFilter(request, response);//过滤器放行
////			response.sendRedirect("/webFront/news/223.html");
////			request.getRequestDispatcher("admin/webFront/news/223.html").forward(request,response);
//    		return;
//		}
//		chain.doFilter(request, response);//过滤器放行
//		System.out.println("测试过滤器222222---请求结束");
//	}
//
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//		System.out.println("初始化------------------");
//	}
//
//}
