package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.HashMap;

public class RuleTestList {

	private ArrayList<RuleTest> ruleTests;
	
	public RuleTestList(ArrayList<RuleTest> list) {
		this.ruleTests = list;
	}

	public ArrayList<RuleTest> getRuleTestList() {
		return ruleTests;
	}

	public void setRuleTestList(ArrayList<RuleTest> ruleTestList) {
		this.ruleTests = ruleTestList;
	}
}
