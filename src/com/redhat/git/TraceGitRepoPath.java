package com.redhat.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.rest.RestReq;

/**
 * 
 * @author jhurlocker
 * 
 * Returns the Trace Git repo path
 *
 */

public class TraceGitRepoPath {

	public String returnPath(){
		RestReq restReq = new RestReq();
		String brmsGitPath = restReq.getBRMSGitPath("brmsMetaData.path");

		Repository repository = null;
		Git git = null;

		String traceGitPath = brmsGitPath.replace("\"", "").concat("/../rm/.git");
		
		return traceGitPath;
		
	}
}
