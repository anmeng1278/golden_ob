package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.logic.ProductLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {

    /**
     * 商品详情
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public String productDetail(@PathVariable("productId") Integer productId,
                                HttpServletRequest request) {

        ProductDto info = ProductLogic.GetProduct(productId);
        request.setAttribute("info", info);

        int stockCount = 0;
        double salePrice = info.getSalePrice();

        if (info.getProductSpecDtos() != null) {
            stockCount = info.getProductSpecDtos().stream().mapToInt(ProductSpecDto::getStockCount).sum();
        }
        request.setAttribute("stockCount", stockCount);
        request.setAttribute("salePrice", salePrice);

        return "index/productDetail";
    }


    /**
     * 添加购物车
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/cart", method = RequestMethod.GET)
    public RestResponseBo addCart(HttpServletRequest request) {

        int productId = Integer.parseInt(request.getParameter("productId"));
        int num = Integer.parseInt(request.getParameter("num"));
        int productSpecId = Integer.parseInt(request.getParameter("productSpecId"));


        return RestResponseBo.ok();
    }

    /**
     * 秒杀
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}/{activityId}", method = RequestMethod.GET)
    public String activityProduct(@PathVariable("productId") Integer productId,
                                  @PathVariable("activityId") Integer activityId,
                                  HttpServletRequest request) {

        return "index/productDetail";
    }

}
