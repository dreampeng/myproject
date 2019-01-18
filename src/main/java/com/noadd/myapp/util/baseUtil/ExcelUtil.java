package com.noadd.myapp.util.baseUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 **/
public class ExcelUtil {
    public static List<Map<String, Object>> readExcel(String filePath, int titleNum, int sheetNumStart, int sheetNumEnd,
                                                      String props) throws Exception {
        List<Map<String, Object>> excelResult = new ArrayList<>();
        if (StringUtil.isEmpty(props)) {
            return excelResult;
        }
        File excel = new File(filePath);
        if (excel.isFile() && excel.exists()) {
            String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
            Workbook wb;
            FileInputStream fis = new FileInputStream(excel);   //文件流对象
            //根据文件后缀（xls/xlsx）进行判断
            if ("xls".equals(split[1])) {
                wb = new HSSFWorkbook(fis);
            } else if ("xlsx".equals(split[1])) {
                wb = new XSSFWorkbook(fis);
            } else {
                throw new Exception("文件类型错误");
            }
            int sheetNum = wb.getNumberOfSheets();
            //超出后只读第一个Sheet
            if (sheetNumStart > sheetNum || sheetNumEnd > sheetNum) {
                readSheet(wb.getSheetAt(0), excelResult, titleNum, props);
            }
            //开始解析
            if (sheetNumStart < 0) {//获取所有
                for (int i = 0; i < sheetNum; i++) {
                    readSheet(wb.getSheetAt(i), excelResult, titleNum, props);
                }
            } else {
                for (int i = sheetNumStart - 1; i < sheetNumEnd; i++) {
                    readSheet(wb.getSheetAt(i), excelResult, titleNum, props);
                }
            }
        } else {
            throw new FileNotFoundException("未找到文件：" + filePath);
        }
        return excelResult;
    }

    private static void readSheet(Sheet sheet, List<Map<String, Object>> excelResult, int titleNum, String props) {
        int firstRowIndex = sheet.getFirstRowNum() + titleNum;   //第一行是列名，所以不读
        int lastRowIndex = sheet.getLastRowNum();
        for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {//遍历行
            Row row = sheet.getRow(rIndex);
            if (row != null) {
                Map<String, Object> rowMap = new HashMap<>();
                int firstCellIndex = row.getFirstCellNum();
                String[] propsList = props.split(",");
                for (int cIndex = firstCellIndex; cIndex < propsList.length; cIndex++) {//遍历列
                    Cell cell = row.getCell(cIndex);
                    if (cell != null) {
                        rowMap.put(propsList[cIndex], cell.toString());
                    }
                }
                excelResult.add(rowMap);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(TimeUtil.getCurrentDate(null));
        System.out.println(readExcel("E:\\清税信息\\6.xlsx", 1, 1, 1,
                "uniscid,nsrsbh,wszg,nsrmc,kjswjg,kjrq,djxh,ssdabh").size());
        System.out.println(TimeUtil.getCurrentDate(null));
    }

}
