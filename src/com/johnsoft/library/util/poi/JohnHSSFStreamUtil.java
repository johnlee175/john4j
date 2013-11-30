package com.johnsoft.library.util.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 此类用于辅助读入写出工作簿
 * 相似方法中带有"H"字符的作用于XLS格式工作簿
 * @author 李哲浩
 *
 */
public class JohnHSSFStreamUtil
{
	/**
	 * 此方法用于将Excel文件读入内存，以进一步操作
	 * @param workbookpath 要读取的Excel文件路径
	 * @param sheetname 要读取的Excel表名称
	 * @return 该工作表的所有单元格数组
	 */
	public static  XSSFCell[][] fileIn(String workbookpath,String sheetname)
	{	
		XSSFWorkbook wb = null;
		XSSFCell[][] cells = null;
		InputStream is = null;
		try {
			is = new FileInputStream(workbookpath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 wb = (XSSFWorkbook)WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XSSFSheet sheet = wb.getSheet(sheetname);
		cells = JohnHSSFRowCellUtil.getAllCells(sheet);
		return cells;
	}
	
	/**
	 * 此方法用于将做好的工作簿写出出去
	 * @param path 要生成的工作簿存放路径
	 * @param wb	要生成的工作簿名称
	 */
	public static void fileOut(String path,XSSFWorkbook wb)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wb.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//以下是03版的xls创建或获得
	/**
	 * 此方法用于将Excel文件读入内存，以进一步操作
	 * @param workbookpath 要读取的Excel文件路径
	 * @param sheetname 要读取的Excel表名称
	 * @return 该工作表的所有单元格数组
	 */
	public static  HSSFCell[][] fileInH(String workbookpath,String sheetname)
	{	
		HSSFWorkbook wb = null;
		HSSFCell[][] cells = null;
		InputStream is = null;
		try {
			is = new FileInputStream(workbookpath);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			 wb = (HSSFWorkbook)WorkbookFactory.create(is);
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		HSSFSheet sheet = wb.getSheet(sheetname);
		cells = JohnHSSFRowCellUtil.getAllCellsH(sheet);
		return cells;
	}
	
	/**
	 * 此方法用于将做好的工作簿写出出去
	 * @param path 要生成的工作簿存放路径
	 * @param wb	要生成的工作簿名称
	 */
	public static void fileOutH(String path,HSSFWorkbook wb)
	{
		OutputStream os = null;
		try {
			os = new FileOutputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			wb.write(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
