package top.zbeboy.zone.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Objects;

public class SpringBootUtil implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(SpringBootUtil.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (Objects.isNull(SpringBootUtil.applicationContext)) {
            SpringBootUtil.applicationContext = applicationContext;
            log.info("\n\n" + "=========================================================\n"
                    + "ApplicationContext inject SpringBootUtil\n"
                    + "=========================================================\n\n");
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
}
