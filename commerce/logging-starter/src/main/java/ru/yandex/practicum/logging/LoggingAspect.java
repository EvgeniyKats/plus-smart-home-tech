package ru.yandex.practicum.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(ru.yandex.practicum.logging.Logging)")
    public void loggingPointCut() {
    }

    @Around("loggingPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Уровень логирования
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logging logging = methodSignature.getMethod().getAnnotation(Logging.class);
        final Level loggingLevel = logging.level();

        // Логирование перед методом
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        String msgStart = createMessage(signature, true, args);
        writeToLog(msgStart, loggingLevel);

        // Логирование после метода
        Object result = joinPoint.proceed();
        String msgEnd = createMessage(signature, false, result);
        writeToLog(msgEnd, loggingLevel);

        return result;
    }

    @AfterThrowing(pointcut = "loggingPointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String typeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        log.error("{} exception {}()", typeName, methodName, exception);
    }

    private String createMessage(Signature signature, boolean start, Object... args) {
        String position = start ? ">>" : "<<";
        return String.format("%s: %s %s", signature, position, Arrays.toString(args));
    }

    private void writeToLog(String msg, final Level level) {
        switch (level) {
            case TRACE -> log.trace(msg);
            case DEBUG -> log.debug(msg);
            case INFO -> log.info(msg);
            case WARN -> log.warn(msg);
            case ERROR -> log.error(msg);
        }
    }
}
