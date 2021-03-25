package org.firzjb.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * excel导出工具类 poi 4.0.1
 * @Author: guandezhi
 * @Date: 2019/3/9 9:47
 */
public class ExcelUtil {

    public static String charset = "utf-8";

    /**
     * @param response
     * @param wb
     * @param fileName 自定义导出的文件取名（导出后文件名叫什么）
     * @throws Exception
     *   调用后浏览器自动生成excel
     */
    public static void exportExcel(HttpServletResponse response, HSSFWorkbook wb, String fileName) throws Exception {

        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), "8859_1"));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();
    }

    /**
     * @param wb
     * @param fileName 自定义导出的文件取名（导出后文件名叫什么）
     * @throws Exception
     *   调用后浏览器自动生成excel
     */
    public static String exportExcel(HSSFWorkbook wb, String fileName) throws Exception {

        String  tmp = new StringBuffer(System.getProperty("etl_platform.root")).append(File.separator).append("tmp").append(File.separator).append(fileName).toString();
        File  file= new File(tmp);
        FileOutputStream out  = new FileOutputStream(file);

        wb.write(out);
        out.flush();
        out.close();

        return encode(tmp);
    }

    public static String encode(String string) {
        if(string == null || string.length() == 0)
            return string;
        try {
            String tmp = URLEncoder.encode(string, charset);
            return tmp.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }


    /**
     * @param wb （转入的HSSFWorkbook 如果为空则自动创建）
     * @param needForTitle 是否需要标题 （即第一行是否需要合并作为标题，如果不需要输入false，输入null默认为需要）
     * @param title 标题 （第一行合并的单元格的内容）
     * @param headers 表头  （每一列字段中文说明）
     * 使用方法：
     * String[] headers = {"序号","单位","机组","容量","开始","结束","工期","类型","发电类型"};
     *
     * @param values 表中元素    （ 二维数组，第一维表示记录数即每一行，第二维表示列 也是java实体类的属性值）
     * 使用方法：
     * （实例化操作可以直接get赋值如下，也可以通过java反射机制）
     *      String[][] values = new String[query.size()][headers.length];
     *   for (int i = 0; i < query.size(); i++) {
     *       Jzjxjh e = query.get(i);
     *       values[i][0]=e.getXh()+"";
     *       values[i][1]=e.getDw();
     *       values[i][2]=e.getJz();
     *       values[i][3]=e.getRl()+"";
     *       values[i][4]=sdf.format(e.getKs());
     *   }
     *
     * @param columnWidth 每一列的宽度
     * 使用方法：  转入null 或者     int[] columnWidth={18,18,18,18,20,20,18,18,18};
     *
     * @return 返回 HSSFWorkbook  wb
     */
    public static HSSFWorkbook getHSSFWorkbook(HSSFWorkbook wb,Boolean needForTitle,String title, String headers[], String[][] values,Integer[] columnWidth) throws Exception {

        //默认行操作从第一行开始
        int curRow = 0;

        if(wb==null){
            // 创建一个HSSFWorkbook，对应一个Excel文件
            wb = new HSSFWorkbook();
        }

        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(title);

        if(needForTitle==null || needForTitle){
            curRow = createTitle(wb,sheet,curRow,title,headers);
        }

        curRow = createHeader(wb, sheet, curRow, headers, columnWidth);

        curRow = createContent(wb, sheet, curRow, values);

        return wb;
    }


    /**
     * @param wb
     * @param sheet
     * @param curRow
     * @param values
     * @return 产生表数据
     * @throws Exception
     */
    private static int createContent(HSSFWorkbook wb, HSSFSheet sheet, int curRow, String[][] values) throws Exception {
        // 创建单元格，内容样式
        HSSFCellStyle contentStyle = getContextStyle(wb);

        // 创建内容
        for (int i = 0; i < values.length; i++) {
            HSSFRow contentRow = sheet.createRow(curRow++);
            for (int j = 0; j < values[i].length; j++) {
                // 将内容按顺序赋给对应列对象
                HSSFCell hssfCell = contentRow.createCell(j);
                hssfCell.setCellValue(values[i][j]);
                hssfCell.setCellStyle(contentStyle);
            }
        }
        return curRow;
    }

    /**
     * @param wb
     * @param sheet
     * @param curRow
     * @param headers
     * @param columnWidth
     * @return 产生表头
     * @throws Exception
     */
    private static int createHeader(HSSFWorkbook wb, HSSFSheet sheet, int curRow, String[] headers, Integer[] columnWidth) throws Exception {
        // 产生表头
        HSSFCellStyle headerStyle = getHeadStyle(wb);
        HSSFRow headerRow = sheet.createRow(curRow++);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell hssfCell = headerRow.createCell(i);
            hssfCell.setCellValue(headers[i]);
            hssfCell.setCellStyle(headerStyle);
        }
        //自适应列宽度(实际效果并不理想)
//        sheet.autoSizeColumn(1);

        //设置表头宽度
        if(columnWidth!=null&&columnWidth.length>0){
            for(int i =0;i<columnWidth.length;i++){
                sheet.setColumnWidth(i, columnWidth[i]*256);
            }
        }
        return curRow;
    }

    /**
     * @param wb
     * @param title
     * @param headers
     * @param curRow
     * @param sheet
     * @return 创建标题行 后 行数据会+1并返回 行数
     * @throws Exception
     */
    private static int createTitle(HSSFWorkbook wb, HSSFSheet sheet, int curRow, String title, String[] headers) throws Exception {
        // 创建标题合并行
        sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) headers.length - 1));
        HSSFCellStyle style = getTitleStyle(wb);
        // 设置标题字体
        Font titleFont = getTitleFont(wb);
        //设置粗体
        // titleFont.setBold(true);
        style.setFont(titleFont);

        // 产生标题行
        HSSFRow hssfRow = sheet.createRow(curRow++);
        HSSFCell cell = hssfRow.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        return curRow;
    }

    /**
     * @param wb
     * @return 设置表内容样式 创建单元格，并设置值表头 设置表头居中
     */
    private static HSSFCellStyle getContextStyle(HSSFWorkbook wb) throws Exception {
        HSSFCellStyle style1 = wb.createCellStyle();
        style1.setBorderBottom(BorderStyle.THIN);
        style1.setBorderLeft(BorderStyle.THIN);
        style1.setBorderRight(BorderStyle.THIN);
        style1.setBorderTop(BorderStyle.THIN);
        return style1;
    }

    /**
     * @param wb
     * @return 设置标题字体
     */
    private static Font getTitleFont(HSSFWorkbook wb) throws Exception {
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        return titleFont;
    }

    /**
     * @param wb
     * @return 设置标题样式
     */
    private static HSSFCellStyle getTitleStyle(HSSFWorkbook wb) throws Exception {
        // 设置标题样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 设置居中样式
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * @param wb
     * @return 设置值表头样式 设置表头居中
     */
    private static HSSFCellStyle getHeadStyle(HSSFWorkbook wb) throws Exception {
        // 设置值表头样式 设置表头居中
        HSSFCellStyle hssfCellStyle = wb.createCellStyle();
        hssfCellStyle.setAlignment(HorizontalAlignment.CENTER); // 设置居中样式
        hssfCellStyle.setBorderBottom(BorderStyle.THIN);
        hssfCellStyle.setBorderLeft(BorderStyle.THIN);
        hssfCellStyle.setBorderRight(BorderStyle.THIN);
        hssfCellStyle.setBorderTop(BorderStyle.THIN);
        return hssfCellStyle;
    }

}
