package com.jsj.member.ob.dto.api.cart;

import com.jsj.member.ob.dto.BaseRequ;

import java.util.ArrayList;
import java.util.List;

public class GetCartProductsRequ {

    public  GetCartProductsRequ(){
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
