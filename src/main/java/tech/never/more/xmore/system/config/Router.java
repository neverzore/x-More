package tech.never.more.xmore.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import tech.never.more.xmore.system.handler.IndexHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {
    @Autowired
    private IndexHandler indexHandler;

    @Bean
    public RouterFunction<ServerResponse> indexRoute() {
        return RouterFunctions.route(GET("/"), indexHandler::index)
                .andRoute(POST("/login").and(accept(MediaType.APPLICATION_JSON)), indexHandler::login)
                .andRoute(GET("/test"), indexHandler::test)
                .andRoute(PUT("/logout"), indexHandler::logout);
    }
}
