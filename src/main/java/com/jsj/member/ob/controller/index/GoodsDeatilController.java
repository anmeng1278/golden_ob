package com.jsj.member.ob.controller.index;

import com.jsj.member.ob.dto.api.activity.ActivityDto;
import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.dto.api.product.ProductImgDto;
import com.jsj.member.ob.logic.ActivityLogic;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.ProductLogic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goodsDetail")
public class GoodsDeatilController {

    /**
     * 商品详情
     * @param model
     * @param productId
     * @param openId
     * @return
     */
    @GetMapping("/goodsDetail")
    public String getGoodsDeatil(@RequestParam(value = "productId", defaultValue = "0") Integer productId,
                                 @RequestParam(value = "openId", defaultValue = "111") String openId,
                                 @RequestParam(value = "activityId", defaultValue = "0") Integer activityId,Model model){

            //商品详情
        ProductDto productDto = ProductLogic.GetProduct(productId);

            //商品的图片
        List<ProductImgDto> productImgDtos = ProductLogic.GetProductImgDtos(productId);

        //活动的信息
        ActivityDto activityDto = new ActivityDto();
        if(activityId > 0){
            activityDto= ActivityLogic.GetActivity(activityId);
        }


        //用户要购买的商品的可用的优惠券
        List<CouponDto> couponDtos = new ArrayList<>();
        if(!StringUtils.isBlank(openId)){
            couponDtos = CouponLogic.GetUserProductCoupon(productId, openId);
        }


        model.addAttribute("activityDto",activityDto);
        model.addAttribute("activityId",activityId);
        model.addAttribute("productId",productId);
        model.addAttribute("productDto",productDto);
        model.addAttribute("productImgDtos",productImgDtos);
        model.addAttribute("couponDtos",couponDtos);
        model.addAttribute("openId",openId);

        return "index/goodsDetail/GoodsDetail";
    }


    /**
     * 商品详情
     * @param model
     * @param openId
     * @return
     */
    @GetMapping("/joinDetail")
    public String getJoinDetail(@RequestParam(value = "openId", defaultValue = "111") String openId,
                                 @RequestParam(value = "activityId", defaultValue = "0") Integer activityId,Model model){

        model.addAttribute("activityId",activityId);
        model.addAttribute("openId",openId);

        return "index/goodsDetail/GoodsOfJoint";
    }

}
