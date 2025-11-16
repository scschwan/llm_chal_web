package kr.co.dimillion.lcapp.application;

import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("/api")
public interface AiServerClient {
    @PostExchange("/admin/manual/sync-manual")
    Mono<Void> syncManuals();

    @PostExchange("/admin/defect-type/refresh-mapping")
    Mono<Void> refreshDefectTypeMapping();

    @PostExchange("/admin/image/sync-normal")
    Mono<Void> syncNormalImages();

    @PostExchange("/admin/image/sync-defect")
    Mono<Void> syncDefectImages();
}
