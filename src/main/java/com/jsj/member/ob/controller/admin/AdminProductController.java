package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.enums.DictType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.DictLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.service.ProductSpecService;
import com.jsj.member.ob.utils.CCPage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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

        String productSpecNames = String.join("|", productSpecDtos.stream().map(x -> x.getSpecName())
                .collect(Collectors.toList()));

        request.setAttribute("info", entity);
        request.setAttribute("productSpecNames", productSpecNames);

        request.setAttribute("productPerproties", productPerproties);
        request.setAttribute("productTypes", productTypes);


        return "admin/Product/info";
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo saveInfo(@PathVariable("productId") Integer productId, HttpServletRequest request) {

        Product entity = productService.selectById(productId);

        String productName = request.getParameter("productName");
        String typeId = request.getParameter("typeId");
        String propertyTypeId = request.getParameter("propertyTypeId");
        String introduce = request.getParameter("introduce");
        String salePrice = request.getParameter("salePrice");
        String originalPrice = request.getParameter("originalPrice");
        String stockCount = request.getParameter("stockCount");
        String sort = request.getParameter("sort");
        String useIntro = request.getParameter("useIntro");
        String unit = request.getParameter("unit");
        String remarks = request.getParameter("remarks");
        String giftCopywriting = request.getParameter("giftCopywriting");
        String ifpickup = request.getParameter("ifpickup");
        String ifpass = request.getParameter("ifpass");
        String ifdistribution = request.getParameter("ifdistribution");
        String opemployeeId = request.getParameter("opemployeeId");
        String createTime = request.getParameter("createTime");
        String updateTime = request.getParameter("updateTime");
        String deleteTime = request.getParameter("deleteTime");


        throw new TipException("方法暂未实现");
    }

}