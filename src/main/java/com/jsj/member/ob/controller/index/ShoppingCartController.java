package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.entity.CartProduct;
import com.jsj.member.ob.logic.CartLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    /**
     * 获得购物车中的商品
     *
     * @param openId
     * @param model
     * @return
     */
    @GetMapping("/cart")
    public String getCartProduct(@RequestParam(value = "openId", defaultValue = "111") String openId, Model model) {

        List<CartProduct> cartProducts = CartLogic.GetCartProducts(openId);

        model.addAttribute("openId", openId);
        model.addAttribute("cartProducts", cartProducts);


        return "index/cart/ShoppingCart";
    }

    /**
     * 添加购物车
     *
     * @param openId
     * @param productSpecId
     * @param number
     * @param productId
     * @param model
     * @return
     */
    @GetMapping("/addProduct")
    public String addProduct(@RequestParam(value = "openId", defaultValue = "111") String openId,
                             @RequestParam(value = "productSpecId", defaultValue = "0") int productSpecId,
                             @RequestParam(value = "activityId", defaultValue = "0") int activityId,
                             @RequestParam(value = "number", defaultValue = "0") int number,
                             @RequestParam("productId") int productId, Model model, RedirectAttributes attr) {

        CartLogic.AddUpdateCartProduct(openId, productId, productSpecId, number);

        model.addAttribute("openId", openId);

        attr.addAttribute("productId", productId);


        if(activityId > 0){
            attr.addAttribute("activityId", activityId);
        }

        return "redirect:/goodsDetail/goodsDetail";
    }

    /**
     * 删除用户购物车中的某件商品
     * @param openId
     * @param cartProductId
     * @param model
     * @return
     */
    @GetMapping("/deleteProduct")
    public String deleteProduct(@RequestParam(value = "openId", defaultValue = "111") String openId,
                                @RequestParam("cartProductId") int cartProductId, Model model,RedirectAttributes attr) {

        CartLogic.DeleteCartProduct(cartProductId);
        model.addAttribute("openId",openId);
        attr.addAttribute("openId",openId);
        return "redirect:/cart/cart";
    }

}