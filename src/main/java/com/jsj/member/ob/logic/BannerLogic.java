package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class BannerLogic extends BaseLogic {

    public static BannerLogic bannerLogic;

    @Autowired
    BannerService bannerService;


    // 初始化的时候，将本类中的sysConfigManager赋值给静态的本类变量
    @PostConstruct
    public void init() {
        bannerLogic = this;
    }


    /**
     * 更新轮播图排序
     *
     * @param bannerId
     * @param ifUp
     */
    public static void Sort(int bannerId, Boolean ifUp) {

        Banner current = bannerLogic.bannerService.selectById(bannerId);

        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
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

    /**
     * 获得banner图
     * @param typeId
     * @return
     */
    public static List<Banner> GetBanner(int typeId){
        EntityWrapper<Banner> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and type_id={0}",typeId);
        List<Banner> banners = bannerLogic.bannerService.selectList(wrapper);
        return banners;
    }

}
