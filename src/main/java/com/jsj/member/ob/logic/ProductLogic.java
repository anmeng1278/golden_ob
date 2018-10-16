package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.order.OrderProduct;
import com.jsj.member.ob.dto.api.product.Product;
import com.jsj.member.ob.dto.api.product.ProductImg;
import com.jsj.member.ob.dto.api.product.ProductSize;
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
    public static Product GetProduct(int productId) {

        com.jsj.member.ob.entity.Product entity = productLogic.productService.selectById(productId);

        //商品实体
        Product product = new Product();

        product.setGiftCopywriting(entity.getGiftCopywriting());
        product.setIfdistribution(entity.getIfdistribution());
        product.setIfpass(entity.getIfpass());
        product.setIfpickup(entity.getIfpickup());
        product.setIntroduce(entity.getIntroduce());

        product.setOriginalPrice(entity.getOriginalPrice());
        product.setProductId(entity.getProductId());
        product.setProductName(entity.getProductName());
        product.setPropertyTypeId(entity.getPropertyTypeId());
        product.setRemarks(entity.getRemarks());

        product.setSalePrice(entity.getSalePrice());
        product.setStockCount(entity.getStockCount());
        product.setTypeId(entity.getTypeId());
        product.setUnit(entity.getUnit());
        product.setUseIntro(entity.getUseIntro());

        //商品图片
        Wrapper<com.jsj.member.ob.entity.ProductImg> productImgWrapper = new EntityWrapper<com.jsj.member.ob.entity.ProductImg>();
        productImgWrapper.where("product_id={0} and delete_time is null", productId);
        productImgWrapper.orderBy("update_time desc");

        List<com.jsj.member.ob.entity.ProductImg> productImgs = productLogic.productImgService.selectList(productImgWrapper);
        for (com.jsj.member.ob.entity.ProductImg pi : productImgs) {

            ProductImg productImg = new ProductImg();

            productImg.setImgPath(pi.getImgPath());
            productImg.setProductId(pi.getProductId());
            productImg.setProductImgId(pi.getProductImgId());
            productImg.setProductImgType(ProductImgType.valueOf(pi.getTypeId()));

            product.getProductImgs().add(productImg);
        }

        //商品尺寸
        Wrapper<com.jsj.member.ob.entity.ProductSize> productSizeWrapper = new EntityWrapper<com.jsj.member.ob.entity.ProductSize>();
        productSizeWrapper.where("product_id={0} and delete_time is null", productId);
        productSizeWrapper.orderBy("update_time desc");

        List<com.jsj.member.ob.entity.ProductSize> productSizes = productLogic.productSizeService.selectList(productSizeWrapper);
        for (com.jsj.member.ob.entity.ProductSize pz : productSizes) {

            ProductSize productSize = new ProductSize();

            productSize.setProductId(pz.getProductId());
            productSize.setProductSizeId(pz.getProductSizeId());
            productSize.setSizeName(pz.getSizeName());

            product.getProductSizes().add(productSize);

        }

        //商品秒杀价
        EntityWrapper<Seckill> seckillWrapper = new EntityWrapper<>();
        seckillWrapper.where("product_id={0} and ( UNIX_TIMESTAMP() between begin_time and end_time )", productId);
        seckillWrapper.where("ifpass = 1");
        seckillWrapper.where("delete_time is null");

        Seckill seckill = productLogic.seckillService.selectOne(seckillWrapper);
        if (seckill != null) {
            product.setStockCount(seckill.getStockCount());
            product.setSeckillId(seckill.getSeckillId());
            product.setSecPrice(seckill.getSeckillPrice().doubleValue());
        }

        return product;

    }


    /**
     * 创建订单后削减商品库存
     *
     * @param orderProducts
     */
    public static void ReductionProductStock(List<OrderProduct> orderProducts) {

        for (OrderProduct opt : orderProducts) {

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
     * @param orderProducts
     */
    public static void RestoreProductStock(List<OrderProduct> orderProducts) {

        for (OrderProduct opt : orderProducts) {

            com.jsj.member.ob.entity.Product product = productLogic.productService.selectById(opt.getProductId());

            product.setStockCount(product.getStockCount() + opt.getNumber());
            productLogic.productService.updateById(product);

        }

    }

}
