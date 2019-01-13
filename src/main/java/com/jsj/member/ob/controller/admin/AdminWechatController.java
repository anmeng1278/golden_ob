package com.jsj.member.ob.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.controller.BaseController;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.stock.StockFlowDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@Controller
@RequestMapping("/admin/wechat")
public class AdminWechatController extends BaseController {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private StockService stockService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    CouponService couponService;

    @Autowired
    WechatCouponService wechatCouponService;

    @Autowired
    ProductService productService;

    @Autowired
    DictService dictService;

    @Autowired
    ProductSpecService productSpecService;


    /**
     * 查询用户列表
     *
     * @param page
     * @param limit
     * @param keys
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        Model model
    ) {

        EntityWrapper<Wechat> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(nickname LIKE concat(concat('%',{0}),'%') or open_id like concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Wechat> pageInfo = new Page<>(page, limit);
        Page<Wechat> pp = wechatService.selectPage(pageInfo, wrapper);

        model.addAttribute("keys", keys);
        model.addAttribute("infos", new CCPage<Wechat>(pp, limit));

        return "admin/wechat/index";

    }


    /**
     * 用户库存
     *
     * @param openId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{openId}/stock", method = RequestMethod.GET)
    public String stock(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @PathVariable("openId") String openId,
            @RequestParam(value = "stockStatus", defaultValue = "-1") Integer stockStatus,
            @RequestParam(value = "stockType", defaultValue = "-1") Integer stockType,
            HttpServletRequest request) {

        WechatDto wechat = WechatLogic.GetWechat(openId);

        EntityWrapper<Stock> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        wrapper.where("open_id={0}", openId);
        wrapper.where(stockStatus > -1, "status={0}", stockStatus);
        wrapper.where(stockType > -1, "type_id={0}", stockType);

        wrapper.orderBy("create_time desc");

        Page<Stock> pageInfo = new Page<>(page, limit);
        Page<Stock> pp = stockService.selectPage(pageInfo, wrapper);

        StockStatus[] stockStatuses = StockStatus.values();
        StockType[] stockTypes = StockType.values();

        request.setAttribute("wechat", wechat);
        request.setAttribute("infos", new CCPage<Stock>(pp, limit));
        request.setAttribute("stockStatuses", stockStatuses);
        request.setAttribute("stockTypes", stockTypes);

        request.setAttribute("stockType", stockType);
        request.setAttribute("stockStatus", stockStatus);


        return "admin/wechat/wechatStock";

    }

    /**
     * 获取库存流转日志
     *
     * @param stockId
     * @return
     */
    @RequestMapping(value = "/{stockId}/stockFlows", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo stockFlows(@PathVariable("stockId") Integer stockId) {

        List<StockFlowDto> stockFlows = StockLogic.GetStockFlows(stockId, true);
        return RestResponseBo.ok(stockFlows);

    }

    /**
     * 获取库存流转日志
     *
     * @param giftId
     * @return
     */
    @RequestMapping(value = "/giftStock/{giftId}/{stockId}", method = RequestMethod.GET)
    public String gift(@PathVariable("giftId") Integer giftId,
                       @PathVariable("stockId") Integer stockId,
                       HttpServletRequest request) {

        GiftDto info = GiftLogic.GetGift(giftId);
        List<StockDto> stockDtos = GiftLogic.GetGiftStocks(giftId);

        request.setAttribute("info", info);
        request.setAttribute("stockDtos", stockDtos);
        request.setAttribute("stockId", stockId);

        return "admin/wechat/giftStocks";
    }


    /**
     * 赠送优惠券
     *
     * @param page  当前页
     * @param limit 每页显示条数
     * @param keys  关键字
     * @param model
     * @return
     */
    @GetMapping("/giveCoupon/{openId}")
    public String giveCoupon(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                             @RequestParam(value = "keys", defaultValue = "") String keys,
                             @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                             @PathVariable("openId") String openId, Model model) {
        EntityWrapper<Coupon> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(coupon_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Coupon> pageInfo = new Page<>(page, limit);
        Page<Coupon> pp = couponService.selectPage(pageInfo, wrapper);

        //优惠券类型
        List<CouponType> couponTypes = Arrays.asList(CouponType.values());

        model.addAttribute("infos", new CCPage<Coupon>(pp, limit));
        model.addAttribute("couponTypes", couponTypes);
        model.addAttribute("keys", keys);
        model.addAttribute("typeId", typeId);
        model.addAttribute("openId", openId);

        return "admin/wechat/giveCoupons";
    }

    @PostMapping("/addCoupon/{openId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo addCoupon(@PathVariable("openId") String openId, HttpServletRequest request) {
        String couponIds = request.getParameter("couponIds");
        List<WechatCoupon> wechatCoupons = new ArrayList<>();

        if (!StringUtils.isBlank(couponIds)) {

            List<Integer> couponIdLists = Arrays.asList(couponIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());

            for (int i = 0; i < couponIdLists.size(); i++) {

                Integer couponId = couponIdLists.get(i);
                CouponDto couponDto = CouponLogic.GetCoupon(couponId);

                WechatCoupon wechatCoupon = new WechatCoupon();
                wechatCoupon.setOpenId(openId);
                wechatCoupon.setCouponId(Integer.valueOf(couponId));
                wechatCoupon.setExpiredTime(DateUtils.getCurrentUnixTime() + couponDto.getValidDays() * 60 * 60 * 24);
                wechatCoupon.setAmount(couponDto.getAmount());
                wechatCoupon.setTypeId(couponDto.getCouponType().getValue());
                wechatCoupon.setCreateTime(DateUtils.getCurrentUnixTime());
                wechatCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());
                wechatCoupons.add(wechatCoupon);

            }
            wechatCouponService.insertBatch(wechatCoupons);
        }
        return RestResponseBo.ok("赠送成功");
    }


    /**
     * 选择活动商品页面
     *
     * @param request
     * @return
     */
    @GetMapping("/chooseProducts")
    public String chooseProducts(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
            @RequestParam(value = "propertyTypeId", defaultValue = "0") Integer propertyTypeId,
            @RequestParam(value = "keys", defaultValue = "") String keys,
            HttpServletRequest request) {

        EntityWrapper<Product> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null and ifpass = 1");
        wrapper.where(!StringUtils.isBlank(keys), "(product_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.where("exists( select * from _product_spec where product_id = _product.product_id )");
        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        if (propertyTypeId > 0) {
            wrapper.where("property_type_id={0}", propertyTypeId);
        }
        wrapper.orderBy("create_time desc");

        Page<Product> pageInfo = new Page<>(page, limit);
        Page<Product> pp = productService.selectPage(pageInfo, wrapper);

        //商品分类
        List<Dict> productType = DictLogic.GetDicts(DictType.PRODUCTTYPE);

        //商品属性
        List<PropertyType> productProperty = Arrays.asList(PropertyType.values());

        request.setAttribute("infos", new CCPage<Dict>(pp, limit));
        request.setAttribute("typeId", typeId);
        request.setAttribute("keys", keys);
        request.setAttribute("propertyTypeId", propertyTypeId);
        request.setAttribute("productType", productType);
        request.setAttribute("productProperty", productProperty);

        return "admin/wechat/chooseProducts";
    }


    @PostMapping("/createOrder")
    @ResponseBody
    public RestResponseBo createOrder(HttpServletRequest request) {

        String openId = request.getParameter("openId");
        String p = request.getParameter("p");
        List<JSONObject> jsonObjects = JSON.parseArray(p, JSONObject.class);


        CreateOrderRequ requ = new CreateOrderRequ();
        requ.setActivityType(ActivityType.NORMAL);
        requ.getBaseRequ().setOpenId(openId);
        requ.setSourceType(SourceType.AWKTC);
        requ.setRemarks("手动添加库存");

        for (JSONObject jo : jsonObjects) {

            int productId = jo.getIntValue("productId");
            int num = jo.getIntValue("num");
            int productSpecId = jo.getInteger("productSpecId");

            OrderProductDto orderProduct = new OrderProductDto();
            orderProduct.setProductId(productId);
            orderProduct.setProductSpecId(productSpecId);
            orderProduct.setNumber(num);

            requ.getOrderProductDtos().add(orderProduct);
        }

        //获取创建订单请求
        OrderBase orderBase = OrderFactory.GetInstance(ActivityType.NORMAL);
        CreateOrderResp resp = orderBase.CreateOrder(requ);

        NotifyModelOuterClass.NotifyModel notifyModel = NotifyModelOuterClass.NotifyModel.getDefaultInstance();
        orderBase.PaySuccessed(resp.getOrderId(), notifyModel);

        return RestResponseBo.ok("添加成功");
    }

    @PostMapping("/delete")
    @ResponseBody
    public RestResponseBo deleteProduct(HttpServletRequest request) {

        String stockId = request.getParameter("stockId");
        Stock stock = stockService.selectById(stockId);
        stock.setDeleteTime(DateUtils.getCurrentUnixTime());
        stockService.updateById(stock);

        return RestResponseBo.ok("删除成功");
    }

}
