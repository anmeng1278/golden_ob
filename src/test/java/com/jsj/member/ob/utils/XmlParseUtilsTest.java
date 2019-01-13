package com.jsj.member.ob.utils;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.dto.api.order.UserOrderProductsDto;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

public class XmlParseUtilsTest {

    @Test
    public void parseXml2List() throws Exception {

        String xml = "<products><product>\n" +
                "<productId>1</productId>\n" +
                "<specId>1</specId>\n" +
                "<productName>怀山堂山药粉(360g)</productName>\n" +
                "<specName>手机1</specName>\n" +
                "<salePrice>0.00</salePrice>\n" +
                "<number>2</number>\n" +
                "<imgPath>http://img.jsjinfo.cn/ce7dde78d05e43bd3403de7ee5157fbf</imgPath>\n" +
                "</product>\n" +
                "<product>\n" +
                "<productId>1</productId>\n" +
                "<specId>2</specId>\n" +
                "<productName>怀山堂山药粉(360g)</productName>\n" +
                "<specName>手机2</specName>\n" +
                "<salePrice>0.01</salePrice>\n" +
                "<number>2</number>\n" +
                "<imgPath>http://img.jsjinfo.cn/ce7dde78d05e43bd3403de7ee5157fbf</imgPath>\n" +
                "</product>\n" +
                "<product>\n" +
                "<productId>1</productId>\n" +
                "<specId>3</specId>\n" +
                "<productName>怀山堂山药粉(360g)</productName>\n" +
                "<specName>手机3</specName>\n" +
                "<salePrice>0.00</salePrice>\n" +
                "<number>2</number>\n" +
                "<imgPath>http://img.jsjinfo.cn/ce7dde78d05e43bd3403de7ee5157fbf</imgPath>\n" +
                "</product>\n" +
                "<product>\n" +
                "<productId>5</productId>\n" +
                "<specId>6</specId>\n" +
                "<productName>金色逸站通用券</productName>\n" +
                "<specName>次卡</specName>\n" +
                "<salePrice>39.80</salePrice>\n" +
                "<number>2</number>\n" +
                "<imgPath>http://img.jsjinfo.cn/901ae7a4b9e86d5fd310f76963bdf2a2</imgPath>\n" +
                "</product></products>\n";

        StringReader reader = new StringReader(xml);
        //initialize jaxb classes
        JAXBContext context = JAXBContext.newInstance(UserOrderProductsDto.class);
        Unmarshaller un = context.createUnmarshaller();
        //convert to desired object
        UserOrderProductsDto item = (UserOrderProductsDto)un.unmarshal(reader);

        List<UserOrderProductsDto.product> product = item.getProduct();
        System.out.println(JSON.toJSONString(product));

    }
}