package com.jsj.member.ob.dto.api.gift;

import com.jsj.member.ob.enums.GiftShareType;
import com.jsj.member.ob.enums.GiftStatus;

public class UserGiftDto {

    /**
     * 赠送编号
     */
    private int giftId;

    /**
     * 赠送唯一标识
     */
    private String giftUniqueCode;

    /**
     * 赠送状态
     */
    private GiftStatus giftStatus;

    /**
     * 赠送类型
     */
    private GiftShareType giftShareType;

    /**
     * 赠送时间
     */
    private int createTime;

    /**
     * 商品图片
     */
    private String imgPath;

    /**
     * 领取人编号
     */
    private String openId;

    /**
     * 领取人
     */
    private String nickName;

    /**
     * 领取人图片
     */
    private String headImgUrl;

    /**
     * 赠送数量
     */
    private int count;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftUniqueCode() {
        return giftUniqueCode;
    }

    public void setGiftUniqueCode(String giftUniqueCode) {
        this.giftUniqueCode = giftUniqueCode;
    }

    public GiftStatus getGiftStatus() {
        return giftStatus;
    }

    public void setGiftStatus(GiftStatus giftStatus) {
        this.giftStatus = giftStatus;
    }

    public GiftShareType getGiftShareType() {
        return giftShareType;
    }

    public void setGiftShareType(GiftShareType giftShareType) {
        this.giftShareType = giftShareType;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
