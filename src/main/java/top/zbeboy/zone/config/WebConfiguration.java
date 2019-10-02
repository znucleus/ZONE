package top.zbeboy.zone.config;


import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.apache.commons.codec.CharEncoding;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.view.ThymeleafView;

import javax.annotation.Resource;
import java.io.File;

/**
 * Spring boot web init.
 *
 * @author zbeboy
 * @version 1.0
 * @since 1.0
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Resource
    private ZoneProperties ZoneProperties;

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

    /**
     * undertow http 重定向 https
     *
     * @return factory
     */
    @Bean
    public UndertowServletWebServerFactory undertow() {
        UndertowServletWebServerFactory undertow = new UndertowServletWebServerFactory();
        undertow.addBuilderCustomizers(builder -> builder.addHttpListener(this.ZoneProperties.getConstants().getServerHttpPort(), "0.0.0.0"));
        undertow.addDeploymentInfoCustomizers(deploymentInfo ->
                deploymentInfo.addSecurityConstraint(new SecurityConstraint()
                        .addWebResourceCollection(new WebResourceCollection()
                                .addUrlPattern("/*"))
                        .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
                        .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
                        .setDefaultEncoding(CharEncoding.UTF_8)
                        .setUrlEncoding(CharEncoding.UTF_8)
                        .setConfidentialPortManager(exchange -> this.ZoneProperties.getConstants().getServerHttpsPort())
        );
        File documentRoot = new File(System.getProperty("user.dir") + "/" + this.ZoneProperties.getConstants().getWebRoot());
        if (!documentRoot.exists()) {
            documentRoot.mkdirs();
        }
        undertow.setDocumentRoot(documentRoot);
        return undertow;
    }
}
