package top.zbeboy.zone.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.view.ThymeleafView;

import javax.annotation.Resource;

/**
 * Spring boot web init.
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 切换语言
     *
     * @return 语言环境
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new SessionLocaleResolver();
    }

    /**
     * ajax 返回页面
     *
     * @return 页面节点
     */
    @Bean
    @Scope("prototype")
    public ThymeleafView thymeleafView() {
        ThymeleafView thymeleafView = new ThymeleafView();
        thymeleafView.setMarkupSelector("#page-wrapper");
        return thymeleafView;
    }
}
