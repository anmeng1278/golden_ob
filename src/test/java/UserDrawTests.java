import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.dto.api.gift.UserDrawDto;
import com.jsj.member.ob.service.GiftService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;
import java.util.List;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = App.class)
public class UserDrawTests {

    @Autowired
    GiftService giftService;

    @Test
    public void SendService() {

        List<UserDrawDto> userDraws = giftService.getUserDraws("oeQDZt-rcgi9QhWm6F7o2mV3dSYY");
        System.out.println(JSON.toJSONString(userDraws));
    }

    @Test
    public void SendTemplate() throws MalformedURLException {

        String url = "https://www.baidu.com?a=1&b=2";

        if (!url.startsWith("https")) {
            url = "https" + url.substring(url.indexOf(":"));
        }

        System.out.println(url);

    }


}
