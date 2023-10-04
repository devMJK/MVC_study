<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="app.domain.BoardVo" %>    
<%
	if (session.getAttribute("midx") == null){
		 out.println("<script>alert('로그인하셔야합니다.');location.href='"+request.getContextPath()+"/member/memberLogin.do'</script>");	
	}
	
	BoardVo bv = (BoardVo)request.getAttribute("bv");
%>   
    
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>글내용보기</title>
<link href="./css/board.css" type="text/css" rel="stylesheet">
<!-- 1.cdn주소걸고 (라이브러리) -->
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<script type="text/javascript">
//자동실행영역
$(document).ready(function(){
	$.boardCommentList();
	
	//버튼을 클릭하면 입력된 데이터를 가지고 commentWrite.do로 넘겨서 DB에 입력
	$("#save").on("click",function(){
		//alert("클릭");
		
		let cwriter = $("#cwriter").val();
		let ccontents = $("#ccontents").val();
		let bidx = "<%=bv.getBidx()%>";
		let midx = "<%=session.getAttribute("midx")%>";
		
		$.ajax({	//인자값을 배열형태로 넘겨줌
			type : "post",	//post방식으로 주소를 넘긴다
			url : "<%=request.getContextPath()%>/comment/commentWrite.do",
			dataType : "json",
			data : {
					"bidx" : bidx,
					"midx" : midx,
					"cwriter" : cwriter,
					"ccontents" : ccontents
			},			
			cache : false,
			success : function(data){
				//alert("통신성공");
				//alert(data.value);
				//if (data.value = 1){
					//alert("등록성공");
				//}
				$.boardCommentList();
				$("#cwriter").val("");
				$("#ccontents").val("");
			},
			
			error : function(){
				alert("통신오류 실패");
			}		
		});
	});
});

//jquery 함수
$.boardCommentList = function(){
	
	$.ajax({
		type : "get",	//get방식으로 주소를 넘긴다
		url : "<%=request.getContextPath()%>/comment/commentList.do", //commentList를 가져오게끔 가상경로를 만들어줌
		dataType : "json",
		cache : false,
		success : function(data){
			//alert("통신성공");
			
			commentList(data);
			//$.each(data, function(index){
			//	alert(index);
			//	alert(data[index].cidx);
			// });
		},
		error : function(){
			alert("통신오류 실패");
			
		}		
	});
	
}

function commentDel(cidx) {	//commentDel는 매개변수로 cidx를 받겠다
	
	$.ajax({
		type : "get",	//cidx값 하나만 넘기니까 get방식으로 주소를 넘긴다 -> get방식은 하단의 URL 끝에 ?붙여서 cidx 붙여주기
		url : "<%=request.getContextPath()%>/comment/commentDelete.do?cidx="+cidx, //commentDelete를 가져오게끔 가상경로를 만들어줌
		dataType : "json",
		cache : false,
		success : function(data){
			//alert("통신성공");
			if (data.value == 1){
					alert("삭제 성공");
				}
			
				$.boardCommentList();
			//$.each(data, function(index){
			//	alert(data[index].cidx);
			// })
		},
		error : function(){
			alert("통신오류 실패");
		}
	});
	return;
}



function commentList(data){
	
	var str="";
	str = "<tr><td>번호</td><td>작성자</td><td>내용</td><td>등록일</td><td>삭제</td></tr>"
	
	var delBtn = "";
	
	var loginMidx = "<%=session.getAttribute("midx")%>";
	
	$(data).each(function(){
	
		if (loginMidx == this.midx){
			delBtn = "<button type='button' id='btn' onclick='commentDel("+this.cidx+");'>삭제</button>";
		}else {
			delBtn = "";
		}
				
		str = str + "<tr><td>"+this.cidx+"</td><td>"+this.cwriter+"</td><td>"+this.ccontents+"</td><td>"+this.cwriteday+"</td><td>"+delBtn+"</td></tr>"	
	
	
	});
	
	$("#tbl").html("<table border=1 style='width:600px'>"+str+"</table>");
	
	return;
}

</script>
</head>
<body>
<h1>게시판 글내용</h1>
		<table border=1 style="width:600px;">
		<tr>
		<th>제목</th>
		<td><%=bv.getSubject() %></td>
		<th>조회수</th>
		<td><%=bv.getViewcnt() %></td>
		</tr>
		<tr>
		<th style="height:200px;">내용</th>
		<td colspan=3><%=bv.getContents() %></td>
		</tr>
		<tr>
		<th>작성자</th>
		<td colspan=3><%=bv.getWriter() %></td>
		</tr>
		<tr>
		<th></th>
		<td style="text-align:right;">
		<button type="button" onclick="location.href='<%=request.getContextPath()%>/board/boardModify.do?bidx=<%=bv.getBidx() %>'">수정</button>
		<button type="button" onclick="location.href='<%=request.getContextPath()%>/board/boardDelete.do?bidx=<%=bv.getBidx() %>'">삭제</button>
		<button type="button" onclick="location.href='<%=request.getContextPath()%>/board/boardReply.do?bidx=<%=bv.getBidx() %>&originbidx=<%=bv.getOriginbidx()%>&depth=<%=bv.getDepth()%>&level_=<%=bv.getLevel_()%>'">답변</button>
		<button type="button" onclick="location.href='<%=request.getContextPath()%>/board/boardList.do'">목록</button>			
		</td>
		</tr>
</table>
<br>
<table border=1 style="width:600px;">
<tr>
<td style="text-align:center;">작성자</td>
<td><input type="text" name="cwriter" id="cwriter" size="20"></td>
<td rowspan=2 style="text-align:center;">
<input type="button" name="btn" value="저장" id="save"></td>
</tr>
<tr>
<td style="text-align:center;">내용</td>
<td>
<textarea name="ccontents" id="ccontents" cols="50" rows="3" placeholder="내용을 입력하세요.">
</textarea>
</td>
</tr>
</table>
<br>
<div id="tbl"></div>

</body>
</html>