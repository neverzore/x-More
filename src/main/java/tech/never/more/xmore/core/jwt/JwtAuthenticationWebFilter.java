package tech.never.more.xmore.core.jwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import reactor.core.publisher.Mono;

/**
 * Created by zhouzb on 2018/9/12.
 */
public class JwtAuthenticationWebFilter extends AuthenticationWebFilter {
    public JwtAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);

        this.setAuthenticationConverter(new JwtTokenAuthenticationConverter());

        this.setAuthenticationSuccessHandler(new JwtAuthenticationSuccessHandler());
        this.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
    }
}
