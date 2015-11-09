package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleList {

	private ArrayList<Rule> rules;
	
	public RuleList(ArrayList<Rule> list) {
		this.rules = list;
	}

	public ArrayList<Rule> getDocumentationList() {
		return rules;
	}

	public void setDocumentationList(ArrayList<Rule> documentationList) {
		this.rules = documentationList;
	}
}
