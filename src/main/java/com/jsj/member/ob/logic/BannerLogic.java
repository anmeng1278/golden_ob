package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.activity.ActivityProductDto;
import com.jsj.member.ob.entity.Activity;
import com.jsj.member.ob.entity.ActivityProduct;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.exception.ActivityStockException;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.ActivityProductService;
import com.jsj.member.ob.service.ActivityService;
import com.jsj.member.ob.service.BannerService;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.jsj.member.ob.logic.ActivityLogic.activityLogic;

@Component
public class BannerLogic {

    public static BannerLogic bannerLogic;

    @Autowired
    BannerService bannerService;


    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        bannerLogic = this;
        bannerLogic.bannerService = this.bannerService;
    }


    /**
     * 更新轮播图排序
     *
     * @param bannerId
     * @param ifUp
     */
    public static void Sort(int bannerId, Boolean ifUp) {

        Banner current = bannerLogic.bannerService.selectById(bannerId);
        int typeId = current.getTypeId();

        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        wrapper.where("type_id={0}", typeId);
        wrapper.orderBy("sort asc, update_time desc");
        List<Banner> banners = bannerLogic.bannerService.selectList(wrapper);

        //重置所有排序
        int sort = 0;
        for (Banner banner : banners) {
            banner.setSort(sort);
            bannerLogic.bannerService.updateById(banner);
            sort++;
        }

        if (ifUp != null) {
            //向上
            if (ifUp) {

                current = banners.stream().filter(p -> p.getBannerId() == bannerId).findFirst().get();
                if (current.getSort() == 0) {
                    return;
                }
                int currentSort = current.getSort();
                Banner prev = banners.stream().filter(p -> p.getSort() == (currentSort - 1)).findFirst().get();
                prev.setSort(prev.getSort() + 1);

                current.setSort(current.getSort() - 1);

                bannerLogic.bannerService.updateById(current);
                bannerLogic.bannerService.updateById(prev);

            } else {
                //向下移动
                current = banners.stream().filter(p -> p.getBannerId() == bannerId).findFirst().get();
                if (current.getSort() == banners.size() - 1) {
                    return;
                }
                int currentSort = current.getSort();
                Banner next = banners.stream().filter(p -> p.getSort() == (currentSort + 1)).findFirst().get();
                next.setSort(next.getSort() - 1);

                current.setSort(current.getSort() + 1);

                bannerLogic.bannerService.updateById(current);
                bannerLogic.bannerService.updateById(next);
            }
        }


    }

}
