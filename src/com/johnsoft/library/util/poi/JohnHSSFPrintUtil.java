package com.johnsoft.library.util.poi;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 此类用于辅助打印功能相关设置
 * 相似方法中带有"H"字符的作用于XLS格式工作簿
 * @author 李哲浩
 *
 */
public class JohnHSSFPrintUtil
{
	/**
	 * 此方法用于设置打印区域
	 * @param sheet 当前工作表
	 * @param firstCell 左上角单元格的引用，如："C5"
	 * @param lastCell 右下角单元格的引用，如："D9"
	 */
	public static void setSimplePrint(XSSFSheet sheet,String firstCell,String lastCell)
	{   
		PrintSetup ps = sheet.getPrintSetup();
		sheet.setAutobreaks(true);
		ps.setFitHeight((short)1);
		ps.setFitWidth((short)1);
		sheet.getWorkbook().setPrintArea(sheet.getWorkbook().getSheetIndex(sheet), firstCell+":"+lastCell);
		
	}
	
	/**
	 * 此方法用于设置打印区域
	 * @param wb 当前工作簿
	 * @param sheetIndex 要打印的工作表索引，从0开始
	 * @param row1 	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2	右下角单元格的列号
	 */
	public static void setSimplePrint(XSSFWorkbook wb,int sheetIndex,int row1,int row2,int col1,int col2)
	{
		PrintSetup ps = wb.getSheetAt(sheetIndex).getPrintSetup();
		wb.getSheetAt(sheetIndex).setAutobreaks(true);
		ps.setFitHeight((short)1);
		ps.setFitWidth((short)1);
		wb.getSheetAt(sheetIndex).getWorkbook().setPrintArea(sheetIndex, col1, col2 ,row1 , row2);
		
	}
	
	//以下是03版的xls创建或获得
	/**
	 * 此方法用于设置打印区域
	 * @param sheet 当前工作表
	 * @param firstCell 左上角单元格的引用，如："C5"
	 * @param lastCell 右下角单元格的引用，如："D9"
	 */
	public static void setSimplePrintH(HSSFSheet sheet,String firstCell,String lastCell)
	{   
		PrintSetup ps = sheet.getPrintSetup();
		sheet.setAutobreaks(true);
		ps.setFitHeight((short)1);
		ps.setFitWidth((short)1);
		sheet.getWorkbook().setPrintArea(sheet.getWorkbook().getSheetIndex(sheet), firstCell+":"+lastCell);
		
	}
	
	/**
	 * 此方法用于设置打印区域
	 * @param wb 当前工作簿
	 * @param sheetIndex 要打印的工作表索引，从0开始
	 * @param row1 	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2	右下角单元格的列号
	 */
	public static void setSimplePrintH(HSSFWorkbook wb,int sheetIndex,int row1,int row2,int col1,int col2)
	{
		PrintSetup ps = wb.getSheetAt(sheetIndex).getPrintSetup();
		wb.getSheetAt(sheetIndex).setAutobreaks(true);
		ps.setFitHeight((short)1);
		ps.setFitWidth((short)1);
		wb.getSheetAt(sheetIndex).getWorkbook().setPrintArea(sheetIndex, col1, col2 ,row1 , row2);
		
	}


}
