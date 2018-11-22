package com.jsj.member.ob.logic;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.dict.GetAreasRequ;
import com.jsj.member.ob.dto.api.dict.GetAreasResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class DictLogicTest {

    /**
     * 获取省份
     */
    @Test
    public void getProvince() {

        GetAreasRequ requ = new GetAreasRequ();
        requ.setParentAreaId(0);

        GetAreasResp resp = DictLogic.GetAreas(requ);
        System.out.println(JSON.toJSONString(resp));


    }

    /**
     * 获取城市
     */
    @Test
    public void getCity() {

        GetAreasRequ requ = new GetAreasRequ();
        requ.setParentAreaId(110000);

        GetAreasResp resp = DictLogic.GetAreas(requ);
        System.out.println(JSON.toJSONString(resp));

        requ.setParentAreaId(130000);

        resp = DictLogic.GetAreas(requ);
        System.out.println(JSON.toJSONString(resp));

    }

    /**
     * 获取区域
     */
    @Test
    public void getArea() {

        GetAreasRequ requ = new GetAreasRequ();
        requ.setParentAreaId(210700);

        GetAreasResp resp = DictLogic.GetAreas(requ);
        System.out.println(JSON.toJSONString(resp));
    }

    @Test
    public void getCascade(){
        GetAreasResp getAreasResp = DictLogic.GetCascade(210700);
        System.out.println(getAreasResp);
    }

}