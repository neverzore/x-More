package tech.never.more.xmore.core.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.never.more.xmore.system.config.XMoreConst;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by zhouzb on 2018/9/12.
 */
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private ReactiveRedisOperations<String, String> tokenRedisOperations;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final Throwable error = new BadCredentialsException("Invalid Credentials");

    private final Predicate<Claims> blacklistVerification = (t) -> {
        Boolean isMember = redisTemplate.opsForSet().isMember(XMoreConst.TOKEN_BLACKLIST, t.get(JwtTokenUtils.TOKEN));
        return !isMember;
    };

    private final Function<JwtAuthenticationToken, Claims> parseToken2Claims = (t) -> {
        Claims claims = jwtTokenUtils.parseToken((String) t.getCredentials());
        return claims;
    };

    private final Function<Claims, JwtAuthenticationToken> reconstruction = (claims) -> {
        List<GrantedAuthority> authorities = jwtTokenUtils.convert2GrantedAuthority(claims.get(JwtTokenUtils.AUTHORITY));
        return new JwtAuthenticationToken(claims.getAudience(), (String) claims.get(JwtTokenUtils.TOKEN), authorities);
    };

    private final Function<ExpiredJwtException, Mono<JwtAuthenticationToken>> regenerate = (e) -> {
        Claims claims = e.getClaims();
        Boolean keep = claims.get(JwtTokenUtils.KEEP, Boolean.class);

        if (keep == null || !keep) {
            return Mono.empty();
        }

        String token = jwtTokenUtils.generateToken(claims.getAudience(), (List<String>) claims.get(JwtTokenUtils.AUTHORITY), true);
        List<GrantedAuthority> authorities = jwtTokenUtils.convert2GrantedAuthority(claims.get(JwtTokenUtils.AUTHORITY));
        return Mono.just(new JwtAuthenticationToken(claims.getAudience(), token, authorities));
    };

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .cast(JwtAuthenticationToken.class)
                .flatMap(this::convertToken);
    }


    private <T> Mono<T> raiseBadCredentials() {
        return Mono.error(error);
    }

    private Mono<JwtAuthenticationToken> convertToken(JwtAuthenticationToken token) {
        return Mono.fromCallable(() -> jwtTokenUtils.parseToken((String) token.getCredentials()))
                .map(claims -> {
                    claims.put(JwtTokenUtils.TOKEN, token.getCredentials());
                    return claims;
                })
                .filter(blacklistVerification).publishOn(Schedulers.elastic())
                .switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .map(reconstruction)
                .onErrorResume(ExpiredJwtException.class, regenerate).switchIfEmpty(Mono.defer(this::raiseBadCredentials))
                .onErrorResume(throwable -> Mono.error(error))
                ;
    }
}
