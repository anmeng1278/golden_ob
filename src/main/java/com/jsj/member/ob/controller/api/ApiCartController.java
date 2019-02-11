package com.jsj.member.ob.controller.api;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.api.Request;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.dto.mini.*;
import com.jsj.member.ob.logic.CartLogic;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${webconfig.virtualPath}/mini")
public class ApiCartController extends BaseController {

    //region (public) 购物车列表 cart


    /**
     * 购物车列表
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "购物车列表")
    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public Response<CartResp> cart(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<CartRequ> requ) {

        CartResp resp = new CartResp();

        String unionId = requ.getRequestBody().getUnionId();
        String openId = requ.getRequestBody().getOpenId();

        List<CartProductDto> cartProductDtos = CartLogic.GetCartProducts(openId, unionId);

        resp.setCartProductDtos(cartProductDtos);
        return Response.ok(resp);
    }
    //endregion

    //region (public) 更新购物车 updateCart

    /**
     * 更新购物车
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "更新购物车")
    @RequestMapping(value = "/updateCart", method = RequestMethod.POST)
    public Response<UpdateCartResp> updateCart(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<UpdateCartRequ> requ) {

        UpdateCartResp resp = new UpdateCartResp();

        String openId = requ.getRequestBody().getOpenId();
        String unionId = requ.getRequestBody().getUnionId();

        if (requ.getRequestBody().getCartProductDtos() == null || requ.getRequestBody().getCartProductDtos().isEmpty()) {
            return Response.ok(resp);
        }

        for (CartProductDto jo : requ.getRequestBody().getCartProductDtos()) {
            CartLogic.AddUpdateCartProduct(openId, unionId, jo.getProductId(), jo.getProductSpecId(), jo.getNumber(), "update");
        }

        return Response.ok(resp);
    }
    //endregion

    //region (public) 删除购物车中的商品 deleteCartProduct

    /**
     * 删除购物车中的商品
     *
     * @param requ
     * @return
     */
    @ApiOperation(value = "删除购物车中的商品")
    @RequestMapping(value = "/deleteCartProduct", method = RequestMethod.POST)
    public Response<DeleteCartProductResp> deleteCartProduct(@ApiParam(value = "请求实体", required = true) @RequestBody @Validated Request<DeleteCartProductRequ> requ) {

        DeleteCartProductResp resp = new DeleteCartProductResp();

        int cartProductId = requ.getRequestBody().getCartProductId();
        CartLogic.DeleteCartProduct(cartProductId);

        return Response.ok(resp);

    }
    //endregion

}
