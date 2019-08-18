//package com.base.common.config;
//
//import java.util.Locale;
//
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//import org.springframework.web.servlet.i18n.CookieLocaleResolver;
//import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
//
///**
// * WebMvc配置-支持国际化 在不使用国际化的情况下屏蔽
// * @ClassName:  MainConfig   
// * @Description:WebMvc配置
// * @author:  huanw
// * @date:   2018年12月24日 下午3:35:23
// */
//@Configuration
//@ComponentScan
//@EnableAutoConfiguration
//public class MainConfig extends WebMvcConfigurationSupport {
//    @Bean
//    public LocaleResolver localeResolver() {
////    	SessionLocaleResolver slr = new SessionLocaleResolver();
//        CookieLocaleResolver slr = new CookieLocaleResolver();
//        // 默认语言 TRADITIONAL_CHINESE繁体 SIMPLIFIED_CHINESE简体
//        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//        return slr;
//    }
//
//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        // 参数名
//        lci.setParamName("_lang");
//        return lci;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    	//addResourceLocations本地路径 
//    	registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("swagger/**").addResourceLocations("classpath:/public/swagger/");
//        
//    }
//}
