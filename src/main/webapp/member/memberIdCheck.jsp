<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="app.dao.MemberDao" %>
<%@ page import="app.domain.MemberVo" %>
<%
String memberId = request.getParameter("memberId");

MemberDao md = new MemberDao();
int value = md.memberIdCheck(memberId);

String str = "{\"cnt\" : \""+value+"\"}";
out.println(str);

%>
