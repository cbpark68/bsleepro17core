package sec03.brd08;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 * Servlet implementation class BoardController
 */
@WebServlet(name = "BoardController8", urlPatterns = { "/board/*" })
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String ARTICLE_IMAGE_REPO = "/home/cbpark68/file_repo/article_image";
	private BoardService service;
	private ArticleVO vo;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		service = new BoardService();
		vo = new ArticleVO();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("GET action=" + action);
		doHandle(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("POST action=" + action);
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		HttpSession session;
		String nextPage = "";
		String action = request.getPathInfo();
		System.out.println("action=" + action);
		try {
			List<ArticleVO> list = new ArrayList<ArticleVO>();
			if (action == null || action.equals("/")) {
				String _section = request.getParameter("section");
				String _pageNum = request.getParameter("pageNum");
				int section = Integer.parseInt(((_section==null)?"1":_section));
				int pageNum = Integer.parseInt(((_pageNum==null)?"1":_pageNum));
				Map<String,Integer> pagingMap = new HashMap<String,Integer>();
				pagingMap.put("section",section);
				pagingMap.put("pageNum",pageNum);
				Map articlesMap  = service.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board07/listArticles.jsp";
			} else if (action.equals("/listArticles.do")) {
				String _section = request.getParameter("section");
				String _pageNum = request.getParameter("pageNum");
				int section = Integer.parseInt(((_section==null)?"1":_section));
				int pageNum = Integer.parseInt(((_pageNum==null)?"1":_pageNum));
				Map pagingMap = new HashMap();
				pagingMap.put("section",section);
				pagingMap.put("pageNum",pageNum);
				Map articlesMap  = service.listArticles(pagingMap);
				articlesMap.put("section", section);
				articlesMap.put("pageNum", pageNum);
				request.setAttribute("articlesMap", articlesMap);
				nextPage = "/board07/listArticles.jsp";
			} else if (action.equals("/articleForm.do")) {
				nextPage = "/board07/articleForm.jsp";
			} else if (action.equals("/addArticle.do")) {
				int articleNO = 0;
				Map<String, String> map = upload(request, response);
				String title = map.get("title");
				String content = map.get("content");
				String imageFileName = map.get("imageFileName");
				String notice_yn = map.get("notice_yn");
				vo.setParentNO(0);
				vo.setId("hong");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				vo.setNotice_yn(notice_yn);
				articleNO = service.addArticle(vo);
				if (imageFileName != null && imageFileName.length() != 0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "/temp/" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "/" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter out = response.getWriter();
				out.print("<script>" + "alert('새글을 추가했습니다.');" + "location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");
				return;
			} else if (action.equals("/viewArticle.do")) {
				String articleNO = request.getParameter("articleNO");
				vo = service.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article", vo);
				nextPage = "/board07/viewArticle.jsp";
			} else if (action.equals("/viewArticle.do")) {
				String articleNO = request.getParameter("articleNO");
				vo = service.viewArticle(Integer.parseInt(articleNO));
				request.setAttribute("article", vo);
				nextPage = "/board07/viewArticle.jsp";
			} else if (action.equals("/modArticle.do")) {
				Map<String, String> map = upload(request, response);
				int articleNO = Integer.parseInt(map.get("articleNO"));
				String title = map.get("title");
				String content = map.get("content");
				String imageFileName = map.get("imageFileName");
				vo.setArticleNO(articleNO);
				vo.setParentNO(0);
				vo.setId("hong");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				service.modArticle(vo);
				if (imageFileName != null && imageFileName.length() != 0) {
					String originalFileName = map.get("originalFileName");
					File srcFile = new File(ARTICLE_IMAGE_REPO + "/temp/" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "/" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
					File oldFile = new File(ARTICLE_IMAGE_REPO + "/" + articleNO + "/" + originalFileName);
					oldFile.delete();
				}
				PrintWriter out = response.getWriter();
				out.print("<script>" + "alert('글을 수정했습니다.');" + "location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
				return;
			} else if (action.equals("/removeArticle.do")) {
				int articleNO = Integer.parseInt(request.getParameter("articleNO"));
				List<Integer> list2 = service.removeArticle(articleNO);
				for (int _articleNO : list2) {
					File imgDir = new File(ARTICLE_IMAGE_REPO + "/" + _articleNO);
					if (imgDir.exists()) {
						FileUtils.deleteDirectory(imgDir);
					}
				}
				PrintWriter out = response.getWriter();
				out.print("<script>" + "alert('글을 삭제했습니다.');" + "location.href='" + request.getContextPath()
						+ "/board/listArticles.do';" + "</script>");
				return;
			} else if (action.equals("/replyForm.do")) {
				try {
					int parentNO = Integer.parseInt(request.getParameter("parentNO"));
					session = request.getSession();
					session.setAttribute("parentNO", parentNO);
					System.out.println("set parentNO=" + parentNO);
					nextPage = "/board07/replyForm.jsp";
				} catch (java.lang.NumberFormatException e) {

				}
			} else if (action.equals("/addReply.do")) {
				session = request.getSession();
				System.out.println("get parentNO=" + session.getAttribute("parentNO"));
				int parentNO = (Integer) session.getAttribute("parentNO");
				session.removeAttribute("parentNO");
				Map<String, String> map = upload(request, response);
				String title = map.get("title");
				String content = map.get("content");
				String imageFileName = map.get("imageFileName");
				vo.setParentNO(parentNO);
				vo.setId("lee");
				vo.setTitle(title);
				vo.setContent(content);
				vo.setImageFileName(imageFileName);
				int articleNO = service.addReply(vo);
				if (imageFileName != null && imageFileName.length() != 0) {
					File srcFile = new File(ARTICLE_IMAGE_REPO + "/temp/" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "/" + articleNO);
					destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);
				}
				PrintWriter out = response.getWriter();
				out.print("<script>" + "alert('답글을 추가했습니다.');" + "location.href='" + request.getContextPath()
						+ "/board/viewArticle.do?articleNO=" + articleNO + "';" + "</script>");
				return;
			} else {
				nextPage = "/board07/listArticles.jsp";
			}
			RequestDispatcher disp = request.getRequestDispatcher(nextPage);
			disp.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> upload(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		String encoding = "utf-8";
		File cpath = new File(ARTICLE_IMAGE_REPO);
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(cpath);
		factory.setSizeThreshold(1024 * 1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List items = upload.parseRequest(request);
			for (int i = 0; i < items.size(); i++) {
				FileItem fileItem = (FileItem) items.get(i);
				if (fileItem.isFormField()) {
					System.out.println(fileItem.getFieldName() + "=" + fileItem.getString(encoding));
					map.put(fileItem.getFieldName(), fileItem.getString(encoding));
				} else {
					if (fileItem.getSize() > 0) {
						int idx = fileItem.getName().lastIndexOf("\\");
						if (idx == -1) {
							idx = fileItem.getName().lastIndexOf("/");
						}
						String fileName = fileItem.getName().substring(idx + 1);
						map.put(fileItem.getFieldName(), fileName);
						File uploadFile = new File(cpath + "/temp/" + fileName);
						fileItem.write(uploadFile);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}