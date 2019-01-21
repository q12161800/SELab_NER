package Controller;

import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import FileModel.MySQL;
import WikiModel.*;

public class WikiNERController {

	private final static String WikiURL = "https://zh.wikipedia.org/zh-tw/";
	private WikiCrawler WikiCrawler;
	private MySQL MySQL;
	private WikiNERInterface WikiNERHandler;
	public JSONArray NewsData;
	public JSONArray ReNewsData = new JSONArray();

	public WikiNERController() {
		WikiCrawler = new WikiCrawler();
		MySQL = new MySQL();
	}

	public void getNERRsult(String tablename, String columnname) {
		NewsData = MySQL.getNERRuleResult(tablename, columnname);
	}

	public void checkTermPos(String nertype, String columnname) throws Exception {
		if (nertype.contains("Place"))
			WikiNERHandler = new WIKIPlace();
		else if (nertype.contains("Organization"))
			WikiNERHandler = new WikiOrganization();
		else {
			System.out.println("input nertype error");
		}

		JSONObject tempobject;
		JSONArray temparray;

		for (int index = 0; index < NewsData.length(); index++) {
			JSONArray newsarray = new JSONArray();
			tempobject = NewsData.getJSONObject(index);
			temparray = tempobject.getJSONArray(columnname);
			try {
				JSONObject singleword;
				for (int jsonindex = 0; jsonindex < temparray.length(); jsonindex++) {
					singleword = temparray.getJSONObject(jsonindex);

					// if that condition is true, it means only catch Nc word
					if (singleword.getString("pos").indexOf("VC") != -1
							|| singleword.getString("pos").indexOf("VCL") != -1) {
						singleword.put("pos", singleword.getString("pos").split("\\+")[1]);
						singleword.put("term", singleword.getString("term").split("\\+")[1]);

						newsarray.put(this.RedefineTerm(singleword, singleword.getString("term"),
								singleword.getString("pos"), nertype));
					}

					else if (singleword.getString("pos").indexOf("Caa") != -1) {

						newsarray.put(this.RedefineTerm(singleword, singleword.getString("term").split("\\+")[0],
								singleword.getString("pos").split("\\+")[0], nertype));

						newsarray.put(this.RedefineTerm(singleword, singleword.getString("term").split("\\+")[2],
								singleword.getString("pos").split("\\+")[2], nertype));
					}

					else {
						singleword.put("pos", singleword.getString("pos").replace("+", ""));
						singleword.put("term", singleword.getString("term").replace("+", ""));

						newsarray.put(this.RedefineTerm(singleword, singleword.getString("term"),
								singleword.getString("pos"), nertype));
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}
			tempobject.put("Place_Rule", newsarray);
			this.ReNewsData.put(tempobject);
		}

	}

	public JSONObject RedefineTerm(JSONObject singleword, String term, String pos, String nertype) {
		JSONObject singlenerresult = new JSONObject();
		try {
			singlenerresult.put("pos", pos);
			singlenerresult.put("term", term);
			singlenerresult.put("orgTf", singleword.getInt("orgTf"));
			singlenerresult.put("tf", singleword.getInt("tf"));
			singlenerresult.put("type", nertype);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return singlenerresult;
	}

	public void runWikiNER(String nertype, String tablename, String columnname, String updatecolumnname) {
		HashMap<String, String> termwikipage = new HashMap<String, String>();
		
		for (int index = 0; index < this.ReNewsData.length(); index++) {
			
			JSONObject singlenerresult;

			try {
				singlenerresult = ReNewsData.getJSONObject(index);
				System.out.println("--------Running " + tablename + " ID " + singlenerresult.getInt("id")+"-------");
				JSONArray tempjsonarray = singlenerresult.getJSONArray(columnname);
				JSONArray wikinerresult = new JSONArray();
				System.out.println(tempjsonarray);
				for (int jsonindex = 0; jsonindex < tempjsonarray.length(); jsonindex++) {
					JSONObject tempjsonobject = tempjsonarray.getJSONObject(jsonindex);

					// System.out.println(tempjsonobject);
					if (termwikipage.containsKey(tempjsonobject.getString("term"))) {
						wikinerresult.put(tempjsonobject);

					}
					//
					else {
						if (WikiNERHandler.checkWIKINER(this.runWikiCrawler(tempjsonobject.getString("term")))) {
							System.out.println("need check wiki page");
							termwikipage.put(tempjsonobject.getString("term"), tempjsonobject.getString("term"));
							wikinerresult.put(tempjsonobject);
						}
					}

				}
				singlenerresult.put("Result", wikinerresult);
				System.out.println(wikinerresult);
				
				System.out.println("database");
				MySQL.updateNERResult(tablename, updatecolumnname,
						String.valueOf(singlenerresult.getJSONArray("Result")), singlenerresult.getInt("id"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



		}
	}

	public String runWikiCrawler(String keyword) throws Exception {
		// 執行Wiki網頁爬蟲
		Document wikihtml = WikiCrawler.crawlWikiPage(keyword);
		if (wikihtml != null) {
			return WikiCrawler.checkInfobox(wikihtml);
		}
		return keyword;

	}

}
