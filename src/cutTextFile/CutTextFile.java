package cutTextFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.sun.jmx.snmp.Enumerated;

public class CutTextFile {
	
	public static void readTextFile() throws Exception{
		String s_srcDir=ReadConfigUtil.getSrcDir();
		File srcDir=new File(s_srcDir);
		if(!srcDir.exists()){
			srcDir.mkdirs();
		}
		
		zipFileWrite(srcDir);
		
		List<File> allFiles=new ArrayList<File>();
		findAllFiles(srcDir,allFiles);//文件查找完毕
		
		for(File file:allFiles){
			CutToFiles(file);
		}
		 
	}
	
	/**
	 * 读一个文件，按照给定的记录条数写出到不同的文件
	 * @param file
	 * @throws IOException
	 */
	private static void CutToFiles(File file) throws IOException{
		BufferedReader read=new BufferedReader(
				new InputStreamReader(
						new FileInputStream(file),ReadConfigUtil.getSrCharset()));
		File dir=new File(ReadConfigUtil.getTargetDir());
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String targetDir=file.getAbsolutePath().replace(ReadConfigUtil.getSrcDir(), ReadConfigUtil.getTargetDir());
		PrintWriter out=null;
		String lineStr="",targetFileName="";
		int count=1,fileName=0;
		
		 while ((lineStr=read.readLine())!=null) {
			 if(count==1){
				 if("numberAppend".equals(ReadConfigUtil.getNameSuffixType())){
					 targetFileName= targetDir.replace(ReadConfigUtil.getSuffix(), (++fileName)+ReadConfigUtil.getSuffix());
				 }else{
					 String str=targetDir.replace(ReadConfigUtil.getTargetDir()+File.separator, "");
					 if(str.lastIndexOf(File.separator)>0){
						 str= str.substring(str.lastIndexOf(File.separator)+1);
					 }
					 str= str.replaceFirst("\\d{1,}", "");
					 targetFileName= targetDir.replace(str, (++fileName)+str);
				 }
				 File d=new File(targetDir.substring(0, targetDir.lastIndexOf(File.separator)+1));
				 if(!d.exists()){
					 d.mkdirs();
				 }
				 out=new PrintWriter(targetFileName,ReadConfigUtil.getTargetCharset());
			 }
			 out.println(lineStr);
			 if((count+"").equals(ReadConfigUtil.getRecodes())){
				 count=1;
				 out.close();
				 continue;
			 }
			count++;
		}
		 
		if(count>1){
			 out.close();
		}
		read.close();
	}
	
	/**
	 * 找到所有文件，以及子目录的文件
	 * @param dir
	 * @param allFiles
	 */
	private static void findAllFiles(File dir,List<File> allFiles){
		File[] files=dir.listFiles();
		for(File file:files){
			if(file.isFile()&&file.getAbsolutePath().endsWith(ReadConfigUtil.getSuffix())){
				allFiles.add(file);
			}else if(file.isDirectory()){
				findAllFiles(file,allFiles);
			}
		}
	}
	
	/**
	 * 找出所有的zip文件，以及子目录的zip文件
	 * @throws IOException 
	 * @throws ZipException 
	 */
	private static void ZipText(File dir,List<File> allZipFiles){
		File[] files=dir.listFiles();
		for(File file:files){
			if(file.isFile()&&file.getAbsolutePath().toUpperCase().endsWith(".ZIP")){
				allZipFiles.add(file);
			}else if(file.isDirectory()){
				ZipText(file,allZipFiles);
			}
		}
	}
	
	/**
	 * 解压所有找到的zip文件
	 * @param allZipFiles
	 * @throws ZipException
	 * @throws IOException
	 */
	private static void zipFileWrite(File dir) throws Exception{
		List<File> allZipFiles=new ArrayList<File>();
		ZipText(dir, allZipFiles);
		for(File file:allZipFiles){
			ZipFile zf=new ZipFile(file,ZipFile.OPEN_READ);
			writeZipText(zf,file);
			zf.close();
			file.delete();
		}
	}
	
	/**
	 * 解压一个zip文件
	 * @param zipFile
	 * @param file
	 * @throws IOException
	 */
	private static void writeZipText(ZipFile zipFile,File file) throws IOException{
		Enumeration<? extends ZipEntry> zf = zipFile.entries();
		ZipEntry ze=null;
		
		while(zf.hasMoreElements()){
			String textFile=file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf(File.separator)+1);
			ze=zf.nextElement();
			BufferedInputStream in=new BufferedInputStream(zipFile.getInputStream(ze));
			BufferedOutputStream out= new BufferedOutputStream(new FileOutputStream(
					textFile+ze.getName()));
			byte[] buffer=new byte[1024*1024];
			int size=0;
			while((size=in.read(buffer))>0){
				out.write(buffer, 0, size);
			}
			
			out.close();
			in.close();
		}
	}
	
	public static void main(String[] args) throws  Exception {
		readTextFile();
	}
	
}
