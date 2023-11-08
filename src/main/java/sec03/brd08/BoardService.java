package sec03.brd08;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {
	BoardDAO dao;
	
	public BoardService() {
		dao = new BoardDAO();
	}
	
	public Map listArticles(Map<String,Integer> pagingMap) {
		Map map = new HashMap();
		List<ArticleVO> notice = dao.selectAllArticles(pagingMap,"Y");
		List<ArticleVO> list = dao.selectAllArticles(pagingMap,"N");
		int tot = dao.selectTotArticles();
		map.put("noticesList", notice);
		map.put("articlesList", list);
		map.put("totArticles", tot);
		//map.put("totArticles", 100);
		return map;
	}

	public List<ArticleVO> listArticles(){
		List<ArticleVO> list = dao.selectAllArticles();
		return list;
	}
	
	public int addArticle(ArticleVO vo) {
		return dao.insertNewArticle(vo);
	}
	
	public ArticleVO viewArticle(int articleNO) {
		ArticleVO vo = null;
		vo = dao.selectArticle(articleNO);
		return vo;
	}
	
	public void modArticle(ArticleVO vo) {
		dao.updateArticle(vo);
	}
	
	public List<Integer> removeArticle(int articleNO){
		List<Integer> articleNOList = dao.selectRemoveArticles(articleNO);
		dao.deleteArticle(articleNO);
		return articleNOList;
	}
	
	public int addReply(ArticleVO vo) {
		return dao.insertNewArticle(vo);
	}
	
	
}