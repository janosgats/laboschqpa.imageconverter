package com.laboschqpa.imageconverter.service;

import com.laboschqpa.imageconverter.api.dto.imagevariant.ProcessCreationJobRequest;
import com.laboschqpa.imageconverter.service.apiclient.filehost.FileHostApiClient;
import com.laboschqpa.imageconverter.service.apiclient.thumbor.ThumborApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ImageVariantCreationJobProcessor {
    private final ThumborApiClient thumborApiClient;
    private final FileHostApiClient fileHostApiClient;

    @Value("${apiClient.fileHost.baseUrl}")
    private String fileHostBaseUrl;
    @Value("${apiClient.fileHost.internalNoAuth.file.downloadOriginalUrl}")
    private String downloadOriginalImagePath;

    public Mono<Void> processCreationJobAsync(@RequestBody ProcessCreationJobRequest request) {
        final long jobId = request.getJobId();

        return thumborApiClient
                .sendUnsafeRequest(getThumborOptions(request), composeImageUrlForThumbor(request.getOriginalFileId()))
                .doOnSuccess(bytes -> {
                    log.trace("Received image from Thumbor. jobId: {}", jobId);
                })
                .flatMap(imageBytes -> fileHostApiClient.uploadImageVariant(jobId, imageBytes))
                .doOnSuccess(bytes -> {
                    log.trace("Uploaded image variant to FileHost. jobId: {}", jobId);
                });
    }

    private String composeImageUrlForThumbor(long fileId) {
        return fileHostBaseUrl + downloadOriginalImagePath + "/" + fileId;
    }

    private List<String> getThumborOptions(ProcessCreationJobRequest request) {
        final String sizeOption = String.format("%dx%d", request.getVariantSize(), request.getVariantSize());
        return List.of("fit-in", sizeOption);
    }
}
