package com.redhat.brms.req;

import java.util.ArrayList;

public class ReqTemplate {

	private String id;
	private String gitProj;
	private String reqTempName;
	private String reqTempDesc;
	private String[] docHeadings;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGitProj() {
		return gitProj;
	}
	public void setGitProj(String gitProj) {
		this.gitProj = gitProj;
	}
	public String[] getDocHeadings() {
		return docHeadings;
	}
	public void setDocHeadings(String docHeadings) {

		this.docHeadings = docHeadings.split(",");

	}
	public String getReqTempName() {
		return reqTempName;
	}
	public void setReqTempName(String reqTempName) {
		this.reqTempName = reqTempName;
	}
	public String getReqTempDesc() {
		return reqTempDesc;
	}
	public void setReqTempDesc(String reqTempDesc) {
		this.reqTempDesc = reqTempDesc;
	}
	
	
}
