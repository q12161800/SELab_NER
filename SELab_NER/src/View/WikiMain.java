package View;

import Controller.WikiNERController;

public class WikiMain {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		WikiNERController WikiCrawlerController = new WikiNERController();

		// connected to database
		//WikiCrawlerController.getNERRsult("lifetable", "Place_Rule");
		// run wiki ner
		//WikiCrawlerController.checkTermPos("Place","Place_Rule");
		//WikiCrawlerController.runWikiNER("Place","lifetable", "Place_Rule","Place_Wiki");
		
//		// connected to database
		//WikiCrawlerController.getNERRsult("mixtable", "Organization_Rule");
		// run wiki ner
		//WikiCrawlerController.checkTermPos("Organization","Organization_Rule");
		//WikiCrawlerController.runWikiNER("Organization","mixtable", "Organization_Rule","Organization_Wiki");
		
//		// connected to database
//		WikiCrawlerController.getNERRsult("fiancetable", "Organization_Rule");
//		// run wiki ner
//		WikiCrawlerController.checkTermPos("Organization","Organization_Rule");
//		WikiCrawlerController.runWikiNER("Organization","fiancetable", "Organization_Rule","Organization_Wiki");
		
		
//		// connected to database
		WikiCrawlerController.getNERRsult("politictable", "Place_Rule");
//		// run wiki ner
		WikiCrawlerController.checkTermPos("Place","Place_Rule");		
		WikiCrawlerController.runWikiNER("Place","politictable", "Place_Rule","Place_Wiki");
		
		
		
	}

}
