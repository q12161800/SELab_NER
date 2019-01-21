package WikiModel;

public class WIKIPlace implements WikiNERInterface {

	@Override
	public boolean checkWIKINER(String content) {
		// TODO Auto-generated method stub
		//比較WikiInfobox分類與NER結果是否符合，如果是即可判定為地名
		
		if(content.contains("人口總數")||
				content.contains("容納人數")||
				content.contains("面積")||
				content.contains("聯邦")||
				content.contains("城市")||
				content.contains("島國")||
				content.contains("景點")||
				content.contains("地標")||
				content.contains("首府")||
				content.contains("海拔")||
				content.contains("時區")||
				content.contains("成員國")||
				content.contains("地名消歧義")||
				content.contains("建築物消歧義")){
			return true;
		}	
		return false;
	}

}
