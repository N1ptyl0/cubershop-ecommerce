package com.cubershop.controller;

import com.cubershop.context.entity.Brand;
import com.cubershop.context.entity.ColorPattern;
import com.cubershop.database.dao.CubeDAO;
import com.cubershop.context.utils.ListProcessor;
import com.cubershop.context.entity.Cube;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public final class PageController {

    @Autowired
    private CubeDAO cubeDAO;

    @GetMapping(path = {"/", "/home", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    public String homePage(@RequestParam Map<String, String> params, Model model) {
        if(params.size() > 0) return "error/400";

        List<Cube> cubes = this.cubeDAO.findHomeCubes();

        model.addAttribute("cubes",
            new HashMap<String, Cube[]>() {
                {
                    String[] types = {"2x2x2", "3x3x3", "4x4x4", "5x5x5"};

                    for(final String t : types) {
                        put(t, cubes.stream()
                            .filter(e -> e.getType().equals(t))
                            .toArray(Cube[]::new));
                    }
                }
            }
        );

        return "home";
    }

    @GetMapping(path = "/catalog/{type:2x2x2|3x3x3|4x4x4|5x5x5|others|big}",
    params = {"order", "sort"})
    public String catalogPage(@PathVariable String type,
    @RequestParam(name = "order") String order,
    @RequestParam(name = "sort") String sort,
    @CookieValue(value = "wishlistProducts", defaultValue = "0") String wishlistCookie, Model model) {
        if(!order.equals("alpha") && !order.equals("price")) return "error/400";
        if(!sort.equals("asc") && !sort.equals("desc")) return "error/400";

        String selectedOrder = "";

        if(order.equals("alpha")) selectedOrder = "alphaAsc";
        else {
            if(sort.equals("asc")) selectedOrder = "priceAsc";
            else selectedOrder = "priceDesc";
        }
        model.addAttribute("selectedOrder", selectedOrder);
        model.addAttribute("productType", type);
        model.addAttribute("cubeList",
            ListProcessor.<Cube>groupBy(this.cubeDAO.findCubesByTypeAndOrder(
            type, order.equals("alpha") ? "name" : order, sort), 3)
        );

        List<String> productCodes = Stream.<String>of(wishlistCookie.split("\\."))
            .collect(Collectors.toList());
        model.addAttribute("productSelectedList", productCodes);

        return "catalog";
    }

    @GetMapping(value = "/description/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String descriptionPage(
    @PathVariable("id") UUID id, Model model,
    @CookieValue(value = "wishlistProducts", defaultValue = "0") String wishlistCookie) {
        Cube cube = this.cubeDAO.findCubeById(id);

        model.addAttribute("cube", cube);

        boolean productSelected = Stream.<String>of(wishlistCookie.split("\\."))
            .anyMatch(i -> i.equals(cube.getId()));
        model.addAttribute("productSelected", productSelected);

        return "description";
    }

    @GetMapping(path = "/form/insertCube", produces = MediaType.TEXT_HTML_VALUE)
    public String getInsertForm(@ModelAttribute Cube cube, BindingResult bindingResult, Model model) {
        model.addAttribute("brands",
            Stream.<Brand>of(Brand.values())
                .map(Enum::name).toArray(String[]::new));

        model.addAttribute("patterns",
            Stream.<ColorPattern>of(ColorPattern.values())
                .map(Enum::name).toArray(String[]::new));

        return "insert-cube-form";
    }
}
