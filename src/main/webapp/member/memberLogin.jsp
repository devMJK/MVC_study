<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Member Login</title>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>

<style type="text/css">

h3 {
	text-align : center;
	color : darkgreen;
	width : 300px;
	}

fieldset {
			border : 3px dotted darkgreen;
			font-size : 15px;
			background : lightyellow;
			color : darkgreen;
			width : 300px;
			height : 150px;
			text-align : center;
			}
table {
		margin : 20px;
		}
input {
		border : 2px solid darkgreen;
		background : lightyellow;
		color : darkgreen;
		margin-bottom : 10px;
		}
input[type=text]:hover {
							background : darkgreen;
							color : white;
							} 
input[type=password]:hover {
							background : darkgreen;
							color : white;
							} 
input[type=button] {
					background : darkgreen;
					color : lightyellow;
					font-size : 15px;
					}
input[type=button]:hover {
							border : 2px solid darkgreen;
							background : lightyellow;
							color : darkgreen;
							}

</style>
</head>
<body>
<script type="text/javascript">
function check(){

	var fm = document.frm;	//문서객체안의 폼객체이름
	
	if(fm.memberId.value == ""){
		alert("아이디를 입력하세요");
		fm.memberId.focus();
		return;
	}else if (fm.memberPwd.value ==""){
		alert("비밀번호를 입력하세요");
		fm.memberPwd.focus();
		return;
	}
	
	fm.action = "<%=request.getContextPath()%>/member/memberLoginAction.do";	//처리하기위해 이동하는 주소
	fm.method = "post";		//이동하는 방식 get : 노출시킴 post : 감추어서 전달
	fm.submit();	//전송시킴
	return;
}
</script>

<h3>회원로그인</h3>
<form name="frm">
<fieldset>
	<legend>Member Login</legend>
		<table>
			<tr>
				<td>아이디 :</td>
				<td><input type="text" name="memberId"></td>
			</tr>
			<tr>
				<td>비밀번호 :</td>
				<td><input type="password" name="memberPwd"></td>
			</tr>
			<tr>
			<td colspan=2 style="text-align:center">
			<input type="button" name="btn" value="확인" onclick="check();">
			</td>
			</tr>
		</table>
</fieldset>
</form>
</body>
</html>