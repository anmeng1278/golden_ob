package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class CartLogicTest {

    @Test
    public void getcart() {
        CartLogic.GetCart("111");
    }
    @Test
    public void getProduct() {
        CartLogic.GetCartProduct("111",10);
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
        CartLogic.AddUpdateCartProduct("3",2,4,1);;
    }


}