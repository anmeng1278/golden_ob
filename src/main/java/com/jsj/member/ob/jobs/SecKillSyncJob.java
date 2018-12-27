package com.jsj.member.ob.jobs;


import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每晚0点执行查询物流信息任务，若已签收修改配送状态为已签收
 */
@Component
public class SecKillSyncJob {

    private static final Logger logger = LoggerFactory.getLogger(SecKillSyncJob.class);

    @Scheduled(cron = "0 0/1 * * * ?")
    public synchronized void start() {

        if (SpringContextUtils.getActiveProfile().equals("dev")) {
            return;
        }

        try {
            List<Integer> activityIds = ActivityLogic.RedisSync();
            if (!activityIds.isEmpty()) {
                logger.info(String.format("同步秒杀活动成功：%s", JSON.toJSONString(activityIds)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}