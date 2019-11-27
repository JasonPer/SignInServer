package org.lh.web.rest;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.lh.domain.EventCheckIn;
import org.lh.service.EventCheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author:liuhuan
 * @date:2019/11/27,14:47
 * @version:1.0
 */
@RestController
@RequestMapping("/api")
public class ImportExcelResource {

    private final Logger log = LoggerFactory.getLogger(EventCheckInResource.class);

    private final EventCheckInService eventCheckInService;

    private final ZoneId systemDefault = ZoneId.systemDefault();

    public ImportExcelResource(EventCheckInService eventCheckInService) {
        this.eventCheckInService = eventCheckInService;
    }

    /**
     * 上传的 Excel 文件
     * @param file
     * @return
     */
    @PostMapping("/uploadExcel")
    public void uploadExcel(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            log.info("File empty!");
        }
        try{
            InputStream inputStream = file.getInputStream();

            String fileName = file.getOriginalFilename();

            Workbook workbook = null;
            if (isExcel2003(fileName)){
                workbook = new HSSFWorkbook(inputStream);
            }else if (isExcel2007(fileName)){
                workbook = new XSSFWorkbook(inputStream);
            }else {
                log.info("Unknown file type!");
            }

            int sheetsNumber = workbook.getNumberOfSheets();
            Sheet sheet = workbook.getSheetAt(0);
            int allRowNum = sheet.getLastRowNum();

            if (allRowNum == 0){
                log.info("The imported data is empty!");
            }

            for (int i = 1; i <= allRowNum; i++) {
                EventCheckIn checkIn = new EventCheckIn();
                Row row = sheet.getRow(i);

                if (row != null){
                    // 获取第几个单元格信息
                    Cell c1 = row.getCell(1);
                    Cell c2 = row.getCell(2);
                    Cell c3 = row.getCell(3);
                    Cell c4 = row.getCell(4);
                    Cell c5 = row.getCell(5);

                    if (c1 != null){
                        c1.setCellType(CellType.STRING);
                        checkIn.setUserName(c1.getStringCellValue());
                    }else {
                        log.info("Line "+i+", row 1 has error!");
                    }

                    if (c2 != null){
                        c2.setCellType(CellType.STRING);
                        checkIn.setPhoneNumber(c2.getStringCellValue());
                    }else {
                        log.info("Line "+i+", row 2 has error!");
                    }

                    if (c3 != null){
                        c3.setCellType(CellType.STRING);
                        checkIn.setAddress(c3.getStringCellValue());
                    }else {
                        log.info("Line "+i+", row 3 has error!");
                    }

                    if (c4 != null){
                        c4.setCellType(CellType.NUMERIC);
                        checkIn.setCheckTime(ZonedDateTime.ofInstant(c4.getDateCellValue().toInstant(), systemDefault));
                    }else {
                        log.info("Line "+i+", row 4 has error!");
                    }

                    if (c5 != null){
                        c5.setCellType(CellType.BOOLEAN);
                        checkIn.setIsCheckIn(c5.getBooleanCellValue());
                    }else {
                        log.info("Line "+i+", row 5 has error!");
                    }

                }

                if (checkIn.getUserName() != null && checkIn.getPhoneNumber() != null && checkIn.getAddress() != null && checkIn.getCheckTime() != null && checkIn.isIsCheckIn() != null){
                    log.info("checkIn",checkIn);
                    eventCheckInService.save(checkIn);
                }else {
                    log.info("Data has error!");
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /***
     * 判断文件类型是不是2003版本
     * @param filePath
     * @return
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 判断文件类型是不是2007版本
     * @param filePath
     * @return
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }
}
