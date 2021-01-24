package com.cubershop.controller;

import com.cubershop.context.entity.Brand;
import com.cubershop.context.entity.ColorPattern;
import com.cubershop.context.entity.Cube;
import com.cubershop.database.dao.CubeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Controller
//@RequestMapping(path = "/form")
public class CubeController {

    @Autowired
    private CubeDAO cubeDAO;

    @GetMapping(path = "static/img/{uuid}", produces = {MediaType.IMAGE_JPEG_VALUE})
    @ResponseBody
    public byte[] getImage(@PathVariable(name = "uuid") UUID uuid) {
        return this.cubeDAO.findImageById(uuid);
    }

    @PostMapping(path = "/form/insertCube/submit", produces = MediaType.TEXT_HTML_VALUE)
    public String insertCube(@Validated @ModelAttribute Cube cube,
    BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()) {
            model.addAttribute("brands",
                Stream.<Brand>of(Brand.values())
                    .map(Enum::name).toArray(String[]::new));

            model.addAttribute("patterns",
                Stream.<ColorPattern>of(ColorPattern.values())
                    .map(Enum::name).toArray(String[]::new));
            return "insert-cube-form";
        }

        this.cubeDAO.insertCube(cube);

        return "success";
    }
}
