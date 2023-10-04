package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.CommentVo;
import app.domain.MemberVo;

public class CommentDao {
	
	//멤버변수 선언하고 전역으로 활용
	private Connection conn;		//멤버변수는 선언만해도 자동초기화된
	private PreparedStatement pstmt;		
	
	//생성자를 만든다음 DB연결
	public CommentDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}
	
	public ArrayList<CommentVo> commentSelectAll(){
		//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
		ArrayList<CommentVo> alist = new ArrayList<CommentVo>();
		ResultSet rs = null;
		String sql = "select * from comment0803 where delyn='N' order by cidx desc";
			try {
				//구문(쿼리)객체
				pstmt = conn.prepareStatement(sql);
				//DB에 있는 값을 담아오는 전용객체
				rs = pstmt.executeQuery();
				//rs.next() : 다음 값의 존재유무를 확인하는 메소드 T(존재)/F(미존재)
				while(rs.next()){
					CommentVo cv = new CommentVo();
					//rs에서 cidx값을 꺼내서 cv에 옮겨담는다
					cv.setCidx(rs.getInt("cidx"));
					cv.setMidx(rs.getInt("midx"));
					cv.setBidx(rs.getInt("bidx"));
					cv.setCwriter(rs.getString("cwriter"));
					cv.setCcontents(rs.getString("ccontents"));
					cv.setCwriteday(rs.getString("cwriteday"));
					alist.add(cv);
					//반복문 돌리면서 컬렉션(창고)에 추가해서 담는다
				}

			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					//close()로 끊어줘서 메모리에 있는 것을 해제시킴
					rs.close();
					pstmt.close();
					conn.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		return alist;	//창고 위치값을 리턴
	}
	
	public int commentInsert(CommentVo cv){
		int exec = 0;
		
		String sql = "insert into comment0803(cwriter,ccontents,bidx,midx)"
		           +" values(?,?,?,?)";
		
		//PreparedStatement : 향상된 Statement
		//해킹방지로 PreparedStatement를 사용하여 insert into 구문의 values의 물음표를 해당 메소드안에서 출력
			try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cv.getCwriter());
			pstmt.setString(2, cv.getCcontents());
			pstmt.setInt(3, cv.getBidx());
			pstmt.setInt(4, cv.getMidx());
			exec = pstmt.executeUpdate();		//실행이 되면 1, 실행이 안되면 0
			}catch(Exception e) {
				e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return exec;
	}
	
	public int commentDelete(int cidx){
		int exec = 0;
		
		String sql = "update comment0803 set delyn = 'Y' where cidx = ?";
		
		try{
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cidx);
			
			exec = pstmt.executeUpdate();		//실행이 되면 1, 실행이 안되면 0
			}catch(Exception e) {
				e.printStackTrace();
		}finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return exec;
	}
}
