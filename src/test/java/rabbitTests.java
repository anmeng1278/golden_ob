import com.jsj.member.ob.App;
import com.jsj.member.ob.rabbitmq.MQSender;
import com.jsj.member.ob.rabbitmq.seckill.SecKillConfig;
import com.jsj.member.ob.rabbitmq.seckill.SecKillDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class rabbitTests {

    @Autowired
    MQSender mqSender;

    @PostConstruct
    public void init() {
        mqSender.setNormalQueue(SecKillConfig.SECKILL_NORMAL_QUEUE);
        mqSender.setErrorQueue(SecKillConfig.SECKILL_ERROR_QUEUE);
    }

    @Test
    public void Sender() {

        SecKillDto dto = new SecKillDto();
        dto.setOpenId("123123");
        dto.setActivityId(1);

        mqSender.sendNormal(dto);

    }

}
