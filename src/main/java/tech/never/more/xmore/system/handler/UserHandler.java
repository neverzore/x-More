package tech.never.more.xmore.system.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.never.more.xmore.system.entity.User;
import tech.never.more.xmore.system.service.IUserService;

@Service
public class UserHandler {
    @Autowired
    private IUserService userService;

    public Mono<ServerResponse> getUser(ServerRequest request) {
        Mono<User> user = Mono.fromCallable(() -> userService.findByNamePassword(request.pathVariable("username"),
                request.queryParam("password").get())).publishOn(Schedulers.parallel());

        return Mono.empty();
    }
}
