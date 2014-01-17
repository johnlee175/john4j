package com.johnsoft.library.swing.effect;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;

public class JohnCanvas extends Component
{
	private static final long serialVersionUID = 1L;
	
	//用户坐标范围(缺省为x[-1,1]y[-1,1])
	protected double userMinx = -1; //用户坐标系的X轴的最小值
	protected double userMaxx = 1; //用户坐标系的X轴的最大值
	protected double userMiny = -1; //用户坐标系的Y轴的最小值
	protected double userMaxy = 1; //用户坐标系的Y轴的最大值
	//视图范围(缺省为x[0,1]y[0,1])
	protected double[] viewMinx; //视图的X轴的最小值
	protected double[] viewMaxx; //视图的X轴的最大值
	protected double[] viewMiny; //视图的Y轴的最小值
	protected double[] viewMaxy; //视图的Y轴的最大值
	final static int DefaultViewportMax = 256; //缺省的视图数
	protected int viewportMax = DefaultViewportMax; //视图数
	protected int viewportNum = 0; //当前视图数
	protected int currentViewport = 0; //当前视图的索引
	//窗口大小
	final static int DefaultWindowSize = 256; //缺省的窗口大小
	protected int windowWidth = DefaultWindowSize; //窗口宽度
	protected int windowHeight = DefaultWindowSize; //窗口高度
	//Java AWT Graphics and its Component
	protected Graphics graphics; //JohnCanvas的Graphics
	protected Component component; //JohnCanvas的Component
	protected Color currentFrontColor = Color.WHITE; //当前前景色
	protected Color currentBackColor = Color.BLACK; //当前背景色	
	//用于线段的光栅化
	protected Image image; //使用MemoryImageSource的Image类数据
	protected MemoryImageSource mis; //MemoryImageSource数据
	protected int[] pixel; //存放为生成MemoryImageSource的色值的数组
	protected int pixelWidth; //上述的pixel数据的宽度
	protected int pixelHeight; //上述的pixel数据的高度
	protected int xoffset; //像素数据的窗口内X坐标中的偏移
	protected int yoffset; //像素数据的窗口内Y坐标中的偏移
	//用于增强MoveTo(x, y)和LineTo(x, y)
	protected double lastx = 0; //当前X值
	protected double lasty = 0; //当前Y值
	
	public JohnCanvas(Component c)
	{
		component = c; 
		graphics = c.getGraphics();
		windowWidth = c.getWidth();
		windowHeight = c.getHeight();
		createViewport(DefaultViewportMax);
	}
	
	//创建缺省视图
	private void createViewport(int max)
	{
		currentViewport = 0; //视图索引的初始值设定
		viewportMax = max; //设定视图数的最大值
		viewMinx = new double[viewportMax];//视图的X轴的最小值数组
		viewMaxx = new double[viewportMax];//视图的X轴的最大值数组
		viewMiny = new double[viewportMax];//视图的Y轴的最小值数组
		viewMaxy = new double[viewportMax];//视图的Y轴的最大值数组
		viewMinx[0] = viewMiny[0] = 0.0; //视图的最小值为0
		viewMaxx[0] = viewMaxy[0] = 1.0; //视图的最大值为1
		viewportNum = 1; //视图的当前索引为1
	}
	
	/**
	 * 用户坐标系的范围设定
	 * @param x1  窗口的X轴最小值
	 * @param x2  窗口的X轴最大值
	 * @param y1  窗口的Y轴最小值
	 * @param y2  窗口的Y轴最大值
	 */
	public void setWindow(double x1, double x2, double y1, double y2)
	{
		userMinx = x1;
		userMaxx = x2;
		userMiny = y1;
		userMaxy = y2;
	}
	
	/**
	 * 视图的设定
	 * @param x1  当前视图的X轴最小值
	 * @param x2  当前视图的X轴最大值
	 * @param y1  当前视图的Y轴最小值
	 * @param y2  当前视图的Y轴最大值
	 */
	public void setViewport(double x1, double x2, double y1, double y2)
	{
		viewMinx[viewportNum] = x1;
		viewMaxx[viewportNum] = x2;
		viewMiny[viewportNum] = y1;
		viewMaxy[viewportNum] = y2;
		currentViewport = viewportNum;
		viewportNum++;
		setClip(x1, y1, x2, y2, true);
	}
	
	/** 视图的复位*/
	public void resetViewport()
	{
		currentViewport = 0;
		viewMinx[0] = viewMiny[0] = 0.0;
		viewMaxx[0] = viewMaxy[0] = 1.0;
		viewportNum = 1;
	}
	
	/** 设定视图剪切*/
	public void setClip(double x1, double x2, double y1, double y2, boolean flag)
	{
		int ix1 = getIntX(x1); //将4个角中任意一个点的X坐标值转换到Java坐标值
		int iy1 = getIntY(y1); //与x1相同点y坐标值转换到Java坐标值
		int ix2 = getIntX(x2); //与x1对角线上的角的x坐标值转换到Java坐标值
		int iy2 = getIntY(y2); //与y1对角线上的角的y坐标值转换到Java坐标值
		int w = Math.abs(ix1 - ix2) + 1; //宽度计算
		int h = Math.abs(iy1 - iy2) + 1; //高度计算
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角点的X坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角点的Y坐标
		graphics.setClip(x0, y0, w, h);
	}
	
	//******************* 将视图坐标转换到Java AWT坐标方法  *******************//
	
	/** 窗口宽度加倍*/
	public int getIntX(double x)
	{
		return (int)(windowWidth * x);
	}
	
	/** 窗口高度加倍*/
	public int getIntY(double y)
	{
		return (int)(windowHeight * (1-y));
	}
	
	//******************* 将用户坐标转换到视图坐标方法  *******************//
	
	public double viewX(double x)
	{
		double s = (x - userMinx) / (userMaxx - userMinx);
		double t = viewMinx[currentViewport] + s * (viewMaxx[currentViewport] - viewMinx[currentViewport]);
		return t;
	}
	
	public double viewY(double y)
	{
		double s = (y - userMiny) / (userMaxy - userMiny);
		double t = viewMiny[currentViewport] + s * (viewMaxy[currentViewport] - viewMiny[currentViewport]);
		return t;
	}
	
	//******************* 将用户坐标转换到Java AWT坐标方法  *******************//
	
	public int getX(double x)
	{
		double xx = viewX(x);
		int ix = getIntX(xx);
		return ix;
	}
	
	public int getY(double y)
	{
		double yy = viewY(y);
		int iy = getIntY(yy);
		return iy;
	}
	
	//******************* 得到Dimension的方法  *******************//
	
	public int getDimensionX(double w)
	{
		double x = viewMaxx[currentViewport] - viewMinx[currentViewport];
		x *= windowWidth * w / (userMaxx - userMinx);
		return ((int)Math.abs(x));
	}
	
	public int getDimensionY(double h)
	{
		double y = viewMaxy[currentViewport] - viewMiny[currentViewport];
		y *= windowHeight * h / (userMaxy - userMiny);
		return ((int)Math.abs(y));
	}
	
	//******************* Java坐标系到用户坐标系的反向转换的方法  *******************//
	
	/** Java坐标到视图的反向转换*/
	public int getViewport(int ix, int iy)
	{
		if(viewportNum == 1) return 0; //默认视图
		double s = (double)(ix) / (double)windowWidth;
		double t = (double)(windowHeight - iy) / (double)windowHeight;
		for(int i = 0; i < viewportNum; i++)
		{
			if(s >= viewMinx[i] && s <= viewMaxx[i] &&
					t >= viewMiny[i] && t <= viewMaxy[i])
				return i;
		}
		return 0;
	}
	
	/** 视图到用户坐标系的反向转换(X坐标)*/
	public double getUserX(int ix, int v)
	{
		double t = (double)(ix) / (double)windowWidth;
		double x = userMinx + (t - viewMinx[v]) / (viewMaxx[v] - viewMinx[v]) *
				(userMaxx - userMinx);
		return x;
	}
	
	/** 视图到用户坐标系的反向转换(Y坐标)*/
	public double getUserY(int iy, int v)
	{
		double t = (double)(windowHeight - iy) / (double)windowHeight;
		double y = userMiny + (t - viewMiny[v]) / (viewMaxy[v] - viewMiny[v]) *
				(userMaxy - userMiny);
		return y;
	}
	
	//******************* 有关颜色设定的方法  *******************//
	
	/** 当前的颜色检索*/
	public Color getColor()
	{
		return currentFrontColor;
	}
	
	/** 当前的颜色设定*/
	public void setColor(Color c)
	{
		graphics.setColor(c);
		currentFrontColor = c;
	}
	
	/** 前景色检索*/
	public Color getForeground()
	{
		return currentFrontColor;
	}
	
	/** 前景色设定*/
	public void setForeground(Color c)
	{
		component.setForeground(c);
		currentFrontColor = c;
	}
	
	/** 背景色检索*/
	public Color getBackground()
	{
		return currentBackColor;
	}
	
	/** 背景色设定*/
	public void setBackground(Color c)
	{
		component.setBackground(c);
		currentBackColor = c;
	}
	
	//******************* 有关描画的方法  *******************//
	
	/** 画直线,传入左上角和右下角用户坐标*/
	public void drawLine(double x1, double y1, double x2, double y2)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		graphics.drawLine(ix1, iy1, ix2, iy2);
	}
	
	/** 画矩形,传入左上角和右下角用户坐标*/
	public void drawRect(double x1, double y1, double x2, double y2)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		graphics.drawRect(x0, y0, w, h);
	}
	
	/** 填充矩形,传入左上角和右下角用户坐标*/
	public void fillRect(double x1, double y1, double x2, double y2)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		graphics.fillRect(x0, y0, w, h);
	}
	
	/** 清除矩形,传入左上角和右下角用户坐标*/
	public void clearRect(double x1, double y1, double x2, double y2)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		graphics.clearRect(x0, y0, w, h);
	}
	
	/** 画圆角矩形,传入左上角和右下角用户坐标*/
	public void drawRoundRect(double x1, double y1, double x2, double y2,
			double arcW, double arcH)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		int iarcW = getDimensionX(arcW);
		int iarcH = getDimensionY(arcH);
		graphics.drawRoundRect(x0, y0, w, h, iarcW, iarcH);
	}
	
	/** 填充圆角矩形,传入左上角和右下角用户坐标*/
	public void fillRoundRect(double x1, double y1, double x2, double y2,
			double arcW, double arcH)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		int iarcW = getDimensionX(arcW);
		int iarcH = getDimensionY(arcH);
		graphics.fillRoundRect(x0, y0, w, h, iarcW, iarcH);
	}
	
	/** 画3D阴影矩形,传入左上角和右下角用户坐标*/
	public void draw3DRect(double x1, double y1, double x2, double y2, boolean raised)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		graphics.draw3DRect(x0, y0, w, h, raised);
	}
	
	/** 填充3D阴影矩形,传入左上角和右下角用户坐标*/
	public void fill3DRect(double x1, double y1, double x2, double y2, boolean raised)
	{   //转换到Java AWT坐标值
		int ix1 = getX(x1);
		int iy1 = getY(y1);
		int ix2 = getX(x2);
		int iy2 = getY(y2);
		//计算宽高
		int w = Math.abs(ix1 - ix2) + 1;
		int h = Math.abs(iy1 - iy2) + 1;
		int x0 = (ix1 <= ix2) ? ix1 : ix2; //左上角X Java坐标
		int y0 = (iy1 <= iy2) ? iy1 : iy2; //左上角Y Java坐标
		graphics.fill3DRect(x0, y0, w, h, raised);
	}
	
	/** 画椭圆,中心在(x,y)用户坐标,半径为用户坐标长度(xr,yr)*/
	public void drawOval(double x, double y, double xr, double yr)
	{
		int ix = getX(x); //椭圆的中心在Java AWT中的X坐标
		int iy = getY(y); //椭圆的中心在Java AWT中的Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //包围椭圆矩形的左上角X Java坐标
		int y0 = iy - iyr; //包围椭圆矩形的左上角Y Java坐标
		graphics.drawOval(x0, y0, 2 * ixr, 2 * iyr);
	}
	
	/** 填充椭圆,中心在(x,y)用户坐标,半径为用户坐标长度(xr,yr)*/
	public void fillOval(double x, double y, double xr, double yr)
	{
		int ix = getX(x); //椭圆的中心在Java AWT中的X坐标
		int iy = getY(y); //椭圆的中心在Java AWT中的Y坐标
		int ixr = getDimensionX(xr); //半径的宽度
		int iyr = getDimensionY(yr); //半径的高度
		int x0 = ix - ixr; //包围椭圆矩形的左上角X Java坐标
		int y0 = iy - iyr; //包围椭圆矩形的左上角Y Java坐标
		graphics.fillOval(x0, y0, 2 * ixr, 2 * iyr);
	}
	
	/** 画圆弧,中心在(x,y)用户坐标,半径为用户坐标长度(xr,yr),startAngle是起始角度,arcAngle是要描画的圆弧夹角度数*/
	public void drawArc(double x, double y, double xr, double yr,
			double startAngle, double arcAngle)
	{
		int ix = getX(x);
		int iy = getY(y);
		int ixr = getDimensionX(xr);
		int iyr = getDimensionY(yr);
		int x0 = ix - ixr; //包围圆弧矩形的左上角X Java坐标
		int y0 = iy - iyr; //包围圆弧矩形的左上角Y Java坐标
		int is = (int)(90 - (startAngle + arcAngle)); //起始角度度数
		int ia = (int)arcAngle; //圆弧夹角度数
		graphics.drawArc(x0, y0, 2 * ixr, 2 * iyr, is, ia);
	}
	
	/** 填充扇形,中心在(x,y)用户坐标,半径为用户坐标长度(xr,yr),startAngle是起始角度,arcAngle是要填充的扇形夹角度数*/
	public void fillArc(double x, double y, double xr, double yr,
			double startAngle, double arcAngle)
	{
		int ix = getX(x);
		int iy = getY(y);
		int ixr = getDimensionX(xr);
		int iyr = getDimensionY(yr);
		int x0 = ix - ixr; //包围圆弧矩形的左上角X Java坐标
		int y0 = iy - iyr; //包围圆弧矩形的左上角Y Java坐标
		int is = (int)(90 - (startAngle + arcAngle)); //起始角度度数
		int ia = (int)arcAngle; //扇形夹角度数
		graphics.fillArc(x0, y0, 2 * ixr, 2 * iyr, is, ia);
	}
	
	/** 画折线*/
	public void drawPolyline(double[] x, double[] y, int numPoints)
	{
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for(int i = 0; i < numPoints; i++)
		{
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		graphics.drawPolyline(ix, iy, numPoints);
	}
	
	/** 画多边形*/
	public void drawPolygon(double[] x, double[] y, int numPoints)
	{
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for(int i = 0; i < numPoints; i++)
		{
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		graphics.drawPolygon(ix,iy,numPoints);
	}
	
	/** 填充多边形*/
	public void fillPolygon(double[] x, double[] y, int numPoints)
	{
		int[] ix = new int[numPoints];
		int[] iy = new int[numPoints];
		for(int i = 0; i < numPoints; i++)
		{
			ix[i] = getX(x[i]);
			iy[i] = getY(y[i]);
		}
		graphics.fillPolygon(ix,iy,numPoints);
	}
	
	/** 刻画文字*/
	public void drawString(String str, double x, double y)
	{
		int ix = getX(x);
		int iy = getY(y);
		graphics.drawString(str, ix, iy);
	}
	
	/** 贴图*/
	public boolean drawImage(Image img, double x, double y, ImageObserver observer)
	{
		int ix = getX(x);
		int iy = getY(y);
		return graphics.drawImage(img, ix, iy, observer);
	}
	
	/** 贴图*/
	public boolean drawImage(Image img, double x, double y, double w, double h, ImageObserver observer)
	{
		int ix = getX(x);
		int iy = getY(y);
		int iw = getDimensionX(w);
		int ih = getDimensionY(h);
		return graphics.drawImage(img, ix, iy, iw, ih, observer);
	}
	
	/** 字体检索*/
	public Font getFont()
	{
		return graphics.getFont();
	}
	
	/** 设置字体*/
	public void setFont(Font f)
	{
		graphics.setFont(f);
	}
	
	/** 当前Graphics类的字体设定*/
	public Font MyFont(String name, int style, double size)
	{
		int DefaultFontSize = 12;
		if(size <= 0)
			size = 1.0; //大小为负则取缺省值
		int isize = (int)(DefaultFontSize * size); //字体大小换算
		Font f = new Font(name, style, isize);
		return f;
	}
	
	/** 为了描画而定当前起始位置*/
	public void moveTo(double x, double y)
	{
		lastx = x;
		lasty = y;
	}
	
	/** 画直线,与moveTo连用*/
	public void lineTo(double x, double y)
	{
		drawLine(lastx, lasty, x, y);
		lastx = x;
		lasty = y;
	}
	
	/** 设定像素的当前色的方法*/
	public void putPixel(int i , int j)
	{
		int r = getColor().getRed() & 0xff;
		int g = getColor().getGreen() & 0xff;
		int b = getColor().getBlue() & 0xff;
		int a = 0xff000000 | (r << 16) | (g << 8) | b;
		pixel[(pixelHeight - 1 - (j - yoffset)) * pixelWidth + (i - xoffset)] = a;
	}
	
	/** 画线(光栅化版)实数型Bresenham算法*/
	public void rasterizeDrawLine(double x1, double y1, double x2, double y2)
	{
		double leftTopx, leftTopy;
		int ix1 = getX(x1);
		int iy1 = windowHeight - getY(y1);
		int ix2 = getX(x2);
		int iy2 = windowHeight - getY(y2);
		if(x1 < x2)
		{
			leftTopx = x1; //左上角X坐标值设定为x1
			xoffset = ix1; //窗口中的偏移设定
		}else{
			leftTopx = x2; //左上角X坐标值设定为x1
			xoffset = ix2; //窗口中的偏移设定
		}
		if(y1 < y2)
		{
			leftTopy = y2; //左上角Y坐标值设定为y2
			yoffset = iy1; //窗口中的偏移设定
		}else{
			leftTopy = y1; //左上角Y坐标值设定为y1
			yoffset = iy2; //窗口中的偏移设定
		}
		int dx = ix2 - ix1; //取得X方向的差分
		int dy = iy2 - iy1; //取得Y方向的差分
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);
		pixelWidth = adx + 1; //分配数组的宽度
		pixelHeight = ady + 1; //分配数组的高度
		pixel = new int[pixelWidth * pixelHeight];
		for(int k = 0; k < pixelWidth * pixelHeight; k++)
			pixel[k] = 0x00000000; //背景完全透明
		int sx = Sign(dx);
		int sy = Sign(dy);
		int x = ix1;
		int y = iy1;
		if(adx == 0)
		{	//Y = 常数的直线
			for(int j = 1; j < ady; j++)
			{
				putPixel(x, y);
				y += sy;
			}
		}
		else if(ady == 0)
		{	//X = 常数的直线
			for(int i = 0; i <= adx; i++)
			{
				putPixel(x, y);
				x += sx;
			}
		}
		else if(adx > ady)
		{	//X方向的角度大时
			double d = (double)dy / (double)dx;
			double ty = (double)y;
			for(int i = 1; i <= adx; i++, x += sx)
			{
				putPixel(x, y);
				ty += sx * d; //加上Y轴的递增量(注意符号)
				if(Math.abs(ty - y) > Math.abs(ty - (y + sy)))
					y += sy; //沿直线前进方向增加Y的值
			}
		}
		else
		{	//adx <= ady Y方向的角度大或和X方向相等时
			double d = (double)dx / (double)dy;
			double tx = (double)x;
			for(int j = 1; j <= ady; j++, y += sy)
			{
				putPixel(x, y);
				tx += sy * d; //加上X轴的递增量(注意符号)
				if(Math.abs(tx - x) > Math.abs(tx - (x + sx)))
					x += sx; //沿直线前进方向增加X的值
			}
		}
		mis = new MemoryImageSource(pixelWidth, pixelHeight, pixel, 0, pixelWidth);
		image = createImage(mis);
		drawImage(image, leftTopx, leftTopy, this);
	}
	
	/**返回i的符号*/
	public int Sign(int i)
	{
		if(i > 0)
			return 1;
		else if(i < 0)
			return -1;
		else
			return 0;
	}
}
