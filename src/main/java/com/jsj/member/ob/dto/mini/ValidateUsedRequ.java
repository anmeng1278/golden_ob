package com.jsj.member.ob.dto.mini;

import com.jsj.member.ob.dto.api.stock.UseProductDto;
import com.jsj.member.ob.enums.PropertyType;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ValidateUsedRequ {

    @ApiModelProperty(value = "openId，必填", required = true)
    private String openId;

    @ApiModelProperty(value = "unionId，必填", required = true)
    private String unionId;

    @ApiModelProperty(value = "商品属性", required = true)
    private PropertyType propertyType;

    @ApiModelProperty(value = "使用商品", required = true)
    private List<UseProductDto> useProductDtos;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public List<UseProductDto> getUseProductDtos() {
        return useProductDtos;
    }

    public void setUseProductDtos(List<UseProductDto> useProductDtos) {
        this.useProductDtos = useProductDtos;
    }
}
