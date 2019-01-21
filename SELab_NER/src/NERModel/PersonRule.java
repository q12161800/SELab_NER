package NERModel;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PersonRule {

	String Rule;
	ArrayList<List> RuleArray = new ArrayList<List>();

	public PersonRule(StringBuilder rule) {
		this.Rule = rule.toString();
		this.readrule();
	}

	public void readrule() {
		for (String s : Rule.split("\n")) {
			String[] rule = s.split("[+]");
			List<String> ruleList = new ArrayList<String>();
			for (int order = 0; order < rule.length; order++) {
				ruleList.add(rule[order].trim());
			}
			RuleArray.add(ruleList);
		}
	}

	public HashMap<String, Integer> recognizeArticle(Article article) throws IOException {
		// ArrayList<List> compareruleresult = new ArrayList<List>();
		HashMap<String, Integer> compareruleresult = new HashMap<String, Integer>();
		// System.out.println("Rule count: " + ruleArray.size());
		for (int pos = 0; pos < article.PosList.size(); pos++) {
			for (int rule = 0; rule < RuleArray.size(); rule++) {
				String ruleanswer = "";
				String posanswer = "";
				List<String> ruleList = RuleArray.get(rule);
				// System.out.println("Round: "+pos+" ruleList: "+ ruleList);
				int order = 0;

				if (article.PosList.get(pos).equals(ruleList.get(order).replaceAll("[()]", ""))) {

					ruleanswer += ruleList.get(order);
					posanswer += article.TermList.get(pos);
					int fitPosition = pos + 1;
					for (; fitPosition < article.PosList.size(); fitPosition++) {
						order++;
						if (order < ruleList.size()) {
							if (article.PosList.get(fitPosition).equals(ruleList.get(order).replaceAll("[()]", ""))) {
								ruleanswer += "+";
								ruleanswer += ruleList.get(order);
								posanswer += "+";
								posanswer += article.TermList.get(fitPosition);
							} else {
								// System.out.println(ruleanswer);
								order = 0;
								break;
							}
						} else {
							// System.out.println(ruleanswer + " && " +
							// posanswer);
							order = 0;
							// insert Result
							if (compareruleresult.containsKey(ruleanswer + "&&" + posanswer)) {
								compareruleresult.put(ruleanswer + "&&" + posanswer,
										compareruleresult.get(ruleanswer + "&&" + posanswer) + 1);
							} else {
								compareruleresult.put(ruleanswer + "&&" + posanswer, 1);
							}

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
			// System.out.println("new round");
		}

		return compareruleresult;
	}

	public HashMap<String, Integer> catchNoun(HashMap<String, Integer> compareruleresult) {
		// TODO Auto-generated method stub

		HashMap<String, Integer> nounresult = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : compareruleresult.entrySet()) {
			StringBuilder singlenoun = new StringBuilder();
			String tempNoun[] = entry.getKey().split("&&")[1].split("\\+");
			String tempPos[] = entry.getKey().split("&&")[0].split("\\+");

			int count = 0;
			StringBuilder SingleNoun = new StringBuilder();
			StringBuilder SinglePos = new StringBuilder();

			for (String key : tempPos) {
				if (key.indexOf("(") != -1 && key.indexOf(")") != -1) {
					if (nounresult.containsKey("Nb&&" + tempNoun[count])) {
						nounresult.put("Nb&&" + tempNoun[count],
								entry.getValue() + nounresult.get("Nb&&" + tempNoun[count]));
					} else {
						nounresult.put("Nb&&" + tempNoun[count], entry.getValue());
					}
				}

				else if (key.indexOf("(") != -1) {
					SingleNoun.append(tempNoun[count]+tempNoun[count+1]);
					SinglePos.append(tempPos[count].replace("(", "")+tempPos[count+1].replace(")", ""));
					if (nounresult.containsKey(SinglePos.toString()+"&&" + SingleNoun.toString())) {
						nounresult.put(SinglePos.toString()+"&&" + SingleNoun.toString(),
								entry.getValue() + nounresult.get(SinglePos.toString()+"&&" + SingleNoun.toString()));
					} else {
						nounresult.put(SinglePos.toString()+"&&" + SingleNoun.toString(), entry.getValue());
					}
				}
				count++;
			}

			// free string array
			Arrays.fill(tempNoun, null);
			Arrays.fill(tempPos, null);

		}
		return nounresult;
	}

}
