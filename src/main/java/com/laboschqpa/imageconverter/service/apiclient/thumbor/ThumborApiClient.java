package com.laboschqpa.imageconverter.service.apiclient.thumbor;

import com.laboschqpa.imageconverter.config.AppConfig;
import com.laboschqpa.imageconverter.service.apiclient.AbstractApiClient;
import com.laboschqpa.imageconverter.service.apiclient.ApiCallerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ThumborApiClient extends AbstractApiClient {
    @Value("${apiClient.thumbor.baseUrl}")
    private String apiBaseUrl;

    public ThumborApiClient(ApiCallerFactory apiCallerFactory,
                            @Qualifier(AppConfig.THUMBOR_WEB_CLIENT_QUALIFIER) WebClient webClient) {
        super(apiCallerFactory, false, webClient);
    }

    public Mono<byte[]> sendUnsafeRequest(List<String> options, String originalImageUrl) {
        StringBuilder path = new StringBuilder();
        path.append("/unsafe");

        for (String option : options) {
            path.append("/");
            path.append(option);
        }
        path.append("/");
        path.append(originalImageUrl);

        return getApiCaller().doCallAndThrowExceptionIfStatuscodeIsNot2xx(byte[].class,
                path.toString(),
                HttpMethod.GET
        );
    }

    @Override
    protected String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
