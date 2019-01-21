package Final_NER_Column;

import java.io.IOException;

import org.json.JSONException;

public class FinalMain {

	public static void main(String[] args) throws JSONException, IOException {
		// TODO Auto-generated method stub
		
		FinalModel FinalModel = new FinalModel();
		
		//tablename ,columnplace ,columnorganization ,columnperson ,updacolumn
//		FinalModel.getNERResult("lifetable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		FinalModel.combineNER("lifetable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		
//
//		FinalModel.getNERResult("politictable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		FinalModel.combineNER("politictable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		
//
//		FinalModel.getNERResult("mixtable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		FinalModel.combineNER("mixtable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		
//
//		FinalModel.getNERResult("fiancetable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
//		FinalModel.combineNER("fiancetable","Place_Wiki","Organization_Wiki","Person_Wiki","Final_NER");
		
		
		
		FileFinalModel FileFinalModel =  new FileFinalModel();
//		FileFinalModel.getNERRsult("fiancetable", "Final_NER");
//		FileFinalModel.getNERRsult("lifetable", "Final_NER");
		FileFinalModel.getNERRsult("mixtable", "Final_NER");
//		FileFinalModel.getNERRsult("politictable", "Final_NER");
		FileFinalModel.createMap("mixtable","Final_NER",1);
	}

}
