package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.cart.CartProductDto;
import com.jsj.member.ob.dto.api.cart.GetCartProductsRequ;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class CartLogicTest {

    @Test
    public void getcart() {
        CartLogic.GetCart("111");
    }

    @Test
    public void deleteCart() {
        CartLogic.DeleteCart("111");
    }

    @Test
    public void deleteCartProduct() {
        CartLogic.DeleteCartProduct(1);
    }

    @Test
    public void  saveAndUpdate(){
        CartLogic.AddUpdateCartProduct("3", "",2,4,1, "add");;
    }

    @Test
    public void getCartProduct(){
        GetCartProductsRequ requ = new GetCartProductsRequ();
        BaseRequ baseRequ = new BaseRequ();
        baseRequ.setOpenId("111");

        List<CartProductDto> cartProducts = new ArrayList<>();

        CartProductDto cartProductDto = new CartProductDto();
        cartProductDto.setCartId(1);
        cartProductDto.setCartProductId(1);
        cartProductDto.setNumber(10);
        cartProductDto.setProductSpecId(1);
        cartProductDto.setOpenId(baseRequ.getOpenId());
        cartProductDto.setProductId(1);

        cartProducts.add(cartProductDto);

        requ.setBaseRequ(baseRequ);

        CartLogic.GetCartProducts("111", "");

    }

}