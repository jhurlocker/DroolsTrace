package com.redhat.brms.req;

public class Rule {

	private String ruleName;
	private String objectId;
	private String ruleContent;
	
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
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
}
