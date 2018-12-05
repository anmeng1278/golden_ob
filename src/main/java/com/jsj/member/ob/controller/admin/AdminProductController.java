package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.ProductImg;
import com.jsj.member.ob.entity.ProductSpec;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.enums.ProductImgType;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.ProductImgService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.service.ProductSpecService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
@RequestMapping("/admin/product")
public class AdminProductController {

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    @Autowired
    ProductService productService;

    @Autowired
    ProductSpecService productSpecService;

    @Autowired
    ProductImgService productImgService;

    /**
     * 商品列表
     *
     * @param page
     * @param limit
     * @param keys
     * @param isSellout
     * @param request
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "20") int limit,
                        @RequestParam(value = "isSellout", defaultValue = "") String isSellout,
                        @RequestParam(value = "ifpass", defaultValue = "") Integer ifpass,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        @RequestParam(value = "typeId", defaultValue = "0") int typeId,
                        HttpServletRequest request) {

        EntityWrapper<Product> wrapper = new EntityWrapper<Product>();
        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(product_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.where(!StringUtils.isBlank(isSellout), "ifnull(( select sum(_product_spec.stock_count) from _product_spec where _product_spec.product_id = _product.product_id), 0) = 0");

        if (ifpass != null) {
            wrapper.where("ifpass={0}", ifpass);
        }
        wrapper.where(typeId > 0, "type_id={0}", typeId);
        wrapper.orderBy("sort asc, update_time desc");

        Page<Product> pageInfo = new Page<>(page, limit);
        Page<Product> pp = productService.selectPage(pageInfo, wrapper);

        //商品分类
        List<Dict> productTypes = DictLogic.GetDicts(DictType.PRODUCTTYPE);


        request.setAttribute("infos", new CCPage<Product>(pp, limit));
        request.setAttribute("keys", keys);
        request.setAttribute("isSellout", isSellout);
        request.setAttribute("ifpass", ifpass);
        request.setAttribute("productTypes", productTypes);
        request.setAttribute("typeId", typeId);


        return "admin/product/index";
    }


    /**
     * 商品详情
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public String info(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        //属性
        List<Dict> productPerproties = DictLogic.GetDicts(DictType.PRODUCTPERPROTY);
        //商品分类
        List<Dict> productTypes = DictLogic.GetDicts(DictType.PRODUCTTYPE);

        //商品信息
        Product info = new Product();

        //商品规格
        List<ProductSpecDto> productSpecs = new ArrayList<>();

        if (productId > 0) {
            info = productService.selectById(productId);
            productSpecs = ProductLogic.GetProductSpecDtos(productId);
        } else {
            info.setIfdistribution(true);
        }

        request.setAttribute("info", info);
        request.setAttribute("productSpecs", productSpecs);
        request.setAttribute("productPerproties", productPerproties);
        request.setAttribute("productTypes", productTypes);

        return "admin/product/info";
    }

    /**
     * 保存提交商品
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveInfo(@PathVariable("productId") Integer productId, HttpServletRequest request) {


        Product product = new Product();

        //商品名称
        String productName = request.getParameter("productName");
        //商品类型
        int typeId = Integer.valueOf(request.getParameter("typeId"));
        //商品属性
        int propertyTypeId = Integer.valueOf(request.getParameter("propertyTypeId"));
        //简介
        String introduce = request.getParameter("introduce");
        //使用说明
        String useIntro = request.getParameter("useIntro");
        //单位
        String unit = request.getParameter("unit");
        //备注
        String remarks = request.getParameter("remarks");
        //赠送方案
        String giftCopywriting = request.getParameter("giftCopywriting");
        //支持自提
        boolean ifpickup = !StringUtils.isBlank(request.getParameter("ifpickup"));
        //是否审核
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));
        //支持配送
        boolean ifdistribution = !StringUtils.isBlank(request.getParameter("ifdistribution"));

        if (productId > 0) {
            product = productService.selectById(productId);

            product.setProductName(productName);
            product.setTypeId(typeId);
            product.setPropertyTypeId(propertyTypeId);
            product.setIntroduce(introduce);

            product.setUseIntro(useIntro);
            product.setUnit(unit);
            product.setRemarks(remarks);
            product.setGiftCopywriting(giftCopywriting);
            product.setIfpickup(ifpickup);

            product.setIfpass(ifpass);
            product.setIfdistribution(ifdistribution);
            product.setUpdateTime(DateUtils.getCurrentUnixTime());

            productService.updateById(product);

            //ProductLogic.Sort(product.getProductId(), null);

        } else {
            product.setProductName(productName);
            product.setTypeId(typeId);
            product.setPropertyTypeId(propertyTypeId);
            product.setIntroduce(introduce);

            product.setUseIntro(useIntro);
            product.setUnit(unit);
            product.setRemarks(remarks);
            product.setGiftCopywriting(giftCopywriting);
            product.setIfpickup(ifpickup);

            product.setIfpass(ifpass);
            product.setIfdistribution(ifdistribution);
            product.setCreateTime(DateUtils.getCurrentUnixTime());
            product.setUpdateTime(DateUtils.getCurrentUnixTime());

            productService.insert(product);

            ProductLogic.Sort(product.getProductId(), true);
        }


        //型号规格
        EntityWrapper<ProductSpec> productSpecWrapper = new EntityWrapper<ProductSpec>();
        productSpecWrapper.where("product_id={0}", product.getProductId());

        List<ProductSpec> productSpecs = productSpecService.selectList(productSpecWrapper);

        //先删除掉所有型号规格
        productSpecs.forEach(ps -> {
            ps.setDeleteTime(DateUtils.getCurrentUnixTime());
            ps.setSort(9999);
            productSpecService.updateById(ps);
        });


        String[] specIds = request.getParameterValues("specId");
        String[] specNames = request.getParameterValues("specName");
        String[] specSalePrices = request.getParameterValues("specSalePrice");
        String[] specOriginalPrices = request.getParameterValues("specOriginalPrice");
        String[] specstockCounts = request.getParameterValues("specstockCount");

        if (specIds != null) {

            for (int i = 0; i < specIds.length; i++) {

                int specId = Integer.parseInt(specIds[i]);
                String specName = specNames[i].trim();
                Double salePrice = Double.valueOf(specSalePrices[i].trim());
                Double originalPrice = Double.valueOf(specOriginalPrices[i].trim());
                Integer stockCount = Integer.valueOf(specstockCounts[i].trim());

                if (specId == 0) {
                    //添加
                    ProductSpec productSpec = new ProductSpec();
                    productSpec.setSpecName(specName);
                    productSpec.setProductId(product.getProductId());
                    productSpec.setSalePrice(salePrice);
                    productSpec.setOriginalPrice(originalPrice);
                    productSpec.setStockCount(stockCount);
                    productSpec.setSort(i);
                    productSpec.setUpdateTime(DateUtils.getCurrentUnixTime());
                    productSpec.setCreateTime(DateUtils.getCurrentUnixTime());

                    productSpecService.insert(productSpec);
                } else {
                    ProductSpec productSpec = productSpecService.selectById(specId);

                    productSpec.setUpdateTime(DateUtils.getCurrentUnixTime());
                    productSpec.setDeleteTime(null);
                    productSpec.setSalePrice(salePrice);
                    productSpec.setOriginalPrice(originalPrice);
                    productSpec.setStockCount(stockCount);
                    productSpec.setSort(i);

                    productSpecService.updateAllColumnById(productSpec);

                }

            }

        }


        return RestResponseBo.ok("保存成功");
    }


    /**
     * 商品图片
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}/productImgs", method = RequestMethod.GET)
    public String productImgs(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        //商品信息
        Product entity = productService.selectById(productId);

        EntityWrapper<ProductImg> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("product_id = {0}", productId);
        entityWrapper.orderBy("type_id asc, product_img_id asc");
        List<ProductImg> productImgs = productImgService.selectList(entityWrapper);

        request.setAttribute("info", entity);
        request.setAttribute("productImgs", productImgs);

        return "admin/product/productImgs";
    }


    /**
     * 商品图片
     *
     * @param productId
     * @param request
     * @return
     */
    @RequestMapping(value = "/{productId}/productImgs", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveProductImgs(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        //删除所有图片
        productImgService.delete(new EntityWrapper<ProductImg>().where("product_id={0}", productId));

        //添加图片
        String[] imgPaths = request.getParameterValues("imgpath");
        if (imgPaths != null && imgPaths.length > 0) {


            int index = 0;
            for (String imgPath : imgPaths) {

                ProductImgType productImgType = ProductImgType.COVER;
                if (index > 0) {
                    productImgType = ProductImgType.PRODUCT;
                }

                ProductImg productImg = new ProductImg();
                productImg.setCreateTime(DateUtils.getCurrentUnixTime());
                productImg.setImgPath(imgPath);
                productImg.setProductId(productId);
                productImg.setTypeId(productImgType.getValue());
                productImg.setUpdateTime(DateUtils.getCurrentUnixTime());

                productImgService.insert(productImg);

                index++;
            }

        }

        return RestResponseBo.ok("操作成功");

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

        Product product = productService.selectById(id);

        if (method.equals("ifpass")) {
            product.setIfpass(!product.getIfpass());
            productService.updateById(product);
        }

        if (method.equals("delete")) {
            product.setDeleteTime(DateUtils.getCurrentUnixTime());
            productService.updateById(product);
        }

        if (method.equals("up") || method.equals("down")) {
            ProductLogic.Sort(id, method.equals("up"));
        }

        return RestResponseBo.ok("操作成功");

    }
}