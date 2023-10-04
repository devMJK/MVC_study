<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="app.domain.BoardVo" %>
<%
BoardVo bv = (BoardVo)request.getAttribute("bv");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>글삭제페이지</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
<script>
function check() {
	//alert("클릭확인");
	var fm = document.frm;
	if (fm.pwd.value == "") {	//"" : 입력된 비밀번호가 없다는 의미
		alert("비밀번호를 입력해주세요.");
		fm.pwd.focus();		//focus : 깜박깜박해주는 기능
		return;
	}
	
	fm.action="<%=request.getContextPath()%>/board/boardDeleteAction.do";
	fm.method="post";
	fm.submit();	
		return;
}
</script>
</head>
<body>
<h1>글삭제페이지</h1>
<form name="frm">
<input type="hidden" name="bidx" value="<%=bv.getBidx() %>">
<table border="1" style="width:600px">
		<tr>
		<th>제목</th>
		<td>
		<%=bv.getSubject() %>
		</td>
		</tr>		
		<tr>
		<th>비밀번호</th>
		<td>
		<input type="password" name="pwd">		
		</td>
		</tr>		
		<tr>
		<td colspan="2" style="text-align:center;">
		<input type="button" name="smt" value="확인" onclick="check();"><!--데이터전송기능버튼 -->
		</td>
		</tr>
</table>
</form>

</body>
</html>