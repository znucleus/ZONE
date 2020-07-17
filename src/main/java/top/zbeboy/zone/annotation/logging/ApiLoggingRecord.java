package top.zbeboy.zone.annotation.logging;

import top.zbeboy.zbase.config.Workbook;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiLoggingRecord {

    /**
     * 备注
     *
     * @return 备注
     */
    String remark() default "";

    /**
     * 渠道
     *
     * @return 渠道
     */
    Workbook.channel channel() default Workbook.channel.API;

    /**
     * 是否需要登录
     *
     * @return 是否需要登录
     */
    boolean needLogin() default false;
}
