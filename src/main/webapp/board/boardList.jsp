<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="app.domain.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
  //ArrayList<BoardVo> list = (ArrayList<BoardVo>)request.getAttribute("alist");
  //PageMaker pm = (PageMaker)request.getAttribute("pm");
  //System.out.println("pm값은"+pm.getStartPage());	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>게시글목록</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
</head>
<body>
<h1>게시판 목록</h1>
<form name="frm" action="${pageContext.request.contextPath}/board/boardList.do" method="post">
<table border=0 style="width:600px">
<tr>
<td style="width:500px">
</td>
<td>
<select name="searchType">
<option value="subject">제목</option>
<option value="writer">작성자</option>
</select>
</td>
<td>
<input type="text" name="keyword" size=10>
</td>
<td>
<input type="submit" name="sbt" value="검색">
</td>
</tr>
</table>
</form>
<table border=1 style="width:600px">	
<thead>
		<tr>
		<th>글번호</th>
		<th>제목</th>
		<th>작성자</th>
		<th>조회수</th>
		<th>작성일</th>
		</tr>
</thead>
<tbody>
		<%// for (BoardVo bv : list) {%>	
		<c:forEach var="bv" items="${alist}">
		<tr>
		<td><%//=bv.getBidx() %>${bv.bidx}</td>
		<td class="subject">
		<%// for(int i=1;i<=bv.getLevel_();i++){
			//	out.print("&nbsp;&nbsp;");
			//	if (i == bv.getLevel_()){
			//		out.print("ㄴ");
			//	}			
			//} 
		%>
		
		<c:forEach var="i" begin="1" end="${bv.level_}" step="1">
			&nbsp;&nbsp;
			<c:if test="${i == bv.level_ }">
				ㄴ
			</c:if>
		</c:forEach>
		<a href="${pageContext.request.contextPath}/board/boardContents.do?bidx=${bv.bidx}">
		<%//=bv.getSubject() %>
		${bv.subject}
		</a>		
		</td>
		<td><%//=bv.getWriter() %>${bv.writer}</td>
		<td><%//=bv.getViewcnt() %>${bv.viewcnt}</td>
		<td><%//=bv.getWriteday() %>${bv.writeday}</td>
		</tr>
		</c:forEach>
		<%// } %>	
		
</tbody>		
</table>
<% 
//String parameter = ""; 		//parameter 변수 초기화시키기
//if (!pm.getScri().getKeyword().equals("")){
//	parameter = "&searchType="+pm.getScri().getSearchType()+"&keyword="+pm.getScri().getKeyword()+"";		//& : 연결문자열
//}
%>
<c:set var="keyword" value="${pm.scri.keyword}" />
<c:set var="parm" value="&searchType=${pm.scri.searchType}&keyword=${pm.scri.keyword}" />

<table border= 0 style="width:600px; text-align:center;">
<tr>
<td style="width:100px; text-align:right;">
<%// if(pm.isPrev()==true) { %>
<c:if test="${pm.prev == true}">
<a href = "${pageContext.request.contextPath}/board/boardList.do?page=${pm.startPage-1}${parm}">
◀
</a>
</c:if>
<%// } %>
</td>
<td> 
<% 
//for(int i=pm.getStartPage();i<=pm.getEndPage();i++){
%>
<c:forEach var="i" begin="${pm.startPage}" end="${pm.endPage}" step="1">
			
<a href="${pageContext.request.contextPath}/board/boardList.do?page=${i}${parm}">${i}</a> &nbsp;
<%// }%>
</c:forEach>
</td>
<td style="width:100px; text-align:left;">
<%// if(pm.isNext()==true && pm.getEndPage()>0) { %>
<c:if test="${pm.next == true && pm.endPage>0}">
<a href = "${pageContext.request.contextPath}/board/boardList.do?page=${pm.endPage+1}${parm}">
▶
</a>
<%// } %>
</c:if>
</td>
</tr>
</table>

<a href= "${pageContext.request.contextPath}/board/boardWrite.do">글쓰기</a>

</body>
</html>