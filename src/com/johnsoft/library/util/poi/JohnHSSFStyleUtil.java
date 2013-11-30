package com.johnsoft.library.util.poi;

import java.awt.Color;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 此类用于辅助设计工作表的样式和主题
 * 相似方法中带有"H"字符的作用于XLS格式工作簿
 * 关于颜色,可用XSSFColor,或静态引用HSSFColor,IndexedColors
 * 关于边框线型,对齐方式,可静态引用X(H)SSFCellStyle的常量
 * 关于超链接，可静态引用Hyperlink的常量
 * @author 李哲浩
 *
 */
public class JohnHSSFStyleUtil 
{	
	/**
	 * 此方法用于辅助字体相关操作
	 * @param cell 要添加字体样式的单元格
	 * @param font 字体名称,如"Courier New","楷体_GB2312"
	 * 可用GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()查看
	 * @param heightInPoint 字体大小
	 * @param color 字体颜色
	 * @param bold 是否加粗
	 * @param italic 是否倾斜
	 * @return 单元格样式
	 */
	public static XSSFCellStyle fontUtil(XSSFCell cell,String font,short heightInPoint,short color,boolean bold,boolean italic)
	{	
		XSSFCellStyle style = cell.getCellStyle();
		XSSFFont myfont = cell.getSheet().getWorkbook().createFont();
		if(font!= null)
		{myfont.setFontName(font);}
		if(heightInPoint!= 0)
		{myfont.setFontHeightInPoints(heightInPoint);}
		if(color!=0)
		{myfont.setColor(color);}
		if(bold!= false)
		{myfont.setBold(true);}
		if(italic!=false)
		{myfont.setItalic(italic);}
		style.setFont(myfont);
		return style;
	}
	
	/**
	 * 此方法用于辅助边框相关操作
	 * @param cell 要添加边框的单元格
	 * @param top 有无上边框
	 * @param bottom	有无下边框
	 * @param left	有无左边框
	 * @param right	有无右边框
	 * @param borderStyle	边框线型,默认为细线
	 * @param color 边框颜色,默认为黑色,
	 * @return 单元格样式
	 */
	public static XSSFCellStyle borderUtil(XSSFCell cell,boolean top,boolean bottom,boolean left,boolean right,short borderStyle,short color)
	{
		XSSFCellStyle style = cell.getCellStyle();
		if(top == true)
		{	
			if(borderStyle !=0)
			{style.setBorderTop(borderStyle);}
			else
			{style.setBorderTop(XSSFCellStyle.BORDER_THIN);}
			
			if(color != 0)
			{style.setTopBorderColor(color);}
			else
			{	Color colour=new Color(0, 0, 0);
			  XSSFColor xcolor=new XSSFColor(colour);
				style.setTopBorderColor(xcolor.getIndexed());}
		}
			
		if(bottom == true)
		{	
			if(borderStyle !=0)
			{style.setBorderBottom(borderStyle);}
			else
			{style.setBorderBottom(XSSFCellStyle.BORDER_THIN);}
			
			if(color != 0)
			{style.setBottomBorderColor(color);}
			else
			{style.setBottomBorderColor(IndexedColors.BLACK.getIndex());}
		}
				
		if(left == true)
		{	
			if(borderStyle !=0)
			{style.setBorderLeft(borderStyle);}
			else
			{style.setBorderLeft(XSSFCellStyle.BORDER_THIN);}
				
			if(color != 0)
			{style.setLeftBorderColor(color);}
			else
			{style.setLeftBorderColor(IndexedColors.BLACK.getIndex());}
		}
		
		if(right == true)
		{	
			if(borderStyle !=0)
			{style.setBorderRight(borderStyle);}
			else
			{style.setBorderRight(XSSFCellStyle.BORDER_THIN);}
						
			if(color != 0)
			{style.setRightBorderColor(color);}
			else
			{style.setRightBorderColor(IndexedColors.BLACK.getIndex());}
		}
		
	return style;
	}
	
	/**
	 * 用于快速给单元格上四周边框
	 * @param cell 要添加边框的单元格
	 * @param borderStyle 边框线型
	 * @param color 边框颜色
	 * @return	单元格样式
	 */
	public static XSSFCellStyle roundBorderUtil(XSSFCell cell,short borderStyle,short color)
	{ 
		XSSFCellStyle style = cell.getCellStyle();
			
			if(borderStyle !=0)
			{
				style.setBorderRight(borderStyle);
				style.setBorderLeft(borderStyle);
				style.setBorderBottom(borderStyle);
				style.setBorderTop(borderStyle);
			}
			
			
			else
			{
				style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
				style.setBorderRight(XSSFCellStyle.BORDER_THIN);
				style.setBorderTop(XSSFCellStyle.BORDER_THIN);
				
			}
			if(color != 0)
			{
				style.setRightBorderColor(color);
				style.setLeftBorderColor(color);
				style.setBottomBorderColor(color);
				style.setTopBorderColor(color);

			}
			else
			{
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			}
			
		return style;
	}
	
	/**
	 * 此方法用于辅助填充颜色相关操作
	 * @param cell 要填充颜色的单元格
	 * @param color 要填充的颜色
	 * @return 单元格样式
	 */
	public static XSSFCellStyle colorUtil(XSSFCell cell,short color)
	{
		XSSFCellStyle style = cell.getCellStyle();
		style.setFillForegroundColor(color);
		style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	/**
	 * 此方法用于辅助对齐相关操作
	 * @param cell 要应用对齐操作的单元格
	 * @param align 水平对齐方式 ,默认为居中对齐
	 * @param vertical 垂直对齐方式，默认为居中对齐
	 * @return 单元格样式
	 */
	public  static XSSFCellStyle alignUtil(XSSFCell cell,short align,short vertical)
	{	XSSFCellStyle style = cell.getCellStyle();
		if(align != 0)
		{
			style.setAlignment(align);
		}
		else
		{
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		}
		if(vertical!=0)
		{
			style.setVerticalAlignment(vertical);
		}
		else
		{
			style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		}
		return style;
	}
	
	/**
	 * 此方法用于辅助合并居中操作
	 * @param sheet 当前工作表
	 * @param row1     要合并区域的左上角单元格行号
	 * @param row2 	要合并区域的右下角单元格行号
	 * @param col1	要合并区域的左上角单元格列号
	 * @param col2	要合并区域的右下角单元格列号
	 */
	public static void mergedCenter(XSSFSheet sheet,int row1,int row2,int col1,int col2)
	{	XSSFCellStyle style = sheet.getRow(row1).getCell(col1).getCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		sheet.addMergedRegion(new CellRangeAddress(row1, row2, col1,col2));
		sheet.getRow(row1).getCell(col1).setCellStyle(style);
		
	}
	
	/**
	 * 此方法用于辅助区域应用样式
	 * @param sheet 当前工作表
	 * @param style 已设定好的样式
	 * @param row1     应用样式区域的左上角单元格行号
	 * @param row2	应用样式区域的左上角单元格行号
	 * @param col1	应用样式区域的右下角单元格列号
	 * @param col2	应用样式区域的右下角单元格列号
	 */
	public static void areaStyle(XSSFSheet sheet,XSSFCellStyle style,int row1,int row2,int col1, int col2)
	{
		for(int i =row1;i<row2+1;i++)
		{
			 for(int j = col1;j<col2+1;j++)
			{
				sheet.getRow(i).getCell(j).setCellStyle(style);
	 		}
		}
	}
	
	/**
	 * 此方法用于辅助格式化数据操作
	 * @param cell 要格式化的单元格
	 * @param myformat 格式字符串,如"0.0","#,##0.0000"
	 * @return 单元格样式
	 */
	public static  XSSFCellStyle dataFormatUtil(XSSFCell cell,String myformat)
	{	
		DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
		XSSFCellStyle style = cell.getCellStyle();
		style.setDataFormat(format.getFormat(myformat));
		return style ;
		
	}
	
	/**
	 * 此方法用于添加超链接
	 * @param cell 要添加超链接的单元格
	 * @param link_text 链接显示的文本
	 * @param linkfontcolor 链接的字体颜色，默认蓝色
	 * @param linkType 链接类型
	 * @param address  链接的地址
	 */
	public static void hyperlink(XSSFCell cell,String link_text,short linkfontcolor,int linkType,String address)
	{	
		cell.setCellValue(link_text);
		CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
		XSSFCellStyle style = cell.getCellStyle();
		Font hlink_font = cell.getSheet().getWorkbook().createFont();
	    hlink_font.setUnderline(Font.U_SINGLE);
	    if(linkfontcolor!=0)
	    {hlink_font.setColor(linkfontcolor);}
	    else
	    {hlink_font.setColor(IndexedColors.BLUE.getIndex()); }
	    style.setFont(hlink_font);
	    Hyperlink link = createHelper.createHyperlink(linkType);
	    link.setAddress(address);
	    cell.setHyperlink(link);
	    cell.setCellStyle(style);
	}
	
	/**
	 * 此方法用于为单元格插入批注
	 * @param cell 要插入批注的单元格
	 * @param commentText 批注的文本
	 * @param author 批注作者
	 */
	public static void addComment(XSSFCell cell,String commentText,String author)
	{
		CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		RichTextString str = helper.createRichTextString(commentText);
		Drawing draw = cell.getSheet().createDrawingPatriarch();
		Comment comment = draw.createCellComment(anchor);
		comment.setString(str);
		comment.setAuthor(author);
		cell.setCellComment(comment);
	}
	
	//以下是03版的xls创建或获得
	/**
	 * 此方法用于辅助字体相关操作
	 * @param cell 要添加字体样式的单元格
	 * @param font 字体名称,如"Courier New","楷体_GB2312"
	 * 可用GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()查看
	 * @param heightInPoint 字体大小
	 * @param color 字体颜色
	 * @param bold 是否加粗
	 * @param italic 是否倾斜
	 * @return 单元格样式
	 */
	public static HSSFCellStyle fontUtilH(HSSFCell cell,String font,short heightInPoint,short color,boolean bold,boolean italic)
	{	
		HSSFCellStyle style = cell.getCellStyle();
		HSSFFont myfont = cell.getSheet().getWorkbook().createFont();
		if(font!= null)
		{myfont.setFontName(font);}
		if(heightInPoint!= 0)
		{myfont.setFontHeightInPoints(heightInPoint);}
		if(color!=0)
		{myfont.setColor(color);}
		if(bold!= false)
		{myfont.setBoldweight(Font.BOLDWEIGHT_BOLD);}
		if(italic!=false)
		{myfont.setItalic(italic);}
		style.setFont(myfont);
		return style;
	}
	
	/**
	 * 此方法用于辅助边框相关操作
	 * @param cell 要添加边框的单元格
	 * @param top 有无上边框
	 * @param bottom	有无下边框
	 * @param left	有无左边框
	 * @param right	有无右边框
	 * @param borderStyle	边框线型,默认为细线
	 * @param color 边框颜色,默认为黑色
	 * @return 单元格样式
	 */
	public static HSSFCellStyle borderUtilH(HSSFCell cell,boolean top,boolean bottom,boolean left,boolean right,short borderStyle,short color)
	{
		HSSFCellStyle style = cell.getCellStyle();
		if(top == true)
		{	
			if(borderStyle !=0)
			{style.setBorderTop(borderStyle);}
			else
			{style.setBorderTop(HSSFCellStyle.BORDER_THIN);}
			
			if(color != 0)
			{style.setTopBorderColor(color);}
			else
			{style.setTopBorderColor(IndexedColors.BLACK.getIndex());}
		}
			
		if(bottom == true)
		{	
			if(borderStyle !=0)
			{style.setBorderBottom(borderStyle);}
			else
			{style.setBorderBottom(HSSFCellStyle.BORDER_THIN);}
			
			if(color != 0)
			{style.setBottomBorderColor(color);}
			else
			{style.setBottomBorderColor(IndexedColors.BLACK.getIndex());}
		}
				
		if(left == true)
		{	
			if(borderStyle !=0)
			{style.setBorderLeft(borderStyle);}
			else
			{style.setBorderLeft(HSSFCellStyle.BORDER_THIN);}
				
			if(color != 0)
			{style.setLeftBorderColor(color);}
			else
			{style.setLeftBorderColor(IndexedColors.BLACK.getIndex());}
		}
		
		if(right == true)
		{	
			if(borderStyle !=0)
			{style.setBorderRight(borderStyle);}
			else
			{style.setBorderRight(HSSFCellStyle.BORDER_THIN);}
						
			if(color != 0)
			{style.setRightBorderColor(color);}
			else
			{style.setRightBorderColor(IndexedColors.BLACK.getIndex());}
		}
		
	return style;
	}
	
	/**
	 * 用于快速给单元格上四周边框
	 * @param cell 要添加边框的单元格
	 * @param borderStyle 边框线型
	 * @param color 边框颜色
	 * @return	单元格样式
	 */
	public static HSSFCellStyle roundBorderUtilH(HSSFCell cell,short borderStyle,short color)
	{ 
		HSSFCellStyle style = cell.getCellStyle();
			
			if(borderStyle !=0)
			{
				style.setBorderRight(borderStyle);
				style.setBorderLeft(borderStyle);
				style.setBorderBottom(borderStyle);
				style.setBorderTop(borderStyle);
			}
			
			
			else
			{
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				
			}
			if(color != 0)
			{
				style.setRightBorderColor(color);
				style.setLeftBorderColor(color);
				style.setBottomBorderColor(color);
				style.setTopBorderColor(color);

			}
			else
			{
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
			}
			
		return style;
	}
	
	/**
	 * 此方法用于辅助填充颜色相关操作
	 * @param cell 要填充颜色的单元格
	 * @param color 要填充的颜色
	 * @return 单元格样式
	 */
	public static HSSFCellStyle colorUtilH(HSSFCell cell,short color)
	{
		HSSFCellStyle style = cell.getCellStyle();
		style.setFillForegroundColor(color);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return style;
	}
	
	/**
	 * 此方法用于辅助对齐相关操作
	 * @param cell 要应用对齐操作的单元格
	 * @param align 水平对齐方式 ,默认为居中对齐
	 * @param vertical 垂直对齐方式，默认为居中对齐
	 * @return 单元格样式
	 */
	public  static HSSFCellStyle alignUtilH(HSSFCell cell,short align,short vertical)
	{	HSSFCellStyle style = cell.getCellStyle();
		if(align != 0)
		{
			style.setAlignment(align);
		}
		else
		{
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
		if(vertical!=0)
		{
			style.setVerticalAlignment(vertical);
		}
		else
		{
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		}
		return style;
	}
	
	/**
	 * 此方法用于辅助合并居中操作
	 * @param sheet 当前工作表
	 * @param row1     要合并区域的左上角单元格行号
	 * @param row2 	要合并区域的右下角单元格行号
	 * @param col1	要合并区域的左上角单元格列号
	 * @param col2	要合并区域的右下角单元格列号
	 */
	public static void mergedCenterH(HSSFSheet sheet,int row1,int row2,int col1,int col2)
	{	HSSFCellStyle style = sheet.getRow(row1).getCell(col1).getCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		sheet.addMergedRegion(new CellRangeAddress(row1, row2, col1,col2));
		sheet.getRow(row1).getCell(col1).setCellStyle(style);
		
	}
	
	/**
	 * 此方法用于辅助区域应用样式
	 * @param sheet 当前工作表
	 * @param style 已设定好的样式
	 * @param row1     应用样式区域的左上角单元格行号
	 * @param row2	应用样式区域的左上角单元格行号
	 * @param col1	应用样式区域的右下角单元格列号
	 * @param col2	应用样式区域的右下角单元格列号
	 */
	public static void areaStyleH(HSSFSheet sheet,HSSFCellStyle style,int row1,int row2,int col1, int col2)
	{
		for(int i =row1;i<row2+1;i++)
		{
			 for(int j = col1;j<col2+1;j++)
			{
				sheet.getRow(i).getCell(j).setCellStyle(style);
	 		}
		}
	}
	
	/**
	 * 此方法用于辅助格式化数据操作
	 * @param cell 要格式化的单元格
	 * @param myformat 格式字符串,如"0.0","#,##0.0000"
	 * @return 单元格样式
	 */
	public static  HSSFCellStyle dataFormatUtilH(HSSFCell cell,String myformat)
	{	
		DataFormat format = cell.getSheet().getWorkbook().createDataFormat();
		HSSFCellStyle style = cell.getCellStyle();
		style.setDataFormat(format.getFormat(myformat));
		return style ;
		
	}
	
	/**
	 * 此方法用于添加超链接
	 * @param cell 要添加超链接的单元格
	 * @param link_text 链接显示的文本
	 * @param linkfontcolor 链接的字体颜色，默认蓝色
	 * @param linkType 链接类型
	 * @param address  链接的地址
	 */
	public static void hyperlinkH(HSSFCell cell,String link_text,short linkfontcolor,int linkType,String address)
	{	
		cell.setCellValue(link_text);
		CreationHelper createHelper = cell.getSheet().getWorkbook().getCreationHelper();
		HSSFCellStyle style = cell.getCellStyle();
		Font hlink_font = cell.getSheet().getWorkbook().createFont();
	    hlink_font.setUnderline(Font.U_SINGLE);
	    if(linkfontcolor!=0)
	    {hlink_font.setColor(linkfontcolor);}
	    else
	    {hlink_font.setColor(IndexedColors.BLUE.getIndex()); }
	    style.setFont(hlink_font);
	    Hyperlink link = createHelper.createHyperlink(linkType);
	    link.setAddress(address);
	    cell.setHyperlink(link);
	    cell.setCellStyle(style);
	}
	
	/**
	 * 此方法用于为单元格插入批注
	 * @param cell 要插入批注的单元格
	 * @param commentText 批注的文本
	 * @param author 批注作者
	 */
	public static void addCommentH(HSSFCell cell,String commentText,String author)
	{
		CreationHelper helper = cell.getSheet().getWorkbook().getCreationHelper();
		ClientAnchor anchor = helper.createClientAnchor();
		RichTextString str = helper.createRichTextString(commentText);
		Drawing draw = cell.getSheet().createDrawingPatriarch();
		Comment comment = draw.createCellComment(anchor);
		comment.setString(str);
		comment.setAuthor(author);
		cell.setCellComment(comment);
	}
}

	