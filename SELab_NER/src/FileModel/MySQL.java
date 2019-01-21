package FileModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MySQL {
	private Connection con = null; // Database objects
	// 連接object
	private Statement stat = null;
	// 執行,傳入之sql為完整字串
	private ResultSet rs = null;
	// 結果集
	private PreparedStatement pst = null;

	PropsReader propsreader = new PropsReader(); 
	// 執行,傳入之sql為預儲之字申,需要傳入變數之位置
	// 先利用?來做標示
	public MySQL() {
		try {
			Class.forName(propsreader.getProperties("driver"));// com.mysql.jdbc.Driver
			// 註冊driver 140.125.84.46:3306
			String sqlconcmd = propsreader.getProperties("url") + propsreader.getProperties("databaseName")
					+ "?useUnicode=" + propsreader.getProperties("useUnicode") + "&characterEncoding="
					+ propsreader.getProperties("characterEncoding") + "&user=" + propsreader.getProperties("user")
					+ "&password=" + propsreader.getProperties("password");

			System.out.println(sqlconcmd);
			con = DriverManager.getConnection(sqlconcmd);

			// 取得connection
			// jdbc:mysql://140.125.84.60:3306/poetry?useUnicode=true&characterEncoding=utf-8&user=root&password=root
			// 140.125.84.60是主機名,poetry是database名
			// useUnicode=true&characterEncoding=utf-8使用的編碼
			System.out.println("成功連結");
		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		} // 有可能會產生sqlexception
		catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
	}

	public JSONArray getNews(String tablename) {
		try {

			String sql = "select id,title, content from " + propsreader.getProperties(tablename) + " ;";
			
			System.out.println(sql);
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int count = 0;
			JSONArray newsarray = new JSONArray();
			while (rs.next()) {
					// 建立jsonobject存放data
					JSONObject singlenews = new JSONObject();
					// 初始化
					singlenews.put("id", rs.getInt("id"));
					singlenews.put("content", rs.getString("title") +"。"+ rs.getString("content").replaceAll("\n", "").replaceAll("[，。？!]", "　"));
					System.out.println(rs.getString("content").length());
					System.out.println(rs.getString("content"));
					// 預設存放斷詞結果
					singlenews.put("NERCKIPSegmentContent", "");

					// put poetry to jsonarray
					newsarray.put(singlenews);
					count++;
						
			}
			System.out.println(count);
			return newsarray;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException:" + e.toString());
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSON Exception :" + e.toString());
		} 
		finally {
			Close();
		}
		return null;
	}
	
	public JSONArray getCKIPNews(String tablename) {
		try {

			String sql = "select id, NERCKIPSegmentContent from " + propsreader.getProperties(tablename) + ";";

			System.out.println(sql);
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int count = 0;
			JSONArray newsarray = new JSONArray();
			while (rs.next()) {
					// 建立jsonobject存放data
					JSONObject singlenews = new JSONObject();
					// 初始化
					singlenews.put("id", rs.getInt("id"));
					singlenews.put("NERCKIPSegmentContent", rs.getString("NERCKIPSegmentContent"));
					// 預設存放斷詞結果

					singlenews.put("RuleResult", "");
					// put poetry to jsonarray
					newsarray.put(singlenews);
					count++;	
			}
			System.out.println(count);
			return newsarray;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException:" + e.toString());
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSON Exception :" + e.toString());
		} 
		finally {
			Close();
		}
		return null;
	}
	
	public JSONArray getNERRuleResult(String tablename, String columnname) {
		try {

			String sql = "select id, NERCKIPSegmentContent, "+ columnname + " from " + propsreader.getProperties(tablename) + ";";

			System.out.println(sql);
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int count = 0;
			JSONArray newsarray = new JSONArray();
			while (rs.next()) {
					// 建立jsonobject存放data
					JSONObject singlenews = new JSONObject();
					// 初始化
					singlenews.put("id", rs.getInt("id"));
					System.out.println("--------Running " + tablename + " ID " + rs.getInt("id")+"-------");
					singlenews.put("NERCKIPSegmentContent", rs.getString("NERCKIPSegmentContent"));
					singlenews.put(columnname,new JSONArray(rs.getString(columnname)));
					// 預設存放斷詞結果
					singlenews.put("PosList", "");
					singlenews.put("TermList", "");
					singlenews.put("Result", "");
					// put poetry to jsonarray
					newsarray.put(singlenews);
					count++;	
			}
			//check the number of data
			//System.out.println(count);
			return newsarray;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException:" + e.toString());
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSON Exception :" + e.toString());
		} 
		finally {
			Close();
		}
		return null;
	}


	public void updateCKIPResult(String tablename,String content, int id) {
		String sql = "UPDATE "+ propsreader.getProperties(tablename) + " SET NERCKIPSegmentContent = ? " + "WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			// con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateNERResult(String tablename,String columnname,String content, int id) {
		String sql = "UPDATE "+ propsreader.getProperties(tablename) + " SET "+ columnname +" = ? " + "WHERE id = ?";

		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, content);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
			// con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONArray getAllNERRuleResult(String tablename, String columnplace , String columnorganization, String columnperson) {
		try {

			String sql = "select id, NERCKIPSegmentContent, "+ columnplace +", " + columnorganization +", " + columnperson +" from " + propsreader.getProperties(tablename) + " ;";

			System.out.println(sql);
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			int count = 0;
			JSONArray newsarray = new JSONArray();
			while (rs.next()) {
					// 建立jsonobject存放data
					JSONObject singlenews = new JSONObject();
					// 初始化
					singlenews.put("id", rs.getInt("id"));
					singlenews.put("NERCKIPSegmentContent", rs.getString("NERCKIPSegmentContent"));
					singlenews.put(columnplace,new JSONArray(rs.getString(columnplace)));
					singlenews.put(columnorganization,new JSONArray(rs.getString(columnorganization)));
					singlenews.put(columnperson,new JSONArray(rs.getString(columnperson)));
					// 預設存放斷詞結果
					singlenews.put("PosList", "");
					singlenews.put("TermList", "");
					singlenews.put("Result", "");
					// put poetry to jsonarray
					newsarray.put(singlenews);
					count++;	
			}
			//check the number of data
			//System.out.println(count);
			return newsarray;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQLException:" + e.toString());
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSON Exception :" + e.toString());
		} 
		finally {
			Close();
		}
		return null;
	}

	
	

	public void Close() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stat != null) {
				stat.close();
				stat = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}
}
