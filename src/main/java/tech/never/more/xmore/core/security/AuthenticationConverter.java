package tech.never.more.xmore.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Created by zhouzb on 2018/9/17.
 */
public class AuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
        // TODO
        return null;
    }
}
