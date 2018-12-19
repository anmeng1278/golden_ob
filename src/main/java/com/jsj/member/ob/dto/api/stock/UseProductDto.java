package com.jsj.member.ob.dto.api.stock;

/**
 * 库存使用时URL上传递参数
 */
public class UseProductDto {

    /**
     * 商品编号
     */
    private int pId;

    /**
     * 规格编号
     */
    private int sId;

    /**
     * 使用数量
     */
    private int num;

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public int getsId() {
        return sId;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
