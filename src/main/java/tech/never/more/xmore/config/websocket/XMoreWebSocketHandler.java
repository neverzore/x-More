package tech.never.more.xmore.config.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class XMoreWebSocketHandler implements WebSocketHandler {
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        System.out.println(session.getId());
        Flux<WebSocketMessage> output = session.receive()
                .doOnNext(message -> {
                    System.out.println(message.getType());
                    System.out.println(message.getPayloadAsText());
                }).doOnComplete(() -> session.pingMessage(factory -> factory.wrap("Ping".getBytes())))
//                .concatMap(message -> {
//                    // ...
//                })
                .map(value -> session.textMessage("Echo " + value));

        return session.send(output);
    }
}
