package tech.never.more.xmore.core.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class UserDetailsAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        HttpHeaders headers = exchange.getResponse()
                .getHeaders();
        // TODO
//        try {
//            headers.add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication));
//        } catch (JOSEException e) {
//            e.printStackTrace();
//        }

        return Mono.empty();
    }
}
