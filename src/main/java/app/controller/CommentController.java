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

import app.dao.BoardDao;
import app.dao.CommentDao;
import app.dao.MemberDao;
import app.domain.BoardVo;
import app.domain.CommentVo;
import app.domain.MemberVo;

@WebServlet("/CommentController")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private String location; 
	public CommentController(String location){
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		if (location.equals("commentList.do")) {
			
			CommentDao cd =new CommentDao();
			ArrayList<CommentVo> list =  cd.commentSelectAll();
			int listCnt = list.size();
			int cidx = 0;
			String cwriter = "";
			String ccontents = "";
			String cwriteday = "";
			int midx = 0;
			String str = "";
			
			//들어가 있는 데이터의 개수만큼 돌리기 위해 for문
			for(int i = 0; i < listCnt; i++) {
				cidx = list.get(i).getCidx();
				cwriter = list.get(i).getCwriter();
				ccontents = list.get(i).getCcontents();
				cwriteday = list.get(i).getCwriteday();
				midx = list.get(i).getMidx();
				
				//마지막 데이터에는 쉼표가 필요없으므로 if구문을 사용
				String comma = "";
				if (i == listCnt-1) {	//i가 listsize의 마지막 횟수이면 ,(comma)가 아닌 쉼표를 쓸거다
					comma = "";
				}else {
					comma = ",";
				}
				
				str = str + "{\"cidx\":\""+cidx+"\",\"cwriter\":\""+cwriter+"\",\"ccontents\":\""+ccontents+"\",\"cwriteday\":\""+cwriteday+"\",\"midx\":\""+midx+"\"}"+comma;	//괄호 밖의 큰따옴표와 괄호 안의 큰따옴표 구분을 위해 역슬래쉬(\) 기입
				
			}
			
			//json파일형식의 여러개의 문서를 배열형태로 담는다
			//String str = " \"data\" : [{\"nm\" : \"홍길동\" },{\"nm\" : \"이순신\" }]";
			
			PrintWriter out = response.getWriter();
			out.println("["+str+"]");
			
		}else if(location.equals("commentWrite.do")) {
			
			String bidx = request.getParameter("bidx");
			String midx = request.getParameter("midx");
			String cwriter = request.getParameter("cwriter");
			String ccontents = request.getParameter("ccontents");
			
			CommentVo cv = new CommentVo();
			cv.setBidx(Integer.parseInt(bidx));		//숫자형으로 변환 후 cv에 담는다 
			cv.setMidx(Integer.parseInt(midx));
			cv.setCwriter(cwriter);
			cv.setCcontents(ccontents);
			
			int value=0;
			//댓글입력 메소드 만든다 -> CommentDao에 만들기(line 64) -> 만들고 CommentController 로 돌아와서 실행시키기
			CommentDao cd = new CommentDao();
			value = cd.commentInsert(cv);
			
			String str ="{\"value\":\""+value+"\"}";
			
			PrintWriter out = response.getWriter();
			out.println(str);		
			
		}else if(location.equals("commentDelete.do")) {
			
			String cidx = request.getParameter("cidx");
			int value=0;	//결과값을 담을 value를 초기화
			//처리하는 메소드를 만든다
			
			CommentDao cd = new CommentDao();	//생성자호출
			value = cd.commentDelete(Integer.parseInt(cidx));
			
			String str ="{\"value\":\""+value+"\"}";	//JSON파일 형태로 화면에 출력한다
			
			PrintWriter out = response.getWriter();
			out.println(str);		
		}
		
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
