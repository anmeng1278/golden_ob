package com.jsj.member.ob.controller.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.jsj.member.ob.dto.api.order.OrderProductDto;
import com.jsj.member.ob.dto.api.stock.StockDto;
import com.jsj.member.ob.entity.Delivery;
import com.jsj.member.ob.entity.Gift;
import com.jsj.member.ob.entity.Order;
import com.jsj.member.ob.enums.*;
import com.jsj.member.ob.logic.*;
import com.jsj.member.ob.service.DeliveryService;
import com.jsj.member.ob.service.GiftService;
import com.jsj.member.ob.service.OrderProductService;
import com.jsj.member.ob.service.OrderService;
import com.jsj.member.ob.utils.DateUtils;
import com.jsj.member.ob.utils.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.jsj.member.ob.logic.DictLogic.GetDict;

@RestController
@RequestMapping("/excel")
public class ExportExcelController {

    @Autowired
    DeliveryService deliveryService;


    @Autowired
    OrderService orderService;

    @Autowired
    GiftService giftService;

    @Autowired
    OrderProductService orderProductService;

    @RequestMapping("/exportDelivery")
    public void exportDelivery(@RequestParam(value = "typeId", defaultValue = "0") Integer typeId,
                               @RequestParam(value = "keys", defaultValue = "") String keys,
                               @RequestParam(value = "status", defaultValue = "-1") Integer status,
                               HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("配送信息表");
        String[] headers = {"用户openId", "用户昵称", "快递号", "手机号", "联系人", "省", "市", "区", "详细地址", "创建时间", "商品属性", "配送方式", "物流状态", "商品名称", "规格", "数量"};

        EntityWrapper<Delivery> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");

        if (typeId > 0) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status >= 0) {
            wrapper.where("status={0}", status);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(express_number like concat(concat('%',{0}),'%') or mobile like concat(concat('%',{0}),'%') or contact_name like concat(concat('%',{0}),'%') or open_id like concat(concat('%',{0}),'%')  )", keys);

        wrapper.orderBy("create_time desc");

        List<Delivery> deliveries = deliveryService.selectList(wrapper);

        int rowNum = 1;
        HSSFRow row = null;
        for (Delivery delivery : deliveries) {
            row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(delivery.getOpenId());
            row.createCell(1).setCellValue(WechatLogic.GetWechat(delivery.getOpenId()).getNickname());
            row.createCell(2).setCellValue(delivery.getExpressNumber());
            row.createCell(3).setCellValue(delivery.getMobile());
            row.createCell(4).setCellValue(delivery.getContactName());
            row.createCell(5).setCellValue(GetDict(delivery.getProvinceId()).getDictName());
            row.createCell(6).setCellValue(GetDict(delivery.getCityId()).getDictName());
            row.createCell(7).setCellValue(GetDict(delivery.getDistrictId()).getDictName());
            row.createCell(8).setCellValue(delivery.getAddress());
            row.createCell(9).setCellValue(DateUtils.formatDateByUnixTime(Long.valueOf(delivery.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            row.createCell(10).setCellValue(PropertyType.valueOf(delivery.getPropertyTypeId()).getMessage());
            row.createCell(11).setCellValue(DeliveryType.valueOf(delivery.getTypeId()).getMessage());
            row.createCell(12).setCellValue(DeliveryStatus.valueOf(delivery.getStatus()).getMessage());

            List<StockDto> stockDtos = DeliveryLogic.GetDeliveryStock(delivery.getDeliveryId());
            for (StockDto stockDto : stockDtos) {
                row.createCell(13).setCellValue(stockDto.getProductDto().getProductName());
                row.createCell(14).setCellValue(ProductLogic.GetProductSpec(stockDto.getProductSpecId()).getSpecName());
                row.createCell(15).setCellValue(stockDto.getNumber());
                rowNum++;
                row = sheet.createRow(rowNum);
            }
            rowNum++;
            sheet.autoSizeColumn(rowNum);
        }

        ExcelUtil.export(response, headers, row, workbook, sheet);
    }


    @RequestMapping("/exportOrder")
    public void exportOrder(@RequestParam(value = "typeId", defaultValue = "-1") Integer typeId,
                            @RequestParam(value = "status", defaultValue = "-1") Integer status,
                            @RequestParam(value = "startDate", defaultValue = "") String startDate,
                            @RequestParam(value = "endDate", defaultValue = "") String endDate,
                            @RequestParam(value = "keys", defaultValue = "") String keys,
                            HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("订单信息表");
        String[] headers = {"订单编号", "类型", "用户昵称", "openId", "数量", "金额", "状态", "创建时间", "商品名称", "规格", "数量"};

        EntityWrapper<Order> wrapper = new EntityWrapper<>();

        if (typeId > -1) {
            wrapper.where("type_id={0}", typeId);
        }
        if (status > -1) {
            wrapper.where("status={0}", status);
        }
        wrapper.where(!StringUtils.isBlank(keys), "(open_id like concat(concat('%',{0}),'%') or order_id like concat(concat('%',{0}),'%')  )", keys);

        if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.between("create_time", startUnix, endUnix);
        } else if (!StringUtils.isBlank(startDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            wrapper.gt("create_time", startUnix);
        } else if (!StringUtils.isBlank(endDate)) {
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.lt("create_time", endUnix);
        }
        wrapper.orderBy("create_time desc");
        List<Order> orders = orderService.selectList(wrapper);

        int rowNum = 1;
        HSSFRow row = null;

        for (Order order : orders) {
            row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(order.getOrderId());
            row.createCell(1).setCellValue(ActivityType.valueOf(order.getTypeId()).getMessage());
            row.createCell(2).setCellValue(WechatLogic.GetWechat(order.getOpenId()).getNickname());
            row.createCell(3).setCellValue(order.getOpenId());
            row.createCell(4).setCellValue(String.valueOf(OrderLogic.GetOrderProducts(order.getOrderId()).size()));
            row.createCell(5).setCellValue(order.getAmount().doubleValue());
            row.createCell(6).setCellValue(OrderStatus.valueOf(order.getStatus()).getMessage());
            row.createCell(7).setCellValue(DateUtils.formatDateByUnixTime(Long.valueOf(order.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            List<OrderProductDto> orderProductDtos = OrderLogic.GetOrderProducts(order.getOrderId());
            for (OrderProductDto orderProductDto : orderProductDtos) {
                row.createCell(8).setCellValue(ProductLogic.GetProduct(orderProductDto.getProductId()).getProductName());
                row.createCell(9).setCellValue(ProductLogic.GetProductSpec(orderProductDto.getProductSpecId()).getSpecName());
                row.createCell(10).setCellValue(orderProductDto.getNumber());
                rowNum++;
                row = sheet.createRow(rowNum);
            }
            rowNum++;
            sheet.autoSizeColumn(rowNum);
        }
        ExcelUtil.export(response, headers, row, workbook, sheet);
    }


    @RequestMapping("/exportGift")
    public void exportGift(@RequestParam(value = "giftShareType", defaultValue = "-1") Integer giftShareType,
                           @RequestParam(value = "giftStatus", defaultValue = "-1") Integer giftStatus,
                           @RequestParam(value = "startDate", defaultValue = "") String startDate,
                           @RequestParam(value = "endDate", defaultValue = "") String endDate,
                           HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("赠送信息");
        String[] headers = {"赠送编号", "赠送码", "赠送人", "赠送数量", "分享类型", "创建时间", "过期时间", "商品名称", "商品规格", "赠送状态", "领取人", "领取人openId"};


        EntityWrapper<Gift> wrapper = new EntityWrapper<>();
        wrapper.where("delete_time is null");
        if (giftShareType > -1) {
            wrapper.where("share_type={0}", giftShareType);
        }
        if (giftStatus > -1) {
            wrapper.where("status={0}", giftStatus);
        }

        if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.between("create_time", startUnix, endUnix);
        } else if (!StringUtils.isBlank(startDate)) {
            int startUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(startDate, "yyyy-MM-dd"));
            wrapper.gt("create_time", startUnix);
        } else if (!StringUtils.isBlank(endDate)) {
            int endUnix = DateUtils.getUnixTimeByDate(DateUtils.dateFormat(endDate, "yyyy-MM-dd"));
            endUnix += 60 * 60 * 24;
            wrapper.lt("create_time", endUnix);
        }

        wrapper.orderBy("create_time desc");
        List<Gift> gifts = giftService.selectList(wrapper);

        int rowNum = 1;
        HSSFRow row = null;

        for (Gift gift : gifts) {
            row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(gift.getGiftId());
            row.createCell(1).setCellValue(gift.getGiftUniqueCode());
            row.createCell(2).setCellValue(WechatLogic.GetWechat(gift.getOpenId()).getNickname());
            row.createCell(3).setCellValue(GiftLogic.GetGiftStockCount(gift.getGiftId()));
            row.createCell(4).setCellValue(GiftShareType.valueOf(gift.getShareType()).getMessage());
            row.createCell(5).setCellValue(DateUtils.formatDateByUnixTime(Long.valueOf(gift.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            row.createCell(6).setCellValue(DateUtils.formatDateByUnixTime(Long.valueOf(gift.getExpiredTime()), "yyyy-MM-dd HH:mm:ss"));
            List<StockDto> stockDtos = GiftLogic.GetGiftStocks(gift.getGiftId());
            for (StockDto stockDto : stockDtos) {

                row.createCell(7).setCellValue(stockDto.getProductSpecDto().getProductDto().getProductName());
                row.createCell(8).setCellValue(stockDto.getProductSpecDto().getSpecName());
                row.createCell(9).setCellValue(stockDto.getStockStatus().getMessage());
                StockDto sto = StockLogic.GetChild(stockDto.getStockId());
                if (sto != null) {
                    row.createCell(10).setCellValue(sto.getWechatDto().getNickname());
                    row.createCell(11).setCellValue(sto.getWechatDto().getOpenId());
                } else {
                    row.createCell(10).setCellValue("");
                    row.createCell(11).setCellValue(" ");
                }
                rowNum++;
                row = sheet.createRow(rowNum);
            }
            rowNum++;
            sheet.autoSizeColumn(rowNum);
        }
        ExcelUtil.export(response, headers, row, workbook, sheet);
    }


}

