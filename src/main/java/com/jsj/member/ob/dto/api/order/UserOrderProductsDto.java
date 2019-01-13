package com.jsj.member.ob.dto.api.order;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "products")
public class UserOrderProductsDto {

    @XmlElement(name = "product")
    private List<product> product;

    public List<product> getProduct() {
        return product;
    }

    public void setProduct(List<product> product) {
        this.product = product;
    }

    public static class product {

        private Integer productId;

        private Integer productSpecId;

        private String productName;

        private String specName;

        private Integer number;

        private Double salePrice;

        private String imgPath;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public Integer getProductSpecId() {
            return productSpecId;
        }

        public void setProductSpecId(Integer productSpecId) {
            this.productSpecId = productSpecId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public Double getSalePrice() {
            return salePrice;
        }

        public void setSalePrice(Double salePrice) {
            this.salePrice = salePrice;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }
    }
}

