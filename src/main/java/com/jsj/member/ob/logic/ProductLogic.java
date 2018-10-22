package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductImgDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.ProductImg;
import com.jsj.member.ob.entity.ProductSpec;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.ProductImgType;
import com.jsj.member.ob.exception.ProductStockException;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ProductImgService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.service.ProductSpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class ProductLogic {

    public static ProductLogic productLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        productLogic = this;
        productLogic.productService = this.productService;
        productLogic.productImgService = this.productImgService;
        productLogic.productSpecService = this.productSpecService;
        productLogic.activityProductService = this.activityProductService;
    }

    @Autowired
    ProductService productService;

    @Autowired
    ProductImgService productImgService;

    @Autowired
    ProductSpecService productSpecService;

    @Autowired
    ActivityProductService activityProductService;

    /**
     * 获取商品信息
     *
     * @param productId
     */
    public static ProductDto GetProduct(int productId) {

        Product entity = productLogic.productService.selectById(productId);

        //商品实体
        ProductDto productDto = new ProductDto();

        productDto.setGiftCopywriting(entity.getGiftCopywriting());
        productDto.setIfdistribution(entity.getIfdistribution());
        productDto.setIfpass(entity.getIfpass());
        productDto.setIfpickup(entity.getIfpickup());
        productDto.setIntroduce(entity.getIntroduce());

        productDto.setProductId(entity.getProductId());
        productDto.setProductName(entity.getProductName());
        productDto.setPropertyTypeId(entity.getPropertyTypeId());
        productDto.setRemarks(entity.getRemarks());

        productDto.setTypeId(entity.getTypeId());
        productDto.setUnit(entity.getUnit());
        productDto.setUseIntro(entity.getUseIntro());

        //商品图片
        List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(productId);
        productDto.setProductImgDtos(productImgDtos);

        //商品规格
        List<ProductSpecDto> productSpecDtos = ProductLogic.GetProductSpecDtos(productId);
        productDto.setProductSpecDtos(productSpecDtos);

        if(!productSpecDtos.isEmpty()){

            //售价
            Optional<ProductSpecDto> minSalePrice = productSpecDtos.stream().min(Comparator.comparing(ProductSpecDto::getSalePrice));
            productDto.setSalePrice(minSalePrice.get().getSalePrice());

            //原价
            Optional<ProductSpecDto> minOriginalPrice = productSpecDtos.stream().min(Comparator.comparing(ProductSpecDto::getOriginalPrice));
            productDto.setOriginalPrice(minSalePrice.get().getOriginalPrice());

            //库存
            int totalStock = productSpecDtos.stream().mapToInt(x -> x.getStockCount()).sum();
            productDto.setStockCount(totalStock);

        }

        //商品正在参加的活动
        List<ActivityDto> currentActivityDtos = ActivityLogic.GetProductCurrentActivityDtos(productId);
        productDto.setCurrentActivityDtos(currentActivityDtos);

        return productDto;

    }


    /**
     * 获取商品规格
     *
     * @param productSpecId
     * @return
     */
    public static ProductSpecDto GetProductSpec(int productSpecId) {

        ProductSpec productSpec = productLogic.productSpecService.selectById(productSpecId);
        ProductSpecDto productSpecDto = new ProductSpecDto();

        productSpecDto.setProductId(productSpec.getProductId());
        productSpecDto.setProductSpecId(productSpec.getProductSpecId());
        productSpecDto.setSpecName(productSpec.getSpecName());
        productSpecDto.setOriginalPrice(productSpec.getOriginalPrice());
        productSpecDto.setSalePrice(productSpec.getSalePrice());

        productSpecDto.setStockCount(productSpec.getStockCount());
        productSpecDto.setSort(productSpec.getSort());

        return productSpecDto;

    }

    /**
     * 获取商品规格
     *
     * @param productId
     * @return
     */
    public static List<ProductSpecDto> GetProductSpecDtos(int productId) {

        List<ProductSpecDto> productSpecDtos = new ArrayList<>();

        //商品规格
        Wrapper<ProductSpec> productSpecWrapper = new EntityWrapper<>();
        productSpecWrapper.where("product_id={0} and delete_time is null", productId);
        productSpecWrapper.orderBy("sort asc, update_time desc");

        List<ProductSpec> productSpecs = productLogic.productSpecService.selectList(productSpecWrapper);
        for (ProductSpec ps : productSpecs) {

            ProductSpecDto productSpecDto = new ProductSpecDto();

            productSpecDto.setProductId(ps.getProductId());
            productSpecDto.setProductSpecId(ps.getProductSpecId());
            productSpecDto.setSpecName(ps.getSpecName());
            productSpecDto.setOriginalPrice(ps.getOriginalPrice());
            productSpecDto.setSalePrice(ps.getSalePrice());

            productSpecDto.setStockCount(ps.getStockCount());
            productSpecDto.setSort(ps.getSort());

            productSpecDtos.add(productSpecDto);
        }

        return productSpecDtos;

    }

    /**
     * 获取商品图片
     *
     * @param productId
     * @return
     */
    public static List<ProductImgDto> GetProductImgDtos(int productId) {

        List<ProductImgDto> productImgDtos = new ArrayList<>();

        //商品图片
        Wrapper<ProductImg> productImgWrapper = new EntityWrapper<com.jsj.member.ob.entity.ProductImg>();
        productImgWrapper.where("product_id={0} and delete_time is null", productId);
        productImgWrapper.orderBy("update_time desc");

        List<ProductImg> productImgs = productLogic.productImgService.selectList(productImgWrapper);
        for (ProductImg pi : productImgs) {

            ProductImgDto productImgDto = new ProductImgDto();

            productImgDto.setImgPath(pi.getImgPath());
            productImgDto.setProductId(pi.getProductId());
            productImgDto.setProductImgId(pi.getProductImgId());
            productImgDto.setProductImgType(ProductImgType.valueOf(pi.getTypeId()));

            productImgDtos.add(productImgDto);
        }

        return productImgDtos;
    }



    /**
     * 削减商品规格库存
     *
     * @param orderProductDtos
     */
    public static void ReductionProductSpecStock(List<OrderProductDto> orderProductDtos, ActivityType activityType, Integer orderId) {

        for (OrderProductDto opt : orderProductDtos) {

            ProductSpec productSpec = productLogic.productSpecService.selectById(opt.getProductSpecId());
            if (productSpec.getStockCount() < opt.getNumber()) {

                ProductStockException pe = new ProductStockException("库存不足");
                pe.setNumber(opt.getNumber());
                pe.setStock(productSpec.getStockCount());
                pe.setActivityType(activityType);
                pe.setOrderId(orderId);
                pe.setProductId(productSpec.getProductId());
                pe.setProductSpecId(opt.getProductSpecId());

                throw pe;
            }

            productSpec.setStockCount(productSpec.getStockCount() - opt.getNumber());
            productLogic.productSpecService.updateById(productSpec);

        }
    }



    /**
     * 恢复商品规格库存
     *
     * @param orderProductDtos
     */
    public static void RestoreProductSpecStock(List<OrderProductDto> orderProductDtos) {

        for (OrderProductDto opt : orderProductDtos) {

            ProductSpec productSpec = productLogic.productSpecService.selectById(opt.getProductSpecId());

            productSpec.setStockCount(productSpec.getStockCount() + opt.getNumber());
            productLogic.productSpecService.updateById(productSpec);

        }

    }

}
