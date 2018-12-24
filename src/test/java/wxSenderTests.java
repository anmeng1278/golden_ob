import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.logic.ConfigLogic;
import com.jsj.member.ob.logic.ThirdPartyLogic;
import com.jsj.member.ob.rabbitmq.wx.TemplateData;
import com.jsj.member.ob.rabbitmq.wx.TemplateDto;
import com.jsj.member.ob.rabbitmq.wx.WxSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class wxSenderTests {

    @Autowired
    WxSender wxSender;

    @Test
    public void SendService() {

        TemplateDto dto = TemplateDto.NewCustomService("o2JcesxmAIQWeqEEqA-vM-i44Miw",
                "你的礼物被人领取了，点击查看<a href='http://h5.ktgj.com/ob/stock'>库存</a>");
        wxSender.sendNormal(dto);

    }

    @Test
    public void SendTemplate() {

        String templateId = "LaqHCNTGO-SeDzdpZqPxjgx427whX2kuIOBCQrGp2J4";

        TemplateDto dto = new TemplateDto();
        dto.setFirst("系统异常");
        dto.setFirstColor("#173177");
        dto.getData().put("keyword1", new TemplateData("keyword1", ""));
        dto.getData().put("keyword2", new TemplateData("keyword2", ""));
        dto.getData().put("keyword3", new TemplateData("keyword3", ""));
        dto.setRemark("remark");
        dto.setRemarkColor("#cccccc");

        dto.setToUser("o2JcesxmAIQWeqEEqA-vM-i44Miw");
        //dto.setToUser("oeQDZt-rcgi9QhWm6F7o2mV3dSYY");
        dto.setTemplateId(templateId);

        ThirdPartyLogic.SendWxTemplate(dto);

        System.out.println(JSON.toJSONString(dto));

    }

    @Test
    public void SendService2(){

        ThirdPartyLogic.SendWxMessage("o2JcesxmAIQWeqEEqA-vM-i44Miw", "hello world");
    }
    /**
     * 支付成功模板
     */
    @Value(value = "${webconfig.WxTemplate.PaySuccessed}")
    private String PaySucccessedTemplateId;

    @Test
    public void ss(){
        System.out.println(PaySucccessedTemplateId);
        System.out.println(ConfigLogic.GetWebConfig().getHost());
    }

}
