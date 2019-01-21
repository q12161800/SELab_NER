package NameChainModel;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PersonNameChain {

	public JSONObject calculatNameChain(JSONObject tempposandtermobject, JSONArray poslist, JSONArray termlist) {
		// TODO Auto-generated method stub
		HashMap<String, Integer> namechainone = new HashMap<String, Integer>();
		HashMap<String, Integer> namechaintwo = new HashMap<String, Integer>();
		StringBuilder tempterm = new StringBuilder();
		for (int index = 0; index < termlist.length(); index++) {
			
			try {
				// System.out.println(tempposandtermobject.getString("term")+"
				// "+termlist.get(index+1).toString());
				if (tempposandtermobject.getString("term").equals(termlist.getString(index))) {
					if (tempposandtermobject.getString("term").length() == 1) {
						if ((tempposandtermobject.getString("term") + termlist.getString(index + 1)).length() == 2) {

							if ((tempposandtermobject.getString("term") + termlist.getString(index + 1)
									+ termlist.getString(index + 2)).length() == 3) {
								// System.out.println((tempposandtermobject.getString("term")
								// +termlist.getString(index+1)
								// +termlist.getString(index+2)));
								tempterm.append((tempposandtermobject.getString("pos") 
										+ poslist.getString(index + 1)
										+ poslist.getString(index + 2))
										+ "&&"
										+ (tempposandtermobject.getString("term")
												+ termlist.getString(index + 1)
												+ termlist.getString(index + 2)));
								
								namechaintwo = this.checkMap(namechaintwo,tempterm.toString());
								
							} else {
								// System.out.println((tempposandtermobject.getString("term")
								// +termlist.getString(index+1)));
								tempterm.append((tempposandtermobject.getString("pos") 
										+ poslist.getString(index + 1))
										+ "&&"
										+ (tempposandtermobject.getString("term")
												+ termlist.getString(index + 1)));
								
								namechainone = this.checkMap(namechainone,tempterm.toString());
							}

						}

						else if ((tempposandtermobject.getString("term") + termlist.getString(index + 1))
								.length() == 3) {
							// System.out.println((tempposandtermobject.getString("term")
							// +termlist.getString(index+1)));
							tempterm.append((tempposandtermobject.getString("pos") 
									+ poslist.getString(index + 1))
									+ "&&"
									+ (tempposandtermobject.getString("term")
											+ termlist.getString(index + 1)));
							namechaintwo = this.checkMap(namechaintwo,tempterm.toString());
						}
					}

					else {
						if ((tempposandtermobject.getString("term") + termlist.getString(index + 1)).length() == 3) {
							if ((tempposandtermobject.getString("term") 
									+ termlist.getString(index + 1)
									+ termlist.getString(index + 2)).length() == 4) {
								// System.out.println(
								// (tempposandtermobject.getString("term")
								// +termlist.getString(index+1)
								// +termlist.getString(index+2)));
								tempterm.append((tempposandtermobject.getString("pos") 
										+ poslist.getString(index + 1)
										+ poslist.getString(index + 2))
										+ "&&"
										+ (tempposandtermobject.getString("term")
												+ termlist.getString(index + 1)
												+ termlist.getString(index + 2)));
								namechaintwo = this.checkMap(namechaintwo,tempterm.toString());
							} else {
								// System.out.println((tempposandtermobject.getString("term")
								// +termlist.getString(index+1)));
								tempterm.append((tempposandtermobject.getString("pos") 
										+ poslist.getString(index + 1))
										+ "&&"
										+ (tempposandtermobject.getString("term")
												+ termlist.getString(index + 1)));
								namechainone = this.checkMap(namechainone,tempterm.toString());
							}

						} else if ((tempposandtermobject.getString("term") + termlist.getString(index + 1))
								.length() == 4) {
							// System.out.println((tempposandtermobject.getString("term")
							// +termlist.getString(index+1)));
							tempterm.append((tempposandtermobject.getString("pos") 
									+ poslist.getString(index + 1))
									+ "&&"
									+ (tempposandtermobject.getString("term")
											+ termlist.getString(index + 1)));
							namechaintwo = this.checkMap(namechaintwo,tempterm.toString());
						}
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("JSONException :" + e.toString());
			}
			
			tempterm.setLength(0);
		}
		
		
		//+1 to avoid X/0 or 0/X
		double onetalfreq = namechainone.values().stream().mapToInt(Integer::intValue).sum()+1;
		double twotalfreq = namechaintwo.values().stream().mapToInt(Integer::intValue).sum()+1;
		
		
		
		if(twotalfreq/onetalfreq>=1){
			
			tempposandtermobject = this.calculatePersonFreq(tempposandtermobject, namechaintwo);
		}
		
		else{
			tempposandtermobject = this.calculatePersonFreq(tempposandtermobject, namechainone);
			
		}
		

		return tempposandtermobject;
	}

	public HashMap<String, Integer> checkMap(HashMap<String, Integer> tempmap, String tempterm) {
		if(tempmap.containsKey(tempterm.toString())){
			tempmap.put(tempterm.toString(), tempmap.get(tempterm.toString())+1);
		}
		else{
			tempmap.put(tempterm.toString(), 1);
		}
		return tempmap;
	}
	
	public JSONObject calculatePersonFreq(JSONObject tempposandtermobject, HashMap<String, Integer> namechainmap){
		
		if(namechainmap.size()>0){
			Map.Entry<String, Integer> maxEntry = namechainmap.entrySet().stream()
					  .max(Map.Entry.comparingByValue()).get();
			try {
				tempposandtermobject.put("term", maxEntry.getKey().split("&&")[1]);
				tempposandtermobject.put("pos", maxEntry.getKey().split("&&")[0]);
				tempposandtermobject.put("tf", maxEntry.getValue());
				tempposandtermobject.put("orgTf", maxEntry.getValue());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return tempposandtermobject;
		}
		
		return tempposandtermobject;
	}
}
