package com.cubershop.controller;

import com.cubershop.database.base.WishlistDAOBase;
import com.cubershop.context.utils.ListProcessor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public final class WishlistController {

    @Autowired
    public WishlistDAOBase wishlistDAOBase;

    @ModelAttribute
    public void modelAttributes(Model model) {
        model.addAttribute("cubeFormatter", new ListProcessor());
    }

    @CrossOrigin
    @GetMapping(path = "/wishlist/fetch", produces = "text/html")
    public String fetchWishlistProducts(
    @CookieValue(value = "wishlistProducts", defaultValue = "0") String wishlistCookie, Model model) {
        model.addAttribute("cubelist",
            wishlistDAOBase.findCubesByIdList(wishlistCookie.split("\\.")));

        return "/snippet/wishlist";
    }
}
