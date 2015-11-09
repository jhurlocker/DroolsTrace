package com.redhat.git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.req.Requirement;
import com.redhat.brms.rest.RestReq;

/**
 * 
 * @author jhurlocker
 * 
 * Adds a Requirement to the Trace Git repo
 *
 */
public class AddReq {

	public void addFile(Requirement req) {

    Repository repository = null;
    Git git = null;
	try {
		//Create a new FileRepostiory to the Trace Git repo
		TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
		repository = new FileRepository(traceGitRepoPath.returnPath());
		git = new Git(repository);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    

    //File name for the requirement.  This should be unique.
    String reqFileName = req.getReqName() + ".breq";
    
    //File is created in the <JBOSS_HOME>/bin/rm directory.
    File reqfile = new File(git.getRepository().getDirectory().getParent(), reqFileName);
    try {
		reqfile.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    FileWriter fw = null;
	try {
		fw = new FileWriter(reqfile.getAbsoluteFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	BufferedWriter bw = new BufferedWriter(fw);
	
	
	try {
		//An ID for the requiement.  Currently not used.
		bw.write("$ID=" + "\n");

		//Requirement name
		bw.write("$Name=" + req.getReqName() + "\n");
		//The BRMS Git Repo the requirement is associated with
		bw.write("$GitRepo=" + req.getRepository() + "\n");
		//Requirement Description
		bw.write("$Desc=" + req.getReqDescription() + "\n");
		//Requirement Version
		bw.write("$Version=" + req.getReqVersion() + "\n");
		//Requirement type
		bw.write("$Type=" + req.getStatus() + "\n");
		//Date requirement is due.  Currently not used.
		bw.write("$DueDate=" + req.getDueDate() + "\n");
		
		//Get all of the custom requirements
		Iterator it = req.getSectionMap().entrySet().iterator();
		//Write the custom key and value if any custom sections exist
		while (it.hasNext()){
			Map.Entry section = (Map.Entry)it.next();
			//Can't have spaces in the key
			String sectionKey = (String) section.getKey();
			bw.write(sectionKey.replaceAll("\\s+", "^") + "=" + section.getValue() + "\n");
		}
	
		//This is the associated rules key
		bw.write("$AssocRules=");
		//Get any associated rules and write to file.  The rule name and rule objectid (not currently used) are 
		//written to the file.
		for (int x = 0; x < req.getAssociatedRuleList().size(); x++){
	
				if (x == (req.getAssociatedRuleList().size() - 1)){
					bw.write(req.getAssociatedRuleList().get(x).getRuleName() + "," + req.getAssociatedRuleList().get(x).getObjectId());
				}else{
					bw.write(req.getAssociatedRuleList().get(x).getRuleName() + "," + req.getAssociatedRuleList().get(x).getObjectId() + ",");
				}
				
		}
	
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//Add the requirement file to the Trace repo
    try {
		git.add()
		        .addFilepattern(reqFileName)
		        .call();
	    git.commit()
	    .setMessage("Added Requirment file " + reqFileName)
	    .call();
	} catch (GitAPIException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}    

    repository.close();
	}
}
