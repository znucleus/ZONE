package top.zbeboy.zone.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.UndertowServletWebServerFactoryCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.stereotype.Component;
import top.zbeboy.zbase.config.ZoneProperties;

import javax.annotation.Resource;
import java.io.File;

@Component
public class UndertowConfiguration extends UndertowServletWebServerFactoryCustomizer {

    @Resource
    private ZoneProperties ZoneProperties;

    public UndertowConfiguration(ServerProperties serverProperties) {
        super(serverProperties);
    }

    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        File documentRoot = new File(System.getProperty("user.dir") + "/" + this.ZoneProperties.getConstants().getWebRoot());
        if (!documentRoot.exists()) {
            documentRoot.mkdirs();
        }
        factory.setDocumentRoot(documentRoot);
    }
}
