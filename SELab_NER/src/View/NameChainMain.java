package View;

import java.util.HashMap;

import Controller.NameChainController;

public class NameChainMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		NameChainController NameChinController = new NameChainController();
//		NameChinController.getNERRsult("fiancetable", "Person_Rule");
//		NameChinController.checkNameChain("fiancetable","Person_Rule","NERCKIPSegmentContent","Person_Wiki");
		
//		NameChinController.getNERRsult("lifetable", "Person_Rule");
//		NameChinController.checkNameChain("lifetable","Person_Rule","NERCKIPSegmentContent","Person_Wiki");

//		NameChinController.getNERRsult("politictable", "Person_Rule");
//		NameChinController.checkNameChain("politictable","Person_Rule","NERCKIPSegmentContent","Person_Wiki");
		
		NameChinController.getNERRsult("mixtable", "Person_Rule");
		NameChinController.checkNameChain("mixtable","Person_Rule","NERCKIPSegmentContent","Person_Wiki");
		
		
	}

}
