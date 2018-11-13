package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.DictType;
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


    //活动活动列表

    /**
     * 查询所有字典列表
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
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values()).stream().filter(x -> x != ActivityType.NORMAL).collect(Collectors.toList());

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
        }
        //获取活动类型
        List<ActivityType> activityTypes = Arrays.asList(ActivityType.values()).stream().filter(x -> !x.equals(ActivityType.NORMAL)).collect(Collectors.toList());
        model.addAttribute("activityTypes", activityTypes);

        model.addAttribute("info", activity);
        model.addAttribute("dictId", activityId);
        model.addAttribute("activityProducts", activityProducts);

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

        String activityName = request.getParameter("activityName");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        String beginTime = request.getParameter("beginTime");
        String endTime = request.getParameter("endTime");
        String salePrice = request.getParameter("salePrice");
        if (StringUtils.isBlank(salePrice)) {
            salePrice = "0";
        }

        String originalPrice = request.getParameter("originalPrice");
        if (StringUtils.isBlank(originalPrice)) {
            originalPrice = "0";
        }

        String _number = request.getParameter("number");
        if (StringUtils.isBlank(_number)) {
            _number = "0";
        }

        int number = Integer.valueOf(_number);
        int stockCount = Integer.parseInt(request.getParameter("stockCount"));

        int beginTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(beginTime, "yyyy-MM-dd HH:mm:ss"));
        int endTimeUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endTime, "yyyy-MM-dd HH:mm:ss"));

        //是否审核
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));

        if (activityId > 0) {
            //修改
            activity = activityService.selectById(activityId);

            activity.setActivityName(activityName);
            activity.setStockCount(stockCount);
            activity.setBeginTime(beginTimeUnix);
            activity.setEndTime(endTimeUnix);
            activity.setStockCount(stockCount);

            activity.setNumber(number);
            activity.setOriginalPrice(Double.valueOf(originalPrice));
            activity.setSalePrice(Double.valueOf(salePrice));
            activity.setTypeId(typeId);
            activity.setIfpass(ifpass);

            activity.setUpdateTime(DateUtils.getCurrentUnixTime());
            activityService.updateById(activity);

        } else {
            //添加
            activity.setActivityName(activityName);
            activity.setStockCount(stockCount);
            activity.setBeginTime(beginTimeUnix);
            activity.setEndTime(endTimeUnix);
            activity.setStockCount(stockCount);

            activity.setIfpass(ifpass);
            activity.setNumber(number);
            activity.setOriginalPrice(Double.valueOf(originalPrice));
            activity.setSalePrice(Double.valueOf(salePrice));
            activity.setTypeId(typeId);

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

        String[] productSpecIds = request.getParameterValues("productSpecId");
        String[] productSpecSalePrices = request.getParameterValues("productSpecSalePrice");

        if (productSpecIds != null) {

            for (int i = 0; i < productSpecIds.length; i++) {

                int specId = Integer.parseInt(productSpecIds[i]);
                double specSalePrice = Double.parseDouble(productSpecSalePrices[i]);

                ActivityProduct activityProduct = activityProductService.selectOne(new EntityWrapper<ActivityProduct>().where("activity_id={0} and product_spec_id={1}", activityId, specId));
                if (activityProduct == null) {

                    ProductSpec productSpec = productSpecService.selectById(specId);

                    activityProduct = new ActivityProduct();
                    activityProduct.setSalePrice(specSalePrice);
                    activityProduct.setActivityId(activityId);
                    activityProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setProductId(productSpec.getProductId());
                    activityProduct.setProductSpecId(specId);
                    activityProduct.setSort(i);
                    activityProductService.insert(activityProduct);

                } else {
                    activityProduct.setSalePrice(specSalePrice);
                    activityProduct.setUpdateTime(DateUtils.getCurrentUnixTime());
                    activityProduct.setDeleteTime(null);
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

        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(product_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.where("exists( select * from _product_spec where product_id = _product.product_id )");
        if(typeId > 0){
            wrapper.where("type_id={0}",typeId);
        }
        if(propertyTypeId > 0){
            wrapper.where("property_type_id={0}",propertyTypeId);
        }
        wrapper.orderBy("create_time desc");

        Page<Product> pageInfo = new Page<>(page, limit);
        Page<Product> pp = productService.selectPage(pageInfo, wrapper);

        //商品分类
        EntityWrapper<Dict> typeWrapper = new EntityWrapper<>();
        typeWrapper.where("parent_dict_id={0} and delete_time is null", DictType.PRODUCTTYPE.getValue());
        List<Dict> productType = dictService.selectList(typeWrapper);

        //商品属性
        EntityWrapper<Dict> propertyWrapper = new EntityWrapper<>();
        propertyWrapper.where("parent_dict_id={0} and delete_time is null", DictType.PRODUCTPERPROTY.getValue());
        List<Dict> productProperty = dictService.selectList(propertyWrapper);


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

                map.put("productName", product.getProductName());
                map.put("specName", ps.getSpecName());
                map.put("salePrice", ps.getSalePrice());
                map.put("originalPrice", ps.getOriginalPrice());
                map.put("stockCount", ps.getStockCount());
                map.put("productSpecId", ps.getProductSpecId());

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

        Activity activity = activityService.selectById(id);

        if (method.equals("ifpass")) {
            activity.setIfpass(!activity.getIfpass());
            //activity.setUpdateTime(DateUtils.getCurrentUnixTime());
            activityService.updateById(activity);
        }
        if (method.equals("delete")) {
            //activity.setUpdateTime(DateUtils.getCurrentUnixTime());
            activity.setDeleteTime(DateUtils.getCurrentUnixTime());
            activityService.updateById(activity);
        }

        if (method.equals("up") || method.equals("down")) {
            ActivityLogic.Sort(id, method.equals("up"));
        }

        return RestResponseBo.ok("操作成功");

    }

}
