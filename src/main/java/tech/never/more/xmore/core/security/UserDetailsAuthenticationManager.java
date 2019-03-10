package tech.never.more.xmore.core.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class UserDetailsAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    @Autowired
    protected ReactiveUserDetailsService userDetailsService;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    public UserDetailsAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String username = authentication.getName();
        return this.userDetailsService.findByUsername(username).publishOn(Schedulers.parallel())
                .cast(XMoreUserDetails.class).filter((u) -> {
            return this.passwordEncoder.matches(StringUtils.join(((String)authentication.getCredentials()).toUpperCase(), u.getSalt()), u.getPassword());
        }).switchIfEmpty(Mono.defer(() -> {
            return Mono.error(new BadCredentialsException("Invalid Credentials"));
        })).map((u) -> {
            return new UsernamePasswordAuthenticationToken(u, u.getPassword(), u.getAuthorities());
        });
    }
}
