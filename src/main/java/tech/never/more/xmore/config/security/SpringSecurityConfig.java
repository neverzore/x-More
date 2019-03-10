package tech.never.more.xmore.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import tech.never.more.xmore.core.jwt.JwtAuthenticationManager;
import tech.never.more.xmore.core.jwt.JwtAuthenticationWebFilter;
import tech.never.more.xmore.core.security.UserDetailsAuthenticationManager;
import tech.never.more.xmore.core.security.UserDetailsService;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SpringSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationManager jwtAuthenticationManager() {
        return new JwtAuthenticationManager();
    }

    @Bean
    public UserDetailsAuthenticationManager authenticationManager(@Autowired UserDetailsService userDetailsService,
                                                                  @Autowired BCryptPasswordEncoder bCryptPasswordEncoder) {
        UserDetailsAuthenticationManager authenticationManager = new UserDetailsAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf().disable()
//                .formLogin()
//                .and().httpBasic().disable()
//                .authorizeExchange()
////                .pathMatchers("/login").permitAll()
//                .anyExchange().authenticated()
////        .addFilterAt(new JwtAuthenticationWebFilter(new JwtAuthenticationManager()), SecurityWebFiltersOrder.AUTHENTICATION);
//        ;

        JwtAuthenticationWebFilter jwtWebFilter = new JwtAuthenticationWebFilter(jwtAuthenticationManager());

        http
                .formLogin().disable()
                .csrf().disable()
                .httpBasic().disable()
                .logout().disable()
                .authorizeExchange().pathMatchers("/login", "/logout", "/ws").permitAll().and()
                .authorizeExchange().anyExchange().authenticated().and()
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        
        return http.build();
    }
}
