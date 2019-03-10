package tech.never.more.xmore.system.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.never.more.xmore.core.datasource.DataSource;
import tech.never.more.xmore.core.jwt.JwtTokenUtils;
import tech.never.more.xmore.core.logging.Log;
import tech.never.more.xmore.core.rest.Response;
import tech.never.more.xmore.system.config.XMoreConst;
import tech.never.more.xmore.system.service.IUserService;

import java.util.Map;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class IndexHandler {
    @Value("${x-more.index}")
    private Resource indexHtml;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReactiveRedisOperations<String, String> tokenRedisOperations;

    @Log
    @DataSource(value = "hello, reactive")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<ServerResponse> index(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml);
    }

    @PreAuthorize("hasRole('ROLE_A')")
    public Mono<ServerResponse> test(ServerRequest request) {
        return ok().contentType(MediaType.TEXT_HTML).syncBody(indexHtml);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .doOnNext(System.out::println)
                .map(param -> userService.findByNamePassword((String)param.getOrDefault("username", ""), (String)param.getOrDefault("password", "")))
                .onErrorResume(throwable -> Mono.error(new UsernameNotFoundException("用户名或密码不正确")))
                .map(user -> jwtTokenUtils.generateToken(user.getName(), user.getAuthority())).publishOn(Schedulers.parallel())
                .flatMap(token -> ServerResponse.ok().header(HttpHeaders.AUTHORIZATION, StringUtils.join(JwtTokenUtils.BEARER, token)).body(fromObject(Response.success())));
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        String token = request.headers().header(HttpHeaders.AUTHORIZATION).get(0);

        redisTemplate.opsForSet().add(XMoreConst.TOKEN_BLACKLIST, token.substring(JwtTokenUtils.BEARER.length()));

        return ServerResponse.ok().body(fromObject(Response.success()));
    }
}
