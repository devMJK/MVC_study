package app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import app.dao.MemberDao;
import app.domain.MemberVo;

// HttpServlet를 상속받았기때문에 클래스가 인터넷페이지가 된다
@WebServlet("/MemberController")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private String location;
	public MemberController(String location) {
		this.location = location;		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				
		//문자열 비교
		if (location.equals("memberList.do")) {
			
			MemberDao md =new MemberDao();
			ArrayList<MemberVo> list =  md.memberSelectAll();
			
			request.setAttribute("list", list);
			
			 String path ="/member/memberList.jsp";
			 //화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			 RequestDispatcher rd = request.getRequestDispatcher(path);
			 rd.forward(request, response);
			
		}else if(location.equals("memberJoin.do")) {
			
			String path ="/member/memberJoin.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		
		}else if(location.equals("memberJoinAction.do")) {			
			
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
			
			String memberBirth  = memberYear+memberMonth+memberDay;
			//쿼리를 실행할 객체를 생성해서 
			
			//DB에 입력한다
			MemberDao md = new MemberDao();
			int exec = md.memberInsert(memberId,memberPwd,memberName,memberBirth,memberGender,memberPhone,memberEmail,memberAddr,str);
			
			PrintWriter out = response.getWriter();
			
			if (exec == 1) {
				//자동이동메소드
				//response.sendRedirect(request.getContextPath()+"/member/memberList.html");	
				out.println("<script>alert('회원가입 되었습니다.');"
				+	"document.location.href='"+request.getContextPath()+"/member/memberList.do'</script>");
			}else{
				out.println("<script>history.back();</script>");	
			}			
		}else if(location.equals("memberLogin.do")) {
			
			String path ="/member/memberLogin.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
			
		}else if(location.equals("memberLoginAction.do")) {
			
			String memberId = request.getParameter("memberId");
			String memberPwd = request.getParameter("memberPwd");
						
			MemberDao md = new MemberDao();
			int midx = 0; 
			midx = md.memberLoginCheck(memberId,memberPwd);
			
			
			//Action처리하는 용도는 send방식으로 보낸다
			
			if (midx !=0){  //일치하면
				//세션에 회원아이디를 담는다
				HttpSession session = request.getSession();
				session.setAttribute("memberId", memberId);
				session.setAttribute("midx", midx);
				
				response.sendRedirect(request.getContextPath()+"/");
			}else{
				response.sendRedirect(request.getContextPath()+"/member/memberLogin.do");
			}	
		}else if(location.equals("memberLogout.do")) {
			
			HttpSession session = request.getSession();
			session.removeAttribute("memberId");
			session.removeAttribute("midx");
			session.invalidate();
			
			response.sendRedirect(request.getContextPath()+"/");
		}	
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
