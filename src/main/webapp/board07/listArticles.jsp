<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<c:set var="articlesList" value="${articlesMap.articlesList}" />
<c:set var="noticesList" value="${articlesMap.noticesList}" />
<c:set var="totArticles" value="${articlesMap.totArticles}" />
<c:set var="section" value="${articlesMap.section}" />
<c:set var="pageNum" value="${articlesMap.pageNum}" />
<%
request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>list</title>
</head>
<style>
.no-uline {
	text-decoration: none;
}

.sel-page {
	text-decoration: none;
	color: red;
}

.cls1 {
	text-decoration: none;
}

.cls2 {
	text-align: center;
	font-size: 30px;
}
</style>
<body>
	<table align="center" border="1" width="80%">
		<colgroup>
			<col width="5%" />
			<col width="10%" />
			<col width="35%" />
			<col width="10%" />
		</colgroup>
		<tr height="10" align="center" bgcolor="lightgreen">
			<th>글번호</th>
			<th>작성자</th>
			<th>제목</th>
			<th>작성일</th>
		</tr>
		<c:choose>
			<c:when test="${empty articlesList}">
				<th colspan="4">
					<p align="center">
						<span style="font-size: 12px;">등록된 글이 없습니다.</span>
					</p>
				</th>
			</c:when>
			<c:otherwise>
				<c:forEach var="article" items="${noticesList}"
					varStatus="articleNum">
					<tr align="center">
						<td>${articleNum.count}</td>
						<td>${article.id}</td>
						<td align="left"><span style="padding-left: 30px;"></span> <a class="cls1"
							href="${ctxPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
						</td>
						<td><fmt:formatDate value="${article.writeDate}" /></td>
					</tr>
				</c:forEach>
				<c:forEach var="article" items="${articlesList}"
					varStatus="articleNum">
					<tr align="center">
						<td>${articleNum.count}</td>
						<td>${article.id}</td>
						<td align="left"><span style="padding-left: 30px;"></span> <c:choose>
								<c:when test="${article.level > 1}">
									<c:forEach begin="1" end="${article.level}">
										<span style="padding-left: 20px;"></span>
									</c:forEach>
									<span style="font-size: 12px;"><img
										src="${ctxPath}/image/ico_re.gif" /></span>
									<a class="cls1"
										href="${ctxPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
									<c:choose>
										<c:when test="${article.newArticle == true}">
											<img src="${ctxPath}/image/ico_new.gif" />
										</c:when>
									</c:choose>
								</c:when>
								<c:otherwise>
									<a class="cls1"
										href="${ctxPath}/board/viewArticle.do?articleNO=${article.articleNO}">${article.title}</a>
									<c:choose>
										<c:when test="${article.newArticle == true}">
											<img src="${ctxPath}/image/ico_new.gif" />
										</c:when>
									</c:choose>
								</c:otherwise>
							</c:choose></td>
						<td><fmt:formatDate value="${article.writeDate}" /></td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</table>
	<div class="cls2">
		<c:if test="${totArticles != null }">
			<c:choose>
				<c:when test="${totArticles > 100}">
					<c:forEach var="page" begin="1" end="10">
						<c:if test="${section > 1 && page == 1 }">
							<a class="no-uline"
								href="${ctxPath}/board/listArticles.do?section=${section-1}&pageNum=${(section-1)*10+1}">&nbsp;
								prev</a>
						</c:if>
						<a class="no-uline"
							href="${ctxPath}/board/listArticles.do?section=${section}&pageNum=${page}">${(section-1)*10+page}</a>
						<c:if test="${page == 10 }">
							<a class="no-uline"
								href="${ctxPath}/board/listArticles.do?section=${section+1}&pageNum=${section*10+1}">&nbsp;
								next</a>
						</c:if>
					</c:forEach>
				</c:when>
				<c:when test="${totArticles == 100}">
					<c:forEach var="page" begin="1" end="10">
						<c:choose>
							<c:when test="${page == pageNum}">
								<a class="sel-page"
									href="${ctxPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:when>
							<c:otherwise>
								<a class="no-uline"
									href="${ctxPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
				<c:when test="${totArticles < 100}">
					<c:forEach var="page" begin="1" end="${totArticles/10 + 1}">
						<c:choose>
							<c:when test="${page == pageNum}">
								<a class="sel-page"
									href="${ctxPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:when>
							<c:otherwise>
								<a class="no-uline"
									href="${ctxPath}/board/listArticles.do?section=${section}&pageNum=${page}">${page}</a>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:if>
	</div>
	<a class="cls1" href="${ctxPath}/board/articleForm.do"><p
			class="cls2">글쓰기</p></a>
</body>
</html>