package sec03.brd08;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource dataFactory;
	private Connection conn;
	private PreparedStatement pstmt;

	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataFactory = (DataSource) envContext.lookup("jdbc/oracle");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List selectAllArticles(Map pagingMap) {
		List list = new ArrayList();
		int section = (Integer) pagingMap.get("section");
		int pageNum = (Integer) pagingMap.get("pageNum");
		try {
			conn = dataFactory.getConnection();
			String sql = "";
			sql ="SELECT * FROM ( "
					+ "select ROWNUM  as recNum,"
						+" LVL,"
						+" articleNO,"
						+" parentNO,"
						+" title,"
						+" id,"
						+" writeDate,"
						+" newArticle "
						+" from (select LEVEL as LVL, "
							+" articleNO,"
							+" parentNO,"
							+" title,"
							+" id,"
							+" writeDate,"
							+" decode(round(sysdate - writeDate),0,'true','false') newArticle "
							+" from t_board" 
							+" START WITH  parentNO=0"
							+" CONNECT BY PRIOR articleNO = parentNO"
							+" ORDER SIBLINGS BY articleNO DESC)"
				+") "
				+" where recNum between(?-1)*100+(?-1)*10+1 and (?-1)*100+?*10";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, section);
			pstmt.setInt(2, pageNum);
			pstmt.setInt(3, section);
			pstmt.setInt(4, pageNum);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("lvl");
				int articleNO = rs.getInt("articleno");
				int parentNO = rs.getInt("parentno");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writedate");
				Boolean newArticle = Boolean.parseBoolean(rs.getString("newarticle"));
				ArticleVO vo = new ArticleVO();
				vo.setLevel(level);
				vo.setArticleNO(articleNO);
				vo.setParentNO(parentNO);
				vo.setTitle(title);
				vo.setId(id);
				vo.setWriteDate(writeDate);
				vo.setNewArticle(newArticle);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List selectAllArticles(Map pagingMap,String notice_yn) {
		List list = new ArrayList();
		int section = (Integer) pagingMap.get("section");
		int pageNum = (Integer) pagingMap.get("pageNum");
		try {
			conn = dataFactory.getConnection();
			String sql = "";
			sql ="SELECT * FROM ( "
					+ "select ROWNUM  as recNum,"
						+" LVL,"
						+" articleNO,"
						+" parentNO,"
						+" title,"
						+" id,"
						+" writeDate,"
						+" newArticle "
						+" from (select LEVEL as LVL, "
							+" articleNO,"
							+" parentNO,"
							+" title,"
							+" id,"
							+" writeDate,"
							+" decode(round(sysdate - writeDate),0,'true','false') newArticle "
							+" from t_board" 
							+" where notice_yn = ?" 
							+" START WITH  parentNO=0"
							+" CONNECT BY PRIOR articleNO = parentNO"
							+" ORDER SIBLINGS BY articleNO DESC)"
				+") "
				+" where recNum between(?-1)*100+(?-1)*10+1 and (?-1)*100+?*10";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, notice_yn);
			pstmt.setInt(2, section);
			pstmt.setInt(3, pageNum);
			pstmt.setInt(4, section);
			pstmt.setInt(5, pageNum);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("lvl");
				int articleNO = rs.getInt("articleno");
				int parentNO = rs.getInt("parentno");
				String title = rs.getString("title");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writedate");
				Boolean newArticle = Boolean.parseBoolean(rs.getString("newarticle"));
				ArticleVO vo = new ArticleVO();
				vo.setLevel(level);
				vo.setArticleNO(articleNO);
				vo.setParentNO(parentNO);
				vo.setTitle(title);
				vo.setId(id);
				vo.setWriteDate(writeDate);
				vo.setNewArticle(newArticle);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List selectAllArticles() {
		List list = new ArrayList();
		try {
			conn = dataFactory.getConnection();
			String sql = "select level,articleno,parentno,title,content,id,writedate from t_board ";
			sql += " start with parentno = 0 connect by prior articleno = parentno order siblings by articleno desc ";
			pstmt = conn.prepareStatement(sql);
			System.out.println("sql=" + sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int level = rs.getInt("level");
				int articleno = rs.getInt("articleno");
				int parentno = rs.getInt("parentno");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String id = rs.getString("id");
				Date writeDate = rs.getDate("writedate");
				ArticleVO vo = new ArticleVO();
				vo.setLevel(level);
				vo.setArticleNO(articleno);
				vo.setParentNO(parentno);
				vo.setTitle(title);
				vo.setContent(content);
				vo.setId(id);
				vo.setWriteDate(writeDate);
				list.add(vo);
				// System.out.println("vo="+vo.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getNewAtricleNO() {
		try {
			conn = dataFactory.getConnection();
			String sql = "select max(articleno) from t_board";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return (rs.getInt(1) + 1);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int insertNewArticle(ArticleVO vo) {
		int articleNO = getNewAtricleNO();
		try {
			conn = dataFactory.getConnection();
			int parentNO = vo.getParentNO();
			String title = vo.getTitle();
			String content = vo.getContent();
			String id = vo.getId();
			String imageFileName = vo.getImageFileName();
			String notice_yn = vo.getNotice_yn();
			String sql = "insert into t_board (articleno,parentno,title,content,imagefilename,id,notice_yn) ";
			sql += " values (?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			pstmt.setInt(2, parentNO);
			pstmt.setString(3, title);
			pstmt.setString(4, content);
			pstmt.setString(5, imageFileName);
			pstmt.setString(6, id);
			pstmt.setString(7, notice_yn);
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNO;
	}

	public ArticleVO selectArticle(int articleNO) {
		ArticleVO vo = new ArticleVO();
		try {
			conn = dataFactory.getConnection();
			String sql = " select articleno,parentno,title,content,nvl(imagefilename,'null') as imagefilename,id,writedate "
					+ " from t_board where articleno = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			int _articleNO = rs.getInt("articleno");
			int parentNO = rs.getInt("parentno");
			String title = rs.getString("title");
			String content = rs.getString("content");
			String imageFileName = URLEncoder.encode(rs.getString("imagefilename"), "utf-8");
			if (imageFileName.equals("null"))
				imageFileName = null;
			String id = rs.getString("id");
			Date writeDate = rs.getDate("writedate");
			vo.setArticleNO(_articleNO);
			vo.setParentNO(parentNO);
			vo.setTitle(title);
			vo.setContent(content);
			vo.setImageFileName(imageFileName);
			vo.setId(id);
			vo.setWriteDate(writeDate);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	public void updateArticle(ArticleVO vo) {
		int articleNO = vo.getArticleNO();
		String title = vo.getTitle();
		String content = vo.getContent();
		String imageFileName = vo.getImageFileName();
		try {
			conn = dataFactory.getConnection();
			String sql = " update t_board set title=?,content=? ";
			if (imageFileName != null && imageFileName.length() != 0) {
				sql += " ,imageFileName=? ";
			}
			sql += " where articleno = ? ";
			System.out.println("sql=" + sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			if (imageFileName != null && imageFileName.length() != 0) {
				pstmt.setString(3, imageFileName);
				pstmt.setInt(4, articleNO);
			} else {
				pstmt.setInt(3, articleNO);
			}
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteArticle(int articleNO) {
		try {
			conn = dataFactory.getConnection();
			String sql = "delete from t_board " + " where articleno in (" + " select articleno from t_board "
					+ " start with articleno = ? " + " connect by prior articleno = parentno) ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			pstmt.executeUpdate();
			System.out.println("sql=" + sql);
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Integer> selectRemoveArticles(int articleNO) {
		List<Integer> articleNOList = new ArrayList<Integer>();
		try {
			conn = dataFactory.getConnection();
			String sql = " select articleno from t_board " + " start with articleno = ? "
					+ " connect by prior articleno = parentno ";
			System.out.println("sql=" + sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, articleNO);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				articleNOList.add(rs.getInt("articleNO"));
			}
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return articleNOList;
	}

	public int selectTotArticles() {
		try {
			conn = dataFactory.getConnection();
			String sql = "select count(articleno) from t_board";
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);
			rs.close();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
