package tech.never.more.xmore.core.jwt;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by zhouzb on 2018/9/18.
 */
public class JwtAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        exchange.getResponse().getHeaders().add(HttpHeaderNames.AUTHORIZATION.toString(), StringUtils.join(JwtTokenUtils.BEARER, authentication.getCredentials()));
        return webFilterExchange.getChain().filter(exchange);
    }
}
