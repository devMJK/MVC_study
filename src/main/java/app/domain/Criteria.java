package app.domain;

//����¡�� �ϱ� ���� ������ �Ǵ� ������ Ŭ����
public class Criteria {
	
	private int page;	//��������ȣ�� ��� (���)����
	private int perPageNum;		//����Ʈ �Խù� ��
	
	//���콺������-source - generate Getters and Setters
	public Criteria() {
		this.page = 1;
		this.perPageNum = 15;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPageNum() {
		return perPageNum;
	}

	public void setPerPageNum(int perPageNum) {
		this.perPageNum = perPageNum;
	}
	
	
	
	
	
	
	
	
	
	
}