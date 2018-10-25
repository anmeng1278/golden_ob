package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductSpecDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ProductLogicTest {

    @Test
    public void getProductSpec() {

        ProductSpecDto productSpecDto = ProductLogic.GetProductSpec(2);
        System.out.println(JSON.toJSONString(productSpecDto));

    }
    @Test
    public void getProduct(){

        ProductDto dto = ProductLogic.GetProduct(3);
        System.out.println(JSON.toJSONString(dto));

    }
}