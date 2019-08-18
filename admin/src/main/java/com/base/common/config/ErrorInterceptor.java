package com.base.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 错误信息拦截器
 * @ClassName:  ErrorInterceptor   
 * @Description:错误信息拦截器
 * @author:  huanw
 * @date:   2019年4月19日 上午10:14:10
 */
@Component
public class ErrorInterceptor implements HandlerInterceptor{
	protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
    	int status = httpServletResponse.getStatus();
    	boolean flag=false;
        String requestURI = httpServletRequest.getRequestURI();
        logger.debug("-------------------请求路径："+requestURI);
        logger.debug("-------------------请求状态："+status);
//        System.out.println(requestURI);
//        System.out.println(status);
        switch (status) {
			case  200:
				flag = true;
				break;
			case 404:
				logger.debug("-------------------404页面路径："+httpServletRequest.getContextPath()+"/404.html");
	            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/404.html");
	            flag = false;
				break;
//			case 500:
//				//前端
//				 httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/500.html");
//				break;
			default:
				break;
		}
    	//放行
        return flag;
    }
 
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
 
    }
 
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
 
    }
}
