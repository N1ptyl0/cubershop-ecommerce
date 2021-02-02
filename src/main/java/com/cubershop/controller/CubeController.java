package com.cubershop.controller;

import com.cubershop.context.entity.Cube;
import com.cubershop.database.dao.CubeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
public class CubeController {

    @Autowired
    private CubeDAO cubeDAO;

    @PostMapping(path = "/form/cube/save", produces = MediaType.TEXT_HTML_VALUE)
    public String insertCube(@Validated @ModelAttribute Cube cube,
    BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()) {
            model.addAttribute("categories",
                new String[]{"2x2x2", "3x3x3", "4x4x4", "5x5x5", "big"});
            return "cube-form";
        }

        model.addAttribute("uuid", this.cubeDAO.saveCube(cube));

        return "success";
    }
}
