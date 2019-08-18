package com.base.common.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网站前端需要登录的方法拦截器
 * 网站是否登录拦截器
 * @ClassName:  WebInterceptor   
 * @Description:网站前端需要登录的方法拦截器
 * @author:  huanw
 * @date:   2019年3月11日 下午2:38:17
 */
@Component
public class WebInterceptor implements HandlerInterceptor{
	
//	@Autowired
//	private MemberService memberService;
	
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        boolean flag =true;
//        // 获取浏览器访问访问服务器时传递过来的cookie数组
//        Cookie[] cookies = httpServletRequest.getCookies();
//        //如果用户是第一次访问，那么得到的cookies将是null
//        if (cookies!=null) {
//            for (int i = 0; i < cookies.length; i++) {
//                Cookie cookie = cookies[i];
//                //如果浏览器cookie还在，就继续让用户保持登陆状态
//                if (cookie.getName().equals(Constant.MHKAOM_MOBLIE)) {
//                	String cookie_mobile = cookie.getValue();
//                	//System.out.println(cookie.getName() + "  "+cookie.getValue()+"  "+cookie.getMaxAge());
//                	HttpSession session = httpServletRequest.getSession();
//                	//session的key是手机号，值是会员实体信息
//                	MemberEntity member= (MemberEntity)session.getAttribute(cookie_mobile);
//                	if(member==null) {
//                		member = memberService.queryMember(cookie_mobile);
//                		//session的key是手机号，值是会员实体信息,补全个人的其他信息
//            			memberService.getMemberInfo(member);
//            			member.setPassword("");//将密码设置为空，出于安全考虑
//            			//session的key是手机号，值是会员实体信息,补全个人的其他信息
//                        session.setAttribute(cookie_mobile, member);
//                		session.setMaxInactiveInterval(60 * 60 * 1);//单位为秒   60 * 60 * 24 表示 24小时
//                    	//System.out.println(member+"   "+session.getMaxInactiveInterval());
//                	}
//                	//客户端的cookies与session'均未过期，免登陆
//                	if(member!=null) {
//                		//System.out.println("验证session时间：   "+session.getMaxInactiveInterval() +"  "+session.getCreationTime());
//                		flag=false;//表示已经登录
//                	}
//                }
//
//            }
//        }
//        //url中包含“/mhkaom/”需要验证前端是否登录
////      System.out.println("网站是否登录拦截器，只拦截需登录的功能---"+requestURI);
//		if(flag) {
//			httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/websiteLogin");
//    		return false;
//		}
    	//放行
        System.out.println("进入拦截器");
        return true;
    }
 
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
 
    }
 
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
 
    }
}
