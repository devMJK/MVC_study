<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "app.domain.*" %>
<%
//포워든 공유속성때문에 넘겨받을 수 있다
ArrayList<MemberVo> list = null;
list = (ArrayList<MemberVo>)request.getAttribute("list"); %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>회원목록</title>
<style type="text/css">
h2 {
	color : purple;
	}
table {
		text-align : center;
		border : 1px solid purple;
		color : purple;
		width : 100%;
		}
thead {
		background : purple;
		color : white;
		height : 40px;
		}
tr {
	height : 30px;
	}
tbody tr:nth-child(even) {
					background : lavender;
					}
tbody tr:nth-child(odd) {
					background : ivory;
					}
tbody tr:hover {
			background : purple;
			color : white;
			
			}
</style>
</head>
<body>
<h2>회원목록</h2>
<table>
<thead>
	<tr>
	<th>회원번호</th>
	<th>아이디</th>
	<th>이름</th>
	<th>가입일</th>
	</tr>
</thead>
<tbody>
	
	<% for(MemberVo mv : list ) {%>
		<tr>
		<td><%=mv.getMidx() %></td>
		<td><%=mv.getMemberId() %></td>
		<td><%=mv.getMemberName() %></td>		
		<td><%=mv.getWriteday() %></td>
		</tr>
	<% } %>	
</tbody>
</table>
</body>
</html>