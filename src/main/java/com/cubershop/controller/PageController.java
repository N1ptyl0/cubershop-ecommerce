package com.cubershop.controller;

import com.cubershop.entity.Cube;
import com.cubershop.entity.Type;
import com.cubershop.exception.CubeNotFoundException;
import com.cubershop.exception.HttpException;
import com.cubershop.service.CubeService;
import com.cubershop.service.TypeService;
import com.cubershop.utils.CubeSort;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Controller
public final class PageController {

    private final CubeService cubeService;
    private final TypeService typeService;

    @Autowired
    public PageController(CubeService cubeService, TypeService typeService) {
        this.cubeService = cubeService;
        this.typeService = typeService;
    }

    @GetMapping(path = {"/", "/home", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String homePage(Model model, HttpServletResponse response) throws Exception {
        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);
        List<Cube> cubes = (List<Cube>) this.cubeService.findAll();

        if (!cubes.isEmpty())
            throw new CubeNotFoundException("There are not cubes to retrieve");

        model.addAttribute("cubes", cubes.stream().collect(groupingBy(Cube::getType, toSet())));
        model.addAttribute("components", List.of("all"));

        return "home";
    }

    @GetMapping(path = "/catalog/{type:2x2x2|3x3x3|4x4x4|5x5x5|others|big}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String catalogPage(
        @PathVariable String type,
        @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model,
        HttpServletResponse response
    ) throws Exception {
        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);

        List<Cube> cubes = this.typeService.findByName(Type.Name.valueOf(type)).orElseThrow().getCubes();

        cubes = cubes.stream().sorted(CubeSort.getComparator(order)).collect(Collectors.toList());

        model.addAttribute("isSearchPage", false);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("type", type);
        model.addAttribute("cubeList", Lists.partition(cubes, 3));
        model.addAttribute("components", List.of("all"));

        return "catalog";
    }

    @GetMapping(value = "/description/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String descriptionPage(
        @PathVariable("id") UUID id, Model model, HttpServletResponse response
    ) throws CubeNotFoundException {
        Cube cube = this.cubeService.findById(id)
            .orElseThrow(() -> new CubeNotFoundException("Cube with id "+id+" not found!"));

        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);
        model.addAttribute("cube", cube);
        model.addAttribute("components", List.of("header", "nav", "news", "footer"));

        return "description";
    }

    @GetMapping(value = "/search", params = "exp", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String searchPage(
        @RequestParam(name = "exp") String expression,
        @RequestParam(name = "order", defaultValue = "alpha_asc") String order,
        Model model, HttpServletResponse response
    ) throws Exception {
        List<Cube> cubes = this.cubeService.findAllByName(expression);

        if (cubes.isEmpty()) throw new CubeNotFoundException("No cube matches to expression: "+expression);

        response.setContentType(MediaType.TEXT_HTML_VALUE);

        cubes = cubes.stream().sorted(CubeSort.getComparator(order)).collect(Collectors.toList());

        model.addAttribute("isSearchPage", true);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", "Results for "+expression);
        model.addAttribute("cubeList", Lists.partition(cubes, 3));
//        model.addAttribute("components", List.<String>of("all"));
        model.addAttribute("components", List.of("header", "nav", "news", "footer"));

        return "catalog";
    }

    @ExceptionHandler
    public String exceptionsResolver(Model model, HttpException exception, HttpServletResponse response) {
        int statusCode = exception.getStatusCode().value();
        String statusName = exception.getStatusName();
        String message = exception.getMessage();

        response.setStatus(statusCode);
        response.addHeader("Content-Type", MediaType.TEXT_HTML_VALUE);

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("statusName", statusName);
        model.addAttribute("message", message);

        return "exception/base";
    }
}
