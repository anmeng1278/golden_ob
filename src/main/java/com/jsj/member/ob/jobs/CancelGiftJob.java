package com.jsj.member.ob.jobs;


import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.logic.GiftLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 每晚0点执行查询物流信息任务，若已签收修改配送状态为已签收
 */
@Component
public class CancelGiftJob {

    private static final Logger logger = LoggerFactory.getLogger(CancelGiftJob.class);

    @Scheduled(cron = "0 0/1 * * * ?")
    public synchronized void start() {
        try {
            List<Integer> giftIds = GiftLogic.CancelGift();
            if (!giftIds.isEmpty()) {
                logger.info(String.format("自动取消分享：%s", JSON.toJSONString(giftIds)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}