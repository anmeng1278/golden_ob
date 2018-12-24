package com.jsj.member.ob.rabbitmq.wx;

import com.alibaba.fastjson.annotation.JSONField;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.TemplateType;
import com.jsj.member.ob.logic.ConfigLogic;
import com.jsj.member.ob.rabbitmq.BaseDto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateDto extends BaseDto {

    public TemplateDto() {
        this.data = new LinkedHashMap<>();
    }

    /**
     * 接收人
     */
    @JSONField(name = "touser")
    private String toUser;
    /**
     * 模板编号
     */
    @JSONField(name = "template_id")
    private String templateId;
    /**
     * 跳转地址
     */
    private String url;

    /**
     * 模板数据
     */
    private Map<String, TemplateData> data;

    private String first;
    private String firstColor;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.data.put("first", new TemplateData(first, this.firstColor));
        this.first = first;
    }

    public String getFirstColor() {
        return firstColor;
    }

    public void setFirstColor(String firstColor) {
        this.data.put("first", new TemplateData(this.first, firstColor));
        this.firstColor = firstColor;
    }

    private String remark;

    private String remarkColor;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.data.put("remark", new TemplateData(remark, this.remarkColor));
        this.remark = remark;
    }

    public String getRemarkColor() {
        return remarkColor;
    }

    public void setRemarkColor(String remarkColor) {
        this.data.put("remark", new TemplateData(this.remark, remarkColor));
        this.remarkColor = remarkColor;
    }

    /**
     * 小程序
     */
    @JSONField(name = "miniprogram")
    private miniProgram miniProgram;

    /**
     * 模板类型
     */
    private TemplateType templateType;

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public TemplateDto.miniProgram getMiniProgram() {
        return miniProgram;
    }

    public void setMiniProgram(TemplateDto.miniProgram miniProgram) {
        this.miniProgram = miniProgram;
    }


    private class miniProgram implements Serializable {

        public miniProgram() {
        }

        /*
        小程序appid
         */
        private String appid;

        /**
         * 小程序pagepath
         */
        @JSONField(name = "pagepath")
        private String pagePath;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagePath() {
            return pagePath;
        }

        public void setPagePath(String pagePath) {
            this.pagePath = pagePath;
        }
    }


    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    private static final String gold_color = "#FF9900"; // 金黄色
    private static final String color = "#173177";


    /**
     * 客服消息
     *
     * @param openId
     * @param content
     * @return
     */
    public static TemplateDto NewCustomService(String openId, String content) {

        TemplateDto dto = new TemplateDto();
        dto.setTemplateType(TemplateType.SERVICE);
        dto.setToUser(openId);
        dto.setRemark(content);

        return dto;
    }

    /**
     * 发送支付成功模板消息
     *
     * @param order
     * @return
     */
    public static TemplateDto NewOrderPaySuccessed(Order order, ProductDto productDto) {

        /*
            {{first.DATA}}
            订单号：{{keyword1.DATA}}
            商品名称：{{keyword2.DATA}}
            支付金额：{{keyword3.DATA}}
            {{remark.DATA}}
        */
        TemplateDto dto = new TemplateDto();
        dto.setToUser(order.getOpenId());
        dto.setTemplateType(TemplateType.PAYSUCCESSED);
        dto.setFirst("您的订单已支付成功，这里的文案需要修改。\n");
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(order.getOrderId() + "", color));
        dto.getData().put("keyword2", new TemplateData(productDto.getProductName(), color));
        dto.getData().put("keyword3", new TemplateData(order.getPayAmount() + "", color));
        dto.setRemark("\n空铁管家祝您旅途愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/order", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }

}
