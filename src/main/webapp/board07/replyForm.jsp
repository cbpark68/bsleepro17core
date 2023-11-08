<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
request.setCharacterEncoding("UTF-8");
%>
<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>article</title>
</head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	function readURL(input) {
		if (input.files && input.files[0]) {
			var reader = new FileReader();
			reader.onload = function(e) {
				$("#preview").attr("src", e.target.result);
			}
			reader.readAsDataURL(input.files[0]);
		}
	}
	function backToList(obj){
		obj.action="${ctxPath}/board/listArticle.do";
		obj.submit();
	}
</script>
<body>
<h1 style="text-align:center;">답글쓰기</h1>
<form name="frmReply" method="post" action="${ctxPath}/board/addReply.do" enctype="multipart/form-data">
<table border="0" align="center">
	<tr>
		<th align="right">글쓴이</th>
		<td colspan="2"><input type="text" size="5" value="lee" disabled/></td>
	</tr>
	<tr>
		<th align="right">글제목</th>
		<td colspan="2"><input type="text" size="67" maxlength="500" name="title"/></td>
	</tr>
	<tr>
		<th align="right" valign="top"><br/>글내용: </th>
		<td colspan="2"><textarea name="content" rows="10" cols="65" maxlength="4000"></textarea></td>
	</tr>
	<tr>
		<th align="right">이미지파일첨부: </th>
		<td><input type="file" name="imageFileName" onchange="readURL(this);"/></td>
		<td><img id="preview" src="#" width="200" height="200"/></td>
	</tr>
	<tr>
		<td align="right"></td>	
		<td colspan="2">
			<button type="submit">답글쓰기</button>	
			<button type="button" onclick="backToList(this.form)">리스트</button>
		</td>
	</tr>
</table>
</form>
</body>
</html>