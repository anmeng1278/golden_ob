package com.jsj.member.ob.controller.admin;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.coupon.CouponDto;
import com.jsj.member.ob.dto.api.product.ProductDto;
import com.jsj.member.ob.entity.*;
import com.jsj.member.ob.enums.CouponType;
import com.jsj.member.ob.enums.CouponUseRange;
import com.jsj.member.ob.enums.RedpacketType;
import com.jsj.member.ob.logic.CouponLogic;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.CouponService;
import com.jsj.member.ob.service.ProductSpecService;
import com.jsj.member.ob.service.RedpacketCouponService;
import com.jsj.member.ob.service.RedpacketService;
import com.jsj.member.ob.utils.CCPage;
import com.jsj.member.ob.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@ApiIgnore
@Controller
@RequestMapping("/admin/redpacket")
public class AdminRedpacketController {

    @Autowired
    RedpacketService redpacketService;

    @Autowired
    RedpacketCouponService redpacketCouponService;

    /**
     * 礼券列表
     * @param page
     * @param limit
     * @param keys
     * @param typeId
     * @param model
     * @return
     */
    @GetMapping(value = {"", "/index"})
    public String index(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                        @RequestParam(value = "keys", defaultValue = "") String keys,
                        @RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                        Model model) {
        EntityWrapper<Redpacket> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(redpacket_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Redpacket> pageInfo = new Page<>(page, limit);
        Page<Redpacket> pp = redpacketService.selectPage(pageInfo, wrapper);

        //红包类型
        List<RedpacketType> redpacketTypes = Arrays.asList(RedpacketType.values());

        model.addAttribute("infos", new CCPage<Redpacket>(pp, limit));
        model.addAttribute("redpacketTypes", redpacketTypes);
        model.addAttribute("keys", keys);
        model.addAttribute("typeId", typeId);

        return "admin/redpacket/index";
    }


    /**
     * 查询信息页面
     *
     * @param redpacketId
     * @param model
     * @return
     */
    @GetMapping("/{redpacketId}")
    public String info(@PathVariable("redpacketId") Integer redpacketId, Model model) {

        Redpacket redpacket = new Redpacket();
        List<RedpacketCoupon> redpacketCoupons = new ArrayList<>();

        if (redpacketId > 0) {
            redpacket = redpacketService.selectById(redpacketId);

            EntityWrapper<RedpacketCoupon> entityWrapper = new EntityWrapper<>();
            entityWrapper.where("delete_time is null");
            entityWrapper.where("redpacket_id={0}", redpacketId);
            redpacketCoupons = redpacketCouponService.selectList(entityWrapper);

        } else {
            redpacket.setTypeId(RedpacketType.COUPONPACKAGE.getValue());
        }
        //红包类型
        List<RedpacketType> redpacketTypes = Arrays.asList(RedpacketType.values());

        model.addAttribute("redpacketTypes", redpacketTypes);
        model.addAttribute("info", redpacket);
        model.addAttribute("redpacketId", redpacketId);
        model.addAttribute("redpacketCoupons", redpacketCoupons);

        return "admin/redpacket/info";
    }


    /**
     * 修改或添加
     *
     * @param redpacketId
     * @param request
     * @return
     */
    @PostMapping("/{redpacketId}")
    @ResponseBody
    @Transactional(Constant.DBTRANSACTIONAL)
    public RestResponseBo saveOrUpdate(@PathVariable("redpacketId") Integer redpacketId, HttpServletRequest request) {

        Redpacket redpacket = new Redpacket();

        String redpacketName = request.getParameter("redpacketName");
        int typeId = Integer.parseInt(request.getParameter("typeId"));
        boolean ifpass = !StringUtils.isBlank(request.getParameter("ifpass"));
        int sort = Integer.parseInt(request.getParameter("sort"));

        if (redpacketId > 0) {
            //修改
            redpacket = redpacketService.selectById(redpacketId);
            redpacket.setRedpacketName(redpacketName);
            redpacket.setTypeId(typeId);
            redpacket.setIfpass(ifpass);
            redpacket.setSort(sort);
            redpacket.setUpdateTime(DateUtils.getCurrentUnixTime());
            redpacketService.updateById(redpacket);

        } else {
            //添加
            redpacket.setRedpacketName(redpacketName);
            redpacket.setTypeId(typeId);
            redpacket.setIfpass(ifpass);
            redpacket.setSort(sort);
            redpacket.setCreateTime(DateUtils.getCurrentUnixTime());
            redpacket.setUpdateTime(DateUtils.getCurrentUnixTime());
            redpacketService.insert(redpacket);
        }

        //先删除礼包中的优惠券
        List<RedpacketCoupon> redpacketCoupons = redpacketCouponService.selectList(new EntityWrapper<RedpacketCoupon>().where("redpacket_id={0} ", redpacket.getRedpacketId()));
        redpacketCoupons.forEach(ap -> {
            ap.setDeleteTime(DateUtils.getCurrentUnixTime());
            redpacketCouponService.updateById(ap);
        });

        //添加or更新优惠券
        redpacketId = redpacket.getRedpacketId();
        String[] numbers = request.getParameterValues("number");
        String[] couponIds = request.getParameterValues("couponIds");
        if(couponIds != null){
            for (int i=0;i<couponIds.length;i++){
                int couponId = Integer.parseInt(couponIds[i]);
                int number = Integer.parseInt(numbers[i]);
                RedpacketCoupon redpacketCoupon = redpacketCouponService.selectOne(new EntityWrapper<RedpacketCoupon>().where("coupon_id={0} and redpacket_id={1} ", couponId, redpacketId));

                //添加优惠券商品
                if (redpacketCoupon == null) {
                    redpacketCoupon = new RedpacketCoupon();
                    redpacketCoupon.setCouponId(couponId);
                    redpacketCoupon.setRedpacketId(redpacketId);
                    redpacketCoupon.setNumber(number);
                    redpacketCoupon.setCreateTime(DateUtils.getCurrentUnixTime());
                    redpacketCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());
                    redpacketCouponService.insert(redpacketCoupon);

                } else {
                    redpacketCoupon.setNumber(number);
                    redpacketCoupon.setUpdateTime(DateUtils.getCurrentUnixTime());
                    redpacketCoupon.setDeleteTime(null);
                    redpacketCouponService.updateAllColumnById(redpacketCoupon);
                }

            }
        }
        return RestResponseBo.ok("保存成功");
    }

    @Autowired
    CouponService couponService;
    /**
     * 选择优惠券商品页面
     *
     * @param request
     * @return
     */
    @GetMapping("/chooseCoupons")
    public String chooseCoupons(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "keys", defaultValue = "") String keys,
            HttpServletRequest request) {

        EntityWrapper<Coupon> wrapper = new EntityWrapper<>();

        wrapper.where("delete_time is null");
        wrapper.where(!StringUtils.isBlank(keys), "(coupon_name LIKE concat(concat('%',{0}),'%') )", keys);
        wrapper.orderBy("create_time desc");

        Page<Coupon> pageInfo = new Page<>(page, limit);
        Page<Coupon> pp = couponService.selectPage(pageInfo, wrapper);

        request.setAttribute("infos", new CCPage<Coupon>(pp, limit));
        request.setAttribute("keys", keys);

        return "admin/redpacket/chooseCoupons";
    }

    /**
     * 红包优惠券
     * @param request
     * @return
     */
    @PostMapping("/redpacketCoupons")
    @ResponseBody
    public RestResponseBo redpacketCoupons(HttpServletRequest request) {

        String couponIds = request.getParameter("couponIds");
        if (!StringUtils.isBlank(couponIds)) {

            List<Integer> couponIdLists = Arrays.asList(couponIds.split(",")).stream().map(Integer::parseInt).collect(Collectors.toList());

            EntityWrapper entityWrapper = new EntityWrapper<Coupon>();
            entityWrapper.where("delete_time is null");
            entityWrapper.in("coupon_id", couponIdLists);

            List<Coupon> coupons = couponService.selectList(entityWrapper);
            List<HashMap<String, Object>> maps = new ArrayList<>();
            for (Coupon coupon : coupons) {

                HashMap<String, Object> map = new HashMap<>();

                CouponDto couponDto = CouponLogic.GetCoupon(coupon.getCouponId());

                map.put("couponId", couponDto.getCouponId());
                map.put("couponName", couponDto.getCouponName());
                map.put("amount", couponDto.getAmount());
                map.put("validDays",couponDto.getValidDays());
                map.put("userRange",couponDto.getCouponUseRange().getMessage());
                map.put("typeId",couponDto.getCouponType().getMessage());
                map.put("number",1);
                maps.add(map);
            }

            return RestResponseBo.ok(maps);

        }

        return RestResponseBo.ok();
    }




    /**
     * 修改状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseBo status(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");

        Redpacket redpacket = redpacketService.selectById(id);

        if (method.equals("ifpass")) {
            redpacket.setIfpass(!redpacket.getIfpass());
            redpacket.setUpdateTime(DateUtils.getCurrentUnixTime());
            redpacketService.updateById(redpacket);
        }
        if (method.equals("delete")) {
            redpacket.setUpdateTime(DateUtils.getCurrentUnixTime());
            redpacket.setDeleteTime(DateUtils.getCurrentUnixTime());
            redpacketService.updateById(redpacket);
        }

        return RestResponseBo.ok("操作成功");

    }


}
