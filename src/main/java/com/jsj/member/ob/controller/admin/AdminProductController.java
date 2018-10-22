package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.ProductSpec;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
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

    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "20") int limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        HttpServletRequest request) {

        Page<Product> pageInfo = new Page<>(page, limit);
        Page<Product> pp = productService.selectPage(pageInfo, new EntityWrapper<Product>());

        request.setAttribute("infos", new CCPage<Product>(pp, limit));
        request.setAttribute("keys", keys);

        return "admin/Product/index";
    }


    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo modifyStatus(@RequestParam(value = "productId", defaultValue = "0") Integer productId,
                                       HttpServletRequest request) {

        String method = request.getParameter("method");
        if (StringUtils.isBlank(method)) {
            throw new TipException("方法名不能为空");
        }

        Product entity = productService.selectById(productId);

        throw new TipException("方法暂未实现");
    }


    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public String info(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        //商品信息
        Product entity = productService.selectById(productId);
        //属性
        List<Dict> productPerproties = DictLogic.GetDicts(DictType.PRODUCTPERPROTY);
        //商品分类
        List<Dict> productTypes = DictLogic.GetDicts(DictType.PRODUCTTYPE);
        //商品规格
        List<ProductSpecDto> productSpecDtos = ProductLogic.GetProductSpecDtos(productId);

        //规格型号
        StringBuilder productSpecs = new StringBuilder();
        productSpecDtos.forEach(x -> {
            productSpecs.append(String.format("%s|%.2f|%.2f|%d|%d\n", x.getSpecName(), x.getSalePrice(), x.getOriginalPrice(), x.getStockCount(), x.getSort()));
        });

        request.setAttribute("info", entity);
        request.setAttribute("productSpecs", productSpecs);
        request.setAttribute("productPerproties", productPerproties);
        request.setAttribute("productTypes", productTypes);

        return "admin/Product/info";
    }

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
        //排序
        int sort = Integer.valueOf(request.getParameter("sort"));
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
            product.setSort(sort);

            product.setUseIntro(useIntro);
            product.setUnit(unit);
            product.setRemarks(remarks);
            product.setGiftCopywriting(giftCopywriting);
            product.setIfpickup(ifpickup);

            product.setIfpass(ifpass);
            product.setIfdistribution(ifdistribution);
            product.setUpdateTime(DateUtils.getCurrentUnixTime());

            productService.updateById(product);
        } else {
            product.setProductName(productName);
            product.setTypeId(typeId);
            product.setPropertyTypeId(propertyTypeId);
            product.setIntroduce(introduce);
            product.setSort(sort);

            product.setUseIntro(useIntro);
            product.setUnit(unit);
            product.setRemarks(remarks);
            product.setGiftCopywriting(giftCopywriting);
            product.setIfpickup(ifpickup);

            product.setIfpass(ifpass);
            product.setIfdistribution(ifdistribution);
            product.setUpdateTime(DateUtils.getCurrentUnixTime());

            productService.insert(product);
        }

        //型号规格
        EntityWrapper<ProductSpec> productSpecWrapper = new EntityWrapper<ProductSpec>();
        productSpecWrapper.where("product_id={0}", product.getProductId());

        List<ProductSpec> productSpecs = productSpecService.selectList(productSpecWrapper);

        //先删除掉所有型号规格
        productSpecs.forEach(ps -> {
            ps.setDeleteTime(DateUtils.getCurrentUnixTime());
            productSpecService.updateById(ps);
        });


        String productSpecss = request.getParameter("productSpecs");
        String[] pss = productSpecss.split("[\n \r \r\n]");
        for (String psi : pss) {

            if (StringUtils.isBlank(psi)) {
                continue;
            }
            String[] pssi = psi.split("[\\|]");
            if (pssi.length != 5) {
                continue;
            }

            String specName = pssi[0].trim();
            Double salePrice = Double.valueOf(pssi[1].trim());
            Double originalPrice = Double.valueOf(pssi[2].trim());
            Integer stockCount = Integer.valueOf(pssi[3].trim());
            Integer psSort = Integer.valueOf(pssi[4].trim());

            ProductSpec productSpec = productSpecService.selectOne(new EntityWrapper<ProductSpec>().where("product_id={0} and spec_name={1}", product.getProductId(), specName));
            if (productSpec == null) {

                productSpec = new ProductSpec();
                productSpec.setSpecName(specName);
                productSpec.setProductId(product.getProductId());
                productSpec.setSalePrice(salePrice);
                productSpec.setOriginalPrice(originalPrice);
                productSpec.setStockCount(stockCount);
                productSpec.setSort(psSort);
                productSpec.setUpdateTime(DateUtils.getCurrentUnixTime());
                productSpec.setCreateTime(DateUtils.getCurrentUnixTime());

                productSpecService.insert(productSpec);
            } else {

                productSpec.setUpdateTime(DateUtils.getCurrentUnixTime());
                productSpec.setDeleteTime(null);
                productSpec.setSalePrice(salePrice);
                productSpec.setOriginalPrice(originalPrice);
                productSpec.setStockCount(stockCount);
                productSpec.setSort(psSort);

                productSpecService.updateAllColumnById(productSpec);
            }
        }
        return RestResponseBo.ok("保存成功");
    }

}