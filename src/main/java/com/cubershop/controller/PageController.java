package com.cubershop.controller;

import com.cubershop.database.base.CubeDAOBase;
import com.cubershop.context.entity.Cube;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.*;

@Controller
public final class PageController {

    @Autowired
    private CubeDAOBase cubeDAO;

    @RequestMapping(method = RequestMethod.GET, path = {"/", "/home", "/index"}, produces = MediaType.TEXT_HTML_VALUE)
    public String homePage(@RequestParam Map<String, String> params, Model model) {
        if(params.size() > 0) return "error/400";

        List<Cube> cubes = this.cubeDAO.findHomeCubes();

        model.addAttribute("cubes", cubes.stream().collect(groupingBy(Cube::getType, toSet())));
        model.addAttribute("components", List.<String>of("all"));

        return "home";
    }

    @GetMapping(path = "/category/{category:2x2x2|3x3x3|4x4x4|5x5x5|others|big}")
    public String categoryPage(@PathVariable String category,
    @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model) {
        if(!order.equals("alpha_asc") && !order.equals("alpha_desc")
            && !order.equals("price_asc") && !order.equals("price_desc"))
                return "error/400";

        model.addAttribute("isSearchPage", false);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", category);
        model.addAttribute("cubeList",
            Lists.partition(this.cubeDAO.findCubesByTypeAndOrder(category, order),3)
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

    @GetMapping(value = "/search", produces = MediaType.TEXT_HTML_VALUE, params = "exp")
    public String searchPage(
    @RequestParam(name = "exp", defaultValue = "") String expression,
    @RequestParam(name = "order", defaultValue = "alpha_asc") String order, Model model) {
        if(!order.equals("alpha_asc") && !order.equals("alpha_desc")
            && !order.equals("price_asc") && !order.equals("price_desc"))
            return "error/400";

        List<Cube> cubes = this.cubeDAO.findCubesByExpressionAndOrder(expression, order);

        model.addAttribute("isSearchPage", true);
        model.addAttribute("selectedOrder", order);
        model.addAttribute("category", "Results for "+expression);
        model.addAttribute("cubeList", Lists.partition(cubes, 3));
//        model.addAttribute("components", List.<String>of("all"));
        model.addAttribute("components", List.<String>of("header", "nav", "news", "footer"));

        return "category";
    }
}
