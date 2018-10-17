package com.jsj.member.ob.logic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jsj.member.ob.dto.api.product.Product;
import com.jsj.member.ob.dto.api.stock.GetMyStockRequ;
import com.jsj.member.ob.dto.api.stock.GetMyStockResp;
import com.jsj.member.ob.entity.Stock;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 获取我的库存
 */
@Component
public class StockLogic {

    public static StockLogic stockLogic;

    @PostConstruct
    public void init() {
        stockLogic = this;
        stockLogic.stockService = this.stockService;
        stockLogic.orderService = this.orderService;
        stockLogic.orderProductService = this.orderProductService;

    }

    @Autowired
    StockService stockService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderProductService orderProductService;


    /**
     * 获取我的库存
     * @param requ
     * @return
     */
    public static GetMyStockResp GetMyStock(GetMyStockRequ requ){
        //查找（支付成功）订单-赠送和待领取的商品+自己领取的

        if(StringUtils.isBlank(requ.getBaseRequ().getOpenId())){
            throw new TipException("参数不合法，用户openId为空");
        }
        //查询用户所有支付成功的订单
        /*EntityWrapper<Order> orderWrapper = new EntityWrapper<>();
        Wrapper<Order> orderWhere = orderWrapper.where("open_id and status = 10 and delete_time is null", openId);
        List<Order> orderList = stockLogic.orderService.selectList(orderWhere);

        for (Order order : orderList) {
        //获取所有订单商品
            List<OrderProduct> orderProductList = stockLogic.orderProductService.selectList(new EntityWrapper<OrderProduct>().where("order_id={0}", order.getOrderId()));
            for (OrderProduct orderProduct : orderProductList) {
            //计算每个商品总量
                int productAmount = stockLogic.orderProductService.selectCount(new EntityWrapper<OrderProduct>().where("product_id={0} and status in (0,10,11) and number={1}", orderProduct.getProductId(),orderProduct.getNumber()));
                //计算每个商品赠送的数量
                int givenAmount = stockLogic.stockService.selectCount(new EntityWrapper<Stock>().where("product_id=={0} and git_stock_id
            }
        }*/
        GetMyStockResp resp = new GetMyStockResp();

        EntityWrapper<Stock> stockWrapper = new EntityWrapper<>();
        stockWrapper.where("open_id={0} and status in(0,11) and delete_time is null",requ.getBaseRequ().getOpenId());
       //查询该用户下所有库存
        List<Stock> stockList = stockLogic.stockService.selectList(stockWrapper);
        if(stockList.size() == 0){
            return resp;
        }
        EntityWrapper<Stock> productWrapper = new EntityWrapper<>();
        for (Stock stock : stockList) {
            //获取库存中商品信息
            Wrapper<Stock> where = productWrapper.where("product_id={0}", stock.getProductId());
            //计算库存中每样商品的数量
            int number = stockLogic.stockService.selectCount(where);
            Product product = ProductLogic.GetProduct(stock.getProductId());
            resp.setOpenId(requ.getBaseRequ().getOpenId());
            resp.setOrderId(stock.getOrderId());
            resp.setProductId(stock.getProductId());
            resp.setStockId(stock.getStockId());
            resp.setNumber(number);
        }
        return resp;
    }

    public void GetMyGiven(String openId){
        if(StringUtils.isBlank(openId)){
            throw new TipException("参数不合法，用户openId空");
        }
        //库存中gift_stock_id 查询出stock_id为我所赠送出去的
        //
        //


    }
}
