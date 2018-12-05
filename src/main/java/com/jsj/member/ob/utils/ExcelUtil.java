package com.jsj.member.ob.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelUtil {


    /**
     * 导出Excel表
     * @param response
     * @param headers
     * @param row
     * @param workbook
     * @param sheet
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String[] headers, HSSFRow row,HSSFWorkbook workbook , HSSFSheet sheet) throws IOException {

        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xls";
        //headers表示excel表中第一行的表头
         row = sheet.createRow(0);

        HSSFCellStyle style=workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        //在excel表中添加表头
        for(int i=0;i<headers.length;i++){
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(style);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }


    public static  void exportExcel(HttpServletResponse response, String[] head,  List<List> body) throws IOException {

        SimpleDateFormat fdate = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String fileName = fdate.format(new Date()) + ".xls";

        //创建一个excel工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个sheet工作表
        HSSFSheet sheet = workbook.createSheet("信息");

        //创建第0行表头，再在这行里在创建单元格，并赋值
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        for (int i = 0; i < head.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(head[i]);//设置值
        }

        //将主体数据填入Excel中
        for (int i = 0, isize = body.size(); i < isize; i++) {
            row = sheet.createRow(i + 1);
            List list = body.get(i);
            for (int j = 0, jsize = list.size(); j < jsize; j++) {
                cell = row.createCell(j);
                cell.setCellValue(list.get(j).toString());//设置值
            }
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());

    }
}
