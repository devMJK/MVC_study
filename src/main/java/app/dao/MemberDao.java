package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import app.dbconn.DbConn;
import app.domain.MemberVo;

public class MemberDao {
	
	//멤버변수 선언하고 전역으로 활용
	private Connection conn;		//멤버변수는 선언만해도 자동초기화된
	private PreparedStatement pstmt;		
	
	public MemberDao() {
		DbConn dbconn = new DbConn();
		this.conn = dbconn.getConnection();
	}
	
public int memberInsert(
		String memberId,String memberPwd,String memberName,
		String memberBirth,String memberGender,String memberPhone,
		String memberEmail,String memberAddr,String memberHooby){
		int exec = 0;
		
		String sql = "insert into member0803(memberid,memberpwd,membername,memberbirth,membergender,memberphone,memberemail,memberaddr,memberhobby)"
		           +" values(?,?,?,?,?,?,?,?,?)";
		
		//PreparedStatement : 향상된 Statement
		//해킹방지로 PreparedStatement를 사용하여 insert into 구문의 values의 물음표를 해당 메소드안에서 출력
		try{
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		pstmt.setString(2, memberPwd);
		pstmt.setString(3, memberName);
		pstmt.setString(4, memberBirth);
		pstmt.setString(5, memberGender);
		pstmt.setString(6, memberPhone);
		pstmt.setString(7, memberEmail);
		pstmt.setString(8, memberAddr);
		pstmt.setString(9, memberHooby);
		exec = pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
	}
	return exec;
}

//메소드 만드는 방식
public ArrayList<MemberVo> memberSelectAll(){
	//무한배열클래스 객체생성해서 데이터를 담을 준비를 한다
	ArrayList<MemberVo> alist = new ArrayList<MemberVo>();
	ResultSet rs = null;
	String sql = "select * from member0803 where delyn='N' order by midx desc";
		try {
			//구문(쿼리)객체
			pstmt = conn.prepareStatement(sql);
			//DB에 있는 값을 담아오는 전용객체
			rs = pstmt.executeQuery();
			//rs.next() : 다음 값의 존재유무를 확인하는 메소드 T(존재)/F(미존재)
			while(rs.next()){
				MemberVo mv = new MemberVo();
				//rs에서 midx값을 꺼내서 mv에 옮겨담는다
				mv.setMidx(rs.getInt("midx"));
				mv.setMemberId(rs.getString("memberid"));
				mv.setMemberName(rs.getString("membername"));
				mv.setWriteday(rs.getString("writeday"));
				alist.add(mv);
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
	return alist;
}

public int memberIdCheck(String memberId){	//아이디로 매개변수(memberId)가 넘어오면 셀렉트로...? 
	int value = 0;	//결과값이 0인지 아닌지
	String sql = "select count(*) as cnt from member0803 where memberid=?";
	ResultSet rs = null;
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
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
		return value;
	}
	
public int memberLoginCheck(String memberId, String memberPwd){
	int value = 0;
	String sql = "select midx from member0803 where memberid=? and memberpwd=?";
	ResultSet rs = null;
	try {
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		pstmt.setString(2, memberPwd);
		rs = pstmt.executeQuery();
		
		if(rs.next()){
			value = rs.getInt("midx");
			//value가 0 : 일치하지x / 1 : 일치함
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
	
	return value;
	}	
	

}
