package com.johnsoft.library.util.poi;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.johnsoft.library.util.gui.JohnException;


public class JohnHSSFExporter
{
	protected JTable table;
	
	protected String sheetName,title;
	
	protected Workbook workbook;
	protected Sheet sheet;
	
	protected Map<String,CellStyle> styleMap=new HashMap<String, CellStyle>();
	
	protected int currRow;
	protected int beginRow;
	protected int beginCol;
	protected int extraRow;
	protected int extraCol;
	protected int fromCol;
	protected int toCol;
	
	public JohnHSSFExporter(JTable table,String sheetName,String title,boolean useXSSF)
	{
		bingingTable(table, sheetName, title,useXSSF);
	}
	
	public synchronized void bingingTable(JTable table,String sheetName,String title,boolean useXSSF)
	{
		this.table=table;
		this.sheetName=sheetName;
		this.title=title;
		initLocationParams();
		prepareSheet(sheetName,useXSSF);
		prepareCellStyles();
		decorateTitle(title);
		styleHeader();
		styleDataCells();
		fitColumnWidth();
	}
	
	protected synchronized void initLocationParams()
	{
		currRow=0;
		beginRow=0;
		beginCol=0;
		extraRow=100;
		extraCol=0;
		fromCol=0;
		toCol=0;//toCol需大于等于fromCol
	}
	
	protected synchronized void prepareSheet(String sheetName,boolean useXSSF)
	{
		if(useXSSF)
		{
			workbook=new XSSFWorkbook();
		}else{
			workbook=new HSSFWorkbook();
		}
		sheet=workbook.createSheet(sheetName);
		createCells();
	}
	
	protected synchronized void createCells()
	{
		for(int i=beginRow;i<=extraRow+table.getRowCount();i++)
		{
			 Row row = sheet.createRow(i);
			 for(int j=beginCol;j<=extraCol+table.getColumnCount();j++)
			 {
				row.createCell(j);
			 }
		}
	}
	
	public synchronized Cell getCell(int row,int col)
	{
		return sheet.getRow(row).getCell(col);
	}
	
	protected synchronized void prepareCellStyles()
	{
		CellStyle cs_title=workbook.createCellStyle();
		cs_title.setBorderBottom(CellStyle.BORDER_THIN);
		cs_title.setBorderLeft(CellStyle.BORDER_THIN);
		cs_title.setBorderRight(CellStyle.BORDER_THIN);
		cs_title.setBorderTop(CellStyle.BORDER_THIN);
		cs_title.setAlignment(CellStyle.ALIGN_CENTER);
		cs_title.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		Font f_title=workbook.createFont();
		f_title.setFontName("微软雅黑");
		f_title.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f_title.setFontHeightInPoints((short)25);
		cs_title.setFont(f_title);
		styleMap.put("title", cs_title);
		
		CellStyle cs_header=workbook.createCellStyle();
		cs_header.setBorderBottom(CellStyle.BORDER_THIN);
		cs_header.setBorderLeft(CellStyle.BORDER_THIN);
		cs_header.setBorderRight(CellStyle.BORDER_THIN);
		cs_header.setBorderTop(CellStyle.BORDER_THIN);
		cs_header.setAlignment(CellStyle.ALIGN_CENTER);
		cs_header.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs_header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		cs_header.setFillPattern(CellStyle.SOLID_FOREGROUND);
		Font f_header=workbook.createFont();
		f_header.setFontName("微软雅黑");
		f_header.setBoldweight(Font.BOLDWEIGHT_BOLD);
		f_header.setFontHeightInPoints((short)12);
		cs_header.setFont(f_header);
		styleMap.put("header", cs_header);
		
		Font f_content=workbook.createFont();
		f_content.setFontName("微软雅黑");
		f_content.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		f_content.setFontHeightInPoints((short)10);
		
		CellStyle cs_center=workbook.createCellStyle();
		cs_center.setBorderBottom(CellStyle.BORDER_THIN);
		cs_center.setBorderLeft(CellStyle.BORDER_THIN);
		cs_center.setBorderRight(CellStyle.BORDER_THIN);
		cs_center.setBorderTop(CellStyle.BORDER_THIN);
		cs_center.setAlignment(CellStyle.ALIGN_CENTER);
		cs_center.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs_center.setFont(f_content);
		styleMap.put("content%2==0", cs_center);
		
		CellStyle cs_altercenter=workbook.createCellStyle();
		cs_altercenter.setBorderBottom(CellStyle.BORDER_THIN);
		cs_altercenter.setBorderLeft(CellStyle.BORDER_THIN);
		cs_altercenter.setBorderRight(CellStyle.BORDER_THIN);
		cs_altercenter.setBorderTop(CellStyle.BORDER_THIN);
		cs_altercenter.setAlignment(CellStyle.ALIGN_CENTER);
		cs_altercenter.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cs_altercenter.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		cs_altercenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cs_altercenter.setFont(f_content);
		styleMap.put("content%2==1", cs_altercenter);
	}
	
	protected synchronized void decorateTitle(String title)
	{
		if(title==null) return;
		CellRangeAddress addr=new CellRangeAddress(beginRow, beginRow, beginCol, beginCol+table.getColumnCount()-1-toCol);
		sheet.addMergedRegion(addr);
		Row row = sheet.getRow(beginRow);
		row.setHeightInPoints(40);
		for(int i=beginCol;i<beginCol+table.getColumnCount()-toCol;i++)
		{
			Cell cell = row.getCell(i);
			cell.setCellValue(title);
			cell.setCellStyle(styleMap.get("title"));
		}
		currRow++;
	}
	
	protected synchronized void styleHeader()
	{
		Row row = sheet.getRow(beginRow+currRow);
		row.setHeightInPoints(25);
		TableModel tm=table.getModel();
		for(int i=beginCol;i<beginCol+tm.getColumnCount()-toCol;i++)
		{
			Cell cell = row.getCell(i);
			cell.setCellValue(tm.getColumnName(i-beginCol+fromCol));
			cell.setCellStyle(styleMap.get("header"));
		}
		sheet.createFreezePane(0, beginRow+2, 0, beginRow+2);
		currRow++;
	}
	
	protected synchronized void styleDataCells()
	{
		for(int i=beginRow+currRow;i<beginRow+currRow+table.getRowCount();i++)
		{
			 for(int j=beginCol;j<beginCol+table.getColumnCount()-toCol;j++)
			 {
				 Cell cell = getCell(i, j);
				 Object obj=null;
				 obj=table.getValueAt(i-beginRow-currRow, j-beginCol+fromCol);
				 cell.setCellValue(obj!=null?obj.toString():"");
				 if(i%2==0)
				 {
					 cell.setCellStyle(styleMap.get("content%2==0"));
				 }else{
					 cell.setCellStyle(styleMap.get("content%2==1"));
				 }
			 }
		}
		currRow+=table.getRowCount();
	}
	
	protected synchronized void fitColumnWidth()
	{
		for(int i=beginCol;i<beginCol+table.getColumnCount();i++)
		{
			sheet.autoSizeColumn(i);
		}
	}
	
	public synchronized void save(String path)
	{
		try
		{
			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (Exception e)
		{
			JohnException.showException(e, null, null, null);
		}
	}
	
	public synchronized void moveRow(int startRow,int endRow,int targetRow)
	{
		sheet.shiftRows(startRow, endRow, -(startRow-targetRow), true, true);
	}
	
	public synchronized void insertRow(int startRow, int rows)
	{
		 sheet.shiftRows(startRow, sheet.getLastRowNum(), Math.abs(rows),true,true);
	}
	
	public synchronized void deleteRow(int startRow,int rowCount)
	{
		for(int i=0;i!=rowCount;i++)
		{
			removeMerge(startRow+i, -1);
		}
		sheet.shiftRows(startRow+rowCount, sheet.getLastRowNum(), -Math.abs(rowCount), true, true);
	}
	
	public synchronized void cancelFreezePane()
	{
		 sheet.createFreezePane(0, 0, 0, 0);
	}
	
	public synchronized void removeMerge(int firstRow,int firstCol)
	{
		List<Integer> list=new ArrayList<Integer>();
		int n=sheet.getNumMergedRegions();
		for(int i=0;i!=n;++i)
		{
			CellRangeAddress cra=sheet.getMergedRegion(i);
			boolean x=cra.getFirstRow()==firstRow;
			if(firstCol>=0)
			{
				x=x&&cra.getFirstColumn()==firstCol;
			}
			if(x)
			{
				list.add(i);
			}
		}
		n=list.size();
		for(int i=0;i!=n;++i)
		{
			sheet.removeMergedRegion(list.get(i));
		}
	}
	
}
