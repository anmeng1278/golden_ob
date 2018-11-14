package com.jsj.member.ob.logic;

import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class ActivityLogicTest {

    @Test
    public void getShowActivity() {
        List<ActivityDto> activityDtos = ActivityLogic.GetShowActivity();
        for (ActivityDto activityDto : activityDtos) {
            System.out.println(activityDto);
        }
    }
}