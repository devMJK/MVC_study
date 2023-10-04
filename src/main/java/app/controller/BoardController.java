package app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import app.dao.BoardDao;
import app.dao.MemberDao;
import app.domain.BoardVo;
import app.domain.Criteria;
import app.domain.MemberVo;
import app.domain.PageMaker;
import app.domain.SearchCriteria;

// HttpServlet를 상속받았기 때문에 클래스가 인터넷페이지가 된다
@WebServlet("/BoardController")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String location; 
	public BoardController(String location){
		this.location = location;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//문자열 비교
		if (location.equals("boardList.do")) {	
			
			String searchType = request.getParameter("searchType");
			if (searchType == null) searchType="subject";		//실행문이 하나면 {}생략가능 -> 아래아래행의 keyword처럼
			String keyword = request.getParameter("keyword");
			if (keyword == null) keyword="";
			String page = request.getParameter("page");
			if(page == null) {page = "1";}
			
			SearchCriteria scri = new SearchCriteria();
			scri.setPage(Integer.parseInt(page));
			scri.setSearchType(searchType);
			scri.setKeyword(keyword);
			
			PageMaker pm = new PageMaker();
			pm.setScri(scri);
			
			BoardDao bd = new BoardDao();
			ArrayList<BoardVo> alist = bd.boardSelectAll(scri);
			int cnt = bd.boardTotalCount(scri);		//전체 게시물 개수 / 위에 boardSelectAll하고 또 다른 사용을 위해 conn을 끊어야함
			//System.out.println("cnt?"+cnt); -> 에러생겼을때 게시물 개수가 제대로 출력되는지 확인하려고 찍어본 코드
			pm.setTotalCount(cnt);
			
			//화면으로 가지고 간다
			request.setAttribute("alist", alist);
			request.setAttribute("pm", pm);
			
			String path ="/board/boardList.jsp";
			//화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보낸다
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
		
		}else if(location.equals("boardWrite.do")) {
			
			String path ="/board/boardWrite.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
		}else if(location.equals("boardWriteAction.do")) {
			//저장하고자 하는 폴더(images)를 만들어서 물리적인 경로를 String(문자형)으로 만들기 -> 두번째 인자값
			String savePath = "D:\\dev0803\\mvcstudy0803\\src\\main\\webapp\\images";	
			//용량 -> 세번째 인자값
			int sizeLimit = 15 * 1024 * 1024;	//15MB
			//데이터 타입 -> 네번째 인자값
			String dataTy = "UTF-8";
			//파일이름이 중복됐을 경우의 정책-> 다섯번째 인자값
			DefaultFileRenamePolicy drp = null;		//null로 초기화 하고 import 해주기
			drp = new DefaultFileRenamePolicy();
			//다양한 파일을 넘겨받는 통신요청객체
			MultipartRequest multi = null;	//null로 초기화 하고 import 해주기
			multi = new MultipartRequest(request,savePath,sizeLimit,dataTy,drp);
			
			
			//1.넘긴 값을 받는다
			String subject = multi.getParameter("subject");
			String contents = multi.getParameter("contents");
			String writer = multi.getParameter("writer");
			String pwd = multi.getParameter("pwd");
			
			//파일넘겨받기
			//열거자에 넘어오는 여러 파일이름을 담는다
			Enumeration files = multi.getFileNames();
			//파일 객체를 꺼낸다
			String file = (String)files.nextElement();	//Object로 나오니 같은 형(String)으로 맞춰줌
			//그 파일의 이름을 추출한다(실제로 저장되는 파일이름)
			String fileName = multi.getFilesystemName(file);	//파일이름을 넣어주면 해당되는 파일객체의 파일이름을 String 타입으로 추출
			//원래 파일이름을 추출
			String originFileName = multi.getOriginalFileName(file);
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			
			
			//2.받은 값을 입력한다 -> 메소드를 만들어야함
			//set : 담는 용도의 메소드
			//객체 안에는 각각의 값이 담겨서 그값만 넘김
			BoardVo bv = new BoardVo();	//bv : 객체변수
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setFilename(fileName);	//실제 저장되는 파일이름
			bv.setPwd(pwd);
			bv.setMidx(midx);
			
			BoardDao bd = new BoardDao();
			int value = bd.boardInsert(bv);		//insert를 하게 되면 value로 받음
			
			//3.처리가 끝났으면 새롭게 이동한다
			if(value == 0) {	//입력 안되었으면 다시 입력페이지로 가게끔
				String path = request.getContextPath()+"/board/boardWrite.do";
				response.sendRedirect(path);
			}else {		//입력이 되었으면 List페이지로
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}
		}else if(location.equals("boardContents.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);		//문자형을 int형으로 변환				
			BoardDao bd = new BoardDao();		//객체생성
			int exec = bd.boardCntUpdate(bidx_int);
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);
			
			String path ="/board/boardContents.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
		}else if(location.equals("boardModify.do")) {
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);	
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);
			
			String path ="/board/boardModify.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
		}else if(location.equals("boardModifyAction.do")) {
			//1.수정데이터 넘겨받고
			String bidx = request.getParameter("bidx");
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			//2.받은 값을 입력한다
			BoardVo bv = new BoardVo();
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPwd(pwd);
			bv.setBidx(Integer.parseInt(bidx));
			bv.setIp(ip);
			BoardDao bd = new BoardDao();
			int value = bd.boardModify(bv);
			
			//3.처리가 끝났으면 새롭게 이동한다
			PrintWriter out = response.getWriter();
			if(value == 0) {	//수정 안되었으면 수정페이지
				
				out.println("<script>alert('비밀번호가 일치하지 않습니다.');location.href='"+request.getContextPath()+"/board/boardModify.do?bidx="+bidx+"'</script>");
				//String path = request.getContextPath()+"/board/boardModify.do?bidx="+bidx;
				//response.sendRedirect(path);
			}else {		//수정되었으면 내용 페이지로 이동	
				String path = request.getContextPath()+"/board/boardContents.do?bidx="+bidx;
				response.sendRedirect(path);
			}
		}else if(location.equals("boardDelete.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);		//넘어온 bidx를 Integer 안에 있는 parseInt함수로 String형을 int 형으로 변환
			
			BoardDao bd = new BoardDao();
			BoardVo bv = bd.boardSelectOne(bidx_int);
			
			request.setAttribute("bv", bv);		//setAttribute 메소드 안에 "bv"를 담는다
			
			
			String path ="/board/boardDelete.jsp";		//화면용도의 주소는 포워드로 토스해서 해당 찐주소로 보냄. 같은 지역이무로 공유해서 꺼낼 수 있음
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);
			
		}else if(location.equals("boardDeleteAction.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String pwd = request.getParameter("pwd");
			
			//처리하는 메소드를 만들어야 함
			int value = 0;
			
			BoardDao bd = new BoardDao();
			value = bd.boardDelete(bidx_int, pwd);
			
			if (value != 0 ) {		//0이 아니면 -> 처리가 되지 않았으면 : 처리가 되면 1이고 처리가 되지 않았으면 0임
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}else {
				String path = request.getContextPath()+"/board/boardDelete.do?bidx="+bidx;
						response.sendRedirect(path);
			}
			
		}else if(location.equals("boardReply.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_ = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);

			BoardVo bv = new BoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);
			
			request.setAttribute("bv", bv);
			
			
			String path ="/board/boardReply.jsp";
			RequestDispatcher rd = request.getRequestDispatcher(path);
			rd.forward(request, response);	
		
		}else if(location.equals("boardReplyAction.do")) {
			
			String bidx = request.getParameter("bidx");
			int bidx_int = Integer.parseInt(bidx);
			String originbidx = request.getParameter("originbidx");
			int originbidx_int = Integer.parseInt(originbidx);
			String depth = request.getParameter("depth");
			int depth_int = Integer.parseInt(depth);
			String level_ = request.getParameter("level_");
			int level_int = Integer.parseInt(level_);
			String subject = request.getParameter("subject");
			String contents = request.getParameter("contents");
			String writer = request.getParameter("writer");
			String pwd = request.getParameter("pwd");
			String ip = InetAddress.getLocalHost().getHostAddress();
			
			int midx = 0;
			HttpSession session = request.getSession();
			midx = (int)session.getAttribute("midx");
			
			BoardVo bv = new BoardVo();
			bv.setBidx(bidx_int);
			bv.setOriginbidx(originbidx_int);
			bv.setDepth(depth_int);
			bv.setLevel_(level_int);
			bv.setSubject(subject);
			bv.setContents(contents);
			bv.setWriter(writer);
			bv.setPwd(pwd);
			bv.setIp(ip);
			bv.setMidx(midx);
			
			int value = 0;
			//처리하는 메소드 만들기
			
			BoardDao bd = new BoardDao();  //객체생성
			value = bd.boardReply(bv);	
			
			if(value != 0) {	//0이 아니면(1이면) 게시판목록으로 이동
				String path = request.getContextPath()+"/board/boardList.do";
				response.sendRedirect(path);
			}else {	//0이면 다시 Reply 페이지로 이동
				String path = request.getContextPath()+"/board/boardReply.do?bidx="+bidx+"&originbidx="+originbidx+"&depth="+depth+"&level_="+level_+"";
				response.sendRedirect(path);				
			}
			
		}
		
	}

		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
