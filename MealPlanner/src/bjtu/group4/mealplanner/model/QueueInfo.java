package bjtu.group4.mealplanner.model;

public class QueueInfo {
	private int userId;//用户ID
	private int restId;//餐厅ID
	private int seqNo;// 排队号ID
	private int seqNow;//现在正在进行的排队号ID
	private int seatType;//几人桌
	private int peopleBefore;// 前面排队的队数
	private int peopleNum;//就餐人数

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRestId() {
		return restId;
	}

	public void setRestId(int restId) {
		this.restId = restId;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}

	public int getPeopleBefore() {
		return peopleBefore;
	}

	public void setPeopleBefore(int peopleBefore) {
		this.peopleBefore = peopleBefore;
	}

	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

	public int getSeqNow() {
		return seqNow;
	}

	public void setSeqNow(int seqNow) {
		this.seqNow = seqNow;
	}

	public int getSeatType() {
		return seatType;
	}

	public void setSeatType(int seatType) {
		this.seatType = seatType;
	}
}
