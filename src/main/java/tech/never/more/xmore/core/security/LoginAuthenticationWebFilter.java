package tech.never.more.xmore.core.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;

/**
 * Created by zhouzb on 2018/9/17.
 */
public class LoginAuthenticationWebFilter extends org.springframework.security.web.server.authentication.AuthenticationWebFilter {
    public LoginAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
}
