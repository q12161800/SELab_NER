package Controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import FileModel.MySQL;
import NameChainModel.PersonNameChain;


public class NameChainController {
	
	public JSONArray NewsData;
	private MySQL MySQL;
	private PersonNameChain PersonNameChine;
	
	public NameChainController(){
		MySQL = new MySQL();
		PersonNameChine = new PersonNameChain();
	}
	
	public void getNERRsult(String tablename, String columnname) {
		// TODO Auto-generated method stub
		NewsData = MySQL.getNERRuleResult(tablename, columnname);
	}
	public void checkNameChain(String tablename, String columnname,String content, String newcolumnname){
		JSONObject tempobject;
		JSONArray temparray;
		for (int index = 0; index < NewsData.length(); index++) {
			JSONArray newsarray = new JSONArray();
			
			try {
				
				tempobject = NewsData.getJSONObject(index);
				System.out.println("--------Running " + tablename + " ID " + tempobject.getInt("id")+" Remaining:"+(NewsData.length()-index)+"-------");
				temparray = tempobject.getJSONArray(columnname);
				
				JSONObject tempposandtermobject = this.setPosandTerm(tempobject.getString(content));
				JSONArray poslist = tempposandtermobject.getJSONArray("PosList");
				JSONArray termlist = tempposandtermobject.getJSONArray("TermList");
				for(int termindex=0; termindex<temparray.length(); termindex++){
					tempposandtermobject = temparray.getJSONObject(termindex);
					if(tempposandtermobject.getString("term").length()==1 
							|| tempposandtermobject.getString("term").length()==2 ){
						//System.out.println(tempposandtermobject.getString("term"));
						tempposandtermobject = PersonNameChine.calculatNameChain(tempposandtermobject,poslist,termlist);
					}
				}
				
				tempobject.put("Result",temparray);
				
				MySQL.updateNERResult(tablename, newcolumnname,
						String.valueOf(tempobject.getJSONArray("Result")), tempobject.getInt("id"));
				
				
				
				//NERCKIPSegmentContent
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}
			
		}
	}
	
	public void updataNameChainResult(String tablename, String columnname, String newcolumnname){
		JSONObject newsobject;
		for (int index = 0; index < NewsData.length(); index++) {
			try {
				newsobject = NewsData.getJSONObject(index);
				MySQL.updateNERResult(
						tablename, 
						newcolumnname, 
						String.valueOf(newsobject.getJSONArray(columnname)), 
						newsobject.getInt("id"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}

		}
	}
	
	
	public final JSONObject setPosandTerm(String content){
		JSONObject tempposandtermobject = new JSONObject();
		JSONArray poslist = new JSONArray();
		JSONArray termlist = new JSONArray();
		for(String term : content.split("　")){
			//System.out.println(term);
			if(term.indexOf("(")!=-1){
				termlist.put(term.substring(0, term.indexOf("(")).trim());
				poslist.put(term.substring(term.indexOf("(") + 1, term.length() - 1).trim());}
			}
		try {
			tempposandtermobject.put("PosList", poslist);
			tempposandtermobject.put("TermList", termlist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSONException :" + e.toString());
		}
		return tempposandtermobject;
	}
	
	
	
}
