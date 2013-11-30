package com.johnsoft.library.util.poi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 此类用于辅助操作Excel表中嵌入图片的问题
 * 相似方法中带有"H"字符的作用于XLS格式工作簿
 * @author 李哲浩
 *
 */
public class JohnHSSFImageUtil
{
	/**
	 * 此方法用于向工作表特定位置插入图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param pictureType 图片类型,静态引用Workbook的常量
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号
	 */
	public static void insertPicture(XSSFSheet sheet,String path,int pictureType,int row1,int row2,int col1,int col2)
	{	XSSFWorkbook wb = sheet.getWorkbook();
		InputStream is =null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int picid = wb.addPicture(bytes,pictureType);
		
		CreationHelper helper = wb.getCreationHelper();
		XSSFDrawing draw = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(col1);
		anchor.setRow1(row1);
		anchor.setCol2(col2);
		anchor.setRow2(row2);
		draw.createPicture(anchor, picid);
	}
	
	/**
	 * 此方法用于向工作表特定位置插入JPEG格式图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号 
	 */
	public static void insertJpgPicture(XSSFSheet sheet,String path,int row1,int row2,int col1,int col2)
	{	
		insertPicture(sheet, path,Workbook.PICTURE_TYPE_JPEG, row1, row2, col1, col2);
	}
	
	/**
	 * 此方法用于向工作表特定位置插入PNG格式图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号 
	 */
	public static void insertPngPicture(XSSFSheet sheet,String path,int row1,int row2,int col1,int col2)
	{	
		insertPicture(sheet, path,Workbook.PICTURE_TYPE_PNG, row1, row2, col1, col2);
	}
	
	//以下是03版的xls创建或获得
	/**
	 * 此方法用于向工作表特定位置插入图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param pictureType 图片类型,静态引用Workbook的常量
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号
	 */
	public static void insertPictureH(HSSFSheet sheet,String path,int pictureType,int row1,int row2,int col1,int col2)
	{	HSSFWorkbook wb = sheet.getWorkbook();
		InputStream is =null;
		try {
			is = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] bytes = null;
		try {
			bytes = IOUtils.toByteArray(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int picid = wb.addPicture(bytes,pictureType);
		
		CreationHelper helper = wb.getCreationHelper();
		HSSFPatriarch draw = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(col1);
		anchor.setRow1(row1);
		anchor.setCol2(col2);
		anchor.setRow2(row2);
		draw.createPicture(anchor, picid);
	}
	
	/**
	 * 此方法用于向工作表特定位置插入JPEG格式图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号 
	 */
	public static void insertJpgPictureH(HSSFSheet sheet,String path,int row1,int row2,int col1,int col2)
	{	
		insertPictureH(sheet, path,Workbook.PICTURE_TYPE_JPEG, row1, row2, col1, col2);
	}
	
	/**
	 * 此方法用于向工作表特定位置插入PNG格式图片
	 * @param sheet 当前工作表
	 * @param path 图片路径
	 * @param row1	左上角单元格的行号
	 * @param row2	右下角单元格的行号
	 * @param col1	左上角单元格的列号
	 * @param col2 	右下角单元格的列号 
	 */
	public static void insertPngPictureH(HSSFSheet sheet,String path,int row1,int row2,int col1,int col2)
	{	
		insertPictureH(sheet, path,Workbook.PICTURE_TYPE_PNG, row1, row2, col1, col2);
	}
}
