package org.lh.web.rest;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lh.domain.EventCheckIn;
import org.lh.service.EventCheckInService;
import org.lh.service.impl.EventCheckInServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author:liuhuan
 * @date:2019/11/28,12:30
 * @version:1.0
 */
@RestController
@RequestMapping("/api")
public class ExportExcelResource {

    private final EventCheckInService eventCheckInService;

    private final Logger log = LoggerFactory.getLogger(EventCheckInServiceImpl.class);

    public ExportExcelResource(EventCheckInService eventCheckInService) {
        this.eventCheckInService = eventCheckInService;
    }

    @RequestMapping("/downloadExcel")
    public void getCheckInDetails(HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("统计表");

        List<EventCheckIn> list = eventCheckInService.getAllPersonDetails();

        String fileName = "CheckIn.xlsx";

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("序号");//第一列
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("手机号");
        row.createCell(3).setCellValue("地址");
        row.createCell(4).setCellValue("是否签到");

        int rowNum = 1;
        if (list.size()>0){
            for (EventCheckIn item:list) {
                Row row1 = sheet.createRow(rowNum);
                row1.createCell(0).setCellValue(rowNum);
                row1.createCell(1).setCellValue(item.getUserName());
                row1.createCell(2).setCellValue(item.getPhoneNumber());
                row1.createCell(3).setCellValue(item.getAddress());
                row1.createCell(4).setCellValue(item.isIsCheckIn());

                rowNum++;
            }
        }

        OutputStream outputStream;
        try {
            fileName = new String(fileName.getBytes("iso8859-1"),"utf-8");
            //设置ContentType请求信息格式
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
