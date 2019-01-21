package CKIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class CKIPModel {
	Socket skt = new Socket();
	
	
	public static void main(String[] arg){
		CKIPModel ckip = new CKIPModel();
		ckip.getCkipString("連接器廠幃翔（6185）在日前獲得外資瑞信證券點名為「台灣今年4家新增的蘋果供應鏈之一」，也讓該公司的蘋果概念股身分獲認證。從營運表現來看，幃翔近年加速提高自動化佈局，毛利率在去年度暴增約11個百分點、飆上41.16％，而該公司今年在蘋果訂單持續貢獻下，全年EPS將可突破去年的3.34元、再創新高。 幃翔去年度營收18.8億元，雖比前一年減少11％，毛利率卻是相當可觀的41.16％，遠高於前一年的29.98％，帶動稅後純益年增55％、EPS創3.34元的近年新高水準。股東會已經通過配發2.8元股利，現金為2.2元、股票為0.6元。 據了解，幃翔原本就透過EMS大廠偉創力，供應蘋果iPad的USB連接器，而幃翔今年獲得外資點名，也意味著在蘋果供應鏈當中的重要性持續提升。法人傳出，幃翔除了iPad新款產品外，也送樣供iPhone認證，若能進一步卡位，從第3季起會有更明顯的成長性。法人分析，蘋果訂單約佔幃翔上半年營收比重的3成，下半年有機會伴隨蘋果新品而攀高，尤其iPad USB連接器有機會成為「主要供應商」。 中國大陸近年勞動成本激增，而幃翔卻因持續拉高自動化比重、至今已達5成，而得以降低衝擊。").replaceAll("[;&]", "");
	}
	
	public CKIPModel(){
		try {
			InetAddress ip = InetAddress.getByName("140.109.19.104");
			skt = new Socket(ip, 1501);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCkipString(String ckipcontext){
		System.out.println("********** 使用中研院斷詞伺服器 *********");
		try {
			PrintStream oos = new PrintStream(skt.getOutputStream());
			BufferedReader ois = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			oos.print("<?xml version=\"1.0\" ?>" + 
					"<wordsegmentation version=\"0.1\" charsetcode=\"UTF-8\">" +
					"<option showcategory=\"1\" />" +
					"<authentication username=\"selab1234\" password=\"selab1234\" />" +
					"<text>"+ ckipcontext + "</text>" + 
					"</wordsegmentation>");
			ckipcontext = (String)ois.readLine();
			System.out.println((String)ois.readLine());
			ckipcontext = xmlreplace(ckipcontext);
			ckipcontext = ckipcontext.replaceAll("</sentence><sentence>", "");
			ckipcontext = garbagereplace(ckipcontext);
			System.out.println(ckipcontext);
			return ckipcontext;
		} catch (IOException e) {
			System.err.println("Error!!! :|");
			return "Error";
		}
	}

	//check processstatus code
	public String xmlreplace(String xmlcontent){
		
		if(xmlcontent.indexOf("code=\"0\"")!=-1){
			xmlcontent = xmlcontent.replace("<?xml version=\"1.0\" ?><wordsegmentation version=\"0.1\"><processstatus code=\"0\">Success</processstatus><result><sentence>　", "");
			xmlcontent = xmlcontent.replace("</sentence></result></wordsegmentation>", "");
		}
		
		else if(xmlcontent.indexOf("code=\"1\"")!=-1){
			xmlcontent = "Service internal error!!!!!!!!!!!!!";
		}
		
		else if(xmlcontent.indexOf("code=\"2\"")!=-1){
			xmlcontent = "XML format error!!!!!!!!!!!!!!!!!!";
		}
		
		else{
			xmlcontent = "Authentication failed!!!!!!!!!!!!!!";
		}

		return xmlcontent;
	}
	
	public String garbagereplace(String garbagecontext){
		String newcatcontent = ""; 
		for (String s : garbagecontext.split("　")) {
			if (s.indexOf("(FW)")==-1 && 
				//，
				s.indexOf("(COMMACATEGORY)")==-1 &&
				//：
				s.indexOf("(COLONCATEGORY)")==-1 &&
				//-
				s.indexOf("(DASHCATEGORY)")==-1 &&
				//!
				s.indexOf("(EXCLAMATIONCATEGORY)")==-1 &&
				//Number
				s.indexOf("(Neu)")==-1 &&
				//１４／５
				s.indexOf("(Neqa)")==-1 &&
				//。
				s.indexOf("(PERIODCATEGORY)")==-1 &&
				//、
				s.indexOf("(PAUSECATEGORY)")==-1 &&
				//()
				s.indexOf("(PARENTHESISCATEGORY)")==-1 &&
				//?
				s.indexOf("(QUESTIONCATEGORY)")==-1 &&
				//;
				s.indexOf("(SEMICOLONCATEGORY)")==-1
					){
				newcatcontent+=s+"　";
				}
	}
		return newcatcontent;
	}

}

