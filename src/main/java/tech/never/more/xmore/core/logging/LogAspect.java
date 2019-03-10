package tech.never.more.xmore.core.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@Aspect
@Component
@Order(value = 300)
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private static final Consumer<Throwable> doOnError = throwable -> {
        logger.error(throwable.getMessage(), throwable);
    };

    @Pointcut("@annotation(tech.never.more.xmore.core.logging.Log)")
    public void execute() {

    }

    @Around(value = "execute() && @annotation(log)")
    public Object log(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        long begin = System.currentTimeMillis();

        Object retVal = joinPoint.proceed();

        retVal = logInternal(joinPoint, log, begin, retVal);

        return retVal;
    }

    private Object logInternal(ProceedingJoinPoint joinPoint, Log log, Long begin, Object retVal) {
        if (retVal instanceof Mono) {
            retVal = ((Mono)retVal).doOnSuccess((p) -> {
//                logger.info("cost" + (System.currentTimeMillis() - begin));
                LogInfo logInfo = LogFactory.create(logger, Level.INFO.toString(), "cost" + (System.currentTimeMillis() - begin));
                LogManager.me().log(logInfo);
            }).doOnError(doOnError);
        } else if (retVal instanceof Flux) {
            ((Flux)retVal).doOnComplete(() -> {
                logger.info("cost" + (System.currentTimeMillis() - begin));
            }).doOnError(doOnError);
        } else {
            logger.info("cost" + (System.currentTimeMillis() - begin));
        }

        return retVal;

    }
}
