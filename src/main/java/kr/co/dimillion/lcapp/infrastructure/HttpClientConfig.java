package kr.co.dimillion.lcapp.infrastructure;

import kr.co.dimillion.lcapp.application.AiServerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration(proxyBeanMethods = false)
public class HttpClientConfig {
    @Value("${ai-server-host}")
    private String aiServerHost;

    @Bean
    public AiServerClient aiServerClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().enableLoggingRequestDetails(true);
                })
                .build();

        WebClient webClient = WebClient.builder()
                .baseUrl(aiServerHost)
                .exchangeStrategies(exchangeStrategies)
                .build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(AiServerClient.class);
    }
}
