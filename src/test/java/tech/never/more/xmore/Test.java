package tech.never.more.xmore;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhouzb on 2018/9/12.
 */
public class Test {
    public static void encoder() {
        BCryptPasswordEncoder d = new BCryptPasswordEncoder();

        String pwd = DigestUtils.sha512Hex("1");
        System.out.println(pwd.toUpperCase());
        System.out.println(pwd.length());

        String rand = RandomStringUtils.random(8, true, true);
        System.out.println(rand);

        System.out.println(StringUtils.join(pwd.toUpperCase(), rand));

        String m = d.encode(StringUtils.join(pwd.toUpperCase(), rand));

        System.out.println(m);

        boolean t = d.matches(StringUtils.join(pwd.toUpperCase(), rand), m);
        System.out.println(t);
    }

    public static void wsClient() throws URISyntaxException {
        WebSocketClient client = new ReactorNettyWebSocketClient();

        URI url = new URI("wss://localhost:8443/ws");
//        client.execute(url, session ->
//                session.receive()
//                        .doOnNext(System.out::println)
//                        .then());
        client.execute(url, session -> session.send(Mono.just(new WebSocketMessage(WebSocketMessage.Type.TEXT, null))));
    }

    public static void context() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap( s -> Mono.subscriberContext()
                        .map( ctx -> s + " " + ctx.get(key)))
                .subscriberContext(ctx -> ctx.put(key, "World"));

        StepVerifier.create(r)
                .expectNext("Hello World")
                .verifyComplete();
    }

    public static void main(String[] args) {
        context();
    }
}
