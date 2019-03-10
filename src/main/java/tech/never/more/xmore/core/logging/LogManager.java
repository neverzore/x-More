package tech.never.more.xmore.core.logging;

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import tech.never.more.xmore.core.threadpool.XMoreThreadFactory;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class LogManager {

    private static class LazyHolder {
        static final LogManager mgr = new LogManager();
    }

    private static final BiConsumer<Thread, Throwable> uncaughtExceptionHandler = (thread, throwable) -> {

    };

    private final Scheduler scheduler;

    private LogManager() {
        ThreadFactory threadFactory = new XMoreThreadFactory("logger-", true, uncaughtExceptionHandler);

        scheduler = Schedulers.fromExecutorService(new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                threadFactory, new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    public static LogManager me() {
        return LazyHolder.mgr;
    }

    public void log(@NonNull LogInfo log) {
        Mono.fromRunnable(() -> {
            Logger logger = log.getLogger();
            String level = log.getLevel();

            if (Level.ERROR.toString().equals(level)) {
                if (logger.isErrorEnabled()) {
                    logger.error(log.getMessage(), log.getThrowable());
                }
            } else if (Level.WARN.toString().equals(level)) {
                if (logger.isWarnEnabled()) {
                    logger.warn(log.getMessage(), log.getThrowable());
                }
            } else if (Level.INFO.toString().equals(level)) {
                if (logger.isInfoEnabled()) {
                    logger.info(log.getMessage());
                }
            } else if (Level.DEBUG.toString().equals(level)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(log.getMessage());
                }
            } else if (Level.TRACE.toString().equals(level)) {
                if (logger.isTraceEnabled()) {
                    logger.trace(log.getMessage());
                }
            }
        }).subscribeOn(scheduler).subscribe();
    }


}
