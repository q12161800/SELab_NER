package Final_NER_Column;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import FileModel.MySQL;

public class FinalModel {
	MySQL MySQL;
	public JSONArray NewsData;
	public FinalModel(){
		MySQL = new MySQL();
	}

	public void getNERResult(String tablename, String columnplace , String columnorganization, String columnperson,String updacolumn) {
		// TODO Auto-generated method stub
		NewsData = MySQL.getAllNERRuleResult(tablename,columnplace,columnorganization,columnperson);
	}
	
	public void combineNER(String tablename, String columnplace , String columnorganization, String columnperson,String updacolumn){
		JSONObject tempobject;
		JSONArray temparray;
		HashMap<String, JSONObject> NERMap = new HashMap<String, JSONObject>();
		HashMap<String, JSONObject> NonuMap = new HashMap<String, JSONObject>();
		
		for (int index = 0; index < NewsData.length(); index++) {			
			try {
				JSONArray finalarray = new JSONArray();
				tempobject = NewsData.getJSONObject(index);
				System.out.println("--------Running " + tablename + " ID " + tempobject.getInt("id")+" Remaining:"+(NewsData.length()-index)+"-------");
				
				temparray = tempobject.getJSONArray(columnplace);
				if(temparray.length()>0)NERMap = this.putInMap(NERMap, temparray);
				
				temparray = tempobject.getJSONArray(columnorganization);
				if(temparray.length()>0)NERMap = this.putInMap(NERMap, temparray);
				
				temparray = tempobject.getJSONArray(columnperson);
				if(temparray.length()>0)NERMap = this.putInMap(NERMap, temparray);
				
				//java8 lambda
				NERMap.forEach((k,v)-> finalarray.put(v));
			
				
				//compare ner and nonu term
				JSONObject tempposandtermobject = this.setPosandTerm(tempobject.getString("NERCKIPSegmentContent"));
				JSONArray poslist = tempposandtermobject.getJSONArray("PosList");
				JSONArray termlist = tempposandtermobject.getJSONArray("TermList");
				NonuMap = this.putNonuInMap(NonuMap, termlist, poslist);
				
				
				
				for(Entry<String, JSONObject> Nonuentry : NonuMap.entrySet()){
					NERMap.forEach((nerk,nerv)->{
						if(Nonuentry.getKey().contains(nerk)){
						try {
							if(Nonuentry.getValue().getInt("tf")-nerv.getInt("tf")>0){
								Nonuentry.getValue().put("tf",(Nonuentry.getValue().getInt("tf")-nerv.getInt("tf")));
								Nonuentry.getValue().put("orgTf",(Nonuentry.getValue().getInt("tf")));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					});
					
					if(Nonuentry.getValue().getInt("tf")>0){finalarray.put(Nonuentry.getValue());}
					
				}
				
				
//				for(Entry<String, JSONObject> Nonuentry : NonuMap.entrySet()){
//					for(Entry<String, JSONObject> NERentry : NERMap.entrySet()){
//						if(Nonuentry.getKey().contains(NERentry.getKey())){
//							if((Nonuentry.getValue().getInt("tf")-NERentry.getValue().getInt("tf"))>0){
//								Nonuentry.getValue().put("tf",(Nonuentry.getValue().getInt("tf")-NERentry.getValue().getInt("tf")));
//								Nonuentry.getValue().put("orgTf",(Nonuentry.getValue().getInt("tf")));
//							}
//							
//							else{
//								break;
//							}
//
//						}
//					}
//					
//					if(Nonuentry.getValue().getInt("tf")>0){finalarray.put(Nonuentry.getValue());}
//					
//				}
				
				
				tempobject.put("Result", finalarray);
				
				MySQL.updateNERResult(tablename, updacolumn,
						String.valueOf(tempobject.getJSONArray("Result")), tempobject.getInt("id"));
				
				
				NERMap.clear();
				NonuMap.clear();
				//NERCKIPSegmentContent
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}
			
		}	
	}
	
	public HashMap<String, JSONObject> putInMap(HashMap<String, JSONObject> singlenermap, JSONArray singlenerarray){
		JSONObject tempobject;
		for(int index=0; index<singlenerarray.length(); index++){
			try {
				tempobject = singlenerarray.getJSONObject(index);
				if(!singlenermap.containsKey(tempobject.getString("term"))){
					singlenermap.put(tempobject.getString("term"), tempobject);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}

		}
		
		return singlenermap;
	}
	
	public HashMap<String, JSONObject> putNonuInMap(HashMap<String, JSONObject> nonumap, JSONArray termarray,
			JSONArray posarray) {
		for (int index = 0; index < termarray.length(); index++) {

			JSONObject tempterm = new JSONObject();
			try {
				if (termarray.getString(index).length() > 1 && (posarray.get(index).equals("Nb") || posarray.get(index).equals("Nc")|| posarray.get(index).equals("Na"))) {
					if (nonumap.containsKey(termarray.getString(index))) {
						tempterm = nonumap.get((termarray.getString(index)));
						tempterm.put("tf", tempterm.getInt("tf") + 1);
						tempterm.put("orgTf", tempterm.getInt("orgTf") + 1);
						nonumap.put(tempterm.getString("term"), tempterm);
					}

					else {
						tempterm.put("tf", 1);
						tempterm.put("orgTf", 1);
						tempterm.put("term", termarray.get(index));
						tempterm.put("pos", posarray.get(index));
						tempterm.put("type", "Noun");
						nonumap.put(termarray.getString(index), tempterm);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}

		}
		return nonumap;

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
