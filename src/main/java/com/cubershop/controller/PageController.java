package com.cubershop.controller;

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
import java.util.stream.Stream;

@Controller
public final class PageController {

    @Autowired
    private CubeDAO cubeDAO;

    @RequestMapping(method = RequestMethod.GET, path = {"/", "/home", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
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
        model.addAttribute("components", List.<String>of("all"));

        return "home";
    }

    @GetMapping(path = "/category/{category:2x2x2|3x3x3|4x4x4|5x5x5|others|big}")
    public String categoryPage(@PathVariable String category,
    @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model) {
        if(!order.equals("alpha_asc") && !order.equals("alpha_desc")
            && !order.equals("price_asc") && !order.equals("price_desc"))
                return "error/400";

        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", category);
        model.addAttribute("cubeList",
            ListProcessor.<Cube>groupBy(this.cubeDAO.findCubesByTypeAndOrder(category, order),3)
        );
        model.addAttribute("components", List.<String>of("all"));

        return "category";
    }

    @GetMapping(value = "/description/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String descriptionPage(
    @PathVariable("id") UUID id, Model model) {
        Cube cube = this.cubeDAO.findCubeById(id);

        model.addAttribute("cube", cube);
        model.addAttribute("components", List.<String>of("header", "nav", "news", "footer"));

        return "description";
    }

    @GetMapping(path = "/form/cube", produces = MediaType.TEXT_HTML_VALUE)
    public String cubeFormPage(@ModelAttribute Cube cube, BindingResult bindingResult, Model model) {
        model.addAttribute("categories",
            new String[]{"2x2x2", "3x3x3", "4x4x4", "5x5x5", "big"});

        return "cube-form";
    }
}
