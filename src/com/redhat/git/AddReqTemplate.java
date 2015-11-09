package com.redhat.git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;

import com.redhat.brms.poi.GenerateDocTemplate;

/**
 * 
 * @author jhurlocker
 * 
 * Class to create a Word document requirement template and the requirement template format text document.
 * 
 */
public class AddReqTemplate {

	public void addFile(String reqHeaders, String project, String reqTempName, String reqTempDesc, String fileName) {
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
    
    File reqTemplatefile = new File(git.getRepository().getDirectory().getParent(), fileName);
    try {
		reqTemplatefile.createNewFile();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    FileWriter fw;
	try {
		fw = new FileWriter(reqTemplatefile.getAbsoluteFile());

	BufferedWriter bw = new BufferedWriter(fw);
	//ID for requirement template.  Not currently used.
	bw.write("$ID=" + "\n");
	//Requirement template name
	bw.write("$Name=" + reqTempName + "\n");
	//Requirement template description
	bw.write("$Desc=" + reqTempDesc + "\n");
	//The BRMS Git Repo path for the requirment template
	bw.write("$GitProj=" + project + "\n");

	//Create a string array of the custom template headers
	String[] splitReqHeaders = reqHeaders.split(",");	

	GenerateDocTemplate genDocTemplate = new GenerateDocTemplate();
	//Generate Custom Template - Word Document
	genDocTemplate.generate(splitReqHeaders, "", fileName, project);

	//Write all of the custom headers to the file
	bw.write("$DocHeadings=");
	for (int x = 0; x < splitReqHeaders.length; x++){
		if (splitReqHeaders[x].toString().contains("label")){
			String[] headerArray = splitReqHeaders[x].toString().split(":");
			String theHeader = headerArray[1].replace("\"", "");
			System.out.println("THE HEADER= " + theHeader);

		if (x == (splitReqHeaders.length - 2)){
			bw.write(theHeader);
		}else{
			bw.write(theHeader + ",");
		}
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
	    .setMessage("Added requirement template - " + fileName)
	    .call();
	} catch (GitAPIException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    repository.close();
	}
}
