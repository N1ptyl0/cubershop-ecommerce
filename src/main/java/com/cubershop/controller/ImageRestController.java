package com.cubershop.controller;

import com.cubershop.database.base.CubeDAOBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class ImageRestController {

    @Autowired
    private CubeDAOBase cubeDAO;

    @GetMapping(path = "static/img/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(@PathVariable(name = "uuid") UUID uuid) {
        return this.cubeDAO.findImageById(uuid);
    }
}
