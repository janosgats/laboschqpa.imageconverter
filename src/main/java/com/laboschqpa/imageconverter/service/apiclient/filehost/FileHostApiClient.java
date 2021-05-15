package com.laboschqpa.imageconverter.service.apiclient.filehost;

import com.laboschqpa.imageconverter.service.apiclient.AbstractApiClient;
import com.laboschqpa.imageconverter.service.apiclient.ApiCallerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Map;
import java.util.Set;

@Service
public class FileHostApiClient extends AbstractApiClient {
    @Value("${apiClient.fileHost.baseUrl}")
    private String apiBaseUrl;

    public FileHostApiClient(ApiCallerFactory apiCallerFactory) {
        super(apiCallerFactory, true);
    }

//    public GetIndexedFileInfoResponse[] getIndexedFileInfo(Set<Long> indexedFileIds) {
//        return getApiCaller().doCallAndThrowExceptionIfStatuscodeIsNot2xx(GetIndexedFileInfoResponse[].class,
//                indexedFileInfoUrl,
//                HttpMethod.GET,
//                BodyInserters.fromValue(indexedFileIds));
//    }
//
//    public void deleteFile(long indexedFileId) {
//        getApiCaller().doCallAndThrowExceptionIfStatuscodeIsNot2xx(String.class,
//                deleteFileUrl,
//                HttpMethod.DELETE,
//                Map.of("id", String.valueOf(indexedFileId)));
//    }

    @Override
    protected String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
