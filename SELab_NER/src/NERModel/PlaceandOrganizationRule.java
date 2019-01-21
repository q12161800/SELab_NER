package NERModel;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlaceandOrganizationRule {
	
	String Rule;
	ArrayList<List> RuleArray = new ArrayList<List>();
	
	public PlaceandOrganizationRule(StringBuilder rule){
		this.Rule = rule.toString();
		this.readrule();
	}
	
	
	public void readrule(){
		for (String s : Rule.split("\n")) {
			String[] rule = s.split("[+]");
			List<String> ruleList = new ArrayList<String>();
			for (int order = 0; order < rule.length; order++) {
				ruleList.add(rule[order].trim());
				//System.out.println(rule[order]);
				}
			RuleArray.add(ruleList);
			}
		}
	
	public HashMap<String, Integer> recognizeArticle(Article article) 
			throws IOException{
		//ArrayList<List> compareruleresult = new ArrayList<List>();
		HashMap<String, Integer> compareruleresult = new HashMap<String, Integer>(); 
//		System.out.println("Rule count: " + ruleArray.size());
		for (int pos = 0; pos < article.PosList.size(); pos++) {
			for (int rule = 0; rule < RuleArray.size(); rule++) {
				String ruleanswer = "";
				String posanswer = "";
				List<String> ruleList = RuleArray.get(rule);
				// System.out.println("Round: "+pos+" ruleList: "+ ruleList);
				int order = 0;

				if (article.PosList.get(pos).equals(ruleList.get(order))) {
					ruleanswer += ruleList.get(order);
					posanswer += article.TermList.get(pos);
					// System.out.println("round"+ rule +"answer"+ruleanswer);
					int fitPosition = pos + 1;
					for (; fitPosition < article.PosList.size(); fitPosition++) {
						order++;
						if (order < ruleList.size()) {
							if (article.PosList.get(fitPosition).equals(
									ruleList.get(order))) {
								ruleanswer += "+";
								ruleanswer += ruleList.get(order);
								posanswer += "+";
								posanswer += article.TermList.get(fitPosition);
							} else {
								// System.out.println(answer);
								order = 0;
								break;
							}
						} else {
//							System.out.println(ruleanswer + " && " + posanswer);
							order = 0;
							// insert Result
							if(compareruleresult.containsKey(ruleanswer + "&&" + posanswer)){
								compareruleresult.put(ruleanswer + "&&" + posanswer, compareruleresult.get(ruleanswer + "&&" + posanswer)+1);
							}
							else{compareruleresult.put(ruleanswer + "&&" + posanswer, 1);}
							
							ruleanswer = "";
							posanswer = "";
							break;
						}
					}
				} else {
					ruleanswer = "";
					posanswer = "";
				}
			}
			//System.out.println("new round");
		}

		return compareruleresult;
	}
	
	
	

}
