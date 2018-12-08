package com.jsj.member.ob.controller.index;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.delivery.DeliveryDto;
import com.jsj.member.ob.dto.api.gift.GiftDto;
import com.jsj.member.ob.dto.api.order.OrderDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Banner;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.enums.BannerType;
import com.jsj.member.ob.logic.DeliveryLogic;
import com.jsj.member.ob.logic.GiftLogic;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.StockLogic;
import com.jsj.member.ob.service.BannerService;
import com.jsj.member.ob.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/myStock")
public class MyStockController {

    @Autowired
    BannerService bannerService;

    @Autowired
    GiftService giftService;

    /**
     * 获得用户的库存
     * @param model
     * @param openId
     * @return
     */
    @GetMapping("/myStock")
    public String myStock(Model model, @RequestParam(value = "openId", defaultValue = "111") String openId){

        //首页轮播图
        EntityWrapper<Banner> bannerWrapper = new EntityWrapper<>();
        bannerWrapper.where("delete_time is null and ifpass is true and type_id={0}", BannerType.COVER.getValue());
        List<Banner> banners = bannerService.selectList(bannerWrapper);

        HashSet<StockDto> stockDtos = StockLogic.GetMyStock(openId);

        model.addAttribute("banners",banners);
        model.addAttribute("openId",openId);
        model.addAttribute("stockDtos",stockDtos);
        return "index/myStock/MyStock";

    }

    /**
     * 获得用户订单记录
     * @param openId
     * @param model
     * @return
     */
    @GetMapping("/orderRecord")
    public String getMyOrder(@RequestParam(value = "openId", defaultValue = "111") String openId,Model model){

        List<OrderDto> orderDtos = OrderLogic.GetMyOrder(openId);

        model.addAttribute("orderDtos",orderDtos);
        model.addAttribute("openId",openId);

        return "index/order/OrderList";
    }

    /**
     * 获得用户配送记录
     * @param openId
     * @param model
     * @return
     */
    @GetMapping("/delivery")
    public String getMyDelivery(@RequestParam(value = "openId", defaultValue = "111") String openId,Model model){

        List<DeliveryDto> deliveryDtos = DeliveryLogic.GetMyDelivery(openId);
        model.addAttribute("deliveryDtos",deliveryDtos);
        model.addAttribute("openId",openId);

        return "index/delivery/DeliveryList";
    }

    /**
     * 用户的赠送记录
     * @param openId
     * @param model
     * @return
     */
    public String getMyGive(@RequestParam(value = "openId", defaultValue = "111") String openId,Model model){


        //用户所有赠送的
        EntityWrapper<Gift> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null and open_id={0}",openId);
        List<Gift> gifts = giftService.selectList(wrapper);
        List<GiftDto> giftDtos = new ArrayList<>();
        for (Gift gift : gifts) {
            GiftDto giftDto = GiftLogic.GetGift(gift.getGiftId());
            giftDtos.add(giftDto);
        }



        //用户所有领取的

        model.addAttribute("giftDtos",giftDtos);
        model.addAttribute("openId",openId);

        return "index/share/GiveList";
    }
}
