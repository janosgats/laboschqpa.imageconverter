package com.laboschqpa.imageconverter.service.apiclient.filehost;

import com.laboschqpa.imageconverter.service.apiclient.AbstractApiClient;
import com.laboschqpa.imageconverter.service.apiclient.ApiCallerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class FileHostApiClient extends AbstractApiClient {
    @Value("${apiClient.fileHost.baseUrl}")
    private String apiBaseUrl;
    @Value("${apiClient.fileHost.internal.imageVariant.uploadVariantUrl}")
    private String uploadImageVariantUrl;

    public FileHostApiClient(ApiCallerFactory apiCallerFactory) {
        super(apiCallerFactory, true);
    }

    public Mono<Void> uploadImageVariant(long jobId, byte[] imageData) {
        final String FORM_FIELD_NAME_APPROXIMATE_FILE_SIZE = "approximateFileSize";

        final MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part(FORM_FIELD_NAME_APPROXIMATE_FILE_SIZE, imageData.length);
        bodyBuilder.part("fileToUpload", new ByteArrayResource(imageData)).filename("imageVariant_" + jobId);

        return getApiCaller().doCallAndThrowExceptionIfStatuscodeIsNot2xx(String.class,
                uploadImageVariantUrl,
                HttpMethod.POST,
                Map.of("jobId", String.valueOf(jobId)),
                BodyInserters.fromMultipartData(bodyBuilder.build())
        ).flatMap((responseBody) -> Mono.empty());
    }

    @Override
    protected String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
