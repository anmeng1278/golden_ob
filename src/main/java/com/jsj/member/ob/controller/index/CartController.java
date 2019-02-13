package com.jsj.member.ob.controller.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.logic.CartLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/cart")
public class CartController extends BaseController {

    /**
     * 购物车列表
     *
     * @param request
     * @return
     */
    @GetMapping("")
    public String index(HttpServletRequest request) {

        String unionId = this.UnionId();
        String openId = this.OpenId();

        List<CartProductDto> cartProductDtos = CartLogic.GetCartProducts(openId, unionId);

        request.setAttribute("cartProductDtos", cartProductDtos);

        return "index/shoppingCart";
    }

    /**
     * 购物车列表
     *
     * @param request
     * @return
     */
    @PostMapping("")
    @ResponseBody
    public RestResponseBo updateCart(HttpServletRequest request) {

        String openId = this.OpenId();
        String unionId = this.UnionId();

        if (StringUtils.isEmpty(request.getParameter("p"))) {
            return RestResponseBo.fail("参数错误");
        }
        String p = request.getParameter("p");
        List<JSONObject> jsonObjects = JSON.parseArray(p, JSONObject.class);

        for (JSONObject jo : jsonObjects) {
            int num = jo.getIntValue("num");
            int specId = jo.getInteger("specId");
            int productId = jo.getIntValue("productId");
            CartLogic.AddUpdateCartProduct(openId, unionId, productId, specId, num, "update", 0, ActivityType.NORMAL);
        }

        return RestResponseBo.ok("操作成功");
    }

    /**
     * 删除购物车中的商品
     *
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public RestResponseBo deleteProduct(HttpServletRequest request) {

        int cartProductId = Integer.parseInt(request.getParameter("cartProductId"));

        CartLogic.DeleteCartProduct(cartProductId);

        return RestResponseBo.ok("删除成功");

    }
}
