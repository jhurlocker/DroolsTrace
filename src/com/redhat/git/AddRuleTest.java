package com.redhat.git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.req.RuleTest;

public class AddRuleTest {

/**
 * 
 * @param ruleTest
 * 
 * Associates a rule test scenario to a rule or list of rules
 */
	public void addFile(RuleTest ruleTest) {
 		Repository repository = null;
		TraceGitRepoPath traceGitRepoPath = new TraceGitRepoPath();
		try {
			//Create a new FileRepostiory to the Trace Git repo
			repository = new FileRepository(traceGitRepoPath.returnPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Git git = new Git(repository);

    String fileName = ruleTest.getTestName().replace(".scenario", "") + ".btest";
    File ruleTestScenariofile = new File(git.getRepository().getDirectory().getParent(), fileName);

    try {
		ruleTestScenariofile.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


    FileWriter fw;
	try {
		fw = new FileWriter(ruleTestScenariofile.getAbsoluteFile());

	BufferedWriter bw = new BufferedWriter(fw);
	//Rule Test scenario ID. Not currently used.
	bw.write("$ID=" + "\n");
	//Name of the test scenario
	bw.write("$Name=" + ruleTest.getTestName() + "\n");
	//The BRMS project path that the test scenario is under
	bw.write("$GitRepo=" + ruleTest.getRepository() + "\n");
	//The rules that the test scenarios are associated to
	bw.write("$AssocRuleTests=");
	for (int x = 0; x < ruleTest.getAssociatedRuleList().size(); x++){

		if (x == (ruleTest.getAssociatedRuleList().size() - 1)){
			bw.write(ruleTest.getAssociatedRuleList().get(x).getRuleName() + "," + ruleTest.getAssociatedRuleList().get(x).getObjectId());
		}else{
			bw.write(ruleTest.getAssociatedRuleList().get(x).getRuleName() + "," + ruleTest.getAssociatedRuleList().get(x).getObjectId() + ",");
		}
		
	}
	bw.close();
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    try {
		git.add()
		        .addFilepattern(fileName)
		        .call();
	    git.commit()
	    .setMessage("Added testfile - " + fileName)
	    .call();
	} catch (GitAPIException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    repository.close();
	}
}
