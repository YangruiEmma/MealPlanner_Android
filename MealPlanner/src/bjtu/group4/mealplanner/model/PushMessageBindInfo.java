package bjtu.group4.mealplanner.model;

public class PushMessageBindInfo {
	private Integer id;

	private Integer userid;

	private String baiduuserid;

	private String channelid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getBaiduuserid() {
		return baiduuserid;
	}

	public void setBaiduuserid(String baiduuserid) {
		this.baiduuserid = baiduuserid == null ? null : baiduuserid.trim();
	}

	public String getChannelid() {
		return channelid;
	}

	public void setChannelid(String channelId) {
		this.channelid = channelId;
	}

}
