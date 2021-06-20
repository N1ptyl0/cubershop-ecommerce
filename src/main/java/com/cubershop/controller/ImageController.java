package com.cubershop.controller;

import com.cubershop.exception.ImageNotFoundException;
import com.cubershop.exception.NumberOfParametersExceededException;
import com.cubershop.service.ImageServiceConcrete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class ImageController {

    private final ImageServiceConcrete imageService;

    @Autowired
    public ImageController(ImageServiceConcrete imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "static/img/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(
        @PathVariable(name = "uuid") UUID uuid,
        @RequestParam Map<String, String> params) throws Exception {
        if(params.size() > 0) throw new NumberOfParametersExceededException();

        return this.imageService.findById(uuid)
            .orElseThrow(() -> new ImageNotFoundException(uuid)).getBody();
    }

    @ExceptionHandler
    public Exception exceptionHandler(final Exception exception) {
        return exception;
    }
}
