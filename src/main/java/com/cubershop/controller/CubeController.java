package com.cubershop.controller;

import com.cubershop.entity.Cube;
import com.cubershop.exception.CubeNotFoundException;
import com.cubershop.exception.EmptyParameterValueException;
import com.cubershop.exception.NumberOfParametersExceededException;
import com.cubershop.service.CubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CubeController {
    
    private final CubeService cubeService;

    @Autowired
    public CubeController(CubeService cubeService) {
        this.cubeService = cubeService;
    }

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

        model.addAttribute("uuid", this.cubeService.save(cube));

        return "success";
    }

    @GetMapping(path = "/search/cube/names",
        produces = MediaType.APPLICATION_JSON_VALUE,
        params = "exp"
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin
    public List<String> searchNames(
        @RequestParam(name = "exp", defaultValue = "") String expression,
        @RequestParam Map<String, String> params) throws Exception {
        if (expression.isEmpty()) throw new EmptyParameterValueException("exp");
        if (params.size() > 1) throw new NumberOfParametersExceededException();

        List<Cube> foundCubes = this.cubeService.findAllByName(expression);

        if (foundCubes.isEmpty()) throw new CubeNotFoundException(expression);

        return this.cubeService.findAllByName(expression)
            .stream().map(Cube::getName).collect(Collectors.toList());
    }
}
