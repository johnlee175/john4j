package com.johnsoft.library.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 压缩/解压ZIP文件辅助类,支持目录/文件,但不支持中文,文件名请使用英文
 * @author john
 * @date 2014-07-29
 */
public class JohnZipUtils
{
	private JohnZipUtils()
	{
	}

	/**
	 * 解压ZIP文件<br>
	 * (1)如果targetDir中存在zipFile同名文件,将覆盖已有的文件;
	 * (2)解压以后不会删除ZIP文件;
	 * 
	 * @param zipFile
	 *            要解压的ZIP文件
	 * @param targetDir
	 *            要解压到的目录路径
	 */
	public static final void unzip(File zipFile, File targetDir)
	{
		if(zipFile == null || targetDir == null)
		{
			throw new RuntimeException("参数不能为空");
		}
		if(targetDir.isFile())
		{
			throw new RuntimeException("解压目标文件不是一个目录");
		}
		if(!targetDir.exists())
		{
			targetDir.mkdirs();
		}
		ZipFile zip = null;
		try
		{
			zip = new ZipFile(zipFile);
		} catch (IOException e)
		{
			throw new RuntimeException("ZIP文件格式不正确", e);
		}
		Enumeration<? extends ZipEntry> entrys = zip.entries();
		while (entrys.hasMoreElements())
		{
			ZipEntry entry = entrys.nextElement();
			String name = entry.getName();
			if (name.endsWith(File.separator))
			{
				File dir = new File(targetDir, name);
				if (!dir.exists())
				{
					dir.mkdirs();
				}
			} 
			else
			{
				File file = new File(targetDir, name);
				if (file.exists())
				{
					file.delete();
				} 
				else
				{
					File fileParentDir = file.getParentFile();
					if (!fileParentDir.exists())
					{
						fileParentDir.mkdirs();
					}
				}
				InputStream in = null;
				FileOutputStream fos = null;
				try
				{
					in = zip.getInputStream(entry);
					fos = new FileOutputStream(file);
					byte[] bytes = new byte[4096];
					int len = -1;
					while ((len = in.read(bytes)) > 0)
					{
						fos.write(bytes, 0, len);
					}
				} catch (IOException e)
				{
					throw new RuntimeException("ZIP文件流输入输出过程中发生错误", e);
				} finally
				{
					try
					{
						if(fos != null)
							fos.close();
						if(in != null)
							in.close();
					} catch (IOException e)
					{
						throw new RuntimeException("ZIP文件流关闭失败", e);
					}
				}
			}
		}
	}

	/**
	 * 创建ZIP文件
	 * 
	 * @param sourceFile
	 *            要压缩的文件或文件夹路径
	 * @param zipFile
	 *            生成的ZIP文件存在路径(包括文件名)
	 */
	public static final void zip(File sourceFile, File zipFile)
			throws RuntimeException
	{
		if(sourceFile == null || zipFile == null)
		{
			throw new RuntimeException("参数不能为空");
		}
		if(!sourceFile.exists())
		{
			throw new RuntimeException("要处理的某个源文件不存在");
		}
		ZipOutputStream zos = null;
		try
		{
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e)
		{
			throw new RuntimeException("无法确认ZIP文件路径", e);
		}
		zip(sourceFile, "", zos);
		try
		{
			if (zos != null)
			{
				zos.close();
			}
		} catch (IOException e)
		{
			throw new RuntimeException("关闭ZIP文件输出流失败", e);
		}
	}
	
	/**
	 * 创建ZIP文件
	 * 
	 * @param sourceFiles
	 *            要压缩的多个文件或文件夹路径
	 * @param zipFile
	 *            生成的ZIP文件存在路径(包括文件名)
	 */
	public static final void zip(File[] sourceFiles, File zipFile)
			throws RuntimeException
	{
		if(sourceFiles == null || zipFile == null)
		{
			throw new RuntimeException("参数不能为空");
		}
		for(File sourceFile : sourceFiles)
		{
			if(!sourceFile.exists())
			{
				throw new RuntimeException("要处理的某个源文件不存在");
			}
		}
		ZipOutputStream zos = null;
		try
		{
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e)
		{
			throw new RuntimeException("无法确认ZIP文件路径", e);
		}
		for(File sourceFile : sourceFiles)
		{
			zip(sourceFile, "", zos);
		}
		try
		{
			if (zos != null)
			{
				zos.close();
			}
		} catch (IOException e)
		{
			throw new RuntimeException("关闭ZIP文件输出流失败", e);
		}
	}

	//do ZIP
	private static final void zip(File sourceFile, String parentPath,
			ZipOutputStream zos) throws RuntimeException
	{
		if (sourceFile.isDirectory())
		{
			parentPath += sourceFile.getName() + File.separator;
			File[] files = sourceFile.listFiles();
			if(files.length > 0)
			{
				for (int i = 0; i < files.length; ++i)
				{
					zip(files[i], parentPath, zos);
				}
			}
			else
			{
				ZipEntry ze = new ZipEntry(parentPath);
				try
				{
					zos.putNextEntry(ze);
				} catch (IOException e)
				{
					throw new RuntimeException("无法创建空目录的ZIP条目", e);
				}
			}
		} 
		else
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(sourceFile);
				ZipEntry ze = new ZipEntry(parentPath + sourceFile.getName());
				zos.putNextEntry(ze);
				byte[] bytes = new byte[4096];
				int len;
				while ((len = fis.read(bytes)) > 0)
				{
					zos.write(bytes, 0, len);
					zos.flush();
				}
			} catch (FileNotFoundException e)
			{
				throw new RuntimeException("无法确认某个源文件或源文件夹路径", e);
			} catch (IOException e)
			{
				throw new RuntimeException("ZIP文件生成过程中输入输出发生错误", e);
			} finally
			{
				try
				{
					if (fis != null)
					{
						fis.close();
					}
				} catch (IOException e)
				{
					throw new RuntimeException("关闭某个源文件输出流失败", e);
				}
			}
		}
	}
}
