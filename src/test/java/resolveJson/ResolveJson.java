package resolveJson;

import com.alibaba.fastjson.JSONObject;
import com.jsj.member.ob.App;
import com.jsj.member.ob.entity.Dict;
import com.jsj.member.ob.service.DictService;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ResolveJson {


    @Autowired
    DictService dictService;

    @Test
    public  void  resolveJson() throws  Exception {
        File jsonfile = ResourceUtils.getFile("classpath:Dict.json");  //对文件目录下的文件进行读取
        String jsonsting = FileUtils.readFileToString(jsonfile);      //读取到的文件转换成为String类型
        JSONObject array = JSONObject.parseObject(jsonsting);             //根据原本的类型转换为json数组

        Map<String, Object> innerMap = array.getInnerMap();

        for (String s : innerMap.keySet()) {
            System.out.println(s );
            System.out.println(innerMap.get(s));

            Dict dict = new Dict();
            dict.setDictType(s);
            dict.setDictName((String) innerMap.get(s));
            dict.setCreateTime(0);
            dict.setUpdateTime(0);
            dictService.insert(dict);
        }

    }


}
