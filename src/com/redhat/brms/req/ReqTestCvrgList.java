package com.redhat.brms.req;

import java.util.ArrayList;
import java.util.HashMap;

public class ReqTestCvrgList {

	private ArrayList<ReqTestCvrg> reqTestCvrg;
	
	public ReqTestCvrgList(ArrayList<ReqTestCvrg> list) {
		this.reqTestCvrg = list;
	}

	public ArrayList<ReqTestCvrg> getReqTestCvrgList() {
		return reqTestCvrg;
	}

	public void setReqTestCvrgList(ArrayList<ReqTestCvrg> ReqTestCvrgList) {
		this.reqTestCvrg = ReqTestCvrgList;
	}
}
