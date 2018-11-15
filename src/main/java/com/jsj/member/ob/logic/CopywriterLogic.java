package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Copywriter;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.CopywriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;

@Component
public class CopywriterLogic {

    public static CopywriterLogic copywriterLogic;

    @PostConstruct
    public void init() {
        copywriterLogic = this;
        copywriterLogic.copywriterService = this.copywriterService;
    }

    @Autowired
    CopywriterService copywriterService;

    /**
     * 获得有效的文案
     *
     * @return
     */
    public static List<Copywriter> GetCopywriter() {

        EntityWrapper<Copywriter> wrapper = new EntityWrapper<>();
        wrapper.where("ifpass = 1 and delete_time is null");
        wrapper.where("UNIX_TIMESTAMP() between begin_time and end_time");

        List<Copywriter> copywriters = copywriterLogic.copywriterService.selectList(wrapper);

        return copywriters;
    }


    public static Copywriter GetOneCopywriter() {

        //获取所有有效的文案
        List<Copywriter> copywriters = CopywriterLogic.GetCopywriter();
        if (copywriters.size() == 0) {
            throw new TipException("暂时没有可用的文案");
        }

        //随机分配一句文案
        Random random = new Random();
        int i = random.nextInt(copywriters.size());
        Copywriter copywriter = copywriters.get(i);

        return copywriter;
    }
}
