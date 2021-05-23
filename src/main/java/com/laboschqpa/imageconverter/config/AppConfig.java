package com.laboschqpa.imageconverter.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Configuration
public class AppConfig {
    public static final String THUMBOR_WEB_CLIENT_QUALIFIER = "thumborWebClient";

    @Value("${apiClient.thumbor.maxResponseBufferSizeBytes}")
    private Integer maxResponseBufferSizeBytes;

    @Bean
    @Qualifier(THUMBOR_WEB_CLIENT_QUALIFIER)
    public WebClient thumborWebClient() {
        return WebClient.builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs()
                            .maxInMemorySize(maxResponseBufferSizeBytes);
                })
                .build();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
