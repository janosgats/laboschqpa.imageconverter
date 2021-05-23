package com.laboschqpa.imageconverter.api.controller;

import com.laboschqpa.imageconverter.api.dto.imagevariant.ProcessCreationJobRequest;
import com.laboschqpa.imageconverter.service.variantjob.ImageVariantCreationJobScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/imageVariant")
public class ImageVariantController {
    private final ImageVariantCreationJobScheduler imageVariantCreationJobScheduler;

    @PostMapping("/processCreationJob")
    public void postProcessCreationJob(@RequestBody ProcessCreationJobRequest request) {
        request.validateSelf();
        imageVariantCreationJobScheduler.scheduleJob(request);
    }
}
