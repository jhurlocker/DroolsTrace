package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CustomRequirement {

	private ArrayList<Rule> associatedRuleList = new ArrayList<Rule>();

	
	private String reqName;
	private String customTempId;

	public String getReqName() {
		return reqName;
	}
	public void setReqName(String reqName) {
		this.reqName = reqName;
	}

	public ArrayList<Rule> getAssociatedRuleList() {
		return associatedRuleList;
	}
	public void setAssociatedRuleList(ArrayList<Rule> associatedRuleList) {
		this.associatedRuleList = associatedRuleList;
	}
	
}
