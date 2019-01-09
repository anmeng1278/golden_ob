package com.jsj.member.ob.rabbitmq.wx;

import com.alibaba.fastjson.annotation.JSONField;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.DeliveryType;
import com.jsj.member.ob.enums.TemplateType;
import com.jsj.member.ob.logic.ConfigLogic;
import com.jsj.member.ob.logic.WechatLogic;
import com.jsj.member.ob.rabbitmq.BaseDto;
import com.jsj.member.ob.utils.DateUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
    public static TemplateDto NewOrderPaySuccessed(Order order, Map map) {

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
        dto.setFirst("您的订单已支付成功\n");
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(order.getOrderId() + "", color));
        dto.getData().put("keyword2", new TemplateData(map.get("productName").toString(), color));
        dto.getData().put("keyword3", new TemplateData(order.getPayAmount() + "", color));
        dto.setRemark("\n金色严选祝您生活愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/order", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }


    /**
     * 发送取消支付订单模板消息
     *
     * @param order
     * @return
     */
    public static TemplateDto CancelUnPayOrder(Order order, Map map) {

        /*
            {{first.DATA}}
            订单号：{{keyword1.DATA}}
            产品名称：{{keyword2.DATA}}
            订单金额：{{keyword3.DATA}}
            订单取消原因：{{keyword4.DATA}}
            {{remark.DATA}}
        */
        TemplateDto dto = new TemplateDto();
        dto.setToUser(order.getOpenId());
        dto.setTemplateType(TemplateType.CANCELUNPAYORDER);
        dto.setFirst(map.get("title").toString()+"\n");
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(order.getOrderId() + "", color));
        dto.getData().put("keyword2", new TemplateData(map.get("productName").toString(), color));
        dto.getData().put("keyword3", new TemplateData(order.getPayAmount() + "", color));
        dto.getData().put("keyword4", new TemplateData(map.get("reason").toString(), color));
        dto.setRemark("\n金色严选祝您生活愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/order", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }


    /**
     * 活动码使用成功模板消息
     *
     * @param delivery
     * @return
     */
    public static TemplateDto QrcodeUseSuccessed(Delivery delivery, List<StockDto> stockDtos) {

        /*{{first.DATA}}
        顾客昵称：{{keyword1.DATA}}
        消费时间：{{keyword2.DATA}}
        {{remark.DATA}}*/

        //消费时间
        String consumeDate = DateUtils.formatDateByUnixTime(Long.parseLong(delivery.getCreateTime() + ""), "yyyy-MM-dd");

        TemplateDto dto = new TemplateDto();
        dto.setToUser(delivery.getOpenId());
        dto.setTemplateType(TemplateType.QRCODEUSESUCCESSED);
        dto.setFirst(String.format("感谢您在%s机场使用金色逸站通用券，点击模板可直接出未用券二维码\n", delivery.getAirportName()));
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(WechatLogic.GetWechat(delivery.getOpenId()).getNickname() + "", color));
        dto.getData().put("keyword2", new TemplateData(consumeDate + "", color));
        dto.setRemark("\n空铁管家祝您旅途愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/stock/qrcode/%s/%s", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath(),delivery.getDeliveryId(), stockDtos.get(0).getStockId()));

        return dto;
    }

    /**
     * 实物使用成功模板消息
     *
     * @param delivery
     * @return
     */
    public static TemplateDto EntityUseSuccessed(Delivery delivery, Map map,List<StockDto> stockDtos) {


        /*{{first.DATA}}
        订单时间：{{keyword1.DATA}}
        订单编号：{{keyword2.DATA}}
        订单物品：{{keyword3.DATA}}
        {{remark.DATA}}*/

        //订单时间
        String orderDate = DateUtils.formatDateByUnixTime(Long.parseLong(delivery.getCreateTime() + ""), "yyyy-MM-dd");

        TemplateDto dto = new TemplateDto();
        dto.setToUser(delivery.getOpenId());
        dto.setTemplateType(TemplateType.ENTITYUSESUCCESSED);
        if (delivery.getTypeId() == DeliveryType.DISTRIBUTE.getValue()) {
            dto.setFirst("您的配送订单已创建成功，我们正在为您安排配送！\n");
        }
        if (delivery.getTypeId() == DeliveryType.PICKUP.getValue()) {
            dto.setFirst("您的配送订单已创建成功，请到相应的自提点提取！\n");
        }
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(orderDate, color));
        dto.getData().put("keyword2", new TemplateData(stockDtos.get(0).getOrderId().toString(), color));
        dto.getData().put("keyword3", new TemplateData(map.get("productName").toString(), color));
        dto.setRemark("\n金色严选祝您生活愉快！");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/delivery", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }

    /**
     * 开卡确认中模板消息
     *
     * @param delivery
     * @return
     */
    public static TemplateDto OpenCardConfirm(DeliveryDto delivery) {

        /*
           {{first.DATA}}
            产品名称：{{keyword1.DATA}}
            激活时间：{{keyword2.DATA}}
            截止时间：{{keyword3.DATA}}
            {{remark.DATA}}
        */

        //生效时间
        String effectiveDate = DateUtils.formatDateByUnixTime(Long.parseLong(delivery.getEffectiveDate() + ""), "yyyy-MM-dd");

        TemplateDto dto = new TemplateDto();
        dto.setToUser(delivery.getOpenId());
        dto.setTemplateType(TemplateType.OPENCARDCONFIRM);
        dto.setFirst("正在为您开卡，请您耐心等待\n");
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1", new TemplateData(delivery.getProductDtos().get(0).getProductName() + "", color));
        dto.getData().put("keyword2", new TemplateData(effectiveDate, color));
        dto.getData().put("keyword3", new TemplateData("依据卡的使用说明", color));
        dto.setRemark("\n空铁管家祝您旅途愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/stock", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }


    /**
     * 开卡成功模板消息
     *
     * @param goldenDto
     * @return
     */
    public static TemplateDto OpenCardSuccess(DeliveryDto goldenDto) {

        /*
            {{first.DATA}}
            公司名称：{{keyword1.DATA}}
            出卡时间：{{keyword2.DATA}}
            {{remark.DATA}}
        */

        //开卡时间
        String openCardDate = DateUtils.formatDateByUnixTime(Long.parseLong(DateUtils.getCurrentUnixTime() + ""), "yyyy-MM-dd");

        TemplateDto dto = new TemplateDto();
        dto.setToUser(goldenDto.getOpenId());
        dto.setTemplateType(TemplateType.OPENCARDSUCCESS);
        dto.setFirst(String.format("您的%s已成功开通\n", goldenDto.getPropertyType().getMessage()));
        dto.setFirstColor(gold_color);
        dto.getData().put("keyword1",new TemplateData("北京金色世纪商旅网络有限公司", color));
        dto.getData().put("keyword2", new TemplateData(openCardDate, color));
        dto.setRemark("\n空铁管家祝您旅途愉快");
        dto.setRemarkColor(gold_color);
        dto.setUrl(String.format("%s%s/delivery", ConfigLogic.GetWebConfig().getHost(), ConfigLogic.GetWebConfig().getVirtualPath()));

        return dto;
    }

    public static Map GetProduct(List<StockDto> stockDtos) {

        Map<String, Object> map = new HashMap<>();

        StringBuilder name = new StringBuilder();

        for (StockDto stockDto : stockDtos) {
            name.append(stockDto.getProductDto().getProductName()+"*"+stockDto.getNumber() + ",");
        }

        if (name.length() > 0) {
            name.deleteCharAt(name.length() - 1);
        }

        String productName = name.toString().toLowerCase();
        map.put("productName", productName);


        return map;
    }
}
