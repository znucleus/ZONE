package top.zbeboy.zone.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zbeboy.zbase.domain.tables.pojos.SystemLoginLog;
import top.zbeboy.zbase.domain.tables.pojos.Users;
import top.zbeboy.zbase.feign.system.SystemLogService;
import top.zbeboy.zbase.tools.service.util.DateTimeUtil;
import top.zbeboy.zbase.tools.service.util.RequestUtil;
import top.zbeboy.zbase.tools.service.util.UUIDUtil;
import top.zbeboy.zone.annotation.logging.LoginLoggingRecord;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
public class LoginLoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoginLoggingAspect.class);

    @Resource
    private SystemLogService systemLogService;

    /**
     * 日志记录切面
     */
    @Pointcut("@annotation(top.zbeboy.zone.annotation.logging.LoginLoggingRecord)")
    public void loginLoggingRecordPointcut() {
    }

    /**
     * 日志记录
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("loginLoggingRecordPointcut()")
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
                    for (Object o : arguments) {
                        if (o instanceof HttpServletRequest) {
                            HttpServletRequest request = (HttpServletRequest) o;
                            Users users = SessionUtil.getUserFromSession();
                            SystemLoginLog systemLog = new SystemLoginLog(UUIDUtil.getUUID(), method.getAnnotation(LoginLoggingRecord.class).description(), DateTimeUtil.getNowLocalDateTime(), users.getUsername(), RequestUtil.getIpAddress(request));
                            systemLogService.save(systemLog);
                            log.info(" Record operator logging to database , the module is {} , the method is {} ", method.getAnnotation(LoginLoggingRecord.class).module(), method.getAnnotation(LoginLoggingRecord.class).methods());
                            break;
                        }
                    }
                    break;
                }
            }
        }
        return point.proceed();
    }
}
