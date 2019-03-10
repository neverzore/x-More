package tech.never.more.xmore.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.support.DefaultServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import tech.never.more.xmore.core.rest.Response;

import java.util.Map;

@Component
public class XMoreGlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(XMoreGlobalExceptionHandler.class);

    private ServerCodecConfigurer serverCodecConfigurer;

    public XMoreGlobalExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, applicationContext);

        serverCodecConfigurer = new DefaultServerCodecConfigurer();

        this.setMessageWriters(serverCodecConfigurer.getWriters());
        this.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(
                RequestPredicates.all(), this::renderErrorResponse);
    }

    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (Integer)errorAttributes.get("status");
        return HttpStatus.valueOf(statusCode);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Throwable throwable = this.getError(request);
        Map<String, Object> errorAttributes = getErrorAttributes(request, false);

        Response response = Response.error();
        response.setMessage(String.valueOf(errorAttributes.get("message")));

        return ServerResponse.status(getHttpStatus(errorAttributes)).
                contentType(MediaType.APPLICATION_JSON_UTF8).
                body(BodyInserters.fromObject(response)).
                doOnNext(signalType -> logger.error(String.valueOf(errorAttributes.get("message")), throwable));
    }
}
