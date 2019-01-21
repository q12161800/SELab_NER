package NERModel;

import java.util.ArrayList;
import java.util.List;

public class Article {
	public List<String> PosList = new ArrayList<String>();
	public List<String> TermList = new ArrayList<String>();
	public String TxtName;
	public String Content;
	
	public Article(String content, String txtname){
		this.Content = content;
		this.TxtName = txtname;
		this.setPosandTerm();
	}
	
	public final void setPosandTerm(){
		for(String term : this.Content.split("　")){
			//System.out.println(term);
			if(term.indexOf("(")!=-1){
				TermList.add(term.substring(0, term.indexOf("(")).trim());
				PosList.add(term.substring(term.indexOf("(") + 1, term.length() - 1).trim());}
			}
		//System.out.println(TxtName + " : " + PosList);
		//System.out.println(TxtName + " : " + TermList);
	}
	
	

}
