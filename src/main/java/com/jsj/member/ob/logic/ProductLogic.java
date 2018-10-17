package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductImgDto;
import com.jsj.member.ob.dto.api.product.ProductSizeDto;
import com.jsj.member.ob.entity.Product;
import com.jsj.member.ob.entity.ProductImg;
import com.jsj.member.ob.entity.ProductSize;
import com.jsj.member.ob.entity.Seckill;
import com.jsj.member.ob.enums.ProductImgType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.ProductImgService;
import com.jsj.member.ob.service.ProductService;
import com.jsj.member.ob.service.ProductSizeService;
import com.jsj.member.ob.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ProductLogic {

    public static ProductLogic productLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        productLogic = this;
        productLogic.productService = this.productService;
        productLogic.productImgService = this.productImgService;
        productLogic.productSizeService = this.productSizeService;
        productLogic.seckillService = this.seckillService;
    }

    @Autowired
    ProductService productService;

    @Autowired
    ProductImgService productImgService;

    @Autowired
    ProductSizeService productSizeService;

    @Autowired
    SeckillService seckillService;


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

        productDto.setOriginalPrice(entity.getOriginalPrice());
        productDto.setProductId(entity.getProductId());
        productDto.setProductName(entity.getProductName());
        productDto.setPropertyTypeId(entity.getPropertyTypeId());
        productDto.setRemarks(entity.getRemarks());

        productDto.setSalePrice(entity.getSalePrice());
        productDto.setStockCount(entity.getStockCount());
        productDto.setTypeId(entity.getTypeId());
        productDto.setUnit(entity.getUnit());
        productDto.setUseIntro(entity.getUseIntro());

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

            productDto.getProductImgDtos().add(productImgDto);
        }

        //商品尺寸
        Wrapper<ProductSize> productSizeWrapper = new EntityWrapper<com.jsj.member.ob.entity.ProductSize>();
        productSizeWrapper.where("product_id={0} and delete_time is null", productId);
        productSizeWrapper.orderBy("update_time desc");

        List<ProductSize> productSizes = productLogic.productSizeService.selectList(productSizeWrapper);
        for (ProductSize pz : productSizes) {

            ProductSizeDto productSizeDto = new ProductSizeDto();

            productSizeDto.setProductId(pz.getProductId());
            productSizeDto.setProductSizeId(pz.getProductSizeId());
            productSizeDto.setSizeName(pz.getSizeName());

            productDto.getProductSizeDtos().add(productSizeDto);

        }

        //商品秒杀价
        EntityWrapper<Seckill> seckillWrapper = new EntityWrapper<>();
        seckillWrapper.where("product_id={0} and ( UNIX_TIMESTAMP() between begin_time and end_time )", productId);
        seckillWrapper.where("ifpass = 1");
        seckillWrapper.where("delete_time is null");

        Seckill seckill = productLogic.seckillService.selectOne(seckillWrapper);
        if (seckill != null) {
            productDto.setStockCount(seckill.getStockCount());
            productDto.setSeckillId(seckill.getSeckillId());
            productDto.setSecPrice(seckill.getSeckillPrice().doubleValue());
        }

        return productDto;

    }


    /**
     * 创建订单后削减商品库存
     *
     * @param orderProductDtos
     */
    public static void ReductionProductStock(List<OrderProductDto> orderProductDtos) {

        for (OrderProductDto opt : orderProductDtos) {

            com.jsj.member.ob.entity.Product product = productLogic.productService.selectById(opt.getProductId());
            if (product.getStockCount() < opt.getNumber()) {
                throw new TipException("商品库存不足");
            }

            product.setStockCount(product.getStockCount() - opt.getNumber());
            productLogic.productService.updateById(product);

        }

    }

    /**
     * 恢复商品库存
     *
     * @param orderProductDtos
     */
    public static void RestoreProductStock(List<OrderProductDto> orderProductDtos) {

        for (OrderProductDto opt : orderProductDtos) {

            com.jsj.member.ob.entity.Product product = productLogic.productService.selectById(opt.getProductId());

            product.setStockCount(product.getStockCount() + opt.getNumber());
            productLogic.productService.updateById(product);

        }

    }

}
