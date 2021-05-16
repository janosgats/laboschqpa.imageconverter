package com.laboschqpa.imageconverter.service.apiclient;

import com.laboschqpa.imageconverter.service.authinterservice.AuthInterServiceCrypto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ApiCallerFactory {
    private final WebClient webClient;
    private final AuthInterServiceCrypto authInterServiceCrypto;

    public ApiCaller createGeneral(String apiBaseUrl) {
        return createGeneral(apiBaseUrl, webClient);
    }

    public ApiCaller createGeneral(String apiBaseUrl, WebClient customWebclient) {
        return new ApiCaller(apiBaseUrl, customWebclient, new String[0]);
    }

    public ApiCaller createForAuthInterService(String apiBaseUrl) {
        return createForAuthInterService(apiBaseUrl, webClient);
    }

    public ApiCaller createForAuthInterService(String apiBaseUrl, WebClient customWebclient) {
        return new ApiCaller(apiBaseUrl, customWebclient, new String[0], authInterServiceCrypto);
    }
}
