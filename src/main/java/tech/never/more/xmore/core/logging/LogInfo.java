package tech.never.more.xmore.core.logging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.slf4j.Logger;

@Setter
@Getter
@AllArgsConstructor
public class LogInfo {
    @NonNull
    private Logger logger;
    @NonNull
    private String level;
    private String message;
    private Throwable throwable;
}
