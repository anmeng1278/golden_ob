package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.logic.CartLogic;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

        //用户OpenId
        String openId = this.OpenId();

        //商品详情
        ProductDto info = ProductLogic.GetProduct(productId);
        request.setAttribute("info", info);

        //库存
        int stockCount = 0;
        //售价
        double salePrice = info.getSalePrice();

        if (info.getProductSpecDtos() != null) {
            stockCount = info.getProductSpecDtos().stream().mapToInt(ProductSpecDto::getStockCount).sum();
        }
        request.setAttribute("stockCount", stockCount);
        request.setAttribute("salePrice", salePrice);

        //商品可用代金券
        List<WechatCouponDto> coupons = CouponLogic.GetWechatCoupons(productId, openId);
        request.setAttribute("coupons", coupons);

        return "index/productDetail";
    }


    /**
     * 添加购物车
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addCart", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo addCart(HttpServletRequest request) {

        String openId = this.OpenId();

        int productId = Integer.parseInt(request.getParameter("productId"));
        int num = Integer.parseInt(request.getParameter("num"));
        int productSpecId = Integer.parseInt(request.getParameter("productSpecId"));

        String method = "add";
        if (!StringUtils.isEmpty(request.getParameter("method"))) {
            method = request.getParameter("method");
        }

        CartLogic.AddUpdateCartProduct(openId, productId, productSpecId, num, method);

        return RestResponseBo.ok("添加购物车成功");
    }

    /**
     * 计算订单价格
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/calculateOrder", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo calculateOrder(HttpServletRequest request) {

        String openId = this.OpenId();

        int productId = Integer.parseInt(request.getParameter("productId"));
        int num = Integer.parseInt(request.getParameter("num"));
        int productSpecId = Integer.parseInt(request.getParameter("productSpecId"));
        int wechatCouponId = 0;

        if (!StringUtils.isEmpty(request.getParameter("wechatCouponId"))) {
            wechatCouponId = Integer.parseInt(request.getParameter("wechatCouponId"));
        }

        OrderBase orderBase = OrderFactory.GetInstance(ActivityType.NORMAL);

        CreateOrderRequ requ = new CreateOrderRequ();
        requ.setActivityType(ActivityType.NORMAL);
        requ.setWechatCouponId(wechatCouponId);
        requ.getBaseRequ().setOpenId(openId);

        List<OrderProductDto> orderProducts = new ArrayList<>();

        OrderProductDto orderProduct = new OrderProductDto();
        orderProduct.setProductId(productId);
        orderProduct.setProductSpecId(productSpecId);
        orderProduct.setNumber(num);
        orderProducts.add(orderProduct);

        requ.setOrderProductDtos(orderProducts);

        CreateOrderResp resp = orderBase.CalculateOrder(requ);
        return RestResponseBo.ok(resp);
    }


    /**
     * 创建订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo createOrder(HttpServletRequest request) {

        String openId = this.OpenId();

        int productId = Integer.parseInt(request.getParameter("productId"));
        int num = Integer.parseInt(request.getParameter("num"));
        int productSpecId = Integer.parseInt(request.getParameter("productSpecId"));
        int wechatCouponId = 0;

        if (!StringUtils.isEmpty(request.getParameter("wechatCouponId"))) {
            wechatCouponId = Integer.parseInt(request.getParameter("wechatCouponId"));
        }

        OrderBase orderBase = OrderFactory.GetInstance(ActivityType.NORMAL);

        CreateOrderRequ requ = new CreateOrderRequ();
        requ.setActivityType(ActivityType.NORMAL);
        requ.setWechatCouponId(wechatCouponId);
        requ.getBaseRequ().setOpenId(openId);

        List<OrderProductDto> orderProducts = new ArrayList<>();

        OrderProductDto orderProduct = new OrderProductDto();
        orderProduct.setProductId(productId);
        orderProduct.setProductSpecId(productSpecId);
        orderProduct.setNumber(num);
        orderProducts.add(orderProduct);

        requ.setOrderProductDtos(orderProducts);

        CreateOrderResp resp = orderBase.CreateOrder(requ);
        return RestResponseBo.ok(resp);

    }

}
