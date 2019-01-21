package View;

import Controller.CKIPController;
import Controller.NERController;
import Controller.WikiNERController;
import FileModel.FileMgr;

public class NERMain {
	
	public static void main(String[] arg) throws Exception{
		//Step1: CKIP
		System.out.println("Step1 get Data then run CKIP.");
		CKIPController ckipcontroller = new CKIPController();
//		ckipcontroller.getNewsdata("lifetable");
//		ckipcontroller.doCKIP();
//		ckipcontroller.savetoDB("NERCKIPSegmentContent");
//		
//		ckipcontroller.getNewsdata("fiancetable");
//		ckipcontroller.doCKIP();
//		ckipcontroller.savetoDB("NERCKIPSegmentContent");
//		
//		ckipcontroller.getNewsdata("politictable");
//		ckipcontroller.doCKIP();
//		ckipcontroller.savetoDB("NERCKIPSegmentContent");
//		
//		ckipcontroller.getNewsdata("mixtable");
//		ckipcontroller.doCKIP();
//		ckipcontroller.savetoDB("NERCKIPSegmentContent");
		

		//Step2: run Rule  
		NERController nercontroller = new NERController();
		//讀取rule檔案，建立rule object
		//用rule取詞
		//P&O=地和組織(PlaceandOrganizationRule.txt)、P=人(PersonRule.txt)
		nercontroller.NERRule("PersonRule.txt", "P");
		//讀SQL file
		System.out.println("Recognize Article by SQL");
		//P1=table name
		//P2=ner type
		nercontroller.recognizeArticlebySQL("mixtable","Person");
		nercontroller.updateNERResult("mixtable","Person_Rule");
//		
//		nercontroller.recognizeArticlebySQL("lifetable","Person");
//		nercontroller.updateNERResult("lifetable","Person_Rule");
//		
//		nercontroller.recognizeArticlebySQL("fiancetable","Person");
//		nercontroller.updateNERResult("fiancetable","Person_Rule");
//		
//		nercontroller.recognizeArticlebySQL("politictable","Organization");
//		nercontroller.updateNERResult("politictable","Organization_Rule");
		
		
	}

}
