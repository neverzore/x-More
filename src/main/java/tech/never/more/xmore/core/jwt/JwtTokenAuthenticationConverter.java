package tech.never.more.xmore.core.jwt;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Created by zhouzb on 2018/9/12.
 */
public class JwtTokenAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
    @Override
    public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {

        return Mono.fromCallable(() -> serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaderNames.AUTHORIZATION.toString()))
                .filter((authorization) -> StringUtils.isNotBlank(authorization) && authorization.startsWith(JwtTokenUtils.BEARER))
                .switchIfEmpty(Mono.empty())
                .map((authorization) -> new JwtAuthenticationToken(authorization.substring(JwtTokenUtils.BEARER.length())));
    }
}
