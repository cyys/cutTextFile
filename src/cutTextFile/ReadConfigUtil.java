package cutTextFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfigUtil {
	
	private static Properties p=new Properties();
	
	static{
		
		try {
			InputStream in = new FileInputStream("app.properties");
			p.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private ReadConfigUtil() {
		
	}

	public static String getRecodes(){
		return isBlank(p.getProperty("recodes"))?"5000":p.getProperty("recodes").trim();
	}
	
	public static String getSrcDir(){
		return isBlank(p.getProperty("srcDir"))?"srcDir":p.getProperty("srcDir").trim();
	}
	
	public static String getTargetDir(){
		return isBlank(p.getProperty("targetDir"))?"targetDir":p.getProperty("targetDir").trim();
	}
	
	public static String getSleepTime(){
		return isBlank(p.getProperty("sleepTime"))?"10000":p.getProperty("sleepTime").trim();
	}
	
	public static String getSuffix(){
		return isBlank(p.getProperty("suffix"))?".txt":p.getProperty("suffix").trim();
	}
	
	public static String getNameSuffixType(){
		return isBlank(p.getProperty("nameSuffixType"))?"numberAdd":p.getProperty("nameSuffixType").trim();
	}
	
	public static String getSrCharset(){
		return isBlank(p.getProperty("srCharset"))?"GBK":p.getProperty("srCharset").trim();
	}
	
	public static String getTargetCharset(){
		return  isBlank(p.getProperty("targetCharset"))?"GBK":p.getProperty("targetCharset").trim();
	}
	
	public static String getBakDir(){
		return isBlank(p.getProperty("bakDir"))?"bak":p.getProperty("bakDir").trim();
	}
	
	private static boolean isBlank(String str){
		if(str==null||"".equals(str.trim())){
			return true;
		}
		return false;
	}
	
}
