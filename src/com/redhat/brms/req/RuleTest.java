package com.redhat.brms.req;

import java.util.ArrayList;

public class RuleTest {

	private String testName;
	private String objectId;
	private String ruleContent;
	private String repository;
	private ArrayList<Rule> associatedRuleList = new ArrayList<Rule>();
	
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getRuleContent() {
		return ruleContent;
	}
	public void setRuleContent(String ruleContent) {
		this.ruleContent = ruleContent;
	}
	public ArrayList<Rule> getAssociatedRuleList() {
		return associatedRuleList;
	}
	public void setAssociatedRuleList(ArrayList<Rule> associatedRuleList) {
		this.associatedRuleList = associatedRuleList;
	}
	public String getRepository() {
		return repository;
	}
	public void setRepository(String repository) {
		this.repository = repository;
	}

}
