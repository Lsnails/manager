package com.base.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtil {

    /**
     * 读取文件
     * @param path
     * @return
     */
    public static List<List<String>> readExcel(String path) {
        List<List<String>> lists = new ArrayList<List<String>>();
        File file = new File(path);
        FileInputStream fis = null;
        Workbook workBook = null;
        int rowSize = 0;
        if (file.exists()) {
            try {
                fis = new FileInputStream(file);
                workBook = WorkbookFactory.create(fis);
                int numberOfSheets = workBook.getNumberOfSheets();
                for (int s = 0; s < numberOfSheets; s++) { // sheet工作表
                    Sheet sheetAt = workBook.getSheetAt(s);
//                  String sheetName = sheetAt.getSheetName(); //获取工作表名称
                    int rowsOfSheet = sheetAt.getPhysicalNumberOfRows(); // 获取当前Sheet的总列数
                    System.out.println("当前表格的总行数:" + rowsOfSheet);
                    for (int r = 0; r < rowsOfSheet; r++) { // 总行
                        ArrayList<String> list = new ArrayList<String>();
                        Row row = sheetAt.getRow(r);
                        if (row == null) {
                            continue;
                        } else {
                            int rowNum = row.getRowNum();
                            System.out.println("当前行:" + rowNum);
                            int numberOfCells = row.getPhysicalNumberOfCells();
                            if(rowNum == 0){
                                rowSize = numberOfCells;
                            }
                            for (int c = 0; c < rowSize; c++) { // 总列(格)
                                Cell cell = row.getCell(c);
                                if (cell == null) {
                                    list.add("");
                                    continue;
                                } else {
                                    int cellType = cell.getCellType();
                                    switch (cellType) {
                                        case Cell.CELL_TYPE_STRING: // 代表文本
                                            String stringCellValue = cell.getStringCellValue();
                                            list.add(stringCellValue);
                                            break;
                                        default:
                                            cell.setCellType(Cell.CELL_TYPE_STRING);
                                            String str = cell.getStringCellValue();
                                            list.add(str);
                                            break;
                                    }
                                }
                            }
                        }
                        lists.add(list);
                    }
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            System.out.println("文件不存在!");
        }
        return lists;
    }


    /**
     * 创建Excel.xls
     * @param lists 需要写入xls的数据
     * @param titles 列标题
     * @param name  文件名
     * @return
     * @throws IOException
     */
    public static Workbook creatExcel(List<List<String>> lists, String[] titles, String name) throws IOException {
        System.out.println(lists);
        //创建新的工作薄
        Workbook wb = new HSSFWorkbook();
        // 创建第一个sheet（页），并命名
        Sheet sheet = wb.createSheet(name);
        // 手动设置列宽。第一个参数表示要为第几列设；，第二个参数表示列的宽度，n为列高的像素数。
        for(int i=0;i<titles.length;i++){
            sheet.setColumnWidth((short) i, (short) (35.7 * 150));
        }

        // 创建第一行
        Row row = sheet.createRow((short) 0);

        // 创建两种单元格格式
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();

        // 创建两种字体
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        // 创建第一种字体样式（用于列名）
        f.setFontHeightInPoints((short) 10);
        f.setColor(IndexedColors.BLACK.getIndex());
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        // 创建第二种字体样式（用于值）
        f2.setFontHeightInPoints((short) 10);
        f2.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于列名）
        cs.setFont(f);
        cs.setBorderLeft(CellStyle.BORDER_THIN);
        cs.setBorderRight(CellStyle.BORDER_THIN);
        cs.setBorderTop(CellStyle.BORDER_THIN);
        cs.setBorderBottom(CellStyle.BORDER_THIN);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        // 设置第二种单元格的样式（用于值）
        cs2.setFont(f2);
        cs2.setBorderLeft(CellStyle.BORDER_THIN);
        cs2.setBorderRight(CellStyle.BORDER_THIN);
        cs2.setBorderTop(CellStyle.BORDER_THIN);
        cs2.setBorderBottom(CellStyle.BORDER_THIN);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);
        //设置列名
        for(int i=0;i<titles.length;i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cs);
        }
        if(lists == null || lists.size() == 0){
            return wb;
        }
        //设置每行每列的值
        for (short i = 1; i <= lists.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short)i);
            for(short j=0;j<titles.length;j++){
                // 在row行上创建一个方格
                Cell cell = row1.createCell(j);
                cell.setCellValue(lists.get(i-1).get(j));
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }

    public static void main(String[] args) {
        String path = "/Users/ZeChaoWei/test/京东.xlsx";
        List<List<String>> lists = readExcel(path);
        for (List<String> list : lists) {
            for (String strs : list) {
                System.out.println(strs);
            }
        }
    }
}
