package top.zbeboy.zone.aop.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.zbeboy.zone.annotation.logging.LoggingRecord;
import top.zbeboy.zone.domain.tables.pojos.SystemOperatorLog;
import top.zbeboy.zone.domain.tables.pojos.Users;
import top.zbeboy.zone.service.system.SystemLogService;
import top.zbeboy.zone.service.util.DateTimeUtil;
import top.zbeboy.zone.service.util.RequestUtil;
import top.zbeboy.zone.service.util.UUIDUtil;
import top.zbeboy.zone.web.util.SessionUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
public class LoggingRecordAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingRecordAspect.class);

    @Resource
    private SystemLogService systemLogService;

    /**
     * 日志记录切面
     */
    @Pointcut("@annotation(top.zbeboy.zone.annotation.logging.LoggingRecord)")
    public void loggingRecordPointcut() {
    }

    /**
     * 日志记录
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("loggingRecordPointcut()")
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
                            SystemOperatorLog systemLog = new SystemOperatorLog(UUIDUtil.getUUID(), String.valueOf(method.getAnnotation(LoggingRecord.class).description()), DateTimeUtil.getNowSqlTimestamp(), users.getUsername(), RequestUtil.getIpAddress(request));
                            systemLogService.save(systemLog);
                            log.info(" Record operator logging to database , the module is {} , the method is {} ", method.getAnnotation(LoggingRecord.class).module(), method.getAnnotation(LoggingRecord.class).methods());
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
