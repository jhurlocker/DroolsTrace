package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.HashMap;

public class RequirementList {

	private ArrayList<Requirement> requirements;
	
	public RequirementList(ArrayList<Requirement> list) {
		this.requirements = list;
	}

	public ArrayList<Requirement> getRequirementList() {
		return requirements;
	}

	public void setRequirementList(ArrayList<Requirement> requirementList) {
		this.requirements = requirementList;
	}
}
