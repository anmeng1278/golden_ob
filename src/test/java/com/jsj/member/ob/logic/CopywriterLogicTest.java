package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.entity.Copywriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class CopywriterLogicTest {

    @Test
    public void getCopywriter() {
        List<Copywriter> copywriters = CopywriterLogic.GetCopywriter(1);
        for (Copywriter copywriter : copywriters) {
            copywriters.forEach(s->s.getRemark());
        }
    }

    @Test
    public void getOneCopywriter(){
        Copywriter copywriter = CopywriterLogic.GetOneCopywriter(1);
        System.out.println(copywriter.getRemark());
    }
}