package FileModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class FileMgr {
	
	public StringBuilder ReadTxt(String txtname) throws Exception {
		StringBuilder ruletxt =  new StringBuilder();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(
				new FileInputStream(txtname), "UTF-8"));
		String flag;
		while (br.ready()) {
			ruletxt.append(br.readLine() + "\r\n");
		}
		//System.out.println(ruletxt.toString());
		return ruletxt;
	}
	
	public HashMap<String, String> ReadPaper(String foldername, String msg) throws Exception {
		System.out.println(msg);
		HashMap<String, String> PaperListMap = new HashMap();
		for (File f : new File(foldername).listFiles()) {
			String filename = SplitFileName(f.toString(), foldername);
			String context = "";
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
					new FileInputStream(f), "UTF-8"));
			while (br.ready()) {
				context = context + br.readLine().trim();
			}
			//System.out.println("Filename: "+filename+", Context: "+context);
			PaperListMap.put(filename, context);
		}
		return PaperListMap;
	}
	
	public void WriteFile(String foldername, String papername, String context) throws IOException {
		BufferedWriter fw = null;
		File f = new File(foldername + "\\"+ papername +"");
		fw = new BufferedWriter(
				new OutputStreamWriter(
						new FileOutputStream(f,false), "utf-8"));
		
		fw.write(context);
		fw.flush();
		fw.close();
	}
	
	
	
	public String SplitFileName(String file, String split) {

		String[] File = file.split(split);
		File[1] = File[1].replace("\\", "");

		return File[1].trim();

	}

}
