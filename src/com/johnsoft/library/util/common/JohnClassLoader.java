package com.johnsoft.library.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class JohnClassLoader extends ClassLoader
{
//	public static void main(String[] args) throws Exception
//	{ 
//		String str="package com.zz;\n\nimport javax.swing.JOptionPane;\n\n\npublic class Test\n{\n\tpublic Test()\n\t{\n\t\tJOptionPane.showMessageDialog(null, \"just test,you know it.\");\n\t}\n}\n";
//		JohnClassLoader loader=JohnClassLoader.getInstance("E:\\ohn");
//		loader.compile("com.zz.Test", str, "UTF-8", "E:\\bin");
//		loader.loadClass("com.zz.Test").newInstance();
//		loader.compileFromSrc("E:\\ohn\\src");
//		loader.compileAndLoad("com.HelloWorld").newInstance();
//	}
	
	public JohnClassLoader()
	{
		super();
	}
	
	public JohnClassLoader(ClassLoader parent)
	{
		super(parent);
	}
	
	private static class Singleton    
  {    
      public final static JohnClassLoader instance = new JohnClassLoader(null);    
  }    
 
  public static JohnClassLoader getInstance(String homePath)    
  {    
  		JohnClassLoader loader=Singleton.instance;
  		Thread.currentThread().setContextClassLoader(loader);
  		if(homePath!=null&&!homePath.isEmpty())
  		{
  			loader.home(homePath);
  		}
      return loader;    
  }    
  
	protected int ohnInt=-19880127;
	protected int step=2;
	protected String charset="UTF-8";
	protected boolean isOhnMode=false;
  protected String[] basePath=new String[]{System.getProperty("user.dir")};
  
	public int getOhnInt()
	{
		return ohnInt;
	}

	public void setOhnInt(int ohnInt)
	{
		this.ohnInt = ohnInt;
	}

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}

	public String getCharset()
	{
		return charset;
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	public boolean isOhnMode()
	{
		return isOhnMode;
	}

	public void setOhnMode(boolean isOhnMode)
	{
		this.isOhnMode = isOhnMode;
	}

	public String[] getBasePath()
	{
		return basePath;
	}

	public void setBasePath(String[] basePath)
	{
		this.basePath = basePath;
	}
	
	public void home(String homePath)
	{
		if(!homePath.endsWith("\\")&&!homePath.endsWith("/"))
		{
			homePath+="/";
		}
		List<String> list=new ArrayList<String>();
		File file=new File(homePath);
		if(file.isDirectory())
		{
			File[] files=file.listFiles();
			for(File f:files)
			{
				String name=f.getName();
				if(name.endsWith(".ohn")||name.endsWith(".jar")||name.endsWith(".class"))
				{
					list.add(f.getAbsolutePath());
				}
			}
			File file1=new File(homePath+"bin");
			if(file1.isDirectory())
			{
				File[] files1=file1.listFiles();
				for(File f1:files1)
				{
					String name1=f1.getName();
					if(name1.endsWith(".ohn")||name1.endsWith(".jar")||name1.endsWith(".class"))
					{
						list.add(f1.getAbsolutePath());
					}
				}
			}
			File file2=new File(homePath+"lib");
			if(file2.isDirectory())
			{
				File[] files2=file2.listFiles();
				for(File f2:files2)
				{
					String name2=f2.getName();
					if(name2.endsWith(".ohn")||name2.endsWith(".jar")||name2.endsWith(".class"))
					{
						list.add(f2.getAbsolutePath());
					}
				}
			}
			File file3=new File(homePath+"resources/lib");
			if(file3.isDirectory())
			{
				File[] files3=file3.listFiles();
				for(File f3:files3)
				{
					String name3=f3.getName();
					if(name3.endsWith(".ohn")||name3.endsWith(".jar")||name3.endsWith(".class"))
					{
						list.add(f3.getAbsolutePath());
					}
				}
			}
			list.add(homePath);
			String[] s=new String[list.size()];
			basePath=list.toArray(s);
		}
 	}
	
	public void compile(Iterable<? extends File> files,String charset,String targetPath)
	{
		File file=new File(targetPath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager=compiler.getStandardFileManager(null, Locale.getDefault(), Charset.forName(charset));
		Iterable<? extends JavaFileObject> iter=manager.getJavaFileObjectsFromFiles(files);
		compiler.getTask(null, manager, null, Arrays.asList("-d",targetPath), null, iter).call();
		try
		{
			manager.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void compile(String name,final String code,String charset,String targetPath)
	{
		File file=new File(targetPath);
		if(!file.exists())
		{
			file.mkdirs();
		}
		SimpleJavaFileObject javaSrc=new SimpleJavaFileObject(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE)
		{
			@Override
			public CharSequence getCharContent(boolean ignoreEncodingErrors)
					throws IOException
			{
				return code;
			}
		};
		JavaCompiler compiler=ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager=compiler.getStandardFileManager(null, Locale.getDefault(), Charset.forName(charset));
		compiler.getTask(null, manager, null, Arrays.asList("-d",targetPath), null, Arrays.asList(javaSrc)).call();
		try
		{
			manager.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void compileFromSrc(String srcPath)
	{
		if(!srcPath.endsWith("\\src")&&!srcPath.endsWith("/src"))
		{
			srcPath+="/src";
		}
		File file=new File(srcPath);
		if(!file.exists()||file.isFile())
		{
			return;
		}
		List<File> list=new ArrayList<File>();
		String docBase=file.getParent();
		iterateCheck(file,docBase,list);
		compile(list, charset, docBase+"/bin");
	}
	
	protected void iterateCheck(File file,String docBase,List<File> list)
	{
		if(file.isDirectory())
		{
			for(File f:file.listFiles())
			{
				iterateCheck(f,docBase,list);
			}
		}
		else if(file.getName().endsWith(".java"))
		{
			list.add(file);
		}
		else
		{
			try
			{
				FileInputStream	fis = new FileInputStream(file);
				BufferedInputStream bis=new BufferedInputStream(fis);
				byte[] bytes=new byte[bis.available()];
				int len=0;
				String path=file.getAbsolutePath();
				path=path.substring(path.indexOf(docBase)+docBase.length()+"/src/".length());
				File newFile=new File(docBase+"/bin/"+path);
				File parent=newFile.getParentFile();
				if(!parent.exists())
				{
					parent.mkdirs();
				}
				if(!newFile.exists())
				{
					newFile.createNewFile();
				}
				FileOutputStream fos=new FileOutputStream(newFile);
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				while((len=bis.read(bytes))>0)
				{
					bos.write(bytes, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected void iterateCheck(File file,List<File> list)
	{
		if(file.isDirectory())
		{
			for(File f:file.listFiles())
			{
				iterateCheck(f,list);
			}
		}
		else if(file.getName().endsWith(".java"))
		{
			list.add(file);
		}
	}
	
	public Class<?> compileAndLoad(String fullClassName)
	{
		String path=basePath[basePath.length-1]+"resources/src";
		File file=new File(path);
		if(!file.exists()||file.isFile())
		{
			return null;
		}
		List<File> list=new ArrayList<File>();
		iterateCheck(file,list);
		String temp=System.getProperty("java.io.tmpdir");
		compile(list, charset,temp);
		String[] conv=new String[basePath.length+1];
		for(int i=0;i<basePath.length;i++)
		{
			conv[i]=basePath[i];
		}
		conv[basePath.length]=temp;
		basePath=conv;
		try
		{
			return loadClass(fullClassName);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected URL findResource(String name)
	{
		try
		{
			return new URL("file:///"+basePath[basePath.length-1]+name);
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected String findLibrary(String libname)
	{
		return basePath[basePath.length-1]+libname;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException
	{
		  byte[] b = loadData(name);
      return defineClass(name, b, 0, b.length);
	}
	
	public byte[] loadData(String name)
	{
		byte[] result=null;
		for(String path:basePath)
		{
			if(path.endsWith(".jar"))
			{
				if(isOhnMode)
				{
					result=loadOhnDataFromJar(name,path);
				}else{
					result=loadClassDataFromJar(name,path);
				}
				if(result!=null)
				{
					break;
				}
			}
			else if(path.endsWith(".ohn"))
			{
				if(isOhnMode)
				{
					result=loadOhnDataFromJarInOhn(name, path);
				}else{
					result=loadClassDataFromJarInOhn(name, path);
				}
				if(result!=null)
				{
					break;
				}
			}
			else
			{
				if(new File(path).isDirectory())
				{
					if(isOhnMode)
					{
						result=loadOhnData(name,path);
					}else{
						result=loadClassData(name,path);
					}
				  if(result!=null)
				  {
						break;
				  }
				}
			}
		}
		return result;
	}
	
	public byte[] loadClassData(String name,String baseDictionary)
	{
		byte[] result=null;
		if(!baseDictionary.endsWith("\\")&&!baseDictionary.endsWith("/"))
		{
			baseDictionary=baseDictionary+"/";
		}
		String path=baseDictionary+name.replace('.', '/')+".class";
		File file=new File(path);
		if(!file.exists())
		{
			return result;
		}
		try
		{
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] bytes=new byte[bis.available()];
			int len=0;
			while((len=bis.read(bytes))>0)
			{
				baos.write(bytes, 0, len);
			}
			result=baos.toByteArray();
			baos.close();
			bis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
		
	public byte[] loadOhnData(String name,String baseDictionary)
	{
		byte[] result=null;
		if(!baseDictionary.endsWith("\\")&&!baseDictionary.endsWith("/"))
		{
			baseDictionary=baseDictionary+"/";
		}
		String path=baseDictionary+name.replace('.', '/')+".ohn";
		File file=new File(path);
		if(!file.exists())
		{
			return result;
		}
		try
		{
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			DataInputStream dis=new DataInputStream(bis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			long ranlong=dis.readLong();
			byte[] bytes=new byte[bis.available()];
		  int len=0;
		  while((len=bis.read(bytes))>0)
		  {
		  	baos.write(bytes, 0, len);
		  }
		  result=baos.toByteArray();
		  baos.close();
		  for(int i=0;i<result.length;i+=step)
	  	{
		  	result[i]=(byte) (result[i]^ohnInt^ranlong);
	  	}
		  dis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;	
	}
	
	public byte[] loadClassDataFromJarInOhn(String name,String ohnPath)
	{
		byte[] result=null;
		String path=name.replace('.', '/').concat(".class");
		File file=new File(ohnPath);
		if(!file.exists())
		{
			return result;
		}
		try
		{
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			DataInputStream dis=new DataInputStream(bis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			long ranlong=dis.readLong();
			byte[] bytes=new byte[bis.available()];
		  int len=0;
		  while((len=bis.read(bytes))>0)
		  {
		  	baos.write(bytes, 0, len);
		  }
		  bytes=baos.toByteArray();
		  baos.close();
		  for(int i=0;i<bytes.length;i+=step)
	  	{
		  	bytes[i]=(byte) (bytes[i]^ohnInt^ranlong);
	  	}
		  dis.close();
			ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
			JarInputStream jis=new JarInputStream(bais);
			while(true)
			{
				JarEntry entry=jis.getNextJarEntry();
	  	  if(entry==null) 
	  	  {
	  	  	break;
	  	  }
	  	  if(entry.getName().equals(path))
	  	  {
	  	  	baos=new ByteArrayOutputStream();
	  	  	bytes=new byte[bais.available()];
	  			len=0;
	  			while((len=jis.read(bytes,0,bytes.length))>0)
	  			{
	  				baos.write(bytes, 0, len);
	  			}
	  			result=baos.toByteArray();
	  			baos.close();
	  			jis.closeEntry();
	  			break;
	  	  }
	  	  jis.closeEntry();
			}
			jis.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] loadOhnDataFromJarInOhn(String name,String ohnPath)
	{
		byte[] result=null;
		String path=name.replace('.', '/').concat(".ohn");
		File file=new File(ohnPath);
		if(!file.exists())
		{
			return result;
		}
		try
		{
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			DataInputStream dis=new DataInputStream(bis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			long ranlong=dis.readLong();
			byte[] bytes=new byte[bis.available()];
		  int len=0;
		  while((len=bis.read(bytes))>0)
		  {
		  	baos.write(bytes, 0, len);
		  }
		  bytes=baos.toByteArray();
		  baos.close();
		  for(int i=0;i<bytes.length;i+=step)
	  	{
		  	bytes[i]=(byte) (bytes[i]^ohnInt^ranlong);
	  	}
		  dis.close();
			ByteArrayInputStream bais=new ByteArrayInputStream(bytes);
			JarInputStream jis=new JarInputStream(bais);
			dis=new DataInputStream(jis);
			while(true)
			{
				JarEntry entry=jis.getNextJarEntry();
	  	  if(entry==null) 
	  	  {
	  	  	break;
	  	  }
	  	  if(entry.getName().equals(path))
	  	  {
	  	  	baos=new ByteArrayOutputStream();
	  	  	ranlong=dis.readLong();
	  	  	bytes=new byte[bais.available()];
	  			len=0;
	  			while((len=jis.read(bytes,0,bytes.length))>0)
	  			{
	  				baos.write(bytes, 0, len);
	  			}
	  			result=baos.toByteArray();
	  			baos.close();
  			  for(int i=0;i<result.length;i+=step)
  		  	{
  			  	result[i]=(byte) (result[i]^ohnInt^ranlong);
  		  	}
  			  jis.closeEntry();
  			  break;
	  	  }
	  	  jis.closeEntry();
			}
			dis.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] loadClassDataFromJar(String name,String jarPath)
	{
		byte[] result=null;
		try
		{
			String path=name.replace('.', '/').concat(".class");
			JarFile jar=new JarFile(jarPath);
			JarEntry entry=jar.getJarEntry(path);
			InputStream is=jar.getInputStream(entry);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] bytes=new byte[is.available()];
			int len=0;
			while((len=is.read(bytes))>0)
			{
				baos.write(bytes, 0, len);
			}
			result=baos.toByteArray();
		  baos.close();
			is.close();
			jar.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] loadOhnDataFromJar(String name,String jarPath)
	{
		byte[] result=null;
		try
		{
			String path=name.replace('.', '/').concat(".ohn");
			JarFile jar=new JarFile(jarPath);
		  JarEntry entry=jar.getJarEntry(path);
		  InputStream is=jar.getInputStream(entry);
			DataInputStream dis=new DataInputStream(is);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
	  	long ranlong=dis.readLong();
	  	byte[] bytes=new byte[is.available()];
	  	int len=0;
			while((len=is.read(bytes))>0)
			{
				baos.write(bytes, 0, len);
			}
			result=baos.toByteArray();
			baos.close();
		  for(int i=0;i<result.length;i+=step)
	  	{
		  	result[i]=(byte) (result[i]^ohnInt^ranlong);
	  	}
			dis.close();
			jar.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
}
