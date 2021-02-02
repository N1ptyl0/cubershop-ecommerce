package com.cubershop.controller;

import com.cubershop.context.entity.Cube;
import com.cubershop.database.base.CubeDAOBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CubeController {

    @Autowired
    private CubeDAOBase cubeDAO;

    @GetMapping(path = "/form/cube", produces = MediaType.TEXT_HTML_VALUE)
    public String cubeFormPage(@ModelAttribute Cube cube, BindingResult bindingResult, Model model) {
        model.addAttribute("categories",
            new String[]{"2x2x2", "3x3x3", "4x4x4", "5x5x5", "big"});

        return "cube-form";
    }

    @PostMapping(path = "/form/cube/save", produces = MediaType.TEXT_HTML_VALUE)
    public String saveCube(@Validated @ModelAttribute Cube cube,
    BindingResult bindingResult, Model model) {
        if(bindingResult.hasFieldErrors()) {
            model.addAttribute("categories",
                new String[]{"2x2x2", "3x3x3", "4x4x4", "5x5x5", "big"});
            return "cube-form";
        }

        model.addAttribute("uuid", this.cubeDAO.saveCube(cube));

        return "success";
    }

    @GetMapping(path = "/api/cube/names",
        produces = MediaType.APPLICATION_JSON_VALUE,
        params = "exp"
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public List<String> searchNamesList(
        @RequestParam(name = "exp", defaultValue = "") String exp) {
        return this.cubeDAO.findNamesByExpression(exp);
    }
}
