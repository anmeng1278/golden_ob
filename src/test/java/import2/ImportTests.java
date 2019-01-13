package import2;

import com.alibaba.fastjson.JSON;
import com.jsj.member.ob.App;
import com.jsj.member.ob.dto.BaseRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderRequ;
import com.jsj.member.ob.dto.api.order.CreateOrderResp;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.proto.NotifyModelOuterClass;
import com.jsj.member.ob.enums.ActivityType;
import com.jsj.member.ob.enums.SourceType;
import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.logic.OrderLogic;
import com.jsj.member.ob.logic.order.OrderBase;
import com.jsj.member.ob.logic.order.OrderFactory;
import com.jsj.member.ob.tuple.ThreeTuple;
import com.jsj.member.ob.tuple.TwoTuple;
import com.jsj.member.ob.utils.TupleUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class ImportTests {

    /**
     * 注意：
     * 1.不发支付成功通知
     * 2.不生成红包
     * 3.删除订单
     */

    @Test
    public void Test() {

        List<String> strings = ReadFromFile.readFileByLines("D:\\async4d\\dev\\java\\tools\\jsj\\member-ob\\src\\test\\java\\import2\\datas");
        //System.out.println(strings);
        List<ThreeTuple<Integer, String, Integer>> lists = new ArrayList<>();

        for (String s : strings) {

            String[] split = s.split("\t");

            int productId = Integer.parseInt(split[0]);
            String openId = split[1];
            int count = Integer.parseInt(split[2]);

            ThreeTuple<Integer, String, Integer> tuple = TupleUtils.tuple(productId, openId, count);
            lists.add(tuple);

        }

        System.out.println(JSON.toJSONString(lists));

        for (ThreeTuple<Integer, String, Integer> threeTuple : lists) {

            CreateOrderRequ requ = new CreateOrderRequ();

            BaseRequ baseRequ = new BaseRequ();
            baseRequ.setOpenId(threeTuple.second);

            requ.setBaseRequ(baseRequ);
            requ.setRemarks("数据导入");
            requ.setActivityType(ActivityType.NORMAL);

            List<OrderProductDto> orderProductDtos = new ArrayList<>();

            OrderProductDto orderProductDto1 = new OrderProductDto();
            orderProductDto1.setNumber(threeTuple.third);

            TwoTuple<Integer, Integer> integerIntegerTwoTuple = ConvertProduct(threeTuple.first);

            orderProductDto1.setProductId(integerIntegerTwoTuple.first);
            orderProductDto1.setProductSpecId(integerIntegerTwoTuple.second);

            orderProductDtos.add(orderProductDto1);
            requ.setOrderProductDtos(orderProductDtos);

            requ.setSourceType(SourceType.AWKTC);

            CreateOrderResp createOrderResp = OrderLogic.CreateOrder(requ);
            System.out.println(JSON.toJSONString(createOrderResp));

            if (!createOrderResp.isSuccess()) {
                throw new TipException(createOrderResp.getMessage());
            }

            NotifyModelOuterClass.NotifyModel notifyModel = NotifyModelOuterClass.NotifyModel.getDefaultInstance();

            int orderId = createOrderResp.getOrderId();

            OrderBase orderBase = OrderFactory.GetInstance(orderId);
            orderBase.PaySuccessed(orderId, notifyModel);

            break;
        }

    }

    public TwoTuple<Integer, Integer> ConvertProduct(int productId) {

        switch (productId) {
            case 1:
                return TupleUtils.tuple(5, 6);
            case 114:
                return TupleUtils.tuple(2, 22);
            case 116:
                return TupleUtils.tuple(3, 44);
            case 119:
                return TupleUtils.tuple(14, 26);
            case 130:
                return TupleUtils.tuple(7, 19);
            case 131:
                return TupleUtils.tuple(8, 23);
            case 140:
                return TupleUtils.tuple(10, 12);
            case 141:
                return TupleUtils.tuple(1, 17);
            case 151:
                return TupleUtils.tuple(9, 11);
            case 170:
                return TupleUtils.tuple(11, 20);
            case 2:
                return TupleUtils.tuple(13, 25);
            case 3:
                return TupleUtils.tuple(15, 28);

            default:
                throw new TipException("没有找到配置");
        }

        //1	金色逸站通用券	5,6
        //114	花下秀洗护套装	2,22
        //116	怀山堂即食山药	3,44
        //118	素园辽参 大隐
        //119	越甲科技20寸旅行箱	14,26
        //130	素园辽参 往来	7,19
        //131	素园辽参 问策	8,23
        //140	素园辽参 参迷	10,12
        //141	怀山堂山药粉(360g)	1,17
        //150	商旅管家·逸站通卡
        //151	商旅管家·全国通卡	9,11
        //170	暖妈山茶油（450mlx2）	11,20
        //2	超级空客·月体验	13,25
        //3	怀山堂山药粉(480g)	15,28
    }

}
