package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductImgDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.ProductImgType;
import com.jsj.member.ob.enums.PropertyType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.*;
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
@RequestMapping("/admin/activity")
public class AdminActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityProductService activityProductService;

    @Autowired
    private DictService dictService;


    /**
     * 查询所有活动活动列表
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
        EntityWrapper<Activity> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(activity_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("sort asc, update_time desc");

        Page<Activity> pageInfo = new Page<>(page, limit);
        Page<Activity> pp = activityService.selectPage(pageInfo, wrapper);


        //所属类型
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values()).stream().filter(x -> !x.equals(ActivityType.NORMAL)).collect(Collectors.toList());

        model.addAttribute("infos", new CCPage<Dict>(pp, limit));
        model.addAttribute("activityTypes", activityTypes);

        model.addAttribute("keys", keys);
        model.addAttribute("typeId", typeId);

        return "admin/activity/index";
    }

    /**
     * 查询信息页面
     *
     * @param activityId
     * @param model
     * @return
     */
    @GetMapping("/{activityId}")
    public String info(@PathVariable("activityId") Integer activityId, Model model) {

        Activity activity = new Activity();
        List<ActivityProduct> activityProducts = new ArrayList<>();

        if (activityId > 0) {
            activity = activityService.selectById(activityId);

            EntityWrapper<ActivityProduct> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("delete_time is null");
            entityWrapper.where("activity_id={0}", activityId);
            entityWrapper.orderBy("sort asc, update_time desc");
            activityProducts = activityProductService.selectList(entityWrapper);

        } else {
            activity.setTypeId(ActivityType.GROUPON.getValue());
            activity.setShowTime(DateUtils.getCurrentUnixTime());
            activity.setBeginTime(DateUtils.getCurrentUnixTime());
        }
        //获取活动类型
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values()).stream().filter(x -> !x.equals(ActivityType.NORMAL)).collect(Collectors.toList());

        ProductImgType seckill = ProductImgType.SECKILL;

        model.addAttribute("activityTypes", activityTypes);
        model.addAttribute("seckill", seckill);

        model.addAttribute("info", activity);
        model.addAttribute("activityId", activityId);
        model.addAttribute("activityProducts", activityProducts);

        boolean allowModify = ActivityLogic.checkActivity(activityId);
        model.addAttribute("allowModify", allowModify);

        return "admin/activity/info";
    }

    /**
     * 修改或添加
     *
     * @param activityId
     * @param request
     * @return
     */
    @PostMapping("/{activityId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("activityId") Integer activityId, HttpServletRequest request) {

        Activity activity = new Activity();

        //活动名称
        String activityName = request.getParameter("activityName");
        //类型
        ActivityType activityType = ActivityType.valueOf(Integer.parseInt(request.getParameter("typeId")));

        //下划线为可选部分
        String _salePrice = request.getParameter("salePrice");
        String _introduce = request.getParameter("introduce");
        String _imgPath = request.getParameter("imgPath");
        String _originalPrice = request.getParameter("originalPrice");
        String _number = request.getParameter("number");
        String _stockCount = request.getParameter("stockCount");

        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String showTime = request.getParameter("showTime");

        int beginTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(beginTime, "yyyy-MM-dd HH:mm:ss"));
        int endTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));
        int showTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(showTime, "yyyy-MM-dd HH:mm:ss"));

        //是否审核
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));

        String[] productSpecIds = request.getParameterValues("productSpecId");
        String[] productSpecSalePrices = request.getParameterValues("productSpecSalePrice");
        String[] productSpecStockCounts = request.getParameterValues("productSpecStockCount");

        if (productSpecIds == null || productSpecIds.length == 0) {
            throw new TipException("请添加活动商品");
        }
        int stockCount = 0;
        double salePrice = 0, originalPrice = 0;
        String introduce = "";
        String imgPath = "";
        int number = 0;

        String final_stockCount = _stockCount;

        switch (activityType) {
            case GROUPON:
                stockCount = Integer.parseInt(_stockCount);
                if (Arrays.stream(productSpecStockCounts).filter(x -> !x.equals(final_stockCount)).findFirst().isPresent()) {
                    throw new TipException("团购库存必须与商品使用库存完全一致");
                }
                salePrice = Double.parseDouble(_salePrice);
                originalPrice = Double.parseDouble(_originalPrice);
                number = Integer.parseInt(_number);
                break;
            case SECKILL:
            case EXCHANGE:
                break;
            case COMBINATION:
                if (StringUtils.isEmpty(_imgPath)) {
                    throw new TipException("请上传活动封面");
                }
                stockCount = Integer.parseInt(_stockCount);
                if (Arrays.stream(productSpecStockCounts).filter(x -> !x.equals(final_stockCount)).findFirst().isPresent()) {
                    throw new TipException("组合活动库存必须与商品使用库存完全一致");
                }
                salePrice = Double.parseDouble(_salePrice);
                originalPrice = Double.parseDouble(_originalPrice);
                introduce = _introduce;
                imgPath = _imgPath;
                break;
        }


        if (activityId > 0) {

            //校验活动是否允许修改
            boolean allowModify = ActivityLogic.checkActivity(activityId);
            if (!allowModify) {
                throw new TipException("秒杀活动已开始，并且已有用户抢购成功！<br />不允许修改。");
            }

            //修改
            activity = activityService.selectById(activityId);

            activity.setActivityName(activityName);
            activity.setStockCount(stockCount);
            activity.setBeginTime(beginTimeUnix);
            activity.setEndTime(endTimeUnix);
            activity.setShowTime(showTimeUnix);

            activity.setNumber(number);
            activity.setOriginalPrice(originalPrice);
            activity.setSalePrice(salePrice);
            activity.setTypeId(activityType.getValue());
            activity.setIfpass(ifpass);
            activity.setIntroduce(introduce);
            activity.setImgPath(imgPath);

            activity.setUpdateTime(DateUtils.getCurrentUnixTime());
            activityService.updateById(activity);

        } else {

            //添加
            activity.setActivityName(activityName);
            activity.setStockCount(stockCount);
            activity.setBeginTime(beginTimeUnix);
            activity.setEndTime(endTimeUnix);
            activity.setShowTime(showTimeUnix);

            activity.setIfpass(ifpass);
            activity.setNumber(number);
            activity.setOriginalPrice(originalPrice);
            activity.setSalePrice(salePrice);
            activity.setTypeId(activityType.getValue());
            activity.setIntroduce(introduce);
            activity.setImgPath(imgPath);

            activity.setCreateTime(DateUtils.getCurrentUnixTime());
            activity.setUpdateTime(DateUtils.getCurrentUnixTime());

            activityService.insert(activity);

            ActivityLogic.Sort(activity.getActivityId(), true);

        }

        //添加更新商品
        List<ActivityProduct> activityProducts = activityProductService.selectList(new EntityWrapper<ActivityProduct>().where("activity_id={0}", activity.getActivityId()));
        activityProducts.forEach(ap -> {
            ap.setDeleteTime(DateUtils.getCurrentUnixTime());
            ap.setSort(9999);
            activityProductService.updateById(ap);
        });

        activityId = activity.getActivityId();

        if (productSpecIds != null) {

            for (int i = 0; i < productSpecIds.length; i++) {

                int specId = Integer.parseInt(productSpecIds[i]);
                double specSalePrice = Double.parseDouble(productSpecSalePrices[i]);
                int productSpecStockCount = Integer.parseInt(productSpecStockCounts[i]);

                ProductSpecDto specDto = ProductLogic.GetProductSpec(specId);
                if (productSpecStockCount > specDto.getStockCount()) {
                    throw new TipException(String.format("\"%s %s\"<br />使用库存不能大于剩余库存<br />当前使用：%d<br />当前剩余：%d",
                            specDto.getProductDto().getProductName(),
                            specDto.getSpecName(),
                            productSpecStockCount,
                            specDto.getStockCount()));
                }

                ActivityProduct activityProduct = activityProductService.selectOne(new EntityWrapper<ActivityProduct>().where("activity_id={0} and product_spec_id={1}", activityId, specId));
                if (activityProduct == null) {

                    activityProduct = new ActivityProduct();
                    activityProduct.setSalePrice(specSalePrice);
                    activityProduct.setActivityId(activityId);
                    activityProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setProductId(specDto.getProductId());
                    activityProduct.setProductSpecId(specId);
                    activityProduct.setSort(i);
                    activityProduct.setStockCount(productSpecStockCount);
                    activityProductService.insert(activityProduct);

                } else {
                    activityProduct.setSalePrice(specSalePrice);
                    activityProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setDeleteTime(null);
                    activityProduct.setStockCount(productSpecStockCount);
                    activityProduct.setSort(i);
                    activityProductService.updateAllColumnById(activityProduct);
                }

            }
        }

        return RestResponseBo.ok("保存成功");
    }


    @Autowired
    ProductService productService;


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
        EntityWrapper<Dict> typeWrapper = new EntityWrapper<>();
        typeWrapper.where("parent_dict_id={0} and delete_time is null", DictType.PRODUCTTYPE.getValue());
        List<Dict> productType = dictService.selectList(typeWrapper);

        //商品属性
        List<PropertyType> productProperty = Arrays.asList(PropertyType.values());

        request.setAttribute("infos", new CCPage<Dict>(pp, limit));
        request.setAttribute("typeId", typeId);
        request.setAttribute("keys", keys);
        request.setAttribute("propertyTypeId", propertyTypeId);
        request.setAttribute("productType", productType);
        request.setAttribute("productProperty", productProperty);

        return "admin/activity/chooseProducts";
    }

    @Autowired
    ProductSpecService productSpecService;

    @PostMapping("/activityProductSpecs")
    @ResponseBody
    public RestResponseBo activityProductSpecs(HttpServletRequest request) {

        String strSpecIds = request.getParameter("specIds");
        if (!StringUtils.isBlank(strSpecIds)) {

            List<Integer> specIds = Arrays.asList(strSpecIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());


            EntityWrapper entityWrapper = new EntityWrapper<ProductSpec>();
            entityWrapper.where("delete_time is null");
            entityWrapper.in("product_spec_id", specIds);

            List<ProductSpec> productSpecs = productSpecService.selectList(entityWrapper);

            List<HashMap<String, Object>> maps = new ArrayList<>();
            for (ProductSpec ps : productSpecs) {

                HashMap<String, Object> map = new HashMap<>();
                Product product = productService.selectById(ps.getProductId());

                List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(ps.getProductId(), ProductImgType.SECKILL);
                if (productImgDtos.size() != 0) {
                    //商品的秒杀图片
                    map.put("imgPath", productImgDtos.get(0).getImgPath());
                } else {
                    map.put("imgPath", null);
                }

                map.put("productName", product.getProductName());
                map.put("specName", ps.getSpecName());
                map.put("salePrice", ps.getSalePrice());
                map.put("originalPrice", ps.getOriginalPrice());
                map.put("stockCount", ps.getStockCount());
                map.put("productSpecId", ps.getProductSpecId());
                map.put("productId", ps.getProductId());

                maps.add(map);
            }

            return RestResponseBo.ok(maps);

        }

        return RestResponseBo.ok();
    }


    @Autowired
    ProductImgService productImgService;

    /**
     * 商品秒杀图片
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{activityId}/{productId}", method = RequestMethod.GET)
    public String productImgs(@PathVariable("productId") Integer productId, @PathVariable("activityId") Integer activityId, HttpServletRequest request) {

        //商品信息
        Product entity = productService.selectById(productId);

        List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(productId, ProductImgType.SECKILL);

        request.setAttribute("info", entity);
        request.setAttribute("productImgDtos", productImgDtos);
        request.setAttribute("activityId", activityId);
        request.setAttribute("productId", productId);

        return "admin/activity/productImgs";
    }


    /**
     * 商品图片
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{activityId}/{productId}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveProductImgs(@PathVariable("productId") Integer productId, @PathVariable("activityId") Integer activityId, HttpServletRequest request) {

        //添加图片
        String[] imgPaths = request.getParameterValues("imgpath");
        if (imgPaths == null || imgPaths.length == 0) {
            throw new TipException("请上传秒杀图片");
        }
        if (StringUtils.isEmpty(imgPaths[0])) {
            throw new TipException("请上传秒杀图片");
        }

        //删除所有图片
        productImgService.delete(new EntityWrapper<ProductImg>().where("product_id={0} and type_id={1}",
                productId, ProductImgType.SECKILL.getValue()));


        if (imgPaths != null && imgPaths.length > 0) {

            for (String imgPath : imgPaths) {

                ProductImg productImg = new ProductImg();
                productImg.setImgPath(imgPath);
                productImg.setProductId(productId);
                productImg.setTypeId(ProductImgType.SECKILL.getValue());
                productImg.setCreateTime(DateUtils.getCurrentUnixTime());
                productImg.setUpdateTime(DateUtils.getCurrentUnixTime());

                productImgService.insert(productImg);
            }

        }

        return RestResponseBo.ok("操作成功", null, imgPaths);

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

        Activity activity = activityService.selectById(id);

        if (method.equals("ifpass")) {
            activity.setIfpass(!activity.getIfpass());
            activityService.updateById(activity);
        }
        if (method.equals("delete")) {
            activity.setDeleteTime(DateUtils.getCurrentUnixTime());
            activityService.updateById(activity);
        }

        if (method.equals("up") || method.equals("down")) {
            ActivityLogic.Sort(id, method.equals("up"));
        }

        return RestResponseBo.ok("操作成功");

    }

}
