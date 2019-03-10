package tech.never.more.xmore.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import tech.never.more.xmore.system.service.IUserService;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsService implements ReactiveUserDetailsService {
    @Autowired
    private IUserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String s) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ADMIN");
        authorities.add(simpleGrantedAuthority);

        Mono<UserDetails> result = Mono.fromCallable(() -> userService.findByName(s)).publishOn(Schedulers.immediate()).
                switchIfEmpty(Mono.empty()).
                flatMap(user -> Mono.just(new XMoreUserDetails(s, user.getPassword(), user.getSalt(), authorities)));

        return result;
    }
}
