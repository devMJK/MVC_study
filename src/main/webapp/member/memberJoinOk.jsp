<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="app.dao.MemberDao" %>
<%@ page import="app.domain.MemberVo" %>


<%
request.setCharacterEncoding("UTF-8");
response.setContentType("text/html;charset=UTF-8");

//데이터를 넘겨주면 요청 객체는 그 값을 받아서 넘어온 매개변수에
//담긴 값을 꺼내서 새로운 변수에 담는다
String memberId = request.getParameter("memberId");
String memberPwd = request.getParameter("memberPwd");
String memberName= request.getParameter("memberName");
String memberYear = request.getParameter("memberYear");
String memberMonth = request.getParameter("memberMonth");
String memberDay = request.getParameter("memberDay");
String memberGender = request.getParameter("memberGender");
String memberPhone = request.getParameter("memberPhone");
String memberEmail = request.getParameter("memberEmail");
String memberAddr = request.getParameter("memberAddr");
String[] memberHobby = request.getParameterValues("memberHobby");
String str = "";
for(int i = 0; i<memberHobby.length;i++){
	str = str + memberHobby[i]+",";	
}
//substring메소드: 첫번째인자 0번부터 시작하고 두번째인자 번호 이전까지
str = str.substring(0, str.length() - 1);

String memberBirth = memberYear+memberMonth+memberDay;
//쿼리를 실행할 객체를 생성해서
//DB에 입력한다


//쿼리를 실행시키는 객체반환 사용
//stmt 객체 사용
//createStatement 쿼리(구문)를 실행시키는 객체반환 사용
//Statement stmt = conn.createStatement();
//String sql = "insert into member0803(midx,memberid,memberpwd,membername,memberbirth,membergender,memberphone,memberemail,memberaddr,memberhobby)"
//              +" values(midx_seq.nextval,'"+memberId+"','"+memberPwd+"','"+memberName+"','"+memberBirth+"','"+memberGender+"','"+memberPhone+"','"+memberEmail+"','"+memberAddr+"','"+str+"')";

//System.out.println(sql);
//boolean tf = stmt.execute(sql);	//해당구문(쿼리) 실행시킨다
//System.out.println(tf);

MemberDao md = new MemberDao();
int exec = md.memberInsert(memberId,memberPwd,memberName,memberBirth,memberGender,memberPhone,memberEmail,memberAddr,str);

if (exec == 1) {
	//자동이동메소드
	//response.sendRedirect(request.getContextPath()+"/member/memberList.html");
	out.println("<script>alert('회원가입 되었습니다.');"
	+ "document.location.href='"+request.getContextPath()+"/member/memberList.jsp'</script>");
}else {
	out.println("<script>history.back();</script>");
}


%> 
