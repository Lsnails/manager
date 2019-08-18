package com.base.common.config;

import com.base.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 * @since 2.1.0 2017-04-21
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 2);
        //过滤应用程序中所有资源,当前应用程序根下的所有文件包括多级子目录下的所有文件，注意这里*前有“/”
        registration.addUrlPatterns("/*");
//        //过滤指定的类型文件资源, 当前应用程序根目录下的所有html文件，注意：*.html前没有“/”,否则错误
//        registration.addUrlPatterns(".html");
//        //过滤指定的目录下的所有文件,当前应用程序根目录下的folder_name子目录（可以是多级子目录）下所有文件
//        registration.addUrlPatterns("/folder_name/*");
//        //过滤指定文件
//        registration.addUrlPatterns("/index.html");
        return registration;
    }

    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
    
//    /**
//     * 自定义过滤器-----未启用（来源于网站前端登录）
//     * @Title: FrontWebFilterRegistration   
//     * @Description: TODO(这里用一句话描述这个方法的作用)   
//     * @author: huanw
//     * @param: @return      
//     * @return: FilterRegistrationBean
//     * @date:   2019年3月11日 下午2:31:14       
//     * @throws
//     */
//    @Bean
//    public FilterRegistrationBean FrontWebFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        registration.setName("frontWebFilter");
//        FrontWebFilter frontWebFilter = new FrontWebFilter();
//        registration.setFilter(frontWebFilter);
//        registration.setOrder(Integer.MAX_VALUE - 1);
//        return registration;
//    }
    
}
