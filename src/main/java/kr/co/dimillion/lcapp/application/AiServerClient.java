package kr.co.dimillion.lcapp.application;

import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("/api")
public interface AiServerClient {
    @PostExchange("/admin/manual/sync-manual")
    Mono<Void> syncManuals();
}
