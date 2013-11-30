package com.johnsoft.tools;

import javax.imageio.ImageIO;

public class ImageIOout
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("available ImageWriter suffixes:");
		String[] writerSuffix = ImageIO.getWriterFileSuffixes();
		for (int i = 0; i < writerSuffix.length; i++)
		{
			System.out.print(writerSuffix[i] + "  ");
		}
		
		System.out.println("\navailable ImageReader suffixes:");
		String[] readerSuffix = ImageIO.getReaderFileSuffixes();
		for (int i = 0; i < readerSuffix.length; i++)
		{
			System.out.print(readerSuffix[i] + "  ");
		}
		
		System.out.println("\navailable ImageWriter formatNames:");
		String[] writerFormatName = ImageIO.getWriterFormatNames();
		for (int i = 0; i < writerFormatName.length; i++)
		{
			System.out.print(writerFormatName[i] + "  ");
		}
		
		System.out.println("\navailable ImageReader formatNames:");
		String[] readerFormatName = ImageIO.getReaderFormatNames();
		for (int i = 0; i < readerFormatName.length; i++)
		{
			System.out.print(readerFormatName[i] + "  ");
		}
		
		System.out.println("\navailable ImageWriter MIMEType:");
		String[] writerMIMEType = ImageIO.getWriterMIMETypes();
		for (int i = 0; i < writerMIMEType.length; i++)
		{
			System.out.print(writerMIMEType[i] + "  ");
		}
		
		System.out.println("\navailable ImageReader MIMEType:");
		String[] readerMIMEType = ImageIO.getReaderMIMETypes();
		for (int i = 0; i < readerMIMEType.length; i++)
		{
			System.out.print(readerMIMEType[i] + "  ");
		}
		
	}
}
