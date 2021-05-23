package com.laboschqpa.imageconverter.service.variantjob;

import com.laboschqpa.imageconverter.model.ProcessCreationJobCommand;
import com.laboschqpa.imageconverter.service.apiclient.filehost.FileHostApiClient;
import com.laboschqpa.imageconverter.service.apiclient.thumbor.ThumborApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public Mono<Void> processCreationJob(ProcessCreationJobCommand command) {
        final long jobId = command.getJobId();

        return thumborApiClient
                .sendUnsafeRequest(getThumborOptions(command), composeImageUrlForThumbor(command.getOriginalFileId()))
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

    private List<String> getThumborOptions(ProcessCreationJobCommand command) {
        final String sizeOption = String.format("%dx%d", command.getVariantSize(), command.getVariantSize());
        return List.of("fit-in", sizeOption);
    }
}
