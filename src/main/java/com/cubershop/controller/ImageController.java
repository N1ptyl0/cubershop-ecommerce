package com.cubershop.controller;

import com.cubershop.database.template.CubeDAOTemplate;
import com.cubershop.exception.ImageNotFoundException;
import com.cubershop.exception.NumberOfParametersExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class ImageController {

    @Autowired
    private CubeDAOTemplate cubeDAO;

    @GetMapping(path = "static/img/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(
        @PathVariable(name = "uuid") UUID uuid,
        @RequestParam Map<String, String> params) throws Exception {
        if(params.size() > 0) throw new NumberOfParametersExceededException();

        return this.cubeDAO.findImageById(uuid)
            .orElseThrow(() -> new ImageNotFoundException(uuid));
    }

    @ExceptionHandler
    public Exception exceptionHandler(final Exception exception) {
        return exception;
    }
}
