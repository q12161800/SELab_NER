package Final_NER_Column;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import FileModel.FileMgr;
import FileModel.MySQL;

public class FileFinalModel {
	public JSONArray NewsData = new JSONArray();
	public FileMgr FileMgr;
	public MySQL MySQL;
	
	public FileFinalModel(){
		MySQL = new MySQL();
		FileMgr = new FileMgr();
	}
	
	public void getNERRsult(String tablename, String columnname) {
		NewsData = MySQL.getNERRuleResult(tablename, columnname);
	}
	
	
	public void createMap(String foldername, String columnname, int count) throws JSONException, IOException{
		this.checkFolder(foldername);
			for(int index=0; index<NewsData.length(); index++){
				HashMap<String, Integer> singlemap = new HashMap<String, Integer>();
				JSONObject singletypeobject = NewsData.getJSONObject(index);
				JSONArray singlearray = singletypeobject.getJSONArray(columnname);
				for(int singleindex=0; singleindex<singlearray.length(); singleindex++){
					if(singlemap.containsKey(singlearray.getJSONObject(singleindex).getString("term"))){
						singlemap.put(singlearray.getJSONObject(singleindex).getString("term"), 
								singlemap.get(singlearray.getJSONObject(singleindex).getString("term"))+1);
					}
					else{
						singlemap.put(singlearray.getJSONObject(singleindex).getString("term"), 1);
					}
					
					
				}
				
				StringBuffer allterm = new StringBuffer();
				
				singlemap.entrySet().forEach(entry -> {
					for(int size=0; size<entry.getValue(); size++){
						allterm.append(entry.getKey()+" ");
					}
				});
				//singletypeobject.getInt("id")
				this.writeFile(foldername, singletypeobject.getInt("id"), allterm.toString());
				allterm.setLength(0);
				count++;
			}
		
	}
	
	public void writeFile(String foldername, int i, String content) throws IOException {
		BufferedWriter fw = null;
		File f = new File(foldername + "\\Uid "+ i +".txt");
		fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f,false), "utf-8"));
		System.out.println("File: "+i);
		StringBuffer stringbuffer = new StringBuffer();
		stringbuffer.append(content);
		fw.write(stringbuffer.toString());
		fw.flush();
		fw.close();
		stringbuffer.setLength(0);
	}
	
	public void checkFolder(String foldername){
	    File folder = new File(foldername);
	    // if the directory does not exist, create it
	    if (!folder.exists()) {
	        System.out.println("creating directory: " + foldername);
	        boolean result = false;

	        try {
	        	folder.mkdir();
	            result = true;
	        } catch (SecurityException se) {
	            //handle it
	        	System.out.println("SecurityException" + se);
	       }
	        if (result) {
	            System.out.println("DIR created");
	        }
	    }
	}
}
