package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.dto.api.wechat.WechatDto;
import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.GiftStatus;

public class GiftDto {

    public GiftDto() {
    }

    /**
     * 主键
     */
    private Integer giftId;
    /**
     * 赠送唯一码，由系统生成
     */
    private String giftUniqueCode;
    /**
     * 公众号open_id
     */
    private String openId;
    /**
     * 祝福语
     */
    private String blessings;
    /**
     * 赠送状态，0：未分享，10：已分享 30：领取中  40：已领完  60：取消赠送
     */
    private GiftStatus giftStatus;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 赠送区分，0:未赠送(此状态下不允许领取) 1：赠送好友，2：群赠送
     */
    private GiftShareType giftShareType;
    /**
     * 领取有效时间
     */
    private Integer expiredTime;
    /**
     * 创建时间
     */
    private Integer createTime;
    /**
     * 更新时间
     */
    private Integer updateTime;
    /**
     * 删除时间
     */
    private Integer deleteTime;

    /**
     * 赠送用户
     */
    private WechatDto wechatDto;

    /**
     * 赠送库存记录
     */
    private List<StockDto> stockDtos;

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getBlessings() {
        return blessings;
    }

    public void setBlessings(String blessings) {
        this.blessings = blessings;
    }

    public GiftStatus getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(GiftStatus giftStatus) {
        this.giftStatus = giftStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public GiftShareType getGiftShareType() {
        return giftShareType;
    }

    public void setGiftShareType(GiftShareType giftShareType) {
        this.giftShareType = giftShareType;
    }

    public Integer getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Integer expiredTime) {
        this.expiredTime = expiredTime;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Integer deleteTime) {
        this.deleteTime = deleteTime;
    }

    public WechatDto getWechatDto() {
        return wechatDto;
    }

    public void setWechatDto(WechatDto wechatDto) {
        this.wechatDto = wechatDto;
    }

    public List<StockDto> getStockDtos() {
        return stockDtos;
    }

    public void setStockDtos(List<StockDto> stockDtos) {
        this.stockDtos = stockDtos;
    }
}
