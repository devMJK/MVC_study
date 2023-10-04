<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
if (session.getAttribute("midx") == null){
	 out.println("<script>alert('로그인하셔야합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");	
 }

%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>글쓰기페이지</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
<script>
function check(){
	var fm = document.frm;	//문서객체안의 폼객체이름
	//유효성검사
	if(fm.subject.value == ""){
		alert("제목을 입력하세요");
		fm.subject.focus();
		return;
	}else if (fm.contents.value ==""){
		alert("내용을 입력하세요");
		fm.contents.focus();
		return;
	}else if (fm.writer.value ==""){
		alert("작성자를 입력하세요");
		fm.writer.focus();
		return;
	}else if (fm.pwd.value ==""){
		alert("비밀번호를 입력하세요");
		fm.pwd.focus();
		return;
	}
	//처리하기위해 이동하는 주소
	fm.action = "<%=request.getContextPath()%>/board/boardWriteAction.do";	
	fm.method = "post";		//이동하는 방식 get : 노출시킴 post : 감추어서 전달
	fm.enctype = "multipart/form-data";
	fm.submit();	//전송시킴
	return;
}

</script>
</head>
<body>

<h1>글쓰기페이지</h1>
	<form name="frm">
		<table border="1" style="width:600px">
		<tr>
		<th>제목</th>
		<td>
		<input type="text" name="subject">		
		</td>
		</tr>
		<tr>
		<th>내용</th>
		<td>
		<textarea name="contents" cols="50" rows="10"></textarea>		
		</td>
		</tr>
		<tr>
		<th>작성자</th>
		<td>
		<input type="text" name="writer">		
		</td>
		</tr>
		<tr>
		<th>비밀번호</th>
		<td>
		<input type="password" name="pwd">		
		</td>
		</tr>
		<tr>
		<th>파일첨부</th>
		<td>
		<input type="file" name="filename">		
		</td>
		</tr>
		<tr>
		<td colspan="2" style="text-align:center;">
		<input type="button" name="smt" value="확인" onclick="check();">
		<!--데이터전송기능버튼 -->
		</td>
		</tr>
</table>
</form>
</body>
</html>