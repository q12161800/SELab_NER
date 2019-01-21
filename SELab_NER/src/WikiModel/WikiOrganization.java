package WikiModel;

public class WikiOrganization implements WikiNERInterface {
	
	@Override
	public boolean checkWIKINER(String content) {
		// TODO Auto-generated method stub
		//比較WikiInfobox分類與NER結果是否符合，如果是即可判定為組織
				
		if(content.contains("員工人數")||
				content.contains("政府機構")||
				content.contains("成立日期")||
				content.contains("公司類型")||
				content.contains("學校類型")||
				content.contains("組織機構消歧義")||
				content.contains("教育機構消歧義‎")||
				content.contains("航空公司消歧義‎")||
				content.contains("球隊")||
				content.contains("樂團")||
				content.contains("機構類型")|| 
				content.contains("成立者")||
				content.contains("創辦人")||
				content.contains("行政部門")||
				content.contains("公司類型")||
				content.contains("成立的公司")){
			return true;
		}
		return false;
		
		
	}

	
}
