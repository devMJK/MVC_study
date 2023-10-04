package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.BoardVo;
import app.domain.Criteria;
import app.domain.MemberVo;
import app.domain.SearchCriteria;
import app.domain.BoardVo;

//이 Class는 DB board0803에 접근해서 데이터를 가져오는 용도
public class BoardDao {
		
		//멤버변수 선언하고 전역으로 활용
		private Connection conn;		//멤버변수는 선언만해도 자동초기화된
		private PreparedStatement pstmt;		
		
		//생성자를 만든다
		public BoardDao() {
			DbConn dbconn = new DbConn();
			this.conn = dbconn.getConnection();
		}
	
		public ArrayList<BoardVo> boardSelectAll(SearchCriteria scri){
			//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
			ArrayList<BoardVo> alist = new ArrayList<BoardVo>();
			ResultSet rs = null;
			
			String str = "";
			if (!scri.getKeyword().equals("")) {
				str = " and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%') ";
			}
			
			String sql = "SELECT bidx, subject, writer, viewcnt, writeday, level_\r\n"
					+ "from board0803 \r\n"
					+ "where delyn='N' \r\n"
					+ str		//검색을 하지 않으면 안씀
					+ "order by originbidx desc, depth asc limit ?, ?";
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, (scri.getPage()-1)*scri.getPerPageNum());
					pstmt.setInt(2, scri.getPerPageNum());
					//DB에 있는 값을 담아오는 전용객체
					rs = pstmt.executeQuery();
					//rs.next() : 다음 값의 존재유무를 확인하는 메소드 T(존재)/F(미존재)
					while(rs.next()){
						BoardVo bv = new BoardVo();
						//rs에서 bidx값을 꺼내서 bv에 옮겨담는다
						bv.setBidx(rs.getInt("bidx"));
						bv.setSubject(rs.getString("subject"));
						bv.setWriter(rs.getString("writer"));
						bv.setViewcnt(rs.getInt("viewcnt"));
						bv.setWriteday(rs.getString("writeday"));
						bv.setLevel_(rs.getInt("level_"));
						alist.add(bv);
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					try {
						//close()로 끊어줘서 메모리에 있는 것을 해제시킴
						rs.close();
						pstmt.close();
						//conn.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				
			return alist;
		}
		
		public int boardInsert(BoardVo bv){	//bv : 매개변수
				int exec = 0;
				
				String sql = "INSERT INTO board0803(originbidx, depth, level_, SUBJECT,CONTENTS,WRITER,midx,pwd,filename)\r\n"
						+ "VALUES(NULL,0,0,?,?,?,?,?,?)";
				String sql2 = "UPDATE board0803 set\r\n"
						+ "originbidx = (select A.bidx from (select max(bidx) as bidx from board0803)A)\r\n"
						+ "where bidx = (select A.bidx from (select max(bidx) as bidx from board0803)A)";
				
				//PreparedStatement : 향상된 Statement
				//해킹방지로 PreparedStatement를 사용하여 insert into 구문의 values의 물음표를 해당 메소드안에서 출력
				try{
				conn.setAutoCommit(false);		//false : 수동커밋, true : 자동커밋 
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, bv.getSubject());
				pstmt.setString(2, bv.getContents());
				pstmt.setString(3, bv.getWriter());
				pstmt.setInt(4, bv.getMidx());
				pstmt.setString(5, bv.getPwd());
				pstmt.setString(6, bv.getFilename());
				pstmt.executeUpdate();
				
				pstmt =  conn.prepareStatement(sql2);
				exec = pstmt.executeUpdate();
				
				conn.commit();
				
				}catch(Exception e) {		//error가 발생하면 catch구문에서 걸림 -> rollback으로 처리
					try {
						conn.rollback();
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
			}
			return exec;
		}

		public int boardTotalCount(SearchCriteria scri){
			int value = 0;	//결과값이 0인지 아닌지
			
			String str = "";
			if (!scri.getKeyword().equals("")) {
				str = " and "+scri.getSearchType()+" like concat('%','"+scri.getKeyword()+"','%') ";
			}
						
			String sql = "select count(*) as cnt from board0803 where delyn='N' "+str;
			ResultSet rs = null;
			try {
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				if(rs.next()){
				value = rs.getInt("cnt");
				}

				}catch(Exception e){
					e.printStackTrace();
				}finally {
					try {
						rs.close();
						pstmt.close();
						conn.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				//System.out.println("DAO 안에 value"+value);
				return value;
	}
	
	public BoardVo boardSelectOne(int bidx) {
		BoardVo bv = null;
		String sql="select * from board0803 where bidx=?";
		PreparedStatement pstmt= null;
		ResultSet rs = null;
		//향상된 구문클래스를 꺼낸다
		try {
			pstmt = conn.prepareStatement(sql);	//빨간줄 뜨면 오른쪽 버튼 try/catch 입혀주기
			pstmt.setInt(1, bidx);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {	//rs에 다음 값이 있다면
				bv = new BoardVo();		//bv에 그 값을 옮겨담기 위해 객체 생성
				bv.setSubject(rs.getString("subject"));
				bv.setContents(rs.getString("contents"));
				bv.setWriter(rs.getString("writer"));
				bv.setBidx(rs.getInt("bidx"));
				bv.setViewcnt(rs.getInt("viewcnt"));
				bv.setOriginbidx(rs.getInt("originbidx"));
				bv.setDepth(rs.getInt("depth"));
				bv.setLevel_(rs.getInt("level_"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try{
				rs.close();
				pstmt.close();
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}	
		
		return bv;
	}

	public int boardCntUpdate(int bidx){
		int exec = 0;
		
		String sql = "UPDATE board0803 set\r\n"
				+ "viewcnt = viewcnt+1\r\n"
				+ "where bidx = ?";
		
		try{		
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);		
			exec = pstmt.executeUpdate();	
			
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardModify(BoardVo bv){
		int exec = 0;
		
		String sql = "UPDATE board0803 set\r\n"
				+ "subject = ? ,\r\n"
				+ "contents = ? ,\r\n"
				+ "writer = ? ,\r\n"
				+ "modifyday = now() ,\r\n"
				+ "ip = ? \r\n"
				+ "where bidx = ? and pwd = ? ";
		
		try{		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bv.getSubject());		
			pstmt.setString(2, bv.getContents());		
			pstmt.setString(3, bv.getWriter());		
			pstmt.setString(4, bv.getIp());		
			pstmt.setInt(5, bv.getBidx());		
			pstmt.setString(6, bv.getPwd());		
			exec = pstmt.executeUpdate();	
			//글이 수정되면 1이 리턴된다.
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;	
	}
	
	public int boardDelete(int bidx, String pwd){
		int exec = 0;
		
		String sql ="update board0803 set \r\n"
					+ "delyn = 'Y' ,\r\n"
					+ "modifyday = now() \r\n"		//where 구문 직전의 마지막 set 구문에는 \r\n 앞에 ,(쉼표) 사용 x
					+ "where bidx = ? and pwd= ? ";
		
		try{	
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bidx);		
			pstmt.setString(2, pwd);		
			exec = pstmt.executeUpdate();	
			
		}catch(Exception e){			
			e.printStackTrace();
		}
		return exec;
	}

	public int boardReply(BoardVo bv){
		int exec = 0;
		
		String sql = "update board0803 set depth = depth+1 where depth > ?";	//? : 넘겨온 depth
				
		String sql2 = "insert into board0803(originbidx, depth, level_, subject, contents, writer, midx,ip)\r\n"
				+ "values(?,?,?,?,?,?,?,?)";
		
		try{
		conn.setAutoCommit(false);		//false : 수동커밋, true : 자동커밋 
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bv.getDepth());
		exec = pstmt.executeUpdate();
		
		pstmt =  conn.prepareStatement(sql2);
		pstmt.setInt(1, bv.getOriginbidx());
		pstmt.setInt(2, bv.getDepth()+1);
		pstmt.setInt(3, bv.getLevel_()+1);
		pstmt.setString(4, bv.getSubject());
		pstmt.setString(5, bv.getContents());
		pstmt.setString(6, bv.getWriter());
		pstmt.setInt(7, bv.getMidx());
		pstmt.setString(8, bv.getIp());
		exec = pstmt.executeUpdate();
		
		conn.commit();
		
		}catch(Exception e) {	//error가 발생하면 catch구문에서 걸림 -> rollback으로 처리
			try {
				conn.rollback();
			}catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return exec;
	}
}
