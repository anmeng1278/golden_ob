package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.dto.api.coupon.WechatCouponDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.dto.thirdParty.GetPayTradeResp;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.CartLogic;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("${webconfig.virtualPath}/product")
public class ProductController extends BaseController {

    /**
     * 商品详情
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String productDetail(HttpServletRequest request) {

        int activityTypeId = 0;
        if (!StringUtils.isEmpty(request.getParameter("activityType"))) {
            activityTypeId = Integer.parseInt(request.getParameter("activityType"));
        }

        ActivityType activityType = ActivityType.valueOf(activityTypeId);

        switch (activityType) {
            case NORMAL:
                return this.normalProduct(request);

            case COMBINATION:
                return this.combProduct(request);
        }

        return this.Redirect("/");
    }

    /**
     * 普通商品详情
     *
     * @param request
     * @return
     */
    private String normalProduct(HttpServletRequest request) {

        int productId = Integer.parseInt(request.getParameter("productId"));

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

        if (info.getProductImgDtos() != null){
            String imgUrl = info.getProductImgDtos().get(0).getImgPath();
            request.setAttribute("imgUrl", imgUrl);
        }else {
            String imgUrl = "https://hezy-static.oss-cn-shanghai.aliyuncs.com/test/product/oncecard_cover.png";
            request.setAttribute("imgUrl", imgUrl);
        }
        return "index/productDetail";
    }


    /**
     * 组合商品详情
     *
     * @param request
     * @return
     */
    public String combProduct(HttpServletRequest request) {

        if (StringUtils.isEmpty(request.getParameter("activityId"))) {
            return this.Redirect("/");
        }

        int activityId = Integer.parseInt(request.getParameter("activityId"));

        ActivityDto info = ActivityLogic.GetActivity(activityId);

        //非组合商品，不允许进此链接
        if (!info.getActivityType().equals(ActivityType.COMBINATION)) {
            return this.Redirect("/");
        }

        //活动中的商品
        List<ActivityProductDto> productDtos = ActivityLogic.GetActivityProductDtos(activityId);

        request.setAttribute("info", info);
        request.setAttribute("productDtos", productDtos);

        return "index/combActivityDetail";
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
    public RestResponseBo createOrder(HttpServletRequest request) throws Exception {

        if (StringUtils.isEmpty(request.getParameter("activityTypeId"))) {
            return RestResponseBo.fail("请求参数错误", null, this.Url("/"));
        }

        int activityTypeId = Integer.parseInt(request.getParameter("activityTypeId"));
        ActivityType activityType = ActivityType.valueOf(activityTypeId);

        OrderBase orderBase = OrderFactory.GetInstance(activityType);
        CreateOrderRequ requ;

        switch (activityType) {
            case NORMAL:
                requ = this.createNormalOrderRequest(request);
                break;
            case COMBINATION:
                requ = this.createCombOrderRequest(request);
                break;

            default:
                return RestResponseBo.fail("方法暂未实现", null, this.Url("/"));
        }

        CreateOrderResp resp = orderBase.CreateOrder(requ);

        if (!resp.isSuccess()) {
            throw new TipException("创建订单失败");
        }

        HashMap<String, Object> data = new HashMap<>();
        if (resp.getAmount() > 0) {
            //调起微信支付
            GetPayTradeResp pay = this.createPay(resp.getOrderId());
            if (!pay.getResponseHead().getCode().equals("0000")) {
                throw new TipException(pay.getResponseHead().getMessage());
            }
            data.put("pay", pay);
        }
        data.put("resp", resp);

        String successUrl = String.format("/pay/success/%s", resp.getOrderUniqueCode());
        data.put("successUrl", this.Url(successUrl));

        String url = this.Url("/order");
        return RestResponseBo.ok("创建订单成功", url, data);

    }

    /**
     * 组织创建普通订单请求
     *
     * @param request
     * @return
     */
    private CreateOrderRequ createNormalOrderRequest(HttpServletRequest request) {

        int productId = Integer.parseInt(request.getParameter("productId"));
        int num = Integer.parseInt(request.getParameter("num"));
        int productSpecId = Integer.parseInt(request.getParameter("productSpecId"));
        int wechatCouponId = 0;

        if (!StringUtils.isEmpty(request.getParameter("wechatCouponId"))) {
            wechatCouponId = Integer.parseInt(request.getParameter("wechatCouponId"));
        }

        String openId = this.OpenId();

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

        return requ;

    }

    /**
     * 组织创建组合订单请求
     *
     * @param request
     * @return
     */
    private CreateOrderRequ createCombOrderRequest(HttpServletRequest request) {

        String openId = this.OpenId();

        int num = Integer.parseInt(request.getParameter("num"));
        int activityId = Integer.parseInt(request.getParameter("activityId"));

        CreateOrderRequ requ = new CreateOrderRequ();
        requ.setActivityType(ActivityType.COMBINATION);
        requ.getBaseRequ().setOpenId(openId);
        requ.setActivityId(activityId);
        requ.setNumber(num);

        return requ;
    }

}
