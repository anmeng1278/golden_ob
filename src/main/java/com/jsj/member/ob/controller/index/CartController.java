package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.logic.CartLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/cart")
public class CartController extends BaseController {

    /**
     * 配送列表
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String openId = this.OpenId();

        List<CartProductDto> cartProductDtos = CartLogic.GetCartProducts(openId);

        request.setAttribute("cartProductDtos",cartProductDtos);

        return "index/shoppingCart";
    }

    /**
     * 删除购物车中的商品
     * @param cartProductId
     * @return
     */
    @PostMapping("/{cartProductId}")
    public RestResponseBo deleteProduct(@PathVariable("cartProductId") int cartProductId){

        CartLogic.DeleteCartProduct(cartProductId);

        return RestResponseBo.ok("删除成功");

    }
}
