package com.cubershop.controller;

import com.cubershop.database.template.CubeDAOTemplate;
import com.cubershop.entity.Cube;
import com.cubershop.exception.CubeNotFoundException;
import com.cubershop.exception.HomeCubesNotFoundException;
import com.cubershop.exception.ListOfCubesNotFoundException;
import com.cubershop.exception.NumberOfParametersExceededException;
import com.cubershop.utils.CubeOrderProcessor;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Controller
public final class PageController {

    private final CubeDAOTemplate cubeDAOTemplate;

    @Autowired
    public PageController(CubeDAOTemplate cubeDAOTemplate) {
        this.cubeDAOTemplate = cubeDAOTemplate;
    }

    @GetMapping(path = {"/", "/home", "/index"})
    @ResponseStatus(HttpStatus.OK)
    public String homePage(
        Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
        if(request.getParameterMap().size() > 0)
            throw new NumberOfParametersExceededException();

        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);

        List<Cube> cubes = this.cubeDAOTemplate
            .findHomeCubes()
            .orElseThrow(HomeCubesNotFoundException::new);

        model.addAttribute("cubes", cubes.stream().collect(groupingBy(Cube::getType, toSet())));
        model.addAttribute("components", List.of("all"));

        return "home";
    }

    @GetMapping(path = "/category/{category:2x2x2|3x3x3|4x4x4|5x5x5|others|big}")
    @ResponseStatus(HttpStatus.OK)
    public String categoryPage(@PathVariable String category,
    @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model,
    HttpServletResponse response) throws Exception {
        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);

        List<Cube> cubes = this.cubeDAOTemplate.findCubesByType(category)
            .orElseThrow(() -> new ListOfCubesNotFoundException(category, order));

        cubes = CubeOrderProcessor.process(cubes, order);

        model.addAttribute("isSearchPage", false);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", category);
        model.addAttribute("cubeList", Lists.partition(cubes, 3));
        model.addAttribute("components", List.of("all"));

        return "category";
    }

    @GetMapping(value = "/description/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String descriptionPage(
    @PathVariable("id") UUID id, Model model, HttpServletResponse response) throws CubeNotFoundException {
        Cube cube = this.cubeDAOTemplate.findCubeById(id)
            .orElseThrow(() -> new CubeNotFoundException(id));

        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);
        model.addAttribute("cube", cube);
        model.addAttribute("components", List.of("header", "nav", "news", "footer"));

        return "description";
    }

    @GetMapping(value = "/search", params = "exp")
    @ResponseStatus(HttpStatus.OK)
    public String searchPage(
    @RequestParam(name = "exp") String expression,
    @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model) throws Exception {
        List<Cube> cubes = this.cubeDAOTemplate.findCubesByName(expression)
            .orElseThrow(ListOfCubesNotFoundException::new);

        cubes = CubeOrderProcessor.process(cubes, order);

        model.addAttribute("isSearchPage", true);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", "Results for "+expression);
        model.addAttribute("cubeList", Lists.partition(cubes, 3));
//        model.addAttribute("components", List.<String>of("all"));
        model.addAttribute("components", List.of("header", "nav", "news", "footer"));

        return "category";
    }

    @ExceptionHandler
    public String exceptionsResolver(Model model, Exception exception, HttpServletResponse response) {
        int statusCode = HttpStatus.BAD_REQUEST.value();
        String statusName = HttpStatus.BAD_REQUEST.name();
        String message = exception.getMessage();

        response.setStatus(statusCode);
        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("statusName", statusName);
        model.addAttribute("message", message);

        return "exception/base";
    }
}
