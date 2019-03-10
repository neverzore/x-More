package tech.never.more.xmore.core.logging;

import org.slf4j.Logger;

public class LogFactory {
    private LogFactory() {

    }

    public static LogInfo create(Logger logger, String level, String message) {
        return create(logger, level, message, null);
    }

    public static LogInfo create(Logger logger, String level, String message, Throwable throwable) {
        return new LogInfo(logger, level, message, throwable);
    }
}
