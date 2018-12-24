import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import org.junit.Test;

public class wxDataTests {

    @Test
    public void test() {
        TemplateDto dto = TemplateDto.NewCustomService("oeQDZt-rcgi9QhWm6F7o2mV3dSYY",
                "你的礼物被人领取了，点击查看<a href='http://h5.ktgj.com/ob/stock'>库存</a>");

        System.out.println(JSON.toJSONString(dto));
        System.out.println(dto.getRemark());
        System.out.println(dto.getToUser());
    }
}
