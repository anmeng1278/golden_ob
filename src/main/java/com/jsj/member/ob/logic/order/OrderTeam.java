package com.jsj.member.ob.logic.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.constant.Constant;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProduct;
import com.jsj.member.ob.dto.api.product.Product;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.entity.Team;
import com.jsj.member.ob.entity.TeamOrder;
import com.jsj.member.ob.entity.TeamProduct;
import com.jsj.member.ob.enums.OrderStatus;
import com.jsj.member.ob.enums.OrderType;
import com.jsj.member.ob.enums.TeamOrderStatus;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.ProductLogic;
import com.jsj.member.ob.service.*;
import com.jsj.member.ob.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀单
 */
@Component
public class OrderTeam extends OrderBase {
    public OrderTeam() {
        super(OrderType.TEAM);
    }


    @Autowired
    public void initService(OrderService orderService, OrderProductService orderProductService) {
        super.orderService = orderService;
        super.orderProductService = orderProductService;
    }


    @Autowired
    TeamOrderService teamOrderService;

    @Autowired
    TeamProductService teamProductService;

    @Autowired
    TeamService teamService;

    /**
     * 创建团单,应该在组团完成后自动创建
     *
     * @param requ
     * @return
     */
    @Override
    @Transactional(Constant.DBTRANSACTIONAL)
    public CreateOrderResp CreateOrder(CreateOrderRequ requ) {

        if (requ.getTeamOrderId() == 0) {
            throw new TipException("组团单号不能为空");
        }
        if (requ.getWechatCouponId() > 0) {
            throw new TipException("组团订单暂不支持优惠券");
        }

        CreateOrderResp resp = new CreateOrderResp();

        //获取团单
        TeamOrder teamOrder = teamOrderService.selectById(requ.getTeamOrderId());
        if (teamOrder.getStatus() != TeamOrderStatus.SUCCESS.getValue()) {
            throw new TipException(String.format("当前团单状态不允许创建订单，%s", TeamOrderStatus.valueOf(teamOrder.getStatus()).getMessage()));
        }

        //判断团单过期时间
        if (teamOrder.getExpireTime() < DateUtils.getCurrentUnixTime()) {

            teamOrder.setRemarks("团单已过期，组团失败");
            teamOrder.setUpdateTime(DateUtils.getCurrentUnixTime());
            teamOrder.setStatus(TeamOrderStatus.FAIL.getValue());
            teamOrderService.updateById(teamOrder);

            resp.setMessage("团单已过期，组团失败");
            return resp;
        }

        //获取组团信息
        Team team = teamService.selectById(teamOrder.getTeamId());
        if (team.getDeleteTime() != null || team.getIfpass() == false) {
            //组团信息已删除

            teamOrder.setRemarks("组团信息已删除或未通过审核");
            teamOrder.setUpdateTime(DateUtils.getCurrentUnixTime());
            teamOrder.setStatus(TeamOrderStatus.FAIL.getValue());
            teamOrderService.updateById(teamOrder);

            resp.setMessage("组团信息已删除或未通过审核，组团失败");
            return resp;
        }

        //获取团单商品
        List<TeamProduct> teamProducts = teamProductService.selectList(new EntityWrapper<TeamProduct>().where("team_id={0} and delete_time is null",
                teamOrder.getTeamId()));

        if(teamProducts.size() == 0){
            resp.setMessage("组团信息下没有商品，组团失败");
            return resp;
        }

        List<TeamOrder> teamOrders = teamOrderService.selectList(new EntityWrapper<TeamOrder>().where("team_order_id={0} or parent_team_order_id={0}", requ.getTeamOrderId()));
        for (TeamOrder to : teamOrders) {

            //组织订单实体
            Order order = new Order();

            order.setOpenId(to.getOpenId());
            order.setTypeId(this.getOrderType().getValue());
            order.setRemarks(requ.getRemarks());
            order.setTeamOrderId(requ.getTeamOrderId());

            order.setStatus(OrderStatus.UNPAY.getValue());
            order.setCreateTime(DateUtils.getCurrentUnixTime());
            order.setUpdateTime(DateUtils.getCurrentUnixTime());
            order.setExpiredTime(DateUtils.getCurrentUnixTime() + Constant.ORDER_EXPIRED_TIME);
            order.setOrderNumber("");

            //商品订单列表，用于保存商品订单
            List<com.jsj.member.ob.entity.OrderProduct> orderProducts = new ArrayList<>();

            //商品订单列表，用于消减商品库存
            List<OrderProduct> orderProducts1 = new ArrayList<>();

            for (TeamProduct tpt : teamProducts) {

                //获取商品信息
                Product product = ProductLogic.GetProduct(tpt.getProductId());

                //判断库存
                if (product.getStockCount() <= 0) {

                    teamOrder.setRemarks(String.format("库存不足，下单失败，商品名称：%s", product.getProductName()));
                    teamOrder.setUpdateTime(DateUtils.getCurrentUnixTime());
                    teamOrder.setStatus(TeamOrderStatus.FAIL.getValue());
                    teamOrderService.updateById(teamOrder);

                    resp.setMessage(String.format("库存不足，下单失败，商品名称：%s", product.getProductName()));
                    return resp;

                }

                //创建商品订单
                com.jsj.member.ob.entity.OrderProduct orderProduct = new com.jsj.member.ob.entity.OrderProduct();

                orderProduct.setNumber(1);
                orderProduct.setOrderProductId(tpt.getProductId());
                orderProduct.setProductSizeId(tpt.getProductSizeId());
                orderProduct.setCreateTime(DateUtils.getCurrentUnixTime());
                orderProduct.setUpdateTime(DateUtils.getCurrentUnixTime());

                orderProduct.setProductId(tpt.getProductId());
                orderProducts.add(orderProduct);


                //组织使用数据,用于消减库存
                OrderProduct orderProduct1 = new OrderProduct();
                orderProduct1.setNumber(1);
                orderProduct1.setProductSizeId(tpt.getProductSizeId());
                orderProduct1.setProductId(tpt.getProductId());

                orderProducts1.add(orderProduct1);

            }

            //订单金额
            order.setAmount(team.getSalePrice());
            order.setPayAmount(team.getSalePrice());

            orderService.insert(order);

            order.setOrderNumber((10000 + order.getOrderId()) + "");
            orderService.updateById(order);

            //更新订单商品中的订单编号
            orderProducts.stream().forEach(op -> {
                op.setOrderId(order.getOrderId());
                orderProductService.insert(op);
            });

            //削减库存
            ProductLogic.ReductionProductStock(orderProducts1);

        }

        //更新团单状态
        teamOrders.stream().forEach(to -> {
            to.setStatus(TeamOrderStatus.ORDERSUCCESS.getValue());
            to.setUpdateTime(DateUtils.getCurrentUnixTime());
            teamOrderService.updateById(to);
        });

        resp.setSuccess(true);
        return resp;

    }
}
