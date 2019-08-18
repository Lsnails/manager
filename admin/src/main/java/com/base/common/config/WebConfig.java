package com.base.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc配置 -如启动国际化需暂时屏蔽掉，使用Mainconfig.java
 *
 * @since 3.0.0 2018-01-25
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/statics/**").addResourceLocations("classpath:/statics/");
    }

    @Bean
    public WebInterceptor WebInterceptor() {
        return new WebInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //网站前端需要登录的方法拦截器 、网站是否登录拦截器
        registry.addInterceptor(WebInterceptor())
                .addPathPatterns("/mhkaom/**/").excludePathPatterns("/**/*.css",
                "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.jpeg", "/**/*.gif");
        registry.addInterceptor(new ErrorInterceptor()).addPathPatterns("/**").excludePathPatterns("/**/*.css",
                "/**/*.js", "/**/*.png", "/**/*.jpg",
                "/**/*.jpeg", "/**/*.gif", "/**/fonts/*");
    }
//    @Override
//    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        ObjectMapper objectMapper = jackson2HttpMessageConverter.getObjemctMapper();
//
//        //生成json时，将所有Long转换成String
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
//        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
//        objectMapper.registerModule(simpleModule);
//
//        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        converters.add(0, jackson2HttpMessageConverter);
//    }

}
