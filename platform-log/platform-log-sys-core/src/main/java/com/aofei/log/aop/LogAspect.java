package com.aofei.log.aop;

import com.aofei.authorization.interceptor.AuthorizationInterceptor;
import com.aofei.log.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 日志切面处理类
 *
 * @author Hao
 * @create 2017-03-29
 */
public class LogAspect {
    /**
     * 日志切入点
     */
    private LogPoint logPoint;

    /**
     * 保存系统操作日志
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 调用出错
     */
    @Around(value = "@annotation(com.aofei.log.annotation.Log)")
    public Object save(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object object = request.getAttribute(AuthorizationInterceptor.REQUEST_CURRENT_KEY);

        /**
         * 解析Log注解
         */
        String methodName = joinPoint.getSignature().getName();
        Method method = currentMethod(joinPoint, methodName);
        Log log = method.getAnnotation(Log.class);

        String username = object!=null ? object.toString() : "";

        /**
         * 日志入库
         */
        if (log != null) {
            logPoint.save(username,joinPoint, methodName, log.module(), log.description());
        }

        /**
         * 方法执行
         */
        return joinPoint.proceed();
    }

    /**
     * 获取当前执行的方法
     *
     * @param joinPoint  连接点
     * @param methodName 方法名称
     * @return 方法
     */
    private Method currentMethod(ProceedingJoinPoint joinPoint, String methodName) {
        /**
         * 获取目标类的所有方法，找到当前要执行的方法
         */
        Method[] methods = joinPoint.getTarget().getClass().getMethods();
        Method resultMethod = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                resultMethod = method;
                break;
            }
        }


        return resultMethod;
    }

    public LogPoint getLogPoint() {
        return logPoint;
    }

    public void setLogPoint(LogPoint logPoint) {
        this.logPoint = logPoint;
    }
}