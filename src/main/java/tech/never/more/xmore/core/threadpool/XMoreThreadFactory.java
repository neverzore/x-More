package tech.never.more.xmore.core.threadpool;

import reactor.core.scheduler.NonBlocking;
import reactor.util.annotation.Nullable;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

// @see ReactorThreadFactory
public class XMoreThreadFactory implements ThreadFactory, Thread.UncaughtExceptionHandler {
    private final String prefix;
    private final ThreadGroup group;
    private final AtomicInteger index = new AtomicInteger(1);
    private final boolean rejectBlocking;
    @Nullable
    private final BiConsumer<Thread, Throwable> uncaughtExceptionHandler;

    public XMoreThreadFactory(String prefix, boolean rejectBlocking, BiConsumer<Thread, Throwable> uncaughtExceptionHandler) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

        this.prefix = prefix;

        this.rejectBlocking = rejectBlocking;

        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = rejectBlocking ? new NonBlockingThread(group, r, this.prefix + index.getAndIncrement()) : new Thread(group, r,this.prefix + index.getAndIncrement());

        if (this.uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(this);
        }

        return thread;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

    }

    static final class NonBlockingThread extends Thread implements NonBlocking {
        public NonBlockingThread(ThreadGroup group, Runnable runnable, String name) {
            super(group, runnable, name);
        }
    }
}
