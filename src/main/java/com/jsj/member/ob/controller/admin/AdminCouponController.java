package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Coupon;
import com.jsj.member.ob.entity.CouponProduct;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.CouponUseRange;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.CouponProductService;
import com.jsj.member.ob.service.CouponService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@Controller
@RequestMapping("/admin/coupon")
public class AdminCouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponProductService couponProductService;

    @Autowired
    ProductService productService;

    /**
     * 查询所有优惠券列表
     *
     * @param page  当前页
     * @param limit 每页显示条数
     * @param keys  关键字
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                        Model model) {
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

        return "admin/coupon/index";
    }

    /**
     * 查询信息页面
     *
     * @param couponId
     * @param model
     * @return
     */
    @GetMapping("/{couponId}")
    public String info(@PathVariable("couponId") Integer couponId, Model model) {

        Coupon coupon = new Coupon();
        List<CouponProduct> couponProducts = new ArrayList<>();

        if (couponId > 0) {
            coupon = couponService.selectById(couponId);

            EntityWrapper<CouponProduct> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("delete_time is null");
            entityWrapper.where("coupon_id={0}", couponId);
            couponProducts = couponProductService.selectList(entityWrapper);

        } else {
            coupon.setTypeId(CouponType.DISCOUNT.getValue());
            coupon.setUserRange(CouponUseRange.ALL.getValue());
        }
        //获取优惠券类型
        List<CouponType> couponTypes = Arrays.asList(CouponType.values()).stream().filter(x -> !x.equals(CouponType.UNSET)).collect(Collectors.toList());

        //获取优惠券使用范围
        List<CouponUseRange> couponUseRanges = Arrays.asList(CouponUseRange.values()).stream().filter(x -> !x.equals(CouponUseRange.UNSET)).collect(Collectors.toList());

        model.addAttribute("couponTypes", couponTypes);
        model.addAttribute("couponUseRanges", couponUseRanges);
        model.addAttribute("info", coupon);
        model.addAttribute("couponId", couponId);
        model.addAttribute("couponProducts", couponProducts);

        return "admin/coupon/info";
    }


    /**
     * 修改或添加
     *
     * @param couponId
     * @param request
     * @return
     */
    @PostMapping("/{couponId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("couponId") Integer couponId, HttpServletRequest request) {

        Coupon coupon = new Coupon();

        String couponName = request.getParameter("couponName");
        Double amount = Double.valueOf(request.getParameter("amount"));
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String instruction = request.getParameter("instruction");
        String remarks = request.getParameter("remarks");
        String userRange = request.getParameter("userRange");
        int validDays = Integer.parseInt(request.getParameter("validDays"));

        if (couponId > 0) {
            //修改
            coupon = couponService.selectById(couponId);
            coupon.setCouponName(couponName);
            coupon.setAmount(amount);
            coupon.setTypeId(typeId);
            coupon.setInstruction(instruction);
            coupon.setRemarks(remarks);
            coupon.setValidDays(validDays);
            coupon.setUpdateTime(DateUtils.getCurrentUnixTime());
            couponService.updateById(coupon);

        } else {
            //添加
            coupon.setCouponName(couponName);
            coupon.setAmount(amount);
            coupon.setTypeId(typeId);
            coupon.setInstruction(instruction);
            coupon.setRemarks(remarks);
            coupon.setValidDays(validDays);
            coupon.setCreateTime(DateUtils.getCurrentUnixTime());
            coupon.setUpdateTime(DateUtils.getCurrentUnixTime());
            couponService.insert(coupon);
        }

        //先删除优惠券商品中的商品
        List<CouponProduct> couponProducts = couponProductService.selectList(new EntityWrapper<CouponProduct>().where("coupon_id={0} ", coupon.getCouponId()));
        couponProducts.forEach(ap -> {
            ap.setDeleteTime(DateUtils.getCurrentUnixTime());
            couponProductService.updateById(ap);
        });

        //添加or更新商品
        couponId = coupon.getCouponId();
        String[] productIds = request.getParameterValues("productId");
        if(productIds != null){
            for (int i=0;i<productIds.length;i++){
                int productId = Integer.parseInt(productIds[i]);
                CouponProduct couponProduct = couponProductService.selectOne(new EntityWrapper<CouponProduct>().where("coupon_id={0} and product_id={1} ", couponId,productId));

                //添加优惠券商品
                if (couponProduct == null) {
                    couponProduct = new CouponProduct();
                    couponProduct.setCouponId(couponId);
                    couponProduct.setProductId(productId);
                    couponProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                    couponProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    couponProductService.insert(couponProduct);

                } else {
                    couponProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    couponProduct.setDeleteTime(null);
                    couponProductService.updateAllColumnById(couponProduct);
                }

            }
        }
        return RestResponseBo.ok("保存成功");
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
            @RequestParam(value = "keys", defaultValue = "") String keys,
            HttpServletRequest request) {

        EntityWrapper<Product> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(product_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Product> pageInfo = new Page<>(page, limit);
        Page<Product> pp = productService.selectPage(pageInfo, wrapper);

        request.setAttribute("infos", new CCPage<Product>(pp, limit));
        request.setAttribute("keys", keys);

        return "admin/coupon/chooseProducts";
    }

    /**
     * 优惠券产品信息
     * @param request
     * @return
     */
    @PostMapping("/couponProducts")
    @ResponseBody
    public RestResponseBo couponProducts(HttpServletRequest request) {

        String productIds = request.getParameter("productIds");
        if (!StringUtils.isBlank(productIds)) {

            List<Integer> productIdLists = Arrays.asList(productIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());

            EntityWrapper entityWrapper = new EntityWrapper<Product>();
            entityWrapper.where("delete_time is null");
            entityWrapper.in("product_id", productIdLists);

            List<Product> products = productService.selectList(entityWrapper);
            List<HashMap<String, Object>> maps = new ArrayList<>();
            for (Product product : products) {

                HashMap<String, Object> map = new HashMap<>();

                ProductDto dto = ProductLogic.GetProduct(product.getProductId());

                map.put("productId", product.getProductId());
                map.put("productName", product.getProductName());
                map.put("salePrice", dto.getSalePrice());
                map.put("originalPrice", dto.getOriginalPrice());
                map.put("stockCount", dto.getStockCount());
                maps.add(map);
            }

            return RestResponseBo.ok(maps);

        }

        return RestResponseBo.ok();
    }


    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo status(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");
        Coupon coupon = couponService.selectById(id);

        if (method.equals("delete")) {
            coupon.setUpdateTime(DateUtils.getCurrentUnixTime());
            coupon.setDeleteTime(DateUtils.getCurrentUnixTime());
            couponService.updateById(coupon);
        }

        return RestResponseBo.ok("操作成功");

    }

}

