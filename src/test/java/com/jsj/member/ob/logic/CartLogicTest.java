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
        CartLogic.GetCartProduct("111");
    }

    @Test
    public void deleteCart() {
        CartLogic.DeleteCart("111");
    }

    @Test
    public void deleteProduct() {
        CartLogic.DeleteProduct("111");
    }

    @Test
    public void  saveAndUpdate(){
        CartLogic.SaveAndUpdate("111",1,2);
    }


}