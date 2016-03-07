package chuangbang.entity;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * 申请开发
 * @author Administrator
 *
 */
public class ServiceDevelop extends BmobObject{

	private User applicat;
	private Project project;
	private Integer developPlatform;
	private String domain;
	private String remarks;//备注
	private Integer state;
	public User getApplicat() {
		return applicat;
	}
	public void setApplicat(User applicat) {
		this.applicat = applicat;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Integer getDevelopPlatform() {
		return developPlatform;
	}
	public void setDevelopPlatform(Integer developPlatform) {
		this.developPlatform = developPlatform;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "ServiceDevelop [applicat=" + applicat + ", project=" + project
				+ ", developPlatform=" + developPlatform + ", domain=" + domain
				+ ", remarks=" + remarks + ", state=" + state + "]";
	}
	
}
