package Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import FileModel.MySQL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import FileModel.FileMgr;
import NERModel.Article;
import NERModel.PlaceandOrganizationRule;
import NERModel.PersonRule;

public class NERController {

	public JSONArray NewsData;
	public FileMgr FileMgr = new FileMgr();
	public MySQL MySQL;
	public PlaceandOrganizationRule PlaceandOrganizationRule;
	public PersonRule PersonRule;
	
	public NERController() {
		this.MySQL = new MySQL();
	}

	public void NERRule(String txtname, String nertype) throws Exception {
		StringBuilder rule = FileMgr.ReadTxt(txtname);
		
		if(nertype.contains("P&O"))this.PlaceandOrganizationRule = new PlaceandOrganizationRule(rule);
		else this.PersonRule = new PersonRule(rule);
	}

	public void recognizeArticlebySQL(String tablename, String nertype) {
		// get file list
		this.NewsData = MySQL.getCKIPNews(tablename);
		JSONObject newsobject;
		for (int index = 0; index < NewsData.length(); index++) {
			try {
				newsobject = NewsData.getJSONObject(index);
				Article article = new Article(newsobject.getString("NERCKIPSegmentContent"), String.valueOf(index));
				HashMap<String, Integer> compareruleresult = null;
				System.out.println("--------Running " + tablename + " ID " + newsobject.getInt("id")+" Remaining:"+(NewsData.length()-index)+"-------");
				
				if(nertype == "Person"){
					compareruleresult = PersonRule.recognizeArticle(article);
					compareruleresult = PersonRule.catchNoun(compareruleresult);
				}
				else{compareruleresult = PlaceandOrganizationRule.recognizeArticle(article);}
				

				JSONArray allnerresult = new JSONArray();
				for (Entry<String, Integer> entry : compareruleresult.entrySet()) {
					JSONObject singlenerresult = new JSONObject();
					singlenerresult.put("pos", entry.getKey().split("&&")[0]);
					singlenerresult.put("term", entry.getKey().split("&&")[1]);
					singlenerresult.put("orgTf", entry.getValue());
					singlenerresult.put("tf", entry.getValue());
					singlenerresult.put("type", nertype);
					allnerresult.put(singlenerresult);
				}
				newsobject.put("RuleResult", allnerresult);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException :" + e.toString());
			}

		}
	}

	public void updateNERResult(String tablename, String columnname) {
		JSONObject newsobject;
		for (int index = 0; index < NewsData.length(); index++) {
			try {
				newsobject = NewsData.getJSONObject(index);
				MySQL.updateNERResult(
						tablename, 
						columnname, 
						String.valueOf(newsobject.getJSONArray("RuleResult")), 
						newsobject.getInt("id"));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}

		}
	}
}
