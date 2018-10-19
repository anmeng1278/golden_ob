package com.jsj.member.ob.dto.api.stock;

import com.jsj.member.ob.dto.api.product.ProductDto;

public class StockDto {

    /**
     * 库存id
     */
    private Integer stockId;

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 商品id
     */
    private Integer productId;

<<<<<<< Updated upstream
    private int productSpecId;

=======
    /**
     * 订单id
     */
>>>>>>> Stashed changes
    private Integer orderId;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 商品信息
     */
    private ProductDto productDto;

    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public int getProductSpecId() {
        return productSpecId;
    }

    public void setProductSpecId(int productSpecId) {
        this.productSpecId = productSpecId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }
}
