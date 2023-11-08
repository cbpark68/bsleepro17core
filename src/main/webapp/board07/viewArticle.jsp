<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%
request.setCharacterEncoding("UTF-8");
pageContext.setAttribute("crcn","\r\n");
pageContext.setAttribute("br","<br/>");
%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>article</title>
</head>
<style>
#tr_btn_modify {
	display: none;
}
</style>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<c:choose>
	<c:when
		test="${not empty article.imageFileName && articleFileName!='null'}">
		<script>
	function fn_modify(){
		document.getElementById("div_view_article").style.display = "none";
		document.getElementById("div_modify_form").style.display = "block";

	}
	function fn_enable(obj){
 		document.getElementById("i_title").disabled = false;
		document.getElementById("i_content").disabled = false;
		document.getElementById("i_imageFileName").disabled = false;
		document.getElementById("tr_btn_modify").style.display = "block";
		document.getElementById("tr_btn").style.display = "none";
	}
</script>
	</c:when>
	<c:otherwise>
		<script>
		document.getElementById("i_title").disabled = false;
		document.getElementById("i_content").disabled = false;
		document.getElementById("tr_btn_modify").style.display = "block";
		document.getElementById("tr_btn").style.display = "none";
</script>
	</c:otherwise>
</c:choose>
<script>
	function backToList() {
		location.href = "${ctxPath}/board/listArticles.do";
	}
	function fn_modify_article(obj){
		obj.action = "${ctxPath}/board/modArticle.do";
		obj.submit();
	}
	function fn_remove_article(url,articleNO){
		var form = document.createElement("form");
		form.setAttribute("method","post");
		form.setAttribute("action",url);
		var articleNOInput = document.createElement("input");
		articleNOInput.setAttribute("type","hidden");
		articleNOInput.setAttribute("name","articleNO");
		articleNOInput.setAttribute("value",articleNO);
		form.appendChild(articleNOInput);
		document.body.appendChild(form);
		form.submit();
	}
	function fn_reply_form(url,parentNO){
		var form = document.createElement("form");
		form.setAttribute("method","post");
		form.setAttribute("action",url);
		var parentNOInput = document.createElement("input");
		parentNOInput.setAttribute("type","hidden");
		parentNOInput.setAttribute("name","parentNO");
		parentNOInput.setAttribute("value",parentNO);
		form.appendChild(parentNOInput);
		document.body.appendChild(form);
		form.submit();
	}
	function readURL(input){
		if(input.files && input.files[0]){
			var reader = new FileReader();
			reader.onload = function(e){
				$("#preview").attr("src",e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
</script>
<body>
	<h1 style="text-align: center;">수정하기</h1>
	<div id="div_view_article">
		<table border="1" align="center">
			<tr>
				<th align="center">글번호</th>
				<td>${article.articleNO}
					<input type="hidden" name="articleNO" value="${article.articleNO}" />
				</td>
			</tr>
			<tr>
				<th align="center">작성자아이디</th>
				<td>${article.id}</td>
			</tr>
			<tr>
				<th align="center">글제목</th>
				<td>${article.title}</td>
			</tr>
			<tr>
				<th align="center">글내용</th>
				<td> ${fn:replace(article.content,crcn,br)} </td>
			</tr>
			<c:if
				test="${not empty article.imageFileName && article.imageFileName!='null'}">
				<tr>
					<th align="right">이미지</th>
					<td> <img
						src="${ctxPath}/download.do?imageFileName=${article.imageFileName}&articleNO=${article.articleNO}" id="preview" /><br /></td>
				</tr>
			</c:if>
			<tr>
				<th align="center">등록일자</th>
				<td><fmt:formatDate value="${article.writeDate}"/></td>
			</tr>
			<tr id="tr_btn">
				<td colspan="2" align="center">
					<button type="button" onclick="fn_modify();">수정하기</button>
					<button type="button"
						onclick="fn_remove_article('${ctxPath}/board/removeArticle.do',${article.articleNO})">삭제하기</button>
					<button type="button" onclick="backToList()">리스트</button>
					<button type="button"
						onclick="fn_reply_form('${ctxPath}/board/replyForm.do',${article.articleNO})">답글쓰기</button>
				</td>
			</tr>
		</table>
	</div>
	<div id="div_modify_form" style="display: none;">
		<form name="frmArticle" method="post" enctype="multipart/form-data">
			<table border="0" align="center">
				<tr>
					<th align="center">글번호</th>
					<td><input type="text" value="${article.articleNO}" disabled />
						<input type="hidden" name="articleNO" value="${article.articleNO}" />
					</td>
				</tr>
				<tr>
					<th align="center">작성자아이디</th>
					<td><input type="text" value="${article.id}" disabled /></td>
				</tr>
				<tr>
					<th align="center">글제목</th>
					<td><input type="text" value="${article.title}" id="i_title"
						name="title" /></td>
				</tr>
				<tr>
					<th align="center">글내용</th>
					<td><textarea name="content" rows="20" cols="60"
							id="i_content" >${article.content}</textarea></td>
				</tr>
				<c:if
					test="${not empty article.imageFileName && article.imageFileName!='null'}">
					<tr>
						<th align="right" rowspan="2">이미지</th>
						<td><input type="hidden" name="originalFileName"
							value="${article.imageFileName}" /> <img
							src="${ctxPath}/download.do?imageFileName=${article.imageFileName}&articleNO=${article.articleNO}"
							id="preview" /><br /></td>
					</tr>
					<tr>
						<td><input type="file" name="imageFileName"
							id="i_imageFileName" onchange="readURL(this);" />
					</tr>
				</c:if>
				<tr>
					<th align="center">등록일자</th>
					<td><input type="text"
						value="<fmt:formatDate value="${article.writeDate}" />" disabled /></td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<button type="button" onclick="fn_modify_article(frmArticle)">수정하기</button>
						<button type="button" onclick="backToList(frmArticle)">취소</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>