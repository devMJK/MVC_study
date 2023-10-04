<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Index</title>
</head>
<body>
기본 페이지입니다.
<br>
<%
String memberId = "";
//세션값이 있다는 것은 로그인을 했다는 것
if(session.getAttribute("memberId") != null) {
memberId = (String)session.getAttribute("memberId");
out.println("<a href='"+request.getContextPath()+"/member/memberLogout.do'>로그아웃</a>");
}
%>
<br>
<a href = "<%=request.getContextPath() %>/member/memberList.do">회원정보보기</a>
<br>
<a href = "<%=request.getContextPath() %>/member/memberJoin.do">회원가입페이지</a>
<br>
<a href = "<%=request.getContextPath() %>/member/memberLogin.do">회원로그인페이지</a>
<br>
<a href = "<%=request.getContextPath() %>/board/boardList.do">게시판가기</a>
<br>
</body>
</html>