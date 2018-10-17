package com.jsj.member.ob.dto.api.stock;

import com.jsj.member.ob.dto.BaseRequ;

public class GetMyStockRequ {
    public GetMyStockRequ() {
        this.baseRequ = new BaseRequ();
    }

    private BaseRequ baseRequ;

    public BaseRequ getBaseRequ() {
        return baseRequ;
    }

    public void setBaseRequ(BaseRequ baseRequ) {
        this.baseRequ = baseRequ;
    }
}
