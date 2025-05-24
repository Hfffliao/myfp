package love.linyi.config;

import love.linyi.exception.SystemException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Component
@Aspect
public class Aop {
    @Pointcut("execution(void love.linyi.service.*.*(..)) || execution(* love.linyi.dao.*.*(..))")
    public void pt(){}

    @Around("pt()")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {

            result = joinPoint.proceed();
        } catch (Throwable e ) {
            // 处理异常
            System.err.println("方法执行出错: " + e.getMessage());
            throw new SystemException(5000,"系统错误",e);
            // 可以根据需要进行异常处理，如日志记录、重试等
        }
        return result;
    }
    @Pointcut("execution(void love.linyi.controller.*.*(..))")
    public void pt1(){}

    @Around("pt1()")
    public Object aroundMethod1(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            // 记录开始时间
            // 执行目标方法
            result = joinPoint.proceed();
            // 记录结束时间
        } catch (Throwable e ) {
            // 处理异常
            System.err.println("方法执行出错: " + e.getMessage());
            throw new SystemException(6000,"business错误",e);
            // 可以根据需要进行异常处理，如日志记录、重试等
        }
        return result;
    }
}