package com.base.common.utils;

import com.csvreader.CsvReader;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderUtil {

    public static void exportExcelObj(String filename, String[] title, List<Object[]> data, HttpServletResponse response) {
        if (filename == null || "".equals(filename)) {
            throw new IllegalArgumentException("parameter filename connot be null!");
        }

        // 创建一个EXCEL
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFCellStyle style = wb.createCellStyle();
        ;
        // 创建一个SHEET
        HSSFSheet sheet1 = wb.createSheet(filename);

        int rowNum = 0;
        short cellNum = 0;
        if (title != null && title.length > 0) {
            HSSFRow row = sheet1.createRow(rowNum);
            HSSFFont f = wb.createFont();
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗
            style.setFont(f);
            // 填充标题
            for (String s : title) {
                HSSFCell cell = row.createCell(cellNum);
                cell.setCellValue(s);
                cell.setCellStyle(style);
                cellNum++;
            }
            rowNum++;
        }

        //填充内容
        for (Object[] a : data) {
            //创建一行
            HSSFRow rowTmp = sheet1.createRow(rowNum);
            cellNum = 0;
            for (Object s : a) {
                if (s == null)
                    rowTmp.createCell(cellNum).setCellValue("");
                else
                    rowTmp.createCell(cellNum).setCellValue(s.toString());
                cellNum++;
            }
            rowNum++;
        }

        try {
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"), "iso8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            wb.write(toClient);
            toClient.flush();
            toClient.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过绝对路径 获取结果
     * @param file
     * @return
     * @throws IOException
     */
    public static List<List<String>> readCsv(String file) throws IOException {
        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = new CsvReader(file, ',', Charset.forName("GBK"));
        csvReader.readHeaders();
        return csvList(csvReader);
    }

    /**
     * 通过流 获取结果
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<List<String>> readCsv(InputStream inputStream) throws IOException {
        // 第一参数：读取文件的路径 第二个参数：分隔符（不懂仔细查看引用百度百科的那段话） 第三个参数：字符集
        CsvReader csvReader = new CsvReader(inputStream, Charset.forName("GBK"));
        // 如果你的文件没有表头，这行不用执行
        // 这行不要是为了从表头的下一行读，也就是过滤表头
        csvReader.readHeaders();
        return csvList(csvReader);
    }

    public static List<List<String>> csvList(CsvReader csvReader){
        List<List<String>> returnList = new ArrayList<>();
        try {
            // 读取每行的内容
            while (csvReader.readRecord()) {
                List<String> data = new ArrayList<>();
                // 获取内容的两种方式
                int count = csvReader.getColumnCount();
                for (int i = 0; i < count; i++) {
                    // 1. 通过下标获取
                    System.out.print(csvReader.get(i));
                    data.add(csvReader.get(i));
                }
                returnList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return returnList;
    }

    /**
     * 读取文件
     *
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
                            if (rowNum == 0) {
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
     * 读取文件
     *
     * @param inputStream
     * @return
     */
    public static List<List<String>> readExcel(InputStream inputStream) {
        List<List<String>> lists = new ArrayList<List<String>>();
        Workbook workBook = null;
        int rowSize = 0;
        try {
            if (inputStream.available() > 0) {
                    workBook = WorkbookFactory.create(inputStream);
                    int numberOfSheets = workBook.getNumberOfSheets();
                    for (int s = 0; s < numberOfSheets; s++) { // sheet工作表
                        Sheet sheetAt = workBook.getSheetAt(s);
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
                                if (rowNum == 0) {
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
            } else {
                System.out.println("文件不存在!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                workBook.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return lists;
    }


    /**
     * 创建Excel.xls
     *
     * @param lists  需要写入xls的数据
     * @param titles 列标题
     * @param name   文件名
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
        for (int i = 0; i < titles.length; i++) {
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
        for (int i = 0; i < titles.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(cs);
        }
        if (lists == null || lists.size() == 0) {
            return wb;
        }
        //设置每行每列的值
        for (short i = 1; i <= lists.size(); i++) {
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            Row row1 = sheet.createRow((short) i);
            for (short j = 0; j < titles.length; j++) {
                // 在row行上创建一个方格
                Cell cell = row1.createCell(j);
                cell.setCellValue(lists.get(i - 1).get(j));
                cell.setCellStyle(cs2);
            }
        }
        return wb;
    }

    /**
     * 获取入库B表信息
     *
     * @return
     */
    public static List<List<String>> getStorageData(List<List<String>> list) {
        List<String> l1 = null;
        List<String> l2 = null;
        List<List<String>> returnList = new ArrayList<>();
        for (int i = 6; i < list.size(); i++) {
            l1 = list.get(i);
            l2 = new ArrayList<>();
            for (int i1 = 0; i1 < l1.size(); i1++) {
                //如果第10项为空,默认跳出
                if (StringUtils.isBlank(l1.get(10).toString())) {
                    break;
                }
                l2.add(l1.get(i1));
            }
            if (null != l2 && l2.size() > 0) {
                returnList.add(l2);
            }
        }
        return returnList;
    }

    /**
     * 获取入库B表日期
     * @param list
     * @return
     */
    public static String getStorageDate(List<List<String>> list) {
        String str = list.get(1).get(10);
        String date = str.split("申请日期：")[1];
        date = date.replace("年", "-").replace("月", "-").replace("日", "");
        return date;
    }

    public static void main(String[] args) throws IOException {
//        String path = "/Users/ZeChaoWei/test/京东.xlsx";
       /* String path="E:/test/A.csv";
        List<List<String>> read = readCsv(path);
        for (List<String> strings : read) {
            for (String string : strings) {
                System.out.println(string + " -- ");
            }
        }*/
       String path = "E:/test/A.xlsx";
        List<List<String>> lists = readExcel(path);
        List<List<String>> storageData = getStorageData(lists);
        for (List<String> storageDatum : storageData) {
            System.out.println(storageDatum.get(1));
            for (String s : storageDatum) {
                System.out.println(s);
            }
        }
        /*for (List<String> list : lists) {
            for (String strs : list) {
                System.out.println(strs);
            }
        }*/
    }
}
