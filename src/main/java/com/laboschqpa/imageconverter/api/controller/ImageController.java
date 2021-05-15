package com.laboschqpa.imageconverter.api.controller;

import com.laboschqpa.imageconverter.config.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @GetMapping("/TODO")
    public String getDownloadOriginalWithParam(@RequestParam("id") Long fileId, HttpServletRequest httpServletRequest) {
        return "TODO";
    }
}
