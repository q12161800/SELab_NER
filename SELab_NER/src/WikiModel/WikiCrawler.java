package WikiModel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class WikiCrawler {
	
	Document WikiDoc;
	
	//wiki網址前半段
	private final static String WikiURL = "https://zh.wikipedia.org/zh-tw/";
	
	
	public Document crawlWikiPage(String keyword){
		//將wiki網址與搜尋字串組合(需先轉碼UTF-8)
		String wikiurl;
		try {
			wikiurl = WikiURL + URLEncoder.encode(keyword, "UTF-8");
			
			System.out.println(wikiurl);
			this.WikiDoc = Jsoup.parse(new URL(wikiurl), 3000);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
		return this.WikiDoc;

	}
	
	public String checkInfobox(Document wikidoc){
		StringBuffer infoboxtxt = new StringBuffer();
		
		//將Infobox、navbox、catlinks中的關鍵字全部抓出
		if(wikidoc.html().contains("infobox")){
			infoboxtxt.append(wikidoc.select("table[class=infobox]")
								 .select("th").text());
		}
		if(wikidoc.html().contains("infobox vcard")){
			infoboxtxt.append(wikidoc.select("table[class=infobox vcard]")
								 .select("th").text());
		}
		if (wikidoc.html().contains("infobox geography vcard")){
			infoboxtxt.append(wikidoc.select("table[class=infobox geography vcard]")
					 			 .select("th").text());
		}
		if (wikidoc.html().contains("infobox vcard plainlist")){
			infoboxtxt.append(wikidoc.select("table[class=infobox vcard plainlist]")
								 .select("th").text());
		}
		if (wikidoc.html().contains("navbox")){
			infoboxtxt.append(wikidoc.select("table[class=navbox]")
					 			 .select("th").text());
		}
		if (wikidoc.html().contains("catlinks")){
			infoboxtxt.append(wikidoc.select("div[class=catlinks]")
								  .select("li").text());
		}
		
		System.out.println(infoboxtxt.toString());
		return infoboxtxt.toString();
	}

}
