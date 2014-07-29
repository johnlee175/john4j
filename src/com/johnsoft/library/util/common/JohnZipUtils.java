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
 * ѹ��/��ѹZIP�ļ�������,֧��Ŀ¼/�ļ�,����֧������,�ļ�����ʹ��Ӣ��
 * @author john
 * @date 2014-07-29
 */
public class JohnZipUtils
{
	private JohnZipUtils()
	{
	}

	/**
	 * ��ѹZIP�ļ�<br>
	 * (1)���targetDir�д���zipFileͬ���ļ�,���������е��ļ�;
	 * (2)��ѹ�Ժ󲻻�ɾ��ZIP�ļ�;
	 * 
	 * @param zipFile
	 *            Ҫ��ѹ��ZIP�ļ�
	 * @param targetDir
	 *            Ҫ��ѹ����Ŀ¼·��
	 */
	public static final void unzip(File zipFile, File targetDir)
	{
		if(zipFile == null || targetDir == null)
		{
			throw new RuntimeException("��������Ϊ��");
		}
		if(targetDir.isFile())
		{
			throw new RuntimeException("��ѹĿ���ļ�����һ��Ŀ¼");
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
			throw new RuntimeException("ZIP�ļ���ʽ����ȷ", e);
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
					throw new RuntimeException("ZIP�ļ���������������з�������", e);
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
						throw new RuntimeException("ZIP�ļ����ر�ʧ��", e);
					}
				}
			}
		}
	}

	/**
	 * ����ZIP�ļ�
	 * 
	 * @param sourceFile
	 *            Ҫѹ�����ļ����ļ���·��
	 * @param zipFile
	 *            ���ɵ�ZIP�ļ�����·��(�����ļ���)
	 */
	public static final void zip(File sourceFile, File zipFile)
			throws RuntimeException
	{
		if(sourceFile == null || zipFile == null)
		{
			throw new RuntimeException("��������Ϊ��");
		}
		if(!sourceFile.exists())
		{
			throw new RuntimeException("Ҫ�����ĳ��Դ�ļ�������");
		}
		ZipOutputStream zos = null;
		try
		{
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e)
		{
			throw new RuntimeException("�޷�ȷ��ZIP�ļ�·��", e);
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
			throw new RuntimeException("�ر�ZIP�ļ������ʧ��", e);
		}
	}
	
	/**
	 * ����ZIP�ļ�
	 * 
	 * @param sourceFiles
	 *            Ҫѹ���Ķ���ļ����ļ���·��
	 * @param zipFile
	 *            ���ɵ�ZIP�ļ�����·��(�����ļ���)
	 */
	public static final void zip(File[] sourceFiles, File zipFile)
			throws RuntimeException
	{
		if(sourceFiles == null || zipFile == null)
		{
			throw new RuntimeException("��������Ϊ��");
		}
		for(File sourceFile : sourceFiles)
		{
			if(!sourceFile.exists())
			{
				throw new RuntimeException("Ҫ�����ĳ��Դ�ļ�������");
			}
		}
		ZipOutputStream zos = null;
		try
		{
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e)
		{
			throw new RuntimeException("�޷�ȷ��ZIP�ļ�·��", e);
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
			throw new RuntimeException("�ر�ZIP�ļ������ʧ��", e);
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
					throw new RuntimeException("�޷�������Ŀ¼��ZIP��Ŀ", e);
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
				throw new RuntimeException("�޷�ȷ��ĳ��Դ�ļ���Դ�ļ���·��", e);
			} catch (IOException e)
			{
				throw new RuntimeException("ZIP�ļ����ɹ��������������������", e);
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
					throw new RuntimeException("�ر�ĳ��Դ�ļ������ʧ��", e);
				}
			}
		}
	}
}
