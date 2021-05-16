package com.laboschqpa.imageconverter.service.apiclient;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractApiClient {
    private final ApiCallerFactory apiCallerFactory;
    private final boolean useAuthInterService;
    private final WebClient customWebClient;
    private final boolean useCustomWebClient;

    private ApiCaller apiCaller;

    public AbstractApiClient(ApiCallerFactory apiCallerFactory, boolean useAuthInterService) {
        this(apiCallerFactory, useAuthInterService, null);
    }

    public AbstractApiClient(ApiCallerFactory apiCallerFactory, boolean useAuthInterService, WebClient customWebClient) {
        useCustomWebClient = customWebClient != null;
        this.apiCallerFactory = apiCallerFactory;
        this.useAuthInterService = useAuthInterService;
        this.customWebClient = customWebClient;
    }

    /**
     * Use this method to instantiate the {@link ApiCaller} so the @Value private fields can be set before they are required at the ApiCaller instantiation.
     */
    protected ApiCaller getApiCaller() {
        if (apiCaller == null) {
            apiCaller = instantiateApiCaller();
        }
        return apiCaller;
    }

    private ApiCaller instantiateApiCaller() {
        if (useAuthInterService) {
            if (useCustomWebClient) {
                return apiCallerFactory.createForAuthInterService(getApiBaseUrl(), customWebClient);
            }
            return apiCallerFactory.createForAuthInterService(getApiBaseUrl());
        }

        if (useCustomWebClient) {
            return apiCallerFactory.createGeneral(getApiBaseUrl(), customWebClient);
        }
        return apiCallerFactory.createGeneral(getApiBaseUrl());

    }

    protected abstract String getApiBaseUrl();
}
