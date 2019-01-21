package FileModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropsReader {
	static String propertiesPath = "resource/Database.properties";
	
	private static Properties props;
	
	public PropsReader(){
		this.loadProperties();
	}

	//test main
//	public static void main(String[] args) {
//		PropsReader pr = new PropsReader();
//		pr.loadProperties();
//		System.out.println(getProperties("databaseName"));
//
//	}

	private static void loadProperties() {
		props = new Properties();
		try {
			props.load(new FileInputStream(propertiesPath));// 設定檔案名稱
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException :" + e.toString());
		} catch (IOException e) {
			System.out.println("IOException :" + e.toString());
		}
	}

	//call itself method = loadProperties()
	public static String getProperties(String key) {// 把Database.properties載入程式
		return props.getProperty(key);
	}

	

}
