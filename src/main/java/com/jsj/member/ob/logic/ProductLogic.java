package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
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
import com.jsj.member.ob.enums.PropertyType;
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
public class ProductLogic extends BaseLogic {

    public static ProductLogic productLogic;

    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        productLogic = this;
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
        return ProductLogic.ToDto(entity);
    }

    /**
     * 实体转换
     *
     * @param entity
     * @return
     */
    public static ProductDto ToDto(Product entity) {

        //商品实体
        ProductDto productDto = new ProductDto();

        productDto.setGiftCopywriting(entity.getGiftCopywriting());
        productDto.setIfdistribution(entity.getIfdistribution());
        productDto.setIfpass(entity.getIfpass());
        productDto.setIfpickup(entity.getIfpickup());
        productDto.setIntroduce(entity.getIntroduce());

        productDto.setProductId(entity.getProductId());
        productDto.setProductName(entity.getProductName());
        productDto.setPropertyType(PropertyType.valueOf(entity.getPropertyTypeId()));
        productDto.setRemarks(entity.getRemarks());

        productDto.setTypeId(entity.getTypeId());
        productDto.setUnit(entity.getUnit());
        productDto.setUseIntro(entity.getUseIntro());

        //商品图片
        List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(entity.getProductId());
        productDto.setProductImgDtos(productImgDtos);

        //商品规格
        List<ProductSpecDto> productSpecDtos = ProductLogic.GetProductSpecDtos(entity.getProductId());
        productDto.setProductSpecDtos(productSpecDtos);
        productDto.setStockCount(0);

        if (!productSpecDtos.isEmpty()) {

            //售价
            Optional<ProductSpecDto> minSalePrice = productSpecDtos.stream().min(Comparator.comparing(ProductSpecDto::getSalePrice));
            productDto.setSalePrice(minSalePrice.get().getSalePrice());

            //原价
            Optional<ProductSpecDto> minOriginalPrice = productSpecDtos.stream().min(Comparator.comparing(ProductSpecDto::getOriginalPrice));
            productDto.setOriginalPrice(minOriginalPrice.get().getOriginalPrice());

            //库存
            int totalStock = productSpecDtos.stream().mapToInt(x -> x.getStockCount()).sum();
            productDto.setStockCount(totalStock);

        }

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
        Product product = productLogic.productService.selectById(productSpec.getProductId());

        ProductSpecDto productSpecDto = new ProductSpecDto();

        productSpecDto.setProductId(productSpec.getProductId());
        productSpecDto.setProductSpecId(productSpec.getProductSpecId());
        productSpecDto.setSpecName(productSpec.getSpecName());
        productSpecDto.setOriginalPrice(productSpec.getOriginalPrice());
        productSpecDto.setSalePrice(productSpec.getSalePrice());

        productSpecDto.setStockCount(productSpec.getStockCount());
        productSpecDto.setSort(productSpec.getSort());

        //商品实体
        ProductDto productDto = new ProductDto();

        productDto.setGiftCopywriting(product.getGiftCopywriting());
        productDto.setIfdistribution(product.getIfdistribution());
        productDto.setIfpass(product.getIfpass());
        productDto.setIfpickup(product.getIfpickup());
        productDto.setIntroduce(product.getIntroduce());

        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setPropertyType(PropertyType.valueOf(product.getPropertyTypeId()));
        productDto.setRemarks(product.getRemarks());

        productDto.setTypeId(product.getTypeId());
        productDto.setUnit(product.getUnit());
        productDto.setUseIntro(product.getUseIntro());

        //商品图片
        List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(product.getProductId());
        productDto.setProductImgDtos(productImgDtos);

        productSpecDto.setProductDto(productDto);

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


            //商品正在参加的活动
            List<ActivityDto> activityDtos = ActivityLogic.GetProductCurrentActivityDtos(productId, ps.getProductSpecId());


            ProductSpecDto productSpecDto = new ProductSpecDto();

            productSpecDto.setProductId(ps.getProductId());
            productSpecDto.setProductSpecId(ps.getProductSpecId());
            productSpecDto.setSpecName(ps.getSpecName());
            productSpecDto.setOriginalPrice(ps.getOriginalPrice());
            productSpecDto.setSalePrice(ps.getSalePrice());

            productSpecDto.setStockCount(ps.getStockCount());
            productSpecDto.setSort(ps.getSort());
            productSpecDto.setActivityDtos(activityDtos);

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
   /* public static List<ProductImgDto> GetProductImgDtos(int productId) {
        return productLogic.GetProductImgDtos(productId, null);
    }*/

    /**
     * 获取商品首图和详情图
     *
     * @param productId
     * @return
     */
    public static List<ProductImgDto> GetProductImgDtos(int productId) {

        List<ProductImgDto> productImgDtos = new ArrayList<>();

        //商品图片
        Wrapper<ProductImg> productImgWrapper = new EntityWrapper<>();
        productImgWrapper.where("product_id={0} and delete_time is null", productId);

        List list = new ArrayList();
        list.add(ProductImgType.COVER.getValue());
        list.add(ProductImgType.PRODUCT.getValue());
        productImgWrapper.in("type_id",list);

        productImgWrapper.orderBy("type_id asc, product_img_id asc");

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
     * 获取商品图片
     *
     * @param productId
     * @return
     */
    public static List<ProductImgDto> GetProductImgDtos(int productId, ProductImgType productImgType) {

        List<ProductImgDto> productImgDtos = new ArrayList<>();

        //商品图片
        Wrapper<ProductImg> productImgWrapper = new EntityWrapper<>();
        productImgWrapper.where("product_id={0} and delete_time is null", productId);
        if (productImgType != null) {
            productImgWrapper.where("type_id = {0}", productImgType.getValue());
        }
        productImgWrapper.orderBy("type_id asc, product_img_id asc");

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


    //region (public) 削减商品规格库存 ReductionProductSpecStock

    /**
     * 削减商品规格库存
     *
     * @param orderProductDtos
     */
    public static void ReductionProductSpecStock(List<OrderProductDto> orderProductDtos, ActivityType activityType, Integer orderId) {

        for (OrderProductDto opt : orderProductDtos) {

            ProductSpecDto dto = ProductLogic.GetProductSpec(opt.getProductSpecId());
            if (dto.getStockCount() < opt.getNumber()) {

                ProductStockException pe = new ProductStockException(String.format("\"%s\"规格库存不足<br />当前剩余：%d",
                        dto.getProductDto().getProductName(),
                        dto.getStockCount()));
                pe.setNumber(opt.getNumber());
                pe.setStock(dto.getStockCount());
                pe.setActivityType(activityType);
                pe.setOrderId(orderId);
                pe.setProductId(dto.getProductId());
                pe.setProductSpecId(opt.getProductSpecId());

                throw pe;
            }

            ProductSpec productSpec = productLogic.productSpecService.selectById(opt.getProductSpecId());

            productSpec.setStockCount(productSpec.getStockCount() - opt.getNumber());
            productLogic.productSpecService.updateById(productSpec);

        }
    }
    //endregion

    //region (public) 恢复商品规格库存 RestoreProductSpecStock

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
    //endregion

    /**
     * 获取已售罄商品
     *
     * @return
     */
    public static Integer GetSellOutCount() {

        EntityWrapper<Product> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where("( select sum(_product_spec.stock_count) from _product_spec where _product_spec.product_id = _product.product_id) = 0");

        return productLogic.productService.selectCount(wrapper);

    }

    /**
     * 获取未审核的商品
     *
     * @return
     */
    public static Integer GetUnPassProduct() {

        EntityWrapper<Product> productWrapper = new EntityWrapper<>();
        productWrapper.where("ifpass = 0 and delete_time is null");

        return productLogic.productService.selectCount(productWrapper);

    }

    /**
     * 更新商品排序
     *
     * @param productId
     * @param ifUp
     */
    public static void Sort(int productId, Boolean ifUp) {

        Product current = productLogic.productService.selectById(productId);
        int typeId = current.getTypeId();

        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        wrapper.where("type_id={0}", typeId);
        wrapper.orderBy("sort asc, update_time desc");
        List<Product> products = productLogic.productService.selectList(wrapper);

        //重置所有排序
        int sort = 0;
        for (Product product : products) {
            product.setSort(sort);
            productLogic.productService.updateById(product);

            sort++;
        }

        if (ifUp != null) {
            //向上
            if (ifUp) {

                current = products.stream().filter(p -> p.getProductId() == productId).findFirst().get();
                if (current.getSort() == 0) {
                    return;
                }
                int currentSort = current.getSort();
                Product prev = products.stream().filter(p -> p.getSort() == (currentSort - 1)).findFirst().get();
                prev.setSort(prev.getSort() + 1);

                current.setSort(current.getSort() - 1);

                productLogic.productService.updateById(current);
                productLogic.productService.updateById(prev);
            } else {
                //向下移动
                current = products.stream().filter(p -> p.getProductId() == productId).findFirst().get();
                if (current.getSort() == products.size() - 1) {
                    return;
                }
                int currentSort = current.getSort();
                Product next = products.stream().filter(p -> p.getSort() == currentSort + 1).findFirst().get();
                next.setSort(next.getSort() - 1);

                current.setSort(current.getSort() + 1);

                productLogic.productService.updateById(current);
                productLogic.productService.updateById(next);
            }
        }


    }


    /**
     * 根据类型获取商品
     *
     * @param typeId
     * @param limit
     * @return
     */
    public static List<ProductDto> GetProductDtos(int typeId, int limit) {

        EntityWrapper<Product> wrapper = new EntityWrapper<>();

        wrapper.where("type_id = {0}", typeId);
        wrapper.where("ifpass = 1 ");
        wrapper.where("delete_time is null");
        wrapper.orderBy("sort asc, update_time desc");

        Page<Product> products = productLogic.productService.selectPage(new Page<>(1, limit), wrapper);
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product p : products.getRecords()) {
            productDtos.add(ProductLogic.ToDto(p));
        }

        return productDtos;

    }

}
