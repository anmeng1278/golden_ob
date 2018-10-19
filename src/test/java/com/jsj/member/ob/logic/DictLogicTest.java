package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.dict.DictDto;
import com.jsj.member.ob.dto.api.dict.DictResp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class DictLogicTest {

    @Test
    public void getArea() {
        DictResp dictResp = DictLogic.GetArea(130100);
        List<DictDto> dictDtoList = dictResp.getDictDtoList();
        for (DictDto dictDto : dictDtoList) {
            System.out.println(dictDto.getDictId()+"  "+dictDto.getDictName());
        }
    }

    @Test
    public void getProvince(){
        DictResp dictResp = DictLogic.GetProvince(0);
        List<DictDto> dictDtoList = dictResp.getDictDtoList();
        for (DictDto dictDto : dictDtoList) {
            System.out.println(dictDto.getDictId()+"  "+dictDto.getDictName());
        }
    }

}