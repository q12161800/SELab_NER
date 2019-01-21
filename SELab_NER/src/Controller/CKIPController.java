package Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import CKIP.*;
import FileModel.MySQL;

public class CKIPController {
	
	public MySQL Mysql;
	public CKIPModel CKIPConnectioin;
	public JSONArray NewsData;
	public String TableName;
	
	public CKIPController(){
		this.Mysql = new MySQL();
	}
	
	public void getNewsdata(String tablename){
		this.TableName = tablename;
		this.NewsData = Mysql.getNews(TableName);
	}

	public void doCKIP() {
		// TODO Auto-generated method stub
		this.CKIPConnectioin = new CKIPModel();
		
		JSONObject newsobject;
		for(int index=0 ; index<NewsData.length() ; index++){
			
			try {
				
				newsobject = NewsData.getJSONObject(index);
				System.out.println(TableName + " - ID = " + newsobject.getInt("id"));
				newsobject.put("NERCKIPSegmentContent", 
						CKIPConnectioin.getCkipString(newsobject.getString("content"))
						);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSON Exception :" + e.toString());
			}
		}
	}
	
	public void savetoDB(String columnname){
		
		JSONObject newsobject;
		for(int index=0 ; index<NewsData.length() ; index++){
			try {
				newsobject = NewsData.getJSONObject(index);
				Mysql.updateCKIPResult(
						TableName,
						newsobject.getString(columnname), 
						newsobject.getInt("id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSON Exception :" + e.toString());
			}
		}
		
	}
	
	

}
