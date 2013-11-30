package com.johnsoft.library.util.poi;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 此类用于辅助创建和获取特定单元格及其区域
 * 相似方法中带有"H"字符的作用于XLS格式工作簿
 * 行列号从0开始
 * @author 李哲浩
 *
 */
public class JohnHSSFRowCellUtil
{	
	/**
	 * 此方法用于创建特定单元格区域
	 * @param sheet 要创建区域的工作表
	 * @param row1 左上角单元格的行号
	 * @param row2 右下角单元格的行号
	 * @param col1  左上角单元格的列号
	 * @param col2 右下角单元格的列号
	 * @param wrapText 如何为true，则该区域的所有单元格具有自动换行功能
	 */
	public static void createXarea(XSSFSheet sheet,int row1,int row2,int col1,int col2,boolean wrapText)
	{	
		
		for(int i = row1; i<row2+1;i++)
		{
			XSSFRow row = sheet.createRow(i);
			if(wrapText == true)
			{
				XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
			    style.setWrapText(true);
			    
			    for(int j = col1; j<col2+1;j++)
				{	
					XSSFCell cell = row.createCell(j);
					cell.setCellStyle(style);
				}
			
			}
			else
			{
				 for(int j = col1; j<col2+1;j++)
					{	
					 		row.createCell(j);
					}
			}
		}
	}

	/**
	 * 此方法用于创建特定单元格
	 * @param sheet 要创建单元格的工作表
	 * @param myrow 该单元格的行号
	 * @param mycol 该单元格的列号
	 */
	public static void creatXcell(XSSFSheet sheet,int myrow,int mycol)
	{ 
		XSSFRow row = sheet.createRow(myrow);
		row.createCell(mycol);
		
	}

	/**
	 * 此方法选取多行
	 * @param sheet 当前工作表
	 * @param row1 从哪一行开始
	 * @param row2到哪一行结束
	 * @return 行的数组
	 */
	public static ArrayList<XSSFRow> getRows(XSSFSheet sheet,int row1,int row2)
	{
		ArrayList<XSSFRow> list = new ArrayList<XSSFRow>();
		for(int i =row1;i<row2+1;i++)
		{
			list.add(sheet.getRow(i));
			
		}
		return list;
	}
	
	/**
	 * 此方法选取多行的单元格
	 * @param sheet 当前工作表
	 * @param row1 从哪一行开始
	 * @param row2到哪一行结束
	 * @return 单元格的二维数组
	 */
	public static XSSFCell[][] getCellsByRows(XSSFSheet sheet,int row1,int row2)
	{
		XSSFCell[][] cells = new XSSFCell[][]{};
		for(int i = row1;i<row2+1;i++)
		{
			XSSFRow row = sheet.getRow(i);
			for(int j = row.getFirstCellNum();j<row.getLastCellNum();j++)
			{
				cells[i][j] = row.getCell(j);
			}
		}
		return cells;
	}
	
	/**
	 * 此方法选取多列的单元格
	 * @param sheet 当前工作表
	 * @param col1	 从哪一列开始
	 * @param col2 	到哪一列结束
	 * @return 单元格的二维数组
	 */
	public static XSSFCell[][] getCellsByColumns(XSSFSheet sheet,int col1,int col2)
	{
		XSSFCell[][] cells = new XSSFCell[][]{};
		for(int i = sheet.getFirstRowNum();i<sheet.getLastRowNum();i++)
		{
			XSSFRow row = sheet.getRow(i);
			for(int j = col1;j<col2;j++)
			{
				cells[i][j] = row.getCell(j);
			}
		}
		return cells;
	}
	
	/**
	 * 此方法全选单元格
	 * @param sheet 当前工作表
	 * @return 单元格的二维数组
	 */
	public static XSSFCell[][] getAllCells(XSSFSheet sheet)
	{		XSSFCell[][] cells = new XSSFCell[][]{};
		
		for(int i = sheet.getFirstRowNum(); i <sheet.getLastRowNum();i++)
		{	XSSFRow row = sheet.getRow(i);
			for(int j = row.getFirstCellNum();j<row.getLastCellNum();j++)
			{
				XSSFCell cell = row.getCell(j);
				cells[i][j]=cell;
			}
		}
		return cells;
	}
	
	/**
	 * 此方法获取特定单元格区域
	 * @param sheet 当前工作表
	 * @param row1 	从哪行开始
	 * @param row2	到哪行结束
	 * @param col1	从哪列开始
	 * @param col2	到哪列结束
	 * @return 被选择的单元格二维数组
	 */
	public static XSSFCell[][] getXarea1(XSSFSheet sheet,int row1,int row2,int col1,int col2)
	{
		XSSFCell[][] cells = new XSSFCell[][]{};
		for(int i = row1; i <row2;i++)
		{	
			for(int j = col1;j<col2;j++)
			{
				cells[i][j] = sheet.getRow(i).getCell(j);
				
			}
		}
		return cells;
		
	}
	
	/**
	 * 此方法获取特定单元格区域，与getXarea1唯一的区别是返回类型
	 * @param sheet 当前工作表
	 * @param row1 	从哪行开始
	 * @param row2	到哪行结束
	 * @param col1	从哪列开始
	 * @param col2	到哪列结束
	 * @return 	单元格集合
	 */
	public static ArrayList<XSSFCell> getXarea2(XSSFSheet sheet,int row1,int row2,int col1,int col2)
	{
		ArrayList<XSSFCell> list = new ArrayList<XSSFCell>();
		for(int i = row1; i <row2;i++)
		{	
			for(int j = col1;j<col2;j++)
			{
				list.add(sheet.getRow(i).getCell(j));
				
			}
		}
		return list;
		
	}
  /**
   * 此方法获取特定单元格的值
   * @param cell 该单元格
   * 
   */
	public static Object getRealValue(XSSFCell cell)
	{ 	
		Object value = null;
	switch(cell.getCellType()) 
	{
      case Cell.CELL_TYPE_STRING:
        value = cell.getRichStringCellValue().getString();
        break;
      case Cell.CELL_TYPE_NUMERIC:
        if(DateUtil.isCellDateFormatted(cell)) 
        {
        value =  cell.getDateCellValue();
        } 
        else 
        {
         cell.getNumericCellValue();
        }
        break;
      case Cell.CELL_TYPE_BOOLEAN:
       value = cell.getBooleanCellValue();
        break;
      case Cell.CELL_TYPE_FORMULA:
       value = cell.getCellFormula();
        break;
      default:
        
		}
		return value;
	}
	
	//以下是03版的xls创建或获得
	/**
	 * 此方法用于创建特定单元格区域
	 * @param sheet 要创建区域的工作表
	 * @param row1 左上角单元格的行号
	 * @param row2 右下角单元格的行号
	 * @param col1  左上角单元格的列号
	 * @param col2 右下角单元格的列号
	 * @param wrapText 如何为true，则该区域的所有单元格具有自动换行功能
	 */
	public static void createXareaH(HSSFSheet sheet,int row1,int row2,int col1,int col2,boolean wrapText)
	{	
		
		for(int i = row1; i<row2+1;i++)
		{
			HSSFRow row = sheet.createRow(i);
			if(wrapText == true)
			{
				HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
			    style.setWrapText(true);
			    
			    for(int j = col1; j<col2+1;j++)
				{	
					HSSFCell cell = row.createCell(j);
					cell.setCellStyle(style);
				}
			
			}
			else
			{
				 for(int j = col1; j<col2+1;j++)
					{	
					 		row.createCell(j);
					}
			}
		}
	}

	/**
	 * 此方法用于创建特定单元格
	 * @param sheet 要创建单元格的工作表
	 * @param myrow 该单元格的行号
	 * @param mycol 该单元格的列号
	 */
	public static void creatXcellH(HSSFSheet sheet,int myrow,int mycol)
	{ 
		HSSFRow row = sheet.createRow(myrow);
		row.createCell(mycol);
		
	}

	/**
	 * 此方法选取多行
	 * @param sheet 当前工作表
	 * @param row1 从哪一行开始
	 * @param row2到哪一行结束
	 * @return 行的数组
	 */
	public static ArrayList<HSSFRow> getRowsH(HSSFSheet sheet,int row1,int row2)
	{
		ArrayList<HSSFRow> list = new ArrayList<HSSFRow>();
		for(int i =row1;i<row2+1;i++)
		{
			list.add(sheet.getRow(i));
			
		}
		return list;
	}
	
	/**
	 * 此方法选取多行的单元格
	 * @param sheet 当前工作表
	 * @param row1 从哪一行开始
	 * @param row2到哪一行结束
	 * @return 单元格的二维数组
	 */
	public static HSSFCell[][] getCellsByRowsH(HSSFSheet sheet,int row1,int row2)
	{
		HSSFCell[][] cells = new HSSFCell[][]{};
		for(int i = row1;i<row2+1;i++)
		{
			HSSFRow row = sheet.getRow(i);
			for(int j = row.getFirstCellNum();j<row.getLastCellNum();j++)
			{
				cells[i][j] = row.getCell(j);
			}
		}
		return cells;
	}
	
	/**
	 * 此方法选取多列的单元格
	 * @param sheet 当前工作表
	 * @param col1	 从哪一列开始
	 * @param col2 	到哪一列结束
	 * @return 单元格的二维数组
	 */
	public static HSSFCell[][] getCellsByColumnsH(HSSFSheet sheet,int col1,int col2)
	{
		HSSFCell[][] cells = new HSSFCell[][]{};
		for(int i = sheet.getFirstRowNum();i<sheet.getLastRowNum();i++)
		{
			HSSFRow row = sheet.getRow(i);
			for(int j = col1;j<col2;j++)
			{
				cells[i][j] = row.getCell(j);
			}
		}
		return cells;
	}
	
	/**
	 * 此方法全选单元格
	 * @param sheet 当前工作表
	 * @return 单元格的二维数组
	 */
	public static HSSFCell[][] getAllCellsH(HSSFSheet sheet)
	{		HSSFCell[][] cells = new HSSFCell[][]{};
		
		for(int i = sheet.getFirstRowNum(); i <sheet.getLastRowNum();i++)
		{	HSSFRow row = sheet.getRow(i);
			for(int j = row.getFirstCellNum();j<row.getLastCellNum();j++)
			{
				HSSFCell cell = row.getCell(j);
				cells[i][j]=cell;
			}
		}
		return cells;
	}
	
	/**
	 * 此方法获取特定单元格区域
	 * @param sheet 当前工作表
	 * @param row1 	从哪行开始
	 * @param row2	到哪行结束
	 * @param col1	从哪列开始
	 * @param col2	到哪列结束
	 * @return 被选择的单元格二维数组
	 */
	public static HSSFCell[][] getXarea1H(HSSFSheet sheet,int row1,int row2,int col1,int col2)
	{
		HSSFCell[][] cells = new HSSFCell[][]{};
		for(int i = row1; i <row2;i++)
		{	
			for(int j = col1;j<col2;j++)
			{
				cells[i][j] = sheet.getRow(i).getCell(j);
				
			}
		}
		return cells;
		
	}
	
	/**
	 * 此方法获取特定单元格区域，与getXarea1唯一的区别是返回类型
	 * @param sheet 当前工作表
	 * @param row1 	从哪行开始
	 * @param row2	到哪行结束
	 * @param col1	从哪列开始
	 * @param col2	到哪列结束
	 * @return 	单元格集合
	 */
	public static ArrayList<HSSFCell> getXarea2H(HSSFSheet sheet,int row1,int row2,int col1,int col2)
	{
		ArrayList<HSSFCell> list = new ArrayList<HSSFCell>();
		for(int i = row1; i <row2;i++)
		{	
			for(int j = col1;j<col2;j++)
			{
				list.add(sheet.getRow(i).getCell(j));
				
			}
		}
		return list;
		
	}
  /**
   * 此方法获取特定单元格的值
   * @param cell 该单元格
   * 
   */
	public static Object getRealValueH(HSSFCell cell)
	{ 	
		Object value = null;
	switch(cell.getCellType()) 
	{
      case Cell.CELL_TYPE_STRING:
        value = cell.getRichStringCellValue().getString();
        break;
      case Cell.CELL_TYPE_NUMERIC:
        if(DateUtil.isCellDateFormatted(cell)) 
        {
        value =  cell.getDateCellValue();
        } 
        else 
        {
         cell.getNumericCellValue();
        }
        break;
      case Cell.CELL_TYPE_BOOLEAN:
       value = cell.getBooleanCellValue();
        break;
      case Cell.CELL_TYPE_FORMULA:
       value = cell.getCellFormula();
        break;
      default:
        
		}
		return value;
	}

}
