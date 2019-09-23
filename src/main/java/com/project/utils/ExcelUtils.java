package com.project.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.bean.others.ExcelBean;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Excel导入导出工具类
 */
@Component
public class ExcelUtils {

    private static ObjectMapper objectMapper;
    private static FormulaEvaluator evaluator;

    @Autowired
    public ExcelUtils(ObjectMapper objectMapper) {
        ExcelUtils.objectMapper = objectMapper;
    }

    /**
     * 导出excel
     */
    public static boolean exportExcel(String path, String fileName, List<ExcelBean> dataBeans, List<?> listContent) {
        FileOutputStream fileOut = null;
        try {
            //创建一个excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            evaluator = new HSSFFormulaEvaluator(wb);
            //创建一个工作薄
            HSSFSheet sheet = wb.createSheet();
            //设置单元格宽度，因为一个单元格宽度定了那么下面多有的单元格高度都确定了所以这个方法是sheet的
            sheet.setColumnWidth((short) 3, 20 * 256);
            //第一个参数是指哪个单元格，第二个参数是单元格的宽度
            sheet.setColumnWidth((short) 4, 20 * 256);
            //有得时候你想设置统一单元格的高度，就用这个方法
            sheet.setDefaultRowHeight((short) 300);
            //创建第一行标题
            HSSFRow head = sheet.createRow(0);
            //设置第一行的每一列的名称
            for (int i = 0; i < dataBeans.size(); i++) {
                head.createCell((short) i).setCellValue(dataBeans.get(i).getName());
            }
            //以下是EXCEL正文数据
            for (int i = 0; i < listContent.size(); i++) {
                //创建一个新的行
                HSSFRow row = sheet.createRow(i + 1);
                //循环通过反射，为每一列赋值
                for (int j = 0; j < dataBeans.size(); j++) {
                    Field field = listContent.get(i).getClass().getDeclaredField(dataBeans.get(j).getType());
                    if (field != null) {
                        field.setAccessible(true);
                        Object va = field.get(listContent.get(i));
                        if (va == null) {
                            va = "";
                        }
                        //创建一个新的列
                        row.createCell((short) j).setCellValue(va.toString());
                    }
                }
            }
            //如果目录不存在，创建一个新的目录
            String nowPath = OthersUtils.getFileSaveParentPath() + path;
            String nowFileName = nowPath + fileName;
            File file = new File(nowPath);
            if (!file.exists()) {
                boolean success = file.mkdirs();
                if (!success) {
                    throw new RuntimeException("创建目录失败");
                }
            }
            fileOut = new FileOutputStream(nowFileName);
            wb.write(fileOut);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 导入Excel
     */
    public static List<Map<String, String>> importExcel(String fileName) {
        List<Map<String, String>> mapList = new LinkedList<>();
        //自动关闭资源
        try (
                InputStream in = new FileInputStream(fileName);
                //poi文件流
                POIFSFileSystem fs = new POIFSFileSystem(in)
        ) {
            //获得excel
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            evaluator = new HSSFFormulaEvaluator(wb);
            HSSFSheet sheet = wb.getSheetAt(0);//获得工作簿
            //是否已到末尾
            boolean end = false;
            for (int i = 1; !end && i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);
                if (row == null || row.getPhysicalNumberOfCells() == 1) {
                    end = true;
                    continue;
                }
                Map<String, String> map = new LinkedHashMap<>();
                for (int j = 0; j < sheet.getRow(0).getPhysicalNumberOfCells(); j++) {
                    Object value = getCellFormatValue(row.getCell((short) j));
                    Object key = getCellFormatValue(sheet.getRow(0).getCell(j));
                    if (key != null && value != null) {
                        map.put(key.toString().trim(), value == null ? null : value.toString().trim());
                    }
                }
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("excel导入失败");
        }
        return mapList;
    }

    /**
     * 通过cell获取里面的值
     */
    private static Object getCellFormatValue(HSSFCell cell) {
        Object cellValue;
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellTypeEnum()) {
                // 如果当前Cell的Type为NUMERIC
                case NUMERIC:
                    Long longVal = Math.round(cell.getNumericCellValue());
                    Double doubleVal = cell.getNumericCellValue();
                    if (Double.parseDouble(longVal + ".0") == doubleVal) {   //判断是否含有小数位.0
                        cellValue = longVal.toString();
                    } else {
                        cellValue = doubleVal;
                    }
                    break;
                case FORMULA: {
                    // 取得当前Cell的数值
                    cellValue = evaluator.evaluate(cell).getNumberValue();
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case STRING:
                    // 取得当前的Cell字符串
                    cellValue = cell.getStringCellValue();
                    break;
                case BLANK:
                    cellValue = null;
                    break;
                default:
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getStringCellValue();
                    break;
            }
        } else {
            cellValue = null;
        }
        return cellValue;
    }
}
