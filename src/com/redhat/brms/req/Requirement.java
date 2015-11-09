package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Requirement {

	private String objectKey;
	private String objectName;
	private String repository;
	private HashMap sectionMap = new HashMap();
	private ArrayList<Rule> associatedRuleList = new ArrayList<Rule>();
	
	private String reqName;
	private String type;
	private String reqDescription;
	private String reqVersion;
	private String severity;
	private Date dueDate;
	private String status;
	
	public String getObjectKey() {
		return objectKey;
	}
	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}
	public String getReqDescription() {
		return reqDescription;
	}
	public void setReqDescription(String reqDescription) {
		this.reqDescription = reqDescription;
	}
	public String getReqVersion() {
		return reqVersion;
	}
	public void setReqVersion(String reqVersion) {
		this.reqVersion = reqVersion;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public HashMap getSectionMap() {
		return sectionMap;
	}
	public void setSectinMap(HashMap ruleMap) {
		this.sectionMap = ruleMap;
	}
	public ArrayList<Rule> getAssociatedRuleList() {
		return associatedRuleList;
	}
	public void setAssociatedRuleList(ArrayList<Rule> associatedRuleList) {
		this.associatedRuleList = associatedRuleList;
	}

	
}
