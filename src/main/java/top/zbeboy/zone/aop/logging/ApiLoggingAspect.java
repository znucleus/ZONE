package top.zbeboy.zone.aop.logging;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import top.zbeboy.zbase.config.Workbook;
import top.zbeboy.zbase.domain.tables.pojos.Channel;
import top.zbeboy.zbase.domain.tables.pojos.SystemApiLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.data.ChannelService;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zone.annotation.logging.ApiLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

@Aspect
public class ApiLoggingAspect {

    @Resource
    private SystemLogService systemLogService;

    @Resource
    private ChannelService channelService;

    /**
     * 日志记录切面
     */
    @Pointcut("@annotation(top.zbeboy.zone.annotation.logging.ApiLoggingRecord)")
    public void apiLoggingRecordPointcut() {
    }

    /**
     * 日志记录
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("apiLoggingRecordPointcut()")
    public Object doRecord(ProceedingJoinPoint point) throws Throwable {
        String targetName = point.getTarget().getClass().getName();
        String methodName = point.getSignature().getName();
        Object[] arguments = point.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    String uri = "";
                    String ip = "";
                    String username = "";
                    boolean needLogin = method.getAnnotation(ApiLoggingRecord.class).needLogin();
                    for (Object o : arguments) {
                        if (o instanceof HttpServletRequest) {
                            HttpServletRequest request = (HttpServletRequest) o;
                            ip = RequestUtil.getIpAddress(request);
                            uri = StringUtils.trim(request.getRequestURI());
                        } else if (o instanceof Principal) {
                            if (needLogin) {
                                Users users = SessionUtil.getUserFromOauth((Principal) o);
                                if (Objects.nonNull(users)) {
                                    username = users.getUsername();
                                }
                            }
                        }
                    }
                    Workbook.channel channel = method.getAnnotation(ApiLoggingRecord.class).channel();

                    if (needLogin) {
                        if (StringUtils.equals(channel.name(), Workbook.channel.WEB.name())) {
                            Users users = SessionUtil.getUserFromSession();
                            if (Objects.nonNull(users)) {
                                username = users.getUsername();
                            }
                        }
                    }

                    Optional<Channel> optionalChannel = channelService.findByChannelName(channel.name());
                    if(optionalChannel.isPresent()){
                        SystemApiLog systemLog =
                                new SystemApiLog(UUIDUtil.getUUID(),
                                        uri,
                                        method.getAnnotation(ApiLoggingRecord.class).remark(),
                                        optionalChannel.get().getChannelId(),
                                        DateTimeUtil.getNowLocalDateTime(), username, ip);
                        systemLogService.save(systemLog);
                    }
                    break;
                }
            }
        }
        return point.proceed();
    }
}
